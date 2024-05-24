FROM openjdk:19

# Expose port 8080 to allow external access to the application
EXPOSE 8080

# Add the Spring Boot application's JAR file to the image
ADD build/libs/user-service-application.jar springboot-images-new.jar

# Set environment variables
ENV DATABASE_URL $DATABASE_URL
ENV DATABASE_USERNAME $DATABASE_USERNAME
ENV DATABASE_PASSWORD $DATABASE_PASSWORD
ENV EMAIL_SENDER $EMAIL_SENDER
ENV EMAIL_PASSWORD $EMAIL_PASSWORD
ENV JWT_SECRET_KEY $JWT_SECRET_KEY

# Echo environment variables for debugging
RUN echo "DATABASE_URL=$DATABASE_URL"
RUN echo "DATABASE_USERNAME=$DATABASE_USERNAME"
RUN echo "DATABASE_PASSWORD=$DATABASE_PASSWORD"
RUN echo "EMAIL_SENDER=$EMAIL_SENDER"
RUN echo "EMAIL_PASSWORD=$EMAIL_PASSWORD"
RUN echo "JWT_SECRET_KEY=$JWT_SECRET_KEY"

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/springboot-images-new.jar"]
