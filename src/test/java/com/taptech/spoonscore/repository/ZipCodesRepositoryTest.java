package com.taptech.spoonscore.repository;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.ApplicationTest;
import com.taptech.spoonscore.entity.ZipCodes;
import com.taptech.spoonscore.service.YelpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tap on 3/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ZipCodesRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(ZipCodesRepositoryTest.class);
    @Inject
    private ZipCodesRepository zipCodesRepository;

    private static final String FULTON = "FULTON";
    @Test
    public void testZipCodeCount(){

        log.info("Starting testZipCodeCount");
        ZipCodes zipCodes = zipCodesRepository.findOneByZipCode(30315);
        log.info("ZipCode => {}",zipCodes);
        assertThat(zipCodes).isNotNull();
        assertThat(zipCodes.getCounty()).isEqualTo(FULTON);
    }
}
