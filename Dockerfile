# Etapa 1: Usar una imagen de Maven para construir el WAR
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y el código fuente
COPY pom.xml .
COPY src ./src

# Instalar las dependencias y construir el WAR
RUN mvn clean package

# Etapa 2: Usar la imagen base de Tomcat para desplegar la aplicación
FROM tomcat:10.1-jdk17-corretto

# Remover la aplicación por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar el WAR generado en el contenedor Tomcat
COPY --from=build /app/target/integracion-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8081 para acceso externo
EXPOSE 8081

# Comando para ejecutar Tomcat
CMD ["catalina.sh", "run"]
