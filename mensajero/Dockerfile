# --- ETAPA 1: Construcción y Compilación con Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Compilamos el proyecto y empaquetamos
RUN mvn clean package -DskipTests

# --- ETAPA 2: Imagen final para AWS Lambda Java 21 ---
FROM public.ecr.aws/lambda/java:21
WORKDIR ${LAMBDA_TASK_ROOT}

# Copiamos las clases compiladas directamente desde target/classes
COPY --from=build /app/target/classes ./

# Copiamos el JAR ejecutable que genera Spring Boot dentro de la carpeta lib/ o como app.jar
# (Spring Boot genera el jar principal en target/)
COPY --from=build /app/target/*.jar ./lib/

# Definimos el Handler de la Lambda (Paquete.Clase::método)
CMD [ "com.kafka.mensajero.WebSocketConnectHandler::handleRequest" ]