#!/bin/bash
curl -H "Content-Type: application/json" \
     -d @search1.json \
     http://localhost:8080/api/restaurant/search
