#!/usr/bin/env pwsh
# Script para simular o acionamento do webhook do Coolify localmente
# Útil para testar a configuração antes de implementá-la no ambiente real

param(
    [string]$webhookUrl = $env:COOLIFY_WEBHOOK,
    [string]$token = $env:COOLIFY_TOKEN,
    [string]$ref = "refs/heads/main"
)

# Verifica se os parâmetros necessários foram fornecidos
if (-not $webhookUrl) {
    Write-Host "⚠️  URL do webhook não fornecida!" -ForegroundColor Yellow
    Write-Host "Use o parâmetro -webhookUrl ou defina a variável de ambiente COOLIFY_WEBHOOK" -ForegroundColor Yellow
    exit 1
}

if (-not $token) {
    Write-Host "⚠️  Token de autenticação não fornecido!" -ForegroundColor Yellow
    Write-Host "Use o parâmetro -token ou defina a variável de ambiente COOLIFY_TOKEN" -ForegroundColor Yellow
    exit 1
}

Write-Host "🚀 Testando webhook do Coolify" -ForegroundColor Cyan
Write-Host "URL: $webhookUrl" -ForegroundColor Cyan
Write-Host "Referência: $ref" -ForegroundColor Cyan

try {
    # Prepara o cabeçalho e o corpo da requisição
    $headers = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer $token"
    }

    $body = @{
        "ref" = $ref
    } | ConvertTo-Json

    # Executa a requisição HTTP
    Write-Host "📤 Enviando requisição..." -ForegroundColor Cyan
    $response = Invoke-RestMethod -Uri $webhookUrl -Method Post -Headers $headers -Body $body -ContentType "application/json"
    
    # Exibe o resultado
    Write-Host "✅ Webhook acionado com sucesso!" -ForegroundColor Green
    Write-Host "Resposta:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 4
}
catch {
    Write-Host "❌ Erro ao acionar webhook:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "Código de status: $statusCode" -ForegroundColor Red
        
        if ($_.ErrorDetails) {
            Write-Host "Detalhes do erro:" -ForegroundColor Red
            Write-Host $_.ErrorDetails.Message -ForegroundColor Red
        }
    }
    
    exit 1
}

Write-Host "`n📋 Próximos passos:" -ForegroundColor Cyan
Write-Host "1. Acesse o painel do Coolify para verificar se o deploy foi iniciado" -ForegroundColor White
Write-Host "2. Verifique os logs do deploy para identificar possíveis problemas" -ForegroundColor White
Write-Host "3. Confirme se o serviço está em execução após o deploy" -ForegroundColor White
