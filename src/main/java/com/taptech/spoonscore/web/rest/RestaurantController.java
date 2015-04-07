package com.taptech.spoonscore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import com.taptech.spoonscore.service.FoodInspectionService;
import com.taptech.spoonscore.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by tap on 3/30/15.
 */
@RestController
@RequestMapping("/api")
public class RestaurantController {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    RestaurantService restaurantService;


    @Inject
    FoodInspectionService foodInspectionService;
    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/restaurant/search",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Collection<Restaurant> findRestaurants(@RequestBody RestaurantSearch search, HttpServletRequest request, HttpServletResponse response) {

        Collection<Restaurant> restaurants = restaurantService.findRestaurants(search);

        if (restaurants == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return restaurants;
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/restaurant/inspectionUpdate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Restaurant getInspectionInfo(@RequestBody Restaurant restaurant, HttpServletRequest request, HttpServletResponse response) {
        //foodInspectionService.setInspectionResults(restaurant,true);
        restaurantService.updateRestaurant(restaurant,true);
        return restaurant;
    }
    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/restaurant/search/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Collection<Restaurant> activateAccount( HttpServletRequest request, HttpServletResponse response) {

        RestaurantSearch search = new RestaurantSearch();
        search.setZipCode(30315);
        Collection<Restaurant> restaurants = restaurantService.findRestaurants(search);

        if (restaurants == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return restaurants;
    }
}
