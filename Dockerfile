# Usar la imagen base de Tomcat
FROM tomcat:10.1-jdk17-corretto

# Remover la aplicación por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar el WAR generado en el contenedor Tomcat
COPY target/integracion-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8080 para acceso externo
EXPOSE 8081

# Comando para ejecutar Tomcat
CMD ["catalina.sh", "run"]
