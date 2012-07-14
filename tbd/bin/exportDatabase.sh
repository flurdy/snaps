#!/bin/bash
mvn -o -DskipTests -P dbunit dbunit:export;
