# Estágio 0: Imagem suficiente com o uso de variáveis de ambiente para a conexão com o BD

FROM tomcat:10.1.48-jdk21

EXPOSE 8080

ENV DB_URL="jdbc:mariadb://db:3306/unifae_med_app?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=America/Sao_Paulo" \
    DB_USER="unifae_med_app" \
    DB_PASS="unifae_med_app"
	
#ENV CATALINA_OPTS="-DDB_URL=${DB_URL} -DDB_USER=${DB_USER} -DDB_PASSWORD=${DB_PASSWORD}${JAVA_OPTS}"
ENV JAVA_OPTS="-DDB_URL=\"${DB_URL}\" -DDB_USER=\"${DB_USER}\" -DDB_PASS=\"${DB_PASS}\" ${JAVA_OPTS}"

COPY target/avaliacao-sistema.war /usr/local/tomcat/webapps/avaliacao-sistema.war

CMD ["catalina.sh", "run"]