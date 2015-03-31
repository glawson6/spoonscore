package com.taptech.spoonscore.service;

import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import com.taptech.spoonscore.domain.YelpCompany;

import java.util.Collection;

/**
 * Created by tap on 3/26/15.
 */
public interface FoodInspectionService {
    public String createFoodInspectionSearchURL(RestaurantSearch restaurantSearch);
    public Restaurant getInspectionResults(RestaurantSearch restaurantSearch);
    public void setInspectionResults(Restaurant restaurant);
    public String createSearchURL(String address, String county);
}
