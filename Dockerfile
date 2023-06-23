FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar /opt/app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/opt/app.jar"]
EXPOSE 80