#!/bin/bash

facade=http://localhost:8080/facade
messages1=http://localhost:8082/messages
messages2=http://localhost:8085/messages
logging1=http://localhost:8081/logging
logging2=http://localhost:8083/logging
logging3=http://localhost:8084/logging

for i in {1..10}; do
  curl -d "msg${i}" -H "Content-Type: text/plain" -X POST ${facade}
  echo "Written msg$i"
done

m1resp=$(curl -sw "%{http_code}" $messages1)
m2resp=$(curl -sw "%{http_code}" $messages2)
m1body="${m1resp:0:${#m1resp}-3}"
m2body="${m2resp:0:${#m2resp}-3}"
echo "Messages service node1: $m1body"
echo "Messages service node2: $m2body"

for i in {1..5}; do
  fresp=$(curl -sw "%{http_code}" $facade)
  fbody="${fresp:0:${#fresp}-3}"
  echo "$i Facade get messages: $fbody"
done
