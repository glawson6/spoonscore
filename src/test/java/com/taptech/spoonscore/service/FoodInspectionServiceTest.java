package com.taptech.spoonscore.service;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Created by tap on 3/26/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FoodInspectionServiceTest {


    private final Logger log = LoggerFactory.getLogger(YelpService.class);

    @Inject
    private Environment env;

    @Inject
    YelpService yelpService;

    @Inject
    FoodInspectionService foodInspectionService;

    @Test
    public void testSanity(){
        log.info("We are sane!!!!!!");
    }

    @Test
    public void testCreateCompanyLocation(){
        String bizName = "Sway";
        String address = "265 Peachtree St NE Atlanta, GA 30308";
        String county = "Fulton";

        RestaurantSearch search = new RestaurantSearch();
        search.setAddress(address);
        search.setCounty(county);
        search.setCompanyName(bizName);
        Restaurant restaurant = foodInspectionService.getInspectionResults(search);
        log.info("CompanyLocation => {}", restaurant);
    }
//    @Test
//    public void testFindCountyFromZipCode(){
//        String zipCode = "30315";
//        String county = foodInspectionService.findCountyFromZipCode(zipCode);
//        log.info("County => {}",county.split(" ")[0]);
//    }
}
