@Grab('org.postgresql:postgresql:9.3-1103-jdbc41')
@GrabConfig(systemClassLoader=true)

import groovy.sql.*
import org.postgresql.ds.*


def username = 'ttis', password = 'ttis', database = 'spoonscore', server = 'centos-vm-local'
def ds = new org.postgresql.ds.PGSimpleDataSource(
        databaseName: database, user: username, password: password, serverName: server
)
def db = new Sql(ds)
db.execute 'drop table if exists states'
db.execute '''
    CREATE TABLE states (
    id SERIAL primary key,
    state_abbrev varchar(2) NOT NULL,
    state_name varchar(35) NOT NULL
)
'''


def stateFile = new File('states.dat')
def states = stateFile.readLines()

states.each {
    lineArray = it.split()
    println "state abrev => ${lineArray[0]} state name => ${lineArray[1]}"
    db.execute 'insert into states(state_abbrev,state_name) values( ?,?)',[lineArray[0], lineArray[1]]
}