# Use the official OpenJDK 19 image as the base image
FROM openjdk:19

# Expose port 8080 to allow external access to the application
EXPOSE 8080

# Add the Spring Boot application's JAR file to the image
ADD build/libs/user-service-application.jar springboot-images-new.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/springboot-images-new.jar"]