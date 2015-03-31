package com.taptech.spoonscore.domain;

/**
 * Created by tap on 3/26/15.
 */
public class Restaurant {
    private String companyName;
    private String companyAddress;
    private String companyInspectionGrade;
    private String companyInspectionScore;
    private Double latitude;
    private Double longitude;
    private String county;
    private String zipCode;
    private String city;
    private Float rating;
    private String inspectionLink;
    private String foundBy;
    private String restaurantID;
    private String inspectionSearchLink;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyInspectionGrade() {
        return companyInspectionGrade;
    }

    public void setCompanyInspectionGrade(String companyInspectionGrade) {
        this.companyInspectionGrade = companyInspectionGrade;
    }

    public String getCompanyInspectionScore() {
        return companyInspectionScore;
    }

    public void setCompanyInspectionScore(String companyInspectionScore) {
        this.companyInspectionScore = companyInspectionScore;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Restaurant{");
        sb.append("companyName='").append(companyName).append('\'');
        sb.append(", companyAddress='").append(companyAddress).append('\'');
        sb.append(", companyInspectionGrade='").append(companyInspectionGrade).append('\'');
        sb.append(", companyInspectionScore='").append(companyInspectionScore).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", county='").append(county).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", inspectionLink='").append(inspectionLink).append('\'');
        sb.append(", foundBy='").append(foundBy).append('\'');
        sb.append(", restaurantID='").append(restaurantID).append('\'');
        sb.append(", inspectionSearchLink='").append(inspectionSearchLink).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getInspectionLink() {
        return inspectionLink;
    }

    public void setInspectionLink(String inspectionLink) {
        this.inspectionLink = inspectionLink;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInspectionSearchLink() {
        return inspectionSearchLink;
    }

    public void setInspectionSearchLink(String inspectionSearchLink) {
        this.inspectionSearchLink = inspectionSearchLink;
    }

    public String getFoundBy() {
        return foundBy;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

}
