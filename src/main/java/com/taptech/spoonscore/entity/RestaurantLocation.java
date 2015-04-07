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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tap
 */
@Entity
@Table(name = "restaurant_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RestaurantLocation.findAll", query = "SELECT r FROM RestaurantLocation r"),
    @NamedQuery(name = "RestaurantLocation.findById", query = "SELECT r FROM RestaurantLocation r WHERE r.id = :id"),
    @NamedQuery(name = "RestaurantLocation.findByRestaurantId", query = "SELECT r FROM RestaurantLocation r WHERE r.restaurantId = :restaurantId"),
    @NamedQuery(name = "RestaurantLocation.findByLatitude", query = "SELECT r FROM RestaurantLocation r WHERE r.latitude = :latitude"),
    @NamedQuery(name = "RestaurantLocation.findByLongitude", query = "SELECT r FROM RestaurantLocation r WHERE r.longitude = :longitude"),
    @NamedQuery(name = "RestaurantLocation.findByCounty", query = "SELECT r FROM RestaurantLocation r WHERE r.county = :county"),
    @NamedQuery(name = "RestaurantLocation.findByZipCode", query = "SELECT r FROM RestaurantLocation r WHERE r.zipCode = :zipCode"),
    @NamedQuery(name = "RestaurantLocation.findByCity", query = "SELECT r FROM RestaurantLocation r WHERE r.city = :city"),
    @NamedQuery(name = "RestaurantLocation.findByStateAbbrev", query = "SELECT r FROM RestaurantLocation r WHERE r.stateAbbrev = :stateAbbrev")})
public class RestaurantLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "restaurant_id")
    private String restaurantId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private double latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private double longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "county")
    private String county;
    @Column(name = "zip_code")
    private Integer zipCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "state_abbrev")
    private String stateAbbrev;

    public RestaurantLocation() {
    }

    public RestaurantLocation(Integer id) {
        this.id = id;
    }

    public RestaurantLocation(Integer id, String restaurantId, float latitude, float longitude, String county, String city, String stateAbbrev) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.county = county;
        this.city = city;
        this.stateAbbrev = stateAbbrev;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public void setStateAbbrev(String stateAbbrev) {
        this.stateAbbrev = stateAbbrev;
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
        if (!(object instanceof RestaurantLocation)) {
            return false;
        }
        RestaurantLocation other = (RestaurantLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RestaurantLocation{");
        sb.append("id=").append(id);
        sb.append(", restaurantId='").append(restaurantId).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", county='").append(county).append('\'');
        sb.append(", zipCode=").append(zipCode);
        sb.append(", city='").append(city).append('\'');
        sb.append(", stateAbbrev='").append(stateAbbrev).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
