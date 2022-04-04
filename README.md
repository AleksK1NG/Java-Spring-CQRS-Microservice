### Spring Kafka PostgreSQL MongoDB CQRS microservice example 👋‍💫✨

#### 👨‍💻 Full list what has been used:
* [Spring](https://spring.io/) - Java Spring
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - data access layer
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) - Spring Data MongoDB
* [Spring Cloud Sleuth](https://spring.io/projects/spring-cloud-sleuth) - Spring Cloud Sleuth distributed tracing
* [Kafka](https://spring.io/projects/spring-kafka) - Spring for Apache Kafka
* [PostgreSQL](https://www.postgresql.org/) - PostgreSQL database.
* [Zipkin](https://zipkin.io/) - Zipkin is a distributed tracing system
* [Docker](https://www.docker.com/) - Docker
* [Prometheus](https://prometheus.io/) - Prometheus
* [Grafana](https://grafana.com/) - Grafana
* [Flyway](https://flywaydb.org/) - Database migrations


### Swagger UI:

http://localhost:6001/swagger-ui/index.html

### Zipkin UI:

http://localhost:9411

### Prometheus UI:

http://localhost:9090

### Grafana UI:

http://localhost:3000


For local development:
```
make local // runs docker-compose.yaml with all required containers
run spring application
```