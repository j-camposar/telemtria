# --- ETAPA 1: Construcción del proyecto (Maven) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos los archivos de configuración y código fuente
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto y empaquetamos (generando el jar o dependencias)
RUN mvn clean package -DskipTests

# --- ETAPA 2: Imagen final para AWS Lambda ---
FROM public.ecr.aws/lambda/java:21
WORKDIR ${LAMBDA_TASK_ROOT}

# Copiamos las clases compiladas y dependencias desde la etapa de compilación
# (Maven suele dejar el JAR con dependencias o el directorio target/classes)
COPY --from=build /app/target/classes ./
COPY --from=build /app/target/dependency/* ./lib/

# Definimos el Handler de la Lambda (Paquete.Clase::método)
# Nota: Asegúrate de que el paquete coincida exactamente con el de tu código (ej: com.kafka.mensajero.WebSocketConnectHandler::handleRequest)
CMD [ "com.kafka.mensajero.WebSocketConnectHandler::handleRequest" ]