FROM openjdk

VOLUME /var/jenkins_home
RUN curl -0 {{http://localhost:8080/job/cicd/27/execution/node/3/ws/target/periodservice-0.0.1.jar}} -o periodservice.jar
RUN sh -c 'touch /periodservice.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/periodservice.jar"]
