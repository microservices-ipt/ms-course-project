##
To run kafka on docker container:

1. run docker daemon
2. type in terminal
```
docker compose -f kafka-docker-compose.yaml up
```

##
To run consul:

1. Install consul
2. type in terminal
```
consul agent -dev
```
3. open http://localhost:8500/ui
4. create configs in key/value storage in consul <br>
   -- key: config/facade-service/spring.consul.kafka.topic, value: messages <br>
   -- key: config/messages-service/spring.consul.kafka.topic, value: messages <br>
   -- key: config/logging-service/spring.consul.hazelcast.map, value: logging-map
5. run services specifying post via service_port=\<port\> environmental variable
   
