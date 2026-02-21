# MyMenuApp (Backend)

Restful API for my menu app.
Frontend: https://github.com/Schim9/mymenuapp-front

## Tech stack

- Java 21 (preview features enabled)
- Spring Boot 3.4.3
- MySQL 5.7+
- Hibernate / Spring Data JPA
- MapStruct 1.6.3
- Lombok
- Log4j2

## Configuration

The project's `src/main/resources/application.yml` contains development defaults (localhost, no credentials).

For deployment, provide an external configuration file with the real values:

```yaml
spring:
  datasource:
    url: jdbc:mysql://<host>:3306/menu-app?serverTimezone=UTC
    username: <username>
    password: <password>

app:
  security:
    user-code: <user-code>
```

## Deployment (Raspberry Pi)

Build the JAR:
```bash
mvn clean package -DskipTests
```

Start the application:
```bash
java -XX:TieredStopAtLevel=1 -Xms64m -Xmx256m \
  -jar menu-back-0.0.1-SNAPSHOT.jar \
  --spring.config.location=/opt/app/MyMenuApp/application.yml
```
