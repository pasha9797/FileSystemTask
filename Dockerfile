FROM tomcat:8.0.20-jre8
RUN mkdir /usr/local/tomcat/root
RUN mkdir /usr/local/tomcat/root/dir1
RUN mkdir /usr/local/tomcat/root/dir2
RUN mkdir /usr/local/tomcat/root/dir3
RUN mkdir /usr/local/tomcat/root/dir1/dir1-1
COPY target/FileSystemTask-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/