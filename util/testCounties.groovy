@Grab('org.postgresql:postgresql:9.3-1103-jdbc41')

import groovy.sql.*
import org.postgresql.ds.*

def username = 'ttis', password = 'ttis', database = 'spoonscore', server = 'centos-vm-local'
def ds = new org.postgresql.ds.PGSimpleDataSource(
        databaseName: database, user: username, password: password, serverName: server
)
def db = new Sql(ds)

def latitude = 34
def longitude = -85.18
def maxdistance = 7700248.670135693
def mindistance = 2168301.069568895
def normalLat = 0.0
def normalLong = 0.0
distance = distanceCalc(latitude, normalLat, longitude, normalLong,
        0.0, 0.0)
normal_distance = normalizedDistance(distance, maxdistance,mindistance)
println "distance => ${distance}, normalized distance => ${normal_distance}"

def query = "SELECT * FROM \n" +
        "(\n" +
        "  (SELECT id, normal_distance, distance, name, state_name FROM counties WHERE normal_distance >= ?  and state_abbrev = 'GA' ORDER BY normal_distance LIMIT 1) \n" +
        "  UNION ALL\n" +
        "  (SELECT id, normal_distance, distance, name, state_name FROM counties WHERE normal_distance < ?  and state_abbrev = 'GA' ORDER BY normal_distance DESC LIMIT 1)\n" +
        ") somerows\n" +
        "ORDER BY abs(?-normal_distance) LIMIT 1"
def query2 = "SELECT * FROM \n" +
        "(\n" +
        "  (SELECT id, normal_distance, distance, name, state_name, state_abbrev FROM counties WHERE normal_distance >= ? ORDER BY normal_distance) \n" +
        "  UNION ALL\n" +
        "  (SELECT id, normal_distance, distance, name, state_name, state_abbrev FROM counties WHERE normal_distance < ? ORDER BY normal_distance)\n" +
        ") somerows\n" +
        "where state_abbrev = 'GA'" +
        "ORDER BY abs(?-normal_distance)"

def countyData = db.firstRow(query2,[normal_distance,normal_distance,normal_distance])

println "County => ${countyData}"

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