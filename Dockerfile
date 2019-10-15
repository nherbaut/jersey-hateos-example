FROM java:8
RUN mkdir -p /opt/local/sbin
COPY target/rest-jersey-hateos-1.0-SNAPSHOT.jar /opt/local/sbin/smartStub.jar
CMD java -jar /opt/local/sbin/smartStub.jar