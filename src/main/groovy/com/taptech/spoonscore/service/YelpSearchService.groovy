package com.taptech.spoonscore.service

import com.taptech.spoonscore.domain.Restaurant
import com.taptech.spoonscore.domain.RestaurantSearch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import javax.inject.Inject

@Service("YelpSearchService")
class YelpSearchService implements CompanySearch{

    private final Logger log = LoggerFactory.getLogger(YelpSearchService.class);

    @Inject
    private Environment env;

    @Override
    Collection<Restaurant> findCompanyLocations(RestaurantSearch restaurantSearch) {
        log.info("Entering findCompanyLocations")
        return null
    }
}