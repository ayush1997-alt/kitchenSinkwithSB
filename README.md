# kitchenSinkwithSB

# Kitchensink Spring Boot Application

## Overview

This is a Spring Boot application that serves as a simple **Kitchensink** example, demonstrating how to use **Spring Data JPA** with a **MySQL** database. The application features basic functionality for storing, retrieving, and managing `Member` entities.

The application includes:
- A REST API to interact with `Member` entities via **JAX-RS** (REST services).
- A simple UI to interact with `Member` entities via **JSF**.

## Prerequisites

Before running the application, make sure you have the following installed:
- **Java 21**
- **MySQL** 
- **Maven**

### Setting Up the Database

1. **Install MySQL**

2. **Create a Database**

3. **Configure Database User**

### Application Configuration

Configure the application to connect to the **MySQL** database.

In the `src/main/resources/application.properties` file, make sure to update the MySQL configuration to match your environment:

```properties
# Spring Data JPA Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/kitchensink
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=ayush

# Hibernate JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Application Server Port
server.port=8080
