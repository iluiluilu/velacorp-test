# Vela corp test project for candidate

## Requirements

- https://gist.github.com/dangdinhtai0001/8bfe14b76280c1fef852805664668613

## Implement

### Setting Up the Spring Boot Project

- Java 17
- Spring boot 3.3.1-SNAPSHOT
- Spring data jpa and hibernate: Simplify the mapping of database tables to Java objects effortlessly.
- Spring cache: Easily configured to automatically handle caching with annotations or through manual processes.
- Redisson to integrate redis: Flexible configuration options for Redis cache using sentinelServersConfig or singleServerConfig.
- Mysql 8.0.
- Redis latest version (standalone).

### Run project

- Install docker and docker compose: https://docs.docker.com
- Run below command to start project

```bash
docker compose build
docker compose up
```

- Access api on url http://localhost:6075/ecommerce-service/
- Access swagger on url http://localhost:6075/ecommerce-service/swagger-ui/index.html#/

### Note

- Redis is available but no caching is used in this project.
