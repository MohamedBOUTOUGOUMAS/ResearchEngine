FROM openjdk:8-jdk-alpine

EXPOSE 8080

RUN mkdir /app

RUN mkdir /indexesTable

RUN mkdir /page-rank-map

RUN mkdir /betweennes-map

RUN mkdir /suggestions-map

RUN mkdir /books-master

COPY run.sh /app/

RUN rm -rf /app/*.jar

COPY target/*.jar /app/

COPY indexesTable /indexesTable/

COPY books-master /books-master/

COPY page-rank-map /page-rank-map/

COPY betweennes-map /betweennes-map/

COPY suggestions-map /suggestions-map/

ENTRYPOINT [ "sh", "/app/run.sh" ]