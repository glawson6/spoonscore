package com.taptech.spoonscore.service;

import com.taptech.spoonscore.domain.Location;
import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import com.taptech.spoonscore.entity.RestaurantDetails;
import com.taptech.spoonscore.entity.RestaurantLocation;
import com.taptech.spoonscore.entity.ZipCodes;
import com.taptech.spoonscore.repository.RestaurantDetailsRepository;
import com.taptech.spoonscore.repository.RestaurantLocationRepository;
import com.taptech.spoonscore.repository.ZipCodesRepository;
import com.taptech.spoonscore.service.locator.LocationService;
import com.taptech.spoonscore.service.locator.RestaurantLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tap on 3/29/15.
 */
@Service
public class DefaultRestaurantService implements RestaurantService {
    private final Logger log = LoggerFactory.getLogger(DefaultRestaurantService.class);

    @Inject
    @Qualifier(value = "YelpRestaurantLocator")
    RestaurantLocator yelpRestaurantLocator;

    @Inject
    @Qualifier(value = "GoogleRestaurantLocator")
    RestaurantLocator googleRestaurantLocator;
    @Inject
    private ZipCodesRepository zipCodesRepository;

    @Inject
    private RestaurantDetailsRepository restaurantDetailsRepository;

    @Inject
    private RestaurantLocationRepository restaurantLocationRepository;

    @Inject
    FoodInspectionService foodInspectionService;

    @Inject
    @Qualifier(value = "GoogleLocationService")
    LocationService locationService;

    @Override
    public Collection<Restaurant> findRestaurants(RestaurantSearch restaurantSearch) {
        Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
        Location location = resolveLocation(restaurantSearch);
        log.info("-------------Gathering Location Info--------------------");
        log.info("Searching for Restaurants using {}",restaurantSearch.toString());
        log.info("Resolved to Location {}",location.toString());
        log.info("--------------------------------------------------------");


        Collection<Restaurant> foundRestaurants = yelpRestaurantLocator.locateRestaurants(restaurantSearch);
        //log.info("{}",foundRestaurants);
        restaurants.addAll(foundRestaurants);
        for (Restaurant restaurant:restaurants){
            Restaurant dbRestaurant = null;
            try {
                restaurant.getLocation().setCounty(location.getCounty());
                //Thread.sleep(1000);
                // Lets remember if we have a restaurant in the database
                dbRestaurant = findRestaurantInDatabase(restaurant.getRestaurantID());
                if (null != dbRestaurant){
                    // Update inspection Reports, Score, Grade
                    updateCurrentRestaurant(restaurant, dbRestaurant);
                    log.info("================================================================");
                    log.info("restaurant => {}",restaurant.toString());
                    log.info("dbRestaurant => {}",dbRestaurant.toString());
                    log.info("================================================================");
                } else {
                    foodInspectionService.setInspectionResults(restaurant, false);
                    log.info("Restaurant after inspection results {}",restaurant.toString());
                    //Persist restaurant if we found inspection info
                    if (null != restaurant.getInspectionLink()){
                        saveRestaurantInDatabase(restaurant);
                    }
                }
            } catch (Exception e) {
                log.error("Exception getting inspection data ", e.getMessage());
            }

        }

        return restaurants;
    }

    @Async
    private void saveRestaurantInDatabase(Restaurant restaurant) {
        RestaurantDetails restaurantDetails = extractRestaurantDetails(restaurant);
        RestaurantLocation restaurantLocation = extractRestaurantLocation(restaurant.getLocation());
        restaurantDetailsRepository.save(restaurantDetails);
        restaurantLocationRepository.save(restaurantLocation);
    }

    private RestaurantLocation extractRestaurantLocation(Location location) {
        RestaurantLocation restaurantLocation = new RestaurantLocation();
        BeanUtils.copyProperties(location, restaurantLocation);
        restaurantLocation.setRestaurantId(location.getId());
        restaurantLocation.setStateAbbrev(location.getState());
        restaurantLocation.setZipCode(Integer.parseInt(location.getZipCode()));
        log.info("Persisting restaurantLocation {}",restaurantLocation.toString());
        return restaurantLocation;
    }

    private RestaurantDetails extractRestaurantDetails(Restaurant restaurant) {
        RestaurantDetails restaurantDetails = new RestaurantDetails();
        BeanUtils.copyProperties(restaurant, restaurantDetails);
        restaurantDetails.setRestaurantId(restaurant.getRestaurantID());
        restaurantDetails.setCompanyGrade(restaurant.getCompanyInspectionGrade().charAt(0));
        restaurantDetails.setCompanyScore(Integer.parseInt(restaurant.getCompanyInspectionScore()));
        restaurantDetails.setImageUrl(restaurant.getImageURL());
        restaurantDetails.setInspectionReportLink(restaurant.getViewReportLink());
        log.info("Persisting restaurantDetails {}",restaurantDetails.toString());
        return restaurantDetails;
    }

    private void updateCurrentRestaurant(Restaurant restaurant, Restaurant dbRestaurant) {
        restaurant.setRatingCommentsLink(dbRestaurant.getRatingCommentsLink());
        restaurant.setCompanyInspectionGrade(dbRestaurant.getCompanyInspectionGrade());
        restaurant.setCompanyInspectionScore(dbRestaurant.getCompanyInspectionScore());
        restaurant.setInspectionLink(dbRestaurant.getInspectionLink());
    }

    private Restaurant findRestaurantInDatabase(String restaurantID) {
        Restaurant restaurant = null;
        RestaurantDetails restaurantDetails = restaurantDetailsRepository.findOneByRestaurantId(restaurantID);
        if (null != restaurantDetails){
            restaurant = new Restaurant();
            BeanUtils.copyProperties(restaurantDetails, restaurant);
            restaurant.setCompanyInspectionScore(restaurantDetails.getCompanyScore().toString());
            restaurant.setCompanyInspectionGrade(restaurantDetails.getCompanyGrade().toString());
            //restaurant.setRatingCommentsLink(restaurantDetails.getRatingCommentsLink());
            restaurant.setInspectionLink(restaurantDetails.getInspectionLink());
            restaurant.setViewReportLink(restaurantDetails.getInspectionReportLink());
        }
        return restaurant;
    }

    private Location resolveLocation(RestaurantSearch restaurantSearch) {
        Location location = null;
        if (null != restaurantSearch.getLatitude() && null != restaurantSearch.getLongitude()){
            location = locationService.getLocationByLatLong(restaurantSearch.getLatitude(), restaurantSearch.getLongitude());
        } else if (null != restaurantSearch.getZipCode()){
            location = locationService.getLocationByZipCode(restaurantSearch.getZipCode());
        } else if (null != restaurantSearch.getCity() && null != restaurantSearch.getState()){
            location = locationService.getLocationByCity(restaurantSearch.getCity(), restaurantSearch.getState());
        }
        if (null == location.getCounty()){
            ZipCodes zipCodes = null;
            String county = null;
            if (null != restaurantSearch.getZipCode()) {
                zipCodes = zipCodesRepository.findOneByZipCode(restaurantSearch.getZipCode());
            } else if (null != restaurantSearch.getCity()){
                List<ZipCodes> zipCodesList = zipCodesRepository.findAllByCity(restaurantSearch.getCity().toUpperCase());
                zipCodes = zipCodesList.get(0);
                county = zipCodesList.get(0).getCounty();
            }
            //log.debug("{} ", zipCodes.toString());
            county = zipCodes.getCounty();
            location.setCounty(county);
            //restaurantSearch.setCounty(county);
        }
        return location;
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant, boolean update) {
        try {
            Restaurant dbRestaurant = findRestaurantInDatabase(restaurant.getRestaurantID());
            if (null != dbRestaurant) {
                // Update inspection Reports, Score, Grade
                updateCurrentRestaurant(restaurant, dbRestaurant);
            } else {
                foodInspectionService.setInspectionResults(restaurant, update);
                log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Restaurant after inspection results {}", restaurant.toString());
                //Persist restaurant if we found inspection info
                if (null != restaurant.getInspectionLink()) {
                    saveRestaurantInDatabase(restaurant);
                }
            }
        } catch (Exception e){
            log.error("Could not update restaurant",e);
        }
        return restaurant;
    }

    private void determineCounty(RestaurantSearch restaurantSearch) {
        String county = null;
        //List<ZipCodes> zipCodesList = null;
        ZipCodes zipCodes = null;
        if (null != restaurantSearch.getZipCode()) {
            zipCodes = zipCodesRepository.findOneByZipCode(restaurantSearch.getZipCode());
        } else if (null != restaurantSearch.getCity()){
            List<ZipCodes> zipCodesList = zipCodesRepository.findAllByCity(restaurantSearch.getCity().toUpperCase());
            zipCodes = zipCodesList.get(0);
            county = zipCodesList.get(0).getCounty();
        }
        //log.debug("{} ", zipCodes.toString());
        county = zipCodes.getCounty();
        restaurantSearch.setCounty(county);
        restaurantSearch.setLatitude((double) zipCodes.getLatitude());
        restaurantSearch.setLongitude((double)zipCodes.getLongitude());
        //log.info("Updated restaurantSearch {}",restaurantSearch);
        //log.debug("County in determine county {}",county);
    }
}
