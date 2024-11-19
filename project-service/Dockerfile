# Etapa de construcción
FROM maven:3.9.5-eclipse-temurin-17 AS build

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
COPY --from=build /usr/src/app/target/project-service-0.0.1-SNAPSHOT.jar project-service.jar

# Exponer el puerto
EXPOSE 8082

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "project-service.jar"]
