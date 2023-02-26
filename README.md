# eMail Propagator

A [Spring Boot](https://spring.io/) microservice providing a REST API that allows storing eMail addresses and sending eMail messages to all added recipients.
API documentation generated using [Swagger UI](https://swagger.io/tools/swagger-ui/) can be found at [localhost:8080/swagger-ui](http://localhost:8080/swagger-ui/index.html) after running the application. 

The application is using the in-memory H2 database and in order to fully work only requires SMTP configuration - *application.properties* file can be found [here](src/main/resources/application.properties) and contains basic setup for Gmail service. User has to fill **spring.mail.username**
and **spring.mail.password** for propagation to work.

Application logs can be found in the *logs* folder - *spring.log*: general application logs, *requests.log* - incoming requests.