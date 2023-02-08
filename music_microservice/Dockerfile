FROM openjdk
RUN mkdir app
COPY target/music-1.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/music-1.jar"]