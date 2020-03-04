FROM springio/spring-mongo-base

EXPOSE 8080

RUN mkdir /app

RUN mkdir /indexesTable

RUN mkdir /books-master

COPY run.sh /app/

RUN rm -rf /app/*.jar

COPY target/*.jar /app/

COPY indexesTable /indexesTable/

COPY books-master /books-master/

ENTRYPOINT [ "sh", "/app/run.sh" ]