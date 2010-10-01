#!/bin/bash
mvn -o -DskipTests \
	-Dhibernate.dialect=org.hibernate.dialect.HSQLDialect \
    -P hbm-export,sqlmaven \
    compile hibernate3:hbm2ddl sql:execute
