# Users API Repository

This repository holds the source code for the Users API built in Spring Boot. 

The program is intended to run in a Docker container. If not using Docker or docker-compose to run the code, several environment variables must be defined for the database connection:
* spring.datasource.url (mysql url endpoint)
* spring.datasource.username (mysql admin account username)
* spring.datasource.password (mysql admin account password)
These variables must also be passed as environment variables to any Docker container created manually.

## Docker

This code is compiled into a Docker image held at amattsonsm/users-api

## API

Current Endpoints:
```sh
/api/v1/users
             /      GET, POST
             /{id}  GET, POST, PUT, DELETE
/api/v1/roles
             /      GET, POST
             /{id}  GET, POST, PUT, DELETE
```
