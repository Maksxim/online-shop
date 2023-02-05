FROM openjdk:18.0.1-jdk-buster
COPY target/online-shop-*.jar /online-shop.jar
EXPOSE 8080
CMD ["sh", "-c", "java -jar /online-shop.jar"]