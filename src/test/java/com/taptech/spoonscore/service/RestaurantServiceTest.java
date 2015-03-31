package com.taptech.spoonscore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Created by tap on 3/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class RestaurantServiceTest {

    private final Logger log = LoggerFactory.getLogger(YelpService.class);

    @Inject
    private Environment env;

    @Inject
    RestaurantService restaurantService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRestaurantService(){
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String zip = "30303";
        String state = "GA";
        RestaurantSearch search = new RestaurantSearch();
        search.setZipCode(30303);
        search.setState(state);
        StringWriter searchWriter = new StringWriter();
        try {
            mapper.writeValue(searchWriter,search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Search {}",searchWriter);
        Collection<Restaurant> restaurants = restaurantService.findRestaurants(search);
        log.info("Total restaurants => {}",restaurants.size());
        int cnt = 0;
        log.info("NULL INSPECTION LINKS START -----------------");
        for (Restaurant restaurant:restaurants){
            StringWriter sw = new StringWriter();
            try {
                mapper.writeValue(sw,restaurant);
                log.info("{}",sw.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            if (null == restaurant.getInspectionLink()) {
                log.info("{}", restaurant);
                cnt++;
            }
            */
        }
        log.info("NULL INSPECTION LINKS END TOTAL {}-----------------",cnt);
    }
}
