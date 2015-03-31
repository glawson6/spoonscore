#!/bin/bash
curl -H "Content-Type: application/json" \
     -d @sampleRestaurant.json \
     http://localhost:8080/api/restaurant/inspectionUpdate
