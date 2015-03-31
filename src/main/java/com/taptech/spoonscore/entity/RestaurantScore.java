/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taptech.spoonscore.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tap
 */
@Entity
@Table(name = "restaurant_score", catalog = "spoonscore", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"restaurant_id"})})
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RestaurantScore.findAll", query = "SELECT r FROM RestaurantScore r"),
        @NamedQuery(name = "RestaurantScore.findById", query = "SELECT r FROM RestaurantScore r WHERE r.id = :id"),
        @NamedQuery(name = "RestaurantScore.findByCompanyName", query = "SELECT r FROM RestaurantScore r WHERE r.companyName = :companyName"),
        @NamedQuery(name = "RestaurantScore.findByCompanyGrade", query = "SELECT r FROM RestaurantScore r WHERE r.companyGrade = :companyGrade"),
        @NamedQuery(name = "RestaurantScore.findByCompanyScore", query = "SELECT r FROM RestaurantScore r WHERE r.companyScore = :companyScore"),
        @NamedQuery(name = "RestaurantScore.findByRating", query = "SELECT r FROM RestaurantScore r WHERE r.rating = :rating"),
        @NamedQuery(name = "RestaurantScore.findByInspectionLink", query = "SELECT r FROM RestaurantScore r WHERE r.inspectionLink = :inspectionLink"),
        @NamedQuery(name = "RestaurantScore.findByZipCode", query = "SELECT r FROM RestaurantScore r WHERE r.zipCode = :zipCode"),
        @NamedQuery(name = "RestaurantScore.findByCity", query = "SELECT r FROM RestaurantScore r WHERE r.city = :city"),
        @NamedQuery(name = "RestaurantScore.findByCounty", query = "SELECT r FROM RestaurantScore r WHERE r.county = :county"),
        @NamedQuery(name = "RestaurantScore.findByInspectionSearchLink", query = "SELECT r FROM RestaurantScore r WHERE r.inspectionSearchLink = :inspectionSearchLink"),
        @NamedQuery(name = "RestaurantScore.findByRestaurantId", query = "SELECT r FROM RestaurantScore r WHERE r.restaurantId = :restaurantId"),
        @NamedQuery(name = "RestaurantScore.findByFoundBy", query = "SELECT r FROM RestaurantScore r WHERE r.foundBy = :foundBy")})
public class RestaurantScore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    @Column(name = "company_grade")
    private Character companyGrade;
    @Column(name = "company_score")
    private Integer companyScore;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 8, scale = 8)
    private Float rating;
    @Size(max = 100)
    @Column(name = "inspection_link", length = 100)
    private String inspectionLink;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zip_code", nullable = false)
    private int zipCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(nullable = false, length = 30)
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(nullable = false, length = 30)
    private String county;
    @Size(max = 250)
    @Column(name = "inspection_search_link", length = 250)
    private String inspectionSearchLink;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "restaurant_id", nullable = false, length = 75)
    private String restaurantId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "found_by", nullable = false, length = 20)
    private String foundBy;

    public RestaurantScore() {
    }

    public RestaurantScore(Integer id) {
        this.id = id;
    }

    public RestaurantScore(Integer id, String companyName, int zipCode, String city, String county, String restaurantId, String foundBy) {
        this.id = id;
        this.companyName = companyName;
        this.zipCode = zipCode;
        this.city = city;
        this.county = county;
        this.restaurantId = restaurantId;
        this.foundBy = foundBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Character getCompanyGrade() {
        return companyGrade;
    }

    public void setCompanyGrade(Character companyGrade) {
        this.companyGrade = companyGrade;
    }

    public Integer getCompanyScore() {
        return companyScore;
    }

    public void setCompanyScore(Integer companyScore) {
        this.companyScore = companyScore;
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

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getInspectionSearchLink() {
        return inspectionSearchLink;
    }

    public void setInspectionSearchLink(String inspectionSearchLink) {
        this.inspectionSearchLink = inspectionSearchLink;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getFoundBy() {
        return foundBy;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RestaurantScore)) {
            return false;
        }
        RestaurantScore other = (RestaurantScore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.taptech.spoonscore.entity.RestaurantScore[ id=" + id + " ]";
    }

}
