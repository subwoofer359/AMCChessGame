FROM tomcat:7.0.77-jre8

WORKDIR /
ADD AMCChessGame.war $CATALINA_HOME/webapps/
ADD tomcat-users.xml $CATALINA_HOME/conf/
run mkdir temp

EXPOSE 8080

CMD ["catalina.sh", "run"]

