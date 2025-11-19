FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1.48-jdk21

ENV DB_URL="jdbc:mariadb://db:3306/unifae_med_app?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=America/Sao_Paulo" \
    DB_USER="unifae_med_app" \
    DB_PASS="unifae_med_app"
	
ENV JAVA_OPTS="-DDB_URL=\"${DB_URL}\" -DDB_USER=\"${DB_USER}\" -DDB_PASS=\"${DB_PASS}\" ${JAVA_OPTS}"

WORKDIR /usr/local/tomcat/webapps
RUN rm -rf ROOT
COPY --from=builder /app/target/*.war ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
