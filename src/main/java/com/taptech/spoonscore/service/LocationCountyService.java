package com.taptech.spoonscore.service;

/**
 * Created by tap on 3/29/15.
 */
public interface LocationCountyService {

    public String findCountyFromZipCode(String zipCode);
    public String findCountyFromCityStateAbbreviation(String city, String state);
}
