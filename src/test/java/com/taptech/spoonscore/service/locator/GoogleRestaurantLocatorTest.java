package com.taptech.spoonscore.service.locator;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by tap on 3/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GoogleRestaurantLocatorTest {
    private final Logger log = LoggerFactory.getLogger(GoogleRestaurantLocatorTest.class);
    @Inject
    private Environment env;

    @Inject
    @Qualifier(value = "GoogleRestaurantLocator")
    RestaurantLocator googleRestaurantLocator;

    @Test
    public void testGoogleRestaurantLocator(){
        log.info("Starting testGoogleRestaurantLocator");
        RestaurantSearch search = new RestaurantSearch();
        search.setCity("Atlanta");
        Collection<Restaurant> restaurants = googleRestaurantLocator.locateRestaurants(search);

        for (Restaurant restaurant: restaurants){
            log.info("{}",restaurant);
        }

    }
}
