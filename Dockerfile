FROM eclipse-temurin:8-jre

ENV CATALINA_HOME=/opt/tomcat
ENV PATH="$CATALINA_HOME/bin:$PATH"

WORKDIR /opt

RUN apt-get update && apt-get install -y curl \
    && curl -O https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.99/bin/apache-tomcat-8.5.99.tar.gz \
    && tar xvf apache-tomcat-8.5.99.tar.gz \
    && mv apache-tomcat-8.5.99 tomcat \
    && rm apache-tomcat-8.5.99.tar.gz \
    && apt-get clean

RUN rm -rf /opt/tomcat/webapps/*

COPY dist/*.war /opt/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]