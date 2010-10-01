#!/bin/bash
mvn -o -DskipTests \
	-Dhibernate.dialect=org.hibernate.dialect.HSQLDialect \
    -P hbm-export,sqlmaven,dbunit \
    clean compile hibernate3:hbm2ddl sql:execute dbunit:operation jetty:run
