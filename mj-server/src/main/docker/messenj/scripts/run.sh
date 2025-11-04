#!/bin/bash
DEBUG_PARAM=""
if [ "true" == "$DEBUG" ]; then
	DEBUG_PARAM="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=0.0.0.0:8787,suspend=n"
fi

echo "$(date +"%T"): =======Messen-J Variables========"
echo "$(date +"%T"): PROFILE=$PROFILE"
echo "$(date +"%T"): DB_URL=$DB_URL"
echo "$(date +"%T"): DB_USER=$DB_USER"
echo "$(date +"%T"): DB_PASSWORD=$DB_PASSWORD"
echo "$(date +"%T"): JAR_LOCATION=$JAR_LOCATION"
echo "$(date +"%T"): BROKER_URL=$BROKER_URL"
echo "$(date +"%T"): BROKER_USERNAME=$BROKER_USERNAME"
echo "$(date +"%T"): BROKER_PASSWORD=$BROKER_PASSWORD"
echo "$(date +"%T"): ========================================================"

COMMAND="java $DEBUG_PARAM $JAVA_OPTS \
  -Dspring.datasource.url=$DB_URL \
  -Dspring.datasource.username=$DB_USER \
  -Dspring.datasource.password=$DB_PASSWORD \
  -Dspring.profiles.active=$PROFILE \
  -Ddebug=false \
  -Dmsg.jms.brokerUrl=$BROKER_URL\
  -Dmsg.jms.username=$BROKER_USERNAME\
  -Dmsg.jms.password=$BROKER_PASSWORD\
	-jar $JAR_LOCATION"
$COMMAND
