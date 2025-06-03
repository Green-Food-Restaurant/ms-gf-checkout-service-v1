# Script para simular o pipeline de CI/CD localmente (Windows)
# Salve como scripts/run-local-pipeline.ps1

Write-Host "===== Iniciando Pipeline Local do Checkout Service =====" -ForegroundColor Cyan

Write-Host "`n`n===== 1. Compilando o projeto =====" -ForegroundColor Cyan
.\mvnw.cmd clean package -DskipTests

Write-Host "`n`n===== 2. Executando testes unitários =====" -ForegroundColor Cyan
.\mvnw.cmd test

Write-Host "`n`n===== 3. Construindo imagem Docker =====" -ForegroundColor Cyan
$IMAGE_NAME = "ms-gf-checkout-service:local"
docker build -t $IMAGE_NAME .

Write-Host "`n`n===== 4. Executando análise de segurança com Trivy =====" -ForegroundColor Cyan
try {
    if (Get-Command trivy -ErrorAction SilentlyContinue) {
        Write-Host "Analisando imagem Docker..." -ForegroundColor Yellow
        trivy image --severity HIGH,CRITICAL --ignore-unfixed $IMAGE_NAME
        
        Write-Host "`nAnalisando vulnerabilidades no código-fonte..." -ForegroundColor Yellow
        trivy fs --severity HIGH,CRITICAL --ignore-unfixed .
    } else {
        Write-Host "Trivy não encontrado. Por favor, instale o Trivy para executar a análise de segurança." -ForegroundColor Red
        Write-Host "Instruções em: https://aquasecurity.github.io/trivy/latest/getting-started/installation/" -ForegroundColor Red
        Write-Host "Dica rápida: Use 'choco install trivy' ou 'scoop install trivy'" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Erro ao executar Trivy: $_" -ForegroundColor Red
}

Write-Host "`n`n===== Pipeline local concluído =====" -ForegroundColor Green
Write-Host "Para testar o aplicativo, execute:" -ForegroundColor Yellow
Write-Host "docker run -p 8080:8080 $IMAGE_NAME" -ForegroundColor Yellow
