FROM openjdk:8-alpine

COPY target/uberjar/book-list.jar /book-list/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/book-list/app.jar"]
