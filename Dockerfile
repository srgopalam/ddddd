FROM openjdk

VOLUME /tmp
ADD maven/periodservice-0.0.1.jar periodservice.jar
RUN sh -c 'touch /periodservice.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/periodservice.jar"]
