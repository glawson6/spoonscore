@Grab('org.postgresql:postgresql:9.3-1103-jdbc41')
@GrabConfig(systemClassLoader=true)

import groovy.sql.*
import org.postgresql.ds.*


def username = 'ttis', password = 'ttis', database = 'spoonscore', server = 'centos-vm-local'
def ds = new org.postgresql.ds.PGSimpleDataSource(
        databaseName: database, user: username, password: password, serverName: server
)
def db = new Sql(ds)

/*
    private String companyPhone;
    private String companyName;
    private String companyAddress;
    private String companyInspectionGrade;
    private String companyInspectionScore;
    private Float rating;
    private String ratingCommentsLink;
    private String inspectionLink;
    private String foundBy;
    private String restaurantID;
    private String inspectionSearchLink;
    private String imageURL;
    private Integer inspectionReportTries = 0;
    private Boolean foundReport = false;
 */
db.execute 'drop table if exists restaurant_details'
db.execute '''
    CREATE TABLE restaurant_details (
    id SERIAL primary key,
    restaurant_id varchar(75) NOT NULL,
    found_by varchar(20) NOT NULL,
    company_name varchar(100) NOT NULL,
    company_phone varchar(20) NOT NULL,
    company_grade char(1),
    company_score integer,
    company_address varchar(150),
    rating real,
    rating_comments_link varchar(150),
    image_url varchar(100),
    inspection_report_link varchar(150),
    inspection_link varchar(150),
    inspection_search_link varchar(250)
)
'''
/*
 private Double latitude;
    private Double longitude;
    private String county;
    private String zipCode;
    private String city;
    private String state;
    private String address;
    private String id;
    private String foundBy;
 */
db.execute '''
    CREATE UNIQUE INDEX ON restaurant_details (restaurant_id)
'''

db.execute 'drop table if exists restaurant_location'
db.execute '''
    CREATE TABLE restaurant_location (
    id SERIAL primary key,
    restaurant_id varchar(75) NOT NULL,
    latitude real NOT NULL,
    longitude real NOT NULL,
    county varchar(50) NOT NULL,
    zip_code integer,
    city varchar(50) NOT NULL,
    state_abbrev varchar(2) NOT NULL
)
'''

db.execute '''
    CREATE UNIQUE INDEX ON restaurant_location (restaurant_id)
'''
