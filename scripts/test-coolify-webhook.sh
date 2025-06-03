#!/bin/bash
# Script para simular o acionamento do webhook do Coolify localmente
# Útil para testar a configuração antes de implementá-la no ambiente real

# Obter parâmetros do ambiente ou da linha de comando
WEBHOOK_URL=${1:-$COOLIFY_WEBHOOK}
TOKEN=${2:-$COOLIFY_TOKEN}
REF=${3:-"refs/heads/main"}

# Verificar se os parâmetros necessários foram fornecidos
if [ -z "$WEBHOOK_URL" ]; then
    echo "⚠️  URL do webhook não fornecida!"
    echo "Use o primeiro parâmetro ou defina a variável de ambiente COOLIFY_WEBHOOK"
    exit 1
fi

if [ -z "$TOKEN" ]; then
    echo "⚠️  Token de autenticação não fornecido!"
    echo "Use o segundo parâmetro ou defina a variável de ambiente COOLIFY_TOKEN"
    exit 1
fi

echo "🚀 Testando webhook do Coolify"
echo "URL: $WEBHOOK_URL"
echo "Referência: $REF"

# Executa a requisição HTTP
echo "📤 Enviando requisição..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$WEBHOOK_URL" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"ref\": \"$REF\"}")

# Extrai o status code e o corpo da resposta
HTTP_STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n -1)

# Verifica o status da resposta
if [ "$HTTP_STATUS" -ge 200 ] && [ "$HTTP_STATUS" -lt 300 ]; then
    echo "✅ Webhook acionado com sucesso!"
    echo "Resposta:"
    echo "$BODY" | jq || echo "$BODY"  # Tenta formatar como JSON se jq estiver disponível
else
    echo "❌ Erro ao acionar webhook. Status: $HTTP_STATUS"
    echo "Resposta:"
    echo "$BODY" | jq || echo "$BODY"  # Tenta formatar como JSON se jq estiver disponível
    exit 1
fi

echo -e "\n📋 Próximos passos:"
echo "1. Acesse o painel do Coolify para verificar se o deploy foi iniciado"
echo "2. Verifique os logs do deploy para identificar possíveis problemas"
echo "3. Confirme se o serviço está em execução após o deploy"
