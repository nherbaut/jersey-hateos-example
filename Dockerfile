FROM openjdk:11-jre-slim
RUN mkdir -p /opt/local/sbin
COPY target/rest-jersey-hateos-1.0-SNAPSHOT.jar /opt/local/sbin/smartStub.jar
CMD java -jar /opt/local/sbin/smartStub.jar
