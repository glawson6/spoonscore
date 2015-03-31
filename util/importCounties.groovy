@Grab('org.postgresql:postgresql:9.3-1103-jdbc41')
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.2')
//@GrabConfig(systemClassLoader=true)

import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import groovy.sql.*
import org.postgresql.ds.*

def username = 'ttis', password = 'ttis', database = 'spoonscore', server = 'centos-vm-local'
def ds = new org.postgresql.ds.PGSimpleDataSource(
        databaseName: database, user: username, password: password, serverName: server
)
def db = new Sql(ds)
db.execute 'drop table if exists counties'
db.execute '''
    CREATE TABLE counties (
    id SERIAL primary key,
    feature_id integer NOT NULL,
    fips_county_cd integer NOT NULL,
    name varchar(50) NOT NULL,
    primary_latitude real NOT NULL,
    primary_longitude real NOT NULL,
    normal_distance real ,
    distance real ,
    state_abbrev varchar(2) NOT NULL,
    state_name varchar(35) NOT NULL
)
'''

def stateFile = new File('states.dat')
def states = stateFile.readLines()
def mindistance = Integer.MAX_VALUE
def maxdistance = Integer.MIN_VALUE
def stateValuesMin = [:]
def stateValuesMax = [:]
def testRows = []

def normalLat = 0.0
def normalLong = 0.0
states.each {
    lineArray = it.split()
    stateName = lineArray[0]
    http = new HTTPBuilder('http://api.sba.gov/geodata/county_links_for_state_of/')
    results = null
    path = stateName + ".json"
    http.get( path: path) { resp, json ->
        //println resp.status
        results = json
    }

    // 33.7550° N, 84.3900°

    //zi=xi−min(x)/max(x)−min(x)

    results.each {
        dbStateAbbrev = it.state_abbrev != null ? it.state_abbrev : stateName.toUpperCase()
        def distance = null
        try {
            distance = distanceCalc(Float.parseFloat(it.primary_latitude), normalLat, Float.parseFloat(it.primary_longitude), normalLong,
                    0.0, 0.0)
            println "distance from ${it.name}, ${it.state_name} ${distance}"
            if (mindistance > distance){
                mindistance = distance
                stateValuesMin = ['stateName':it.state_name, 'name':it.name, 'distance':distance]
            }
            if (maxdistance < distance){
                maxdistance = distance
                stateValuesMax = ['stateName':it.state_name, 'name':it.name, 'distance':distance]
            }
        } catch (Exception e){
            println "${e.getMessage()} distance => ${distance}  it => ${it}"
        }

        try {
            row = [it.feature_id.toInteger(), it.fips_county_cd.toInteger(), it.name, Float.parseFloat(it.primary_latitude),Float.parseFloat(it.primary_longitude),distance,distance, dbStateAbbrev, it.state_name]
        } catch (Exception e) {
            println "${e.getMessage()} row => ${row}"
        }
        db.execute 'insert into counties(feature_id,fips_county_cd,name,primary_latitude,primary_longitude,normal_distance,distance,state_abbrev,state_name) values( ?,?,?,?,?,?,?,?,?)',row
    }
}
println mindistance
println stateValuesMin
println maxdistance
println stateValuesMax

states.each {
    lineArray = it.split()
    stateName = lineArray[0]
    http = new HTTPBuilder('http://api.sba.gov/geodata/county_links_for_state_of/')
    results = null
    path = stateName + ".json"
    http.get( path: path) { resp, json ->
        //println resp.status
        results = json
    }

    results.each {
        dbStateAbbrev = it.state_abbrev != null ? it.state_abbrev : stateName.toUpperCase()
        def distance = null
        def normal_distance = null
        try {
            distance = distanceCalc(Float.parseFloat(it.primary_latitude), 64.0000, Float.parseFloat(it.primary_longitude), -150.0000,
                    0.0, 0.0)
            normal_distance = normalizedDistance(distance, maxdistance,mindistance)
            println "calculated normal distance from ${it.name}, ${it.state_name} ${normal_distance}"
            db.execute 'update counties set normal_distance = ? where feature_id = ?',[normal_distance,it.feature_id.toInteger()]
        } catch (Exception e){
            println "${e.getMessage()} distance => ${normal_distance}  it => ${it}"
        }

    }
}

//def stateName = "ga"
//def http = new HTTPBuilder('http://api.sba.gov/geodata/county_links_for_state_of/')
//def results = null
//def path = stateName + ".json"
//http.get( path: path) { resp, json ->
//
//    println resp.status
//    results = json
//}
//println results[1]
//results.each {
//    def dbStateAbbrev = it.state_abbrev != null ? it.state_abbrev : stateName.toUpperCase()
//    def row = [it.feature_id.toInteger(),it.fips_county_cd.toInteger(),it.name,Float.parseFloat(it.primary_latitude),Float.parseFloat(it.primary_longitude),dbStateAbbrev,it.state_name]
//    db.execute 'insert into counties(feature_id,fips_county_cd,name,primary_latitude,primary_longitude,state_abbrev,state_name) values( ?,?,?,?,?,?,?)',row
//}

private double distanceCalc(double lat1, double lat2, double lon1, double lon2,
                        double el1, double el2) {

    final int R = 6371;

    Double latDistance = deg2rad(lat2 - lat1);
    Double lonDistance = deg2rad(lon2 - lon1);
    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
    + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;
    distance = Math.pow(distance, 2) + Math.pow(height, 2);
    return Math.sqrt(distance);
}

private double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
}

private double normalizedDistance(double currentX, double maxX, double minX){
    //println "currentX => ${currentX}, maxX => ${maxX}, minX => ${minX}"
    double denom = maxX - minX
    double distance = (currentX / denom) * 1000;
    return distance;
}


/*
| id | number |
|  1 |     .7 |
|  2 |   1.25 |
|  3 |   1.01 |
|  4 |    3.0 |

? = 2

SELECT * FROM
(
  (SELECT id, number FROM t WHERE number >= ? ORDER BY number LIMIT 1) AS above
  UNION ALL
  (SELECT id, number FROM t WHERE number < ? ORDER BY number DESC LIMIT 1) as below
)
ORDER BY abs(?-number) LIMIT 1;
 */