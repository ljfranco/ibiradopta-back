# ibiradopta-back

# Documentation

The API Gateway documentation is available at `http://localhost:9090/swagger-ui.html`. or your host

# API Gateway

This project is an API Gateway built with Spring Boot and Spring Cloud Gateway. It acts as a single entry point for multiple backend services.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Configuration

### Environment Variables

Make sure to set the following environment variables in the `.env` file:

```dotenv
ISSUER_URI=URL of the OAuth2 issuer
CLIENT_ID=Client ID for the OAuth2 client
CLIENT_SECRET=Client secret for the OAuth2 client
REDIRECT_URI=Redirect URI for the OAuth2 client
USERS_SERVICE_URI=URL of the Users service
PROJECTS_SERVICE_URI=URL of the Projects service
PAYMENTS_SERVICE_URI=URL of the Payments service
SERVER_PORT=Port number for the API Gateway server
SWAGGER_LOGIN_CLIENT_ID=Client ID for Swagger login (implicit flow)
```

## Running the Application

To run the application, execute the following command:

```shell
mvn spring-boot:run
```

The application will be available at `http://localhost:9090`.

## Building the Application

To build the application, execute the following command:

```shell

mvn clean package
```

The JAR file will be available at `target/api-gateway-0.0.1-SNAPSHOT.jar`.

## Running the Application with Docker

To run the application with Docker, execute the following commands:

```shell

docker build -t api-gateway .
docker run -p 9090:9090 --env-file .env api-gateway
```

# Project Service

This project is a project management service built with Spring Boot. It provides an API to manage projects and payments.

## Requirements
- Java 17 or higher
- Maven 3.6.3 or higher

## Configuration

### Environment VariablesMake sure to set the following environment variables in the `.env` file:

```dotenv
PROJECTS_SERVICE_PORT=8082 - Port for the project service
MYSQL_HOST=MySQL database host
MYSQL_PORT=MySQL database port
MYSQL_DATABASE=MySQL database name
MYSQL_USER=MySQL database user
MYSQL_PASSWORD=MySQL database password
OAUTH2_ISSUER_URI=OAuth2 issuer URL
SWAGGER_LOGIN_CLIENT_ID=Client ID for Swagger login
```


## Running the Application
To run the application, execute the following command:

```shell
mvn spring-boot:run
```

The application will be available at `http://localhost:8082`.

## Building the Application

To build the application, execute the following command:

```shell
mvn clean package
```

The JAR file will be available at `target/project-service-0.0.1-SNAPSHOT.jar`.

## Running the Application with Docker

To run the application with Docker, execute the following commands:

```shell
docker build -t project-service .
docker run -p 8082:8082 --env-file .env project-service
```

# Users Service

This project is a user management service built with Spring Boot. It provides an API to manage users from keycloak IAM.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Configuration

### Environment Variables

Make sure to set the following environment variables in the `.env` file:

```dotenv
USERS_SERVICE_PORT = 8081 -  Port for the users service
OAUTH2_ISSUER_URI = OAuth2 issuer URL
KEYCLOAK_SERVER_URL = Keycloak server URL
KEYCLOAK_REALM = Keycloak realm
KEYCLOAK_CLIENT_ID = Keycloak client ID
KEYCLOAK_CLIENT_SECRET = Keycloak client secret
GATEWAY_URL = API Gateway URL
SWAGGER_LOGIN_CLIENT_ID = Client ID for Swagger login
```

## Running the Application

To run the application, execute the following command:

```shell    
mvn spring-boot:run
```

The application will be available at `http://localhost:8081`.

## Building the Application

To build the application, execute the following command:

```shell
mvn clean package
```

The JAR file will be available at `target/users-service-0.0.1-SNAPSHOT.jar`.

## Running the Application with Docker

To run the application with Docker, execute the following commands:

```shell
docker build -t users-service .
docker run -p 8081:8081 --env-file .env users-service
```




