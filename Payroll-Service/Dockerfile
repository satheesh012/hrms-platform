FROM eclipse-temurin:17-jre

RUN addgroup --system spring && adduser --system spring --ingroup spring

WORKDIR /app

COPY target/*.jar app.jar

RUN chown -R spring:spring /app

USER 1000

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]