# Etapa de construcción
FROM maven:3.8.8-openjdk-17 AS build

# Establecer el directorio de trabajo
WORKDIR /usr/src/app

# Copiar archivos necesarios para construir
COPY pom.xml .
COPY src ./src

# Construir el proyecto y empaquetar el JAR
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /usr/app

# Copiar el JAR generado desde la etapa de construcción
COPY --from=build /usr/src/app/target/api-gateway-0.0.1-SNAPSHOT.jar gateway-service.jar

# Exponer el puerto
EXPOSE 9090

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "gateway-service.jar"]

