@Grab('org.postgresql:postgresql:9.3-1103-jdbc41')
@GrabConfig(systemClassLoader = true)

import groovy.sql.*
import org.postgresql.ds.*

twoWordStatesAbbrev = ['SD', 'NC', 'ND', 'NH', 'NJ', 'NM', 'NY', 'RI', 'SC', 'SD', 'WV']
twoWordStates = ['NORTH CAROLINA', 'NORTH DAKOTA', 'NEW HAMPSHIRE', 'NEW JERSEY',
                 'NEW MEXICO', 'NEW YORK', 'RHODE ISLAND', 'SOUTH CAROLINA', 'SOUTH DAKOTA', 'WEST VIRGINIA']

alaskaCodes = ['261', '270', '220', '188', '185', '180', '170', '164', '150', '122', '240', '070', '050', '020', '016', '013']
county = "COUNTY"
DC = "DISTRICT OF COLUMBIA"
def username = 'ttis', password = 'ttis', database = 'spoonscore', server = 'centos-vm-local'
def ds = new org.postgresql.ds.PGSimpleDataSource(
        databaseName: database, user: username, password: password, serverName: server
)
def db = new Sql(ds)
db.execute 'drop table if exists zip_codes'
db.execute '''
    CREATE TABLE zip_codes (
    id SERIAL primary key,
    country varchar(2) NOT NULL,
    zip_code integer NOT NULL,
    city varchar(30) NOT NULL,
    state varchar(30) NOT NULL,
    state_abbrev varchar(2) NOT NULL,
    county varchar(30) NOT NULL,
    county_code varchar(5) NOT NULL,
    latitude real NOT NULL,
    longitude real NOT NULL
)
'''
db.execute '''
    CREATE UNIQUE INDEX ON zip_codes (zip_code)
'''

db.execute '''
    CREATE INDEX ON zip_codes (city)
'''

db.execute '''
    CREATE INDEX ON zip_codes (state_abbrev)
'''

//def zipCodeFile = new File('testUS.txt')
//def zipCodeFile = new File('US/US.txt')
def zipCodeFile = new File('ga.txt')
def zipCodeInfo = zipCodeFile.readLines()
zipCodeInfo.each {
    lineArray = it.split("\t")

    row = getRowObject(lineArray)
    println "row ${row}, size ${row.size()}"
    try {
        db.execute 'insert into zip_codes(country,zip_code,city,state,state_abbrev,county,county_code,latitude,longitude) values(?,?,?,?,?,?,?,?,?)', row
    } catch (Exception e) {
        println "e ${e.getMessage()}, rowData ${lineArray}, size ${lineArray.length}"
    }
}

def getRowObject(String[] rowData) {
    def row = [];
    row = [rowData[0], rowData[1].toInteger(), rowData[2], rowData[3],rowData[4],rowData[5],
          rowData[6], Float.parseFloat(rowData[9]), Float.parseFloat(rowData[10])]
    countyData = rowData[5].toUpperCase()
    try {
        if (countyData.contains(county)) {
            index = countyData.indexOf(county)
            countyName = countyData.substring(0,index)
            row = [rowData[0], rowData[1].toInteger(), rowData[2].toUpperCase(), rowData[3].toUpperCase(),rowData[4],countyName,
                   rowData[6], Float.parseFloat(rowData[9]), Float.parseFloat(rowData[10])]
        } else {
            // Probably DC
            if (null == countyData || countyData.isEmpty()){
                countyData = DC
            }
            row = [rowData[0], rowData[1].toInteger(), rowData[2].toUpperCase(), rowData[3].toUpperCase(),rowData[4],countyData,
                   rowData[6], Float.parseFloat(rowData[9]), Float.parseFloat(rowData[10])]
        }
    } catch (Exception e) {
        println "e ${e.getMessage()}, rowData ${rowData}, size ${rowData.length}"
    }
    return row
}