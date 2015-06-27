FROM java:8u45-jdk

MAINTAINER William Markito <wmarkito@pivotal.io>
MAINTAINER Ashvin Agrawal <ashvin@pivotal.io>

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64


RUN apt-get update && apt-get -y install maven git
RUN mvn -version
RUN git clone https://github.com/ashvina/teeny
RUN cd teeny && /usr/bin/mvn package

ENV PATH /usr/lib/jvm/java-8-openjdk-amd64/bin:$PATH

CMD java -jar /teeny/target/teeny-0.1-SNAPSHOT.war

EXPOSE  8080

