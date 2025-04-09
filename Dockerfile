# Usamos una imagen base de OpenJDK
FROM openjdk:21-jdk-slim AS build

# Establecemos el directorio de trabajo en el contenedor
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src /app/src

# Construimos el archivo JAR
RUN mvn clean package -DskipTests

# Usamos una imagen más ligera para ejecutar el JAR
FROM openjdk:17-jre-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR desde la imagen de construcción
COPY --from=build /app/target/tu-aplicacion.jar /app/tu-aplicacion.jar

# Exponemos el puerto que usará la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/tu-aplicacion.jar"]