#!/bin/bash
# Script para simular o pipeline de CI/CD localmente
# Salve como scripts/run-local-pipeline.sh

set -e  # Falha ao encontrar qualquer erro

echo "===== Iniciando Pipeline Local do Checkout Service ====="

echo -e "\n\n===== 1. Compilando o projeto ====="
./mvnw clean package -DskipTests

echo -e "\n\n===== 2. Executando testes unitários ====="
./mvnw test

echo -e "\n\n===== 3. Construindo imagem Docker ====="
IMAGE_NAME="ms-gf-checkout-service:local"
docker build -t $IMAGE_NAME .

echo -e "\n\n===== 4. Executando análise de segurança com Trivy ====="
if command -v trivy &> /dev/null; then
    echo "Analisando imagem Docker..."
    trivy image --severity HIGH,CRITICAL --ignore-unfixed $IMAGE_NAME
    
    echo -e "\nAnalisando vulnerabilidades no código-fonte..."
    trivy fs --severity HIGH,CRITICAL --ignore-unfixed .
else
    echo "Trivy não encontrado. Por favor, instale o Trivy para executar a análise de segurança."
    echo "Instruções em: https://aquasecurity.github.io/trivy/latest/getting-started/installation/"
fi

echo -e "\n\n===== Pipeline local concluído ====="
echo "Para testar o aplicativo, execute:"
echo "docker run -p 8080:8080 $IMAGE_NAME"
