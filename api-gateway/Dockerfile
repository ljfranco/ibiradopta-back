# Imagen base para construir el proyecto
FROM maven:3.8.8-openjdk-17 AS build

# Establecer el directorio de trabajo
WORKDIR /usr/src/app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Construir el proyecto y empaquetar el JAR
RUN mvn clean package -DskipTests


# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR de la aplicación (asegúrate de que el JAR ya esté construido)
COPY target/api-gateway-0.0.1-SNAPSHOT.jar /usr/app/gateway-service.jar

# Establecer el directorio de trabajo
WORKDIR /usr/app

# Exponer el puerto de la aplicación
EXPOSE 9090

# Ejecutar la aplicación
CMD ["java", "-jar", "gateway-service.jar"]
