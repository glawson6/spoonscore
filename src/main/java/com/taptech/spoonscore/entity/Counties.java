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
@Table(catalog = "spoonscore", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Counties.findAll", query = "SELECT c FROM Counties c"),
    @NamedQuery(name = "Counties.findById", query = "SELECT c FROM Counties c WHERE c.id = :id"),
    @NamedQuery(name = "Counties.findByFeatureId", query = "SELECT c FROM Counties c WHERE c.featureId = :featureId"),
    @NamedQuery(name = "Counties.findByFipsCountyCd", query = "SELECT c FROM Counties c WHERE c.fipsCountyCd = :fipsCountyCd"),
    @NamedQuery(name = "Counties.findByName", query = "SELECT c FROM Counties c WHERE c.name = :name"),
    @NamedQuery(name = "Counties.findByPrimaryLatitude", query = "SELECT c FROM Counties c WHERE c.primaryLatitude = :primaryLatitude"),
    @NamedQuery(name = "Counties.findByPrimaryLongitude", query = "SELECT c FROM Counties c WHERE c.primaryLongitude = :primaryLongitude"),
    @NamedQuery(name = "Counties.findByNormalDistance", query = "SELECT c FROM Counties c WHERE c.normalDistance = :normalDistance"),
    @NamedQuery(name = "Counties.findByDistance", query = "SELECT c FROM Counties c WHERE c.distance = :distance"),
    @NamedQuery(name = "Counties.findByStateAbbrev", query = "SELECT c FROM Counties c WHERE c.stateAbbrev = :stateAbbrev"),
    @NamedQuery(name = "Counties.findByStateName", query = "SELECT c FROM Counties c WHERE c.stateName = :stateName")})
public class Counties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feature_id", nullable = false)
    private int featureId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fips_county_cd", nullable = false)
    private int fipsCountyCd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false, length = 50)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "primary_latitude", nullable = false)
    private float primaryLatitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "primary_longitude", nullable = false)
    private float primaryLongitude;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "normal_distance", precision = 8, scale = 8)
    private Float normalDistance;
    @Column(precision = 8, scale = 8)
    private Float distance;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "state_abbrev", nullable = false, length = 2)
    private String stateAbbrev;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "state_name", nullable = false, length = 35)
    private String stateName;

    public Counties() {
    }

    public Counties(Integer id) {
        this.id = id;
    }

    public Counties(Integer id, int featureId, int fipsCountyCd, String name, float primaryLatitude, float primaryLongitude, String stateAbbrev, String stateName) {
        this.id = id;
        this.featureId = featureId;
        this.fipsCountyCd = fipsCountyCd;
        this.name = name;
        this.primaryLatitude = primaryLatitude;
        this.primaryLongitude = primaryLongitude;
        this.stateAbbrev = stateAbbrev;
        this.stateName = stateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public int getFipsCountyCd() {
        return fipsCountyCd;
    }

    public void setFipsCountyCd(int fipsCountyCd) {
        this.fipsCountyCd = fipsCountyCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrimaryLatitude() {
        return primaryLatitude;
    }

    public void setPrimaryLatitude(float primaryLatitude) {
        this.primaryLatitude = primaryLatitude;
    }

    public float getPrimaryLongitude() {
        return primaryLongitude;
    }

    public void setPrimaryLongitude(float primaryLongitude) {
        this.primaryLongitude = primaryLongitude;
    }

    public Float getNormalDistance() {
        return normalDistance;
    }

    public void setNormalDistance(Float normalDistance) {
        this.normalDistance = normalDistance;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public void setStateAbbrev(String stateAbbrev) {
        this.stateAbbrev = stateAbbrev;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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
        if (!(object instanceof Counties)) {
            return false;
        }
        Counties other = (Counties) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.taptech.spoonscore.entity.Counties[ id=" + id + " ]";
    }
    
}
