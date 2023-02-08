FROM openjdk
RUN mkdir app
COPY target/gateway-1.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/gateway-1.jar"]