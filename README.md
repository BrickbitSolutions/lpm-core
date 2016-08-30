#LPM PROJECT

##CORE MODULE

This module handles the user magament of LPM.

[![CircleCI](https://circleci.com/gh/BrickbitSolutions/lpm-core/tree/develop.svg?style=shield)](https://circleci.com/gh/BrickbitSolutions/lpm-core/tree/develop) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/43cd9de7535549d4ad03cf4892855af3)](https://www.codacy.com/app/soulscammer/lpm-core?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrickbitSolutions/lpm-core&amp;utm_campaign=Badge_Grade) [![codecov](https://codecov.io/gh/BrickbitSolutions/lpm-core/branch/develop/graph/badge.svg)](https://codecov.io/gh/BrickbitSolutions/lpm-core)

###Installation

Compile the code with Gradle 2.14 or with the delivered Gradle wrapper. This will generate a .jar in the folder build/libs

```sh
$ Gradle build
```

The module can run completely stand-alone without any form of configuration. In this case it will use a in-memory H2 database. 
However, we do recommend do at least set it up with a postgreSQL database. A configuration example can be found here:

```
spring.datasource.url=jdbc:postgresql://192.168.99.100:32776/lpm
spring.datasource.username=lpm
spring.datasource.password=lpm

spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.generate-ddl=false
spring.jpa.show-sql=true

logging.level.org.hibernate=TRACE
logging.level.org.springframework.web=DEBUG

spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false
spring.jackson.serialization.indent_output=true

server.port=8081
```

You may further configure the module to your liking using the known [spring boot application properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) . To start the module use the following command:

```
$ java -jar lpm-core-1.0-SNAPSHOT.jar --spring.config.location=file:/path/to/application.properties
```

or

```
$ java -jar lpm-core-1.0-SNAPSHOT.jar
```

Spring Boot will start a container and serve the API default on localhost:configured_port(defaults to 8080).