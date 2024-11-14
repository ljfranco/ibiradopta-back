# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR de la aplicación (asegúrate de que el JAR ya esté construido)
COPY target/users-service-0.0.1-SNAPSHOT.jar /usr/app/user-service.jar

# Establecer el directorio de trabajo
WORKDIR /usr/app

# Exponer el puerto de la aplicación
EXPOSE 8081

# Ejecutar la aplicación
CMD ["java", "-jar", "user-service.jar"]
