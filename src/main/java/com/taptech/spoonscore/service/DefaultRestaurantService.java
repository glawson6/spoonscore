package com.taptech.spoonscore.service;

import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import com.taptech.spoonscore.entity.ZipCodes;
import com.taptech.spoonscore.repository.ZipCodesRepository;
import com.taptech.spoonscore.service.locator.RestaurantLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
    FoodInspectionService foodInspectionService;

    @Override
    public Collection<Restaurant> findRestaurants(RestaurantSearch restaurantSearch) {
        Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
        Collection<Restaurant> foundRestaurants = yelpRestaurantLocator.locateRestaurants(restaurantSearch);
        determineCounty(restaurantSearch);
        log.info("{}",foundRestaurants);
        restaurants.addAll(foundRestaurants);
        for (Restaurant restaurant:restaurants){
            try {
                restaurant.setCounty(restaurantSearch.getCounty());
                //Thread.sleep(1000);
                foodInspectionService.setInspectionResults(restaurant);
            } catch (Exception e) {
                log.error("Exception getting inspection data ", e);
            }

        }
        return restaurants;
    }

    @Override
    public Restaurant updateRestaurant(RestaurantSearch restaurantSearch) {
        return null;
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
        restaurantSearch.setLatitutde((double)zipCodes.getLatitude());
        restaurantSearch.setLongitude((double)zipCodes.getLongitude());
        //log.info("Updated restaurantSearch {}",restaurantSearch);
        //log.debug("County in determine county {}",county);
    }
}
