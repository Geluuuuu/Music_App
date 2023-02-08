FROM openjdk
RUN mkdir app
COPY target/playlist-1.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/playlist-1.jar"]