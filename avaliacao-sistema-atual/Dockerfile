# Estágio 0: Imagem suficiente com o uso de variáveis de ambiente para a conexão com o BD

#FROM tomcat:10.1.48-jdk21

#EXPOSE 8080

#COPY target/avaliacao-sistema.war /usr/local/tomcat/webapps/avaliacao-sistema.war

#CMD ["catalina.sh", "run"]

# Fim do Estágio 0

### ROLÊ DESNECESSARIO COM UM novo WAR modificado

# Estágio 1: Build/Preparação para modificar o WAR
FROM alpine:latest AS builder

# Instala ferramentas necessárias (unzip, zip, sed)
RUN apk add --no-cache unzip zip sed

# Copia o WAR para o estágio de modificação
COPY target/avaliacao-sistema.war /tmp/avaliacao-sistema.war

# Diretório de trabalho
WORKDIR /tmp/app

# --- Processo de Substituição do persistence.xml ---
RUN unzip /tmp/avaliacao-sistema.war -d .


# Define as variáveis de substituição:
    ENV NEW_HOST_PORT="db:3306"
    ENV NEW_DB_USER="unifae_med_app"
    ENV NEW_DB_PASS="unifae_med_app"    

# Comando SED: Faz as 3 substituições
RUN find . -name "persistence.xml" -exec sed -i \
    # 1. Substitui Host/Porta
    -e "s|localhost:3306|${NEW_HOST_PORT}|g" \
    \
    # 2. Substitui Usuário (assumindo que era 'root')
    -e "s|value=\"root\"|value=\"${NEW_DB_USER}\"|g" \
    \
    # 3. Substitui a linha de SENHA VAZIA pela linha com a nova senha (MAIS SEGURO)
    -e "s|<property name=\"jakarta.persistence.jdbc.password\" value=\"\"/>|<property name=\"jakarta.persistence.jdbc.password\" value=\"${NEW_DB_PASS}\"/>|g" \
    \
    {} \;

# Recompacta o WAR
RUN zip -r /tmp/avaliacao-sistema-modificado.war .

# ----------------------------------------------------------------------------------

# Estágio 2: Imagem Final (Tomcat)
FROM tomcat:10.1.48-jdk21

EXPOSE 8080

# Copia o WAR MODIFICADO do estágio anterior
COPY --from=builder /tmp/avaliacao-sistema-modificado.war /usr/local/tomcat/webapps/avaliacao-sistema.war

# CMD padrão (agora deve funcionar com a URL/USER/PASS corrigidos)
CMD ["catalina.sh", "run"]
