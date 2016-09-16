#LPM PROJECT

##CORE MODULE

This module handles the user magament of LPM and can run completely stand-alone. This module acts as a authorization & authentication service and will manage the user base of the LPM system.

[![CircleCI](https://circleci.com/gh/BrickbitSolutions/lpm-core/tree/develop.svg?style=shield)](https://circleci.com/gh/BrickbitSolutions/lpm-core/tree/develop) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/43cd9de7535549d4ad03cf4892855af3)](https://www.codacy.com/app/soulscammer/lpm-core?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrickbitSolutions/lpm-core&amp;utm_campaign=Badge_Grade) [![codecov](https://codecov.io/gh/BrickbitSolutions/lpm-core/branch/develop/graph/badge.svg)](https://codecov.io/gh/BrickbitSolutions/lpm-core)

###Installation

Compile the code with Gradle 3.0 or with the delivered Gradle wrapper. This will generate a .jar in the folder build/libs

```sh
$ Gradle build
```

To start the module use the following command:

```
$ java -jar lpm-core-1.0-SNAPSHOT.jar --spring.config.location=file:/path/to/application.properties
```

or the following if you don't want to pass any configuration:

```
$ java -jar lpm-core-1.0-SNAPSHOT.jar
```

The module can run completely stand-alone without any form of configuration. In this case it will start with a in-memory H2 database and the Spring Boot defaults on port 8080. 
However, we do recommend to connect it with a postgreSQL database and configure it further to you liking using the known [spring boot application properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).