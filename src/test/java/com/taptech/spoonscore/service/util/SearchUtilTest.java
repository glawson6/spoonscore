package com.taptech.spoonscore.service.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tap on 3/27/15.
 */
public class SearchUtilTest {

    private final Logger log = LoggerFactory.getLogger(SearchUtilTest.class);
    private static final String TEST_ADDRESS = "265 Peachtree St NE Atlanta, GA 30308";

    @Test
    public void testHTMLParsing(){
        String address = "265 Peachtree St NE Atlanta, GA 30308";
        String firstPartOfAddress = SearchUtil.createInspectionSearchAddress(TEST_ADDRESS);
        assertThat(firstPartOfAddress).isEqualTo("265 Peachtree");
    }
}
