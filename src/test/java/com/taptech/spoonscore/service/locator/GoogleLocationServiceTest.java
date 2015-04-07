package com.taptech.spoonscore.service.locator;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.domain.Location;
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
public class GoogleLocationServiceTest {
    private final Logger log = LoggerFactory.getLogger(GoogleLocationServiceTest.class);
    @Inject
    private Environment env;

    @Inject
    @Qualifier(value = "GoogleLocationService")
    LocationService googleLocationService;

    @Test
    public void testGoogleLocationService(){
        log.info("Starting testGoogleLocationService");
        Double latitude = 33.7550;
        Double longitude = -84.3900;
        Location location = googleLocationService.getLocationByLatLong(latitude,longitude);
        log.info("Location {}",location.toString());
    }
}
