#!/usr/bin/env groovy
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.2')
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import net.sf.json.JSON

import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.TEXT

def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText('{ "name": "John Doe" } /* some comment */')

System.out.println("Obj "+object);
//def http = new HTTPBuilder('https://api.instagram.com/v1/tags/hollyscake/media/recent?client_id=b5b626821f614efe85807985795f47c5')
//def http = new HTTPBuilder( 'http://api.openweathermap.org/data/2.5/' )

//https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=API_KEY
def http = new HTTPBuilder('https://api.instagram.com/v1/')
http.get( path: 'tags/hollyscake/media/recent', query: ['client_id': 'b5b626821f614efe85807985795f47c5'] ) { resp, json ->

    println resp.status

    println json['data'][0]["images"]
}
