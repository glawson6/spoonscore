package com.taptech.spoonscore.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by tap on 3/29/15.
 */
@Service
public class DefaultLocationCountyService implements LocationCountyService {

    private final Logger log = LoggerFactory.getLogger(DefaultLocationCountyService.class);
    private static final String ZIP_CODE_URL = "http://www.unitedstateszipcodes.org/";
    private static final String COUNTY_SELECTOR = "div#zip-info.col-xs-12.col-sm-6.col-md-12 table tbody tr:nth-of-type(3) td";

    @Override
    public String findCountyFromZipCode(String zipCode){
        String county = null;
        String url = ZIP_CODE_URL + zipCode + "/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36").get();
            Elements countyElement = doc.select(COUNTY_SELECTOR);
            log.info("County Element html => {}",countyElement.html());
            county = countyElement.html();
        } catch (Exception e){
            log.error("Exception ",e);
            e.printStackTrace();
        }
        return county;
    }

    @Override
    public String findCountyFromCityStateAbbreviation(String city, String state) {
        return null;
    }
}
