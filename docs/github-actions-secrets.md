# Configuração de Secrets para GitHub Actions

Para que a pipeline de CI/CD funcione corretamente, é necessário configurar os seguintes secrets no GitHub:

## Secrets Obrigatórios

1. **DOCKER_USERNAME**
   - O nome de usuário da sua conta no Docker Hub
   - Ex: `greenfoodorg`

2. **DOCKER_TOKEN**
   - Um token de acesso pessoal (PAT) do Docker Hub
   - Pode ser gerado em: https://hub.docker.com/settings/security
   - Conceda apenas as permissões necessárias (read, write, delete)

## Secrets Opcionais (para notificações)

3. **SLACK_WEBHOOK**
   - URL do webhook do Slack para notificações
   - Pode ser criado em: https://api.slack.com/apps > Seu App > Incoming Webhooks
   - Só é necessário se você quiser receber notificações no Slack

## Como configurar os secrets

1. Acesse o repositório no GitHub
2. Vá para "Settings" > "Secrets and variables" > "Actions"
3. Clique em "New repository secret"
4. Adicione cada um dos secrets listados acima

## Observações

- Nunca compartilhe esses tokens ou os inclua no código-fonte
- Revise periodicamente os tokens e renove-os conforme necessário
- Para ambientes corporativos, considere usar GitHub Environments para separar os secrets por ambiente (dev, staging, prod)
