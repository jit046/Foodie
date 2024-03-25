# Secure RESTful API with Spring Data MongoDB and Microsoft EntraID

## Blog Post

This repository contains the source code for a secure RESTful API that leverages Spring Data MongoDB and Microsoft EntraID for authentication and authorization. The step-by-step tutorial for building this project can be found in the MongoDB Developer Center.

## Supported versions:

- Java 17 or higher
- Spring Boot (Latest stable version as of tutorial writing)
- MongoDB Atlas
- Microsoft EntraID for OAuth2
- Apache Maven (Latest stable version as of tutorial writing)
- OpenAPI 3 for API documentation

## MongoDB Atlas

Get started with a Free Tier Cluster on [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) to host your database. Configure your `application.properties` with the MongoDB URI provided by Atlas.

## Tutorial Overview

This tutorial covers:

- Setting up a Spring Boot application with essential dependencies.
- Integrating Spring Data MongoDB for database operations.
- Configuring Microsoft EntraID for secure API authentication and authorization.
- Utilizing Swagger for API documentation and interactive testing.

Ensure you meet the prerequisites outlined in the tutorial, including having a MongoDB account, an Azure subscription, JDK 17 or higher, and Maven installed on your development machine.

## Commands to Start and Test the Application

- To start the server: `mvn spring-boot:run`
- For unit tests: `mvn clean test`
- For integration tests: `mvn clean integration-test`
- To build the project: `mvn clean package`
- To run the built project: `java -jar target/{your-built-jar-file}.jar`

## Configuring Microsoft EntraID

Follow the tutorial steps to register your application with Microsoft EntraID and set up OAuth2 authentication and authorization properly. This includes configuring the `application.properties` file with your Azure and MongoDB Atlas credentials.

## Swagger & OpenAPI 3

Swagger is configured by default with the Springdoc OpenAPI library included in the project dependencies. Access the Swagger UI at `http://localhost:8080/swagger-ui.html` to interact with the API endpoints securely.

## API Endpoints

This project includes endpoints for managing to-do items, such as creating, updating, fetching, and deleting tasks.

## Features Showcase
This project showcases advanced integration and security features, combining the robustness of MongoDB with the flexibility of Spring Boot and the security of Microsoft EntraID:
- Spring Data MongoDB Integration: Utilizes Spring Data MongoDB for seamless interaction with MongoDB databases. It demonstrates how to perform CRUD operations efficiently using repository abstractions, significantly reducing boilerplate code. See TodoRepository.java for basic CRUD implementation.
- OAuth2 Security with Microsoft EntraID: Implements OAuth2 authentication and authorization using Microsoft EntraID, securing API endpoints and ensuring that only authorized users can access sensitive information. This part of the tutorial illustrates configuring Spring Security to integrate with Azure Active Directory for robust security mechanisms.
- Dynamic Configuration with MongoDB Atlas: Shows how to connect a Spring Boot application to a MongoDB Atlas cluster, highlighting the ease of using cloud-based MongoDB instances for scalable applications. Configuration details are provided for setting up the MongoDB URI in application.properties.
- Secure API Endpoints: Demonstrates how to secure RESTful API endpoints using Spring Security annotations. By leveraging OAuth2 scopes provided by Microsoft EntraID, the tutorial guides through securing operations like creating, updating, and deleting to-do items, ensuring that only users with the appropriate permissions can perform these actions.

## Author
Tim Kelly
- Timotheekelly on [Github](https://github.com/timotheekelly)
