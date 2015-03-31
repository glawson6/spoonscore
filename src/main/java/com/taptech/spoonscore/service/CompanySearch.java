package com.taptech.spoonscore.service;

import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;

import java.util.Collection;

/**
 * Created by tap on 3/27/15.
 */
public interface CompanySearch {

    public Collection<Restaurant> findCompanyLocations(RestaurantSearch restaurantSearch);
}
