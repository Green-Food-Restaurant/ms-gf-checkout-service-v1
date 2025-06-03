# Deploy com Coolify

Este documento descreve a configuração e o funcionamento do deploy automatizado utilizando o Coolify como plataforma PaaS.

## O que é o Coolify?

O Coolify é uma alternativa auto-hospedada de código aberto ao Heroku/Netlify/Vercel, permitindo hospedar aplicações, bancos de dados e serviços com simplicidade. Suas características incluem:

- Implantação automatizada a partir de repositórios Git
- Gerenciamento de SSL automático
- Suporte para Docker, Node.js, PHP, Python, etc.
- Monitoramento de recursos e logs em tempo real
- Interface amigável com gráficos detalhados

## Integração com GitHub Actions

O pipeline configura um job específico (`deploy-coolify`) que é executado após o job de build ser concluído com sucesso. Este job faz o seguinte:

1. Envia uma solicitação HTTP para o webhook do Coolify
2. Verifica o status do deploy
3. Notifica a equipe sobre o início do deploy através do Slack

### Como o webhook do Coolify funciona

O webhook do Coolify aceita solicitações POST e inicia um deploy baseado nas seguintes informações:

1. **Autenticação**: Utiliza um token de API do Coolify para autorização
2. **Referência**: Informa qual branch ou tag deve ser implantada
3. **Payload JSON**: Contém informações sobre o commit ou evento que disparou o deploy

Quando o webhook é acionado, o Coolify:
- Puxa as mudanças mais recentes do repositório
- Reconstrói a aplicação baseado na configuração definida
- Implanta a nova versão com zero downtime

## Configuração Necessária

### 1. Secrets no GitHub

Os seguintes secrets precisam ser configurados no repositório GitHub:

- `COOLIFY_WEBHOOK`: URL do webhook para o seu aplicativo no Coolify  
  Exemplo: `https://coolify.greenfood.com/api/v1/webhooks/12345abcde`

- `COOLIFY_TOKEN`: Token de autenticação para o webhook  
  Exemplo: Token gerado no painel do Coolify para o webhook

### 2. Configuração no Coolify

Para configurar o projeto no Coolify, siga estes passos:

1. Faça login no painel de administração do Coolify
2. Crie um novo projeto ou selecione um existente
3. Adicione um novo serviço e selecione "Docker"
4. Configure o serviço para utilizar o repositório Git deste projeto
5. Em "Deploy Settings", configure:
   - Tipo de deploy: "Docker Compose" ou "Dockerfile"
   - Registry: Docker Hub (se aplicável)
   - Credenciais: As mesmas do Docker Hub utilizadas no pipeline
6. Em "Webhooks", gere um webhook e copie a URL e o token
7. Configure variáveis de ambiente necessárias para o projeto

## Fluxo Completo de Deploy

1. Desenvolvedor faz push de alterações para o branch `main`
2. O GitHub Actions executa o job de build:
   - Compila o código
   - Executa testes
   - Escaneia por vulnerabilidades
   - Constrói e publica a imagem Docker

3. Após o sucesso do build, o job `deploy-coolify` é executado:
   - Aciona o webhook do Coolify com o token de autenticação
   - O Coolify detecta a nova versão da imagem
   - O serviço é reconstruído e reimplantado

4. Notificações são enviadas:
   - Para o canal do Slack quando o deploy é iniciado
   - O status pode ser acompanhado no painel do Coolify

## Troubleshooting

Se ocorrerem problemas durante o deploy, verifique:

1. **Logs do GitHub Actions**
   - Verifique se o job `deploy-coolify` foi executado
   - Procure por erros na resposta da chamada do webhook

2. **Painel do Coolify**
   - Acesse o painel do Coolify e verifique os logs do serviço
   - Verifique se o serviço está em execução e respondendo

3. **Problemas comuns**
   - Token de autenticação expirado ou incorreto
   - Webhook URL incorreta
   - Coolify não consegue acessar o registro Docker
   - Problemas de rede entre o Coolify e o Docker Hub

## Rollback

Para reverter para uma versão anterior:

1. No painel do Coolify, acesse o serviço
2. Vá para a guia "Deployments"
3. Encontre a versão anterior desejada
4. Clique em "Rollback" para essa versão

Também é possível fazer rollback via API do Coolify para automação:

```bash
curl -X POST https://coolify.greenfood.com/api/v1/services/{service-id}/rollback/{deployment-id} \
  -H "Authorization: Bearer {seu-token-de-api}"
```

## Monitoramento

Para monitorar o status da aplicação após o deploy:

1. Métricas de recursos: CPU, memória e disco são visíveis no painel do Coolify
2. Logs de aplicação: Disponíveis na seção "Logs" do serviço no Coolify
3. Healthchecks: Configurados para verificar se o serviço está operacional

## Recursos Adicionais

- [Documentação oficial do Coolify](https://coolify.io/docs/)
- [Guia de integração com GitHub Actions](https://coolify.io/docs/knowledge-base/git/github/github-actions)
- [API do Coolify](https://coolify.io/docs/api-documentation)
