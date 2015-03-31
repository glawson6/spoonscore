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
 */
db.execute 'drop table if exists restaurant_score'
db.execute '''
    CREATE TABLE restaurant_score (
    id SERIAL primary key,
    company_name varchar(100) NOT NULL,
    company_grade char(1),
    company_score integer,
    rating real,
    inspection_link varchar(100),
    zip_code integer NOT NULL,
    city varchar(30) NOT NULL,
    county varchar(30) NOT NULL,
    inspection_search_link varchar(250),
    restaurant_id varchar(75) NOT NULL,
    found_by varchar(20) NOT NULL
)
'''

db.execute '''
    CREATE UNIQUE INDEX ON restaurant_score (restaurant_id)
'''
