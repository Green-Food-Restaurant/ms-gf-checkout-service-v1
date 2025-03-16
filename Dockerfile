# Usar a imagem do Maven para construir o projeto
FROM maven:3.9.9-amazoncorretto-21-alpine AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar apenas o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixar as dependências do Maven
RUN mvn verify --fail-never

# Copiar o diretório src para o contêiner
COPY src ./src

# Executar o Maven para construir o projeto
RUN mvn clean package -DskipTests

# Usar a imagem do OpenJDK para rodar a aplicação
FROM amazoncorretto:21

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o jar gerado do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expor a porta que a aplicação irá rodar
EXPOSE 8090

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
