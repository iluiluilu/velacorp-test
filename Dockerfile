FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

#RUN mvn clean package
FROM openjdk:17
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
COPY --from=build /app/target/*.jar /app/spring-app.jar
RUN mkdir -p benchmark-result
EXPOSE 6075
CMD ["java", "-jar", "/app/spring-app.jar"]
