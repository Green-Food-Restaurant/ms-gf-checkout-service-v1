# Guia de Configuração do Coolify para o Checkout Service

Este guia fornece instruções detalhadas sobre como configurar o Coolify para o serviço de checkout da Green Food.

## Pré-requisitos

- Acesso ao painel de administração do Coolify
- Credenciais de acesso ao Docker Hub
- Acesso ao repositório GitHub do projeto

## 1. Configuração Inicial do Coolify

### 1.1. Criação de Projeto

1. Faça login no painel do Coolify
2. Clique em "Projetos" no menu lateral
3. Clique em "Novo Projeto"
4. Nomeie o projeto como "Green Food"
5. Adicione os membros da equipe que terão acesso ao projeto

### 1.2. Configuração de Ambiente

1. Dentro do projeto "Green Food", clique em "Ambientes"
2. Crie um novo ambiente para:
   - `production`
   - `staging` (opcional)
   - `development` (opcional)
3. Configure as variáveis de ambiente específicas para cada ambiente
4. Para o ambiente de produção, configure as seguintes variáveis:
   ```
   SPRING_PROFILES_ACTIVE=prod
   SERVER_PORT=8080
   LOG_LEVEL=INFO
   MERCADOPAGO_ACCESS_TOKEN=seu_token
   ```

## 2. Configuração do Serviço

### 2.1. Adicionar Serviço

1. No ambiente desejado, clique em "Adicionar Serviço" 
2. Selecione "Docker"
3. Escolha o repositório GitHub `Green-Food-Restaurant/ms-gf-checkout-service-v1`
4. Configure as seguintes opções:
   - **Nome do Serviço**: `checkout-service`
   - **Tipo de Deploy**: `Dockerfile` (o arquivo já existe na raiz do projeto)
   - **Porta Pública**: `8080` (porta que será exposta para acesso externo)
   - **Porta do Contêiner**: `8080` (porta interna do aplicativo Spring Boot)
   - **Autoscaling**: Configurar de acordo com as necessidades
   - **Healthcheck**: `/actuator/health` (endpoint para verificar saúde da aplicação)

### 2.2. Configuração do Docker Hub

1. Em "Configurações do Serviço", vá para a seção "Registry"
2. Selecione "Docker Hub" como registro
3. Configure as credenciais do Docker Hub:
   - **Username**: mesmo valor configurado em `DOCKER_USERNAME` no GitHub
   - **Password**: gere um token de acesso no Docker Hub para o Coolify

### 2.3. Configuração de Recursos

Configure os recursos alocados para o serviço:

1. **CPU**: 1 CPU (ajuste conforme necessário)
2. **Memória**: 1024MB (ajuste conforme necessário)
3. **Swap**: 512MB (opcional)

## 3. Configuração do Webhook para CI/CD

### 3.1. Criar Webhook

1. Na configuração do serviço, vá para a aba "Webhooks"
2. Clique em "Adicionar Webhook"
3. Configure as seguintes opções:
   - **Nome**: `github-actions-trigger`
   - **Método**: `POST`
   - **Autenticação**: `Bearer Token`

### 3.2. Copiar Credenciais do Webhook

Após a criação do webhook, você verá:
1. URL do webhook: copie este valor para o secret `COOLIFY_WEBHOOK` no GitHub
2. Token de autenticação: copie este valor para o secret `COOLIFY_TOKEN` no GitHub

### 3.3. Testar o Webhook

Para verificar se o webhook está funcionando corretamente:

```bash
curl -X POST "URL_DO_WEBHOOK" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer TOKEN_COOLIFY" \
-d '{"ref": "refs/heads/main"}'
```

## 4. Configuração de Banco de Dados e Dependências

### 4.1. Banco de Dados (se aplicável)

1. Em "Serviços", adicione um novo serviço de Banco de Dados
2. Selecione o tipo de banco (PostgreSQL, MySQL, etc.)
3. Configure o banco de dados conforme necessário
4. Anote os detalhes de conexão para configurar nas variáveis de ambiente

### 4.2. Kafka (se aplicável)

O serviço de checkout depende do Kafka para comunicação assíncrona:

1. Em "Serviços", adicione um novo serviço "Docker-compose"
2. Aponte para o arquivo `docker/docker-compose.yml` no repositório
3. Configure as portas expostas para o Kafka e Zookeeper

## 5. Monitoramento e Logs

### 5.1. Configuração de Logs

1. Na configuração do serviço, vá para a aba "Logs"
2. Configure a retenção de logs conforme política da empresa
3. Opcionalmente, configure integração com soluções como ELK Stack ou Graylog

### 5.2. Alertas e Monitoramento

1. Em "Notificações", configure alertas para:
   - Falhas de deploy
   - Alto uso de CPU/memória
   - Indisponibilidade do serviço
2. Configure os canais de notificação (e-mail, Slack, etc.)

## 6. Backup e Recuperação

### 6.1. Configuração de Backups

1. Em "Configurações do Projeto", vá para a seção "Backups"
2. Configure backups automáticos:
   - **Frequência**: Diária
   - **Hora**: 03:00 (ou outro horário de baixo tráfego)
   - **Retenção**: 7 dias (ou conforme política da empresa)

### 6.2. Testes de Recuperação

É recomendado realizar testes de recuperação regularmente:

1. Gere um backup manual
2. Crie um ambiente de teste
3. Restaure o backup no ambiente de teste
4. Verifique se o serviço funciona corretamente

## 7. Domínios e TLS/SSL

### 7.1. Configuração de Domínio

1. Na configuração do serviço, vá para a aba "Domínios"
2. Adicione o domínio desejado (ex: checkout.greenfood.com)
3. Configure as opções de redirecionamento conforme necessário

### 7.2. Configuração de SSL/TLS

1. Na mesma aba "Domínios", habilite SSL
2. Selecione "Let's Encrypt" para certificado automático ou faça upload de certificado personalizado
3. Configure renovação automática (recomendado)

## Troubleshooting

### Problemas Comuns

#### O deploy falha com erro de registro Docker

Verifique:
- Se as credenciais do Docker Hub estão corretas
- Se o token de acesso não expirou
- Se o usuário tem permissão para acessar a imagem

#### A aplicação não inicia corretamente

Verifique:
- Logs do contêiner para identificar erros específicos
- Se todas as variáveis de ambiente necessárias estão configuradas
- Se os recursos alocados são suficientes (CPU/memória)

#### O webhook não está funcionando

Verifique:
- Se a URL do webhook e o token estão configurados corretamente no GitHub Actions
- Se o Coolify está acessível pela internet
- Se não há firewalls bloqueando a comunicação

## Recursos Adicionais

- [Documentação oficial do Coolify](https://coolify.io/docs/)
- [GitHub Actions com Coolify](https://coolify.io/docs/knowledge-base/git/github/github-actions)
- [FAQ do Coolify](https://coolify.io/docs/knowledge-base/faq)
