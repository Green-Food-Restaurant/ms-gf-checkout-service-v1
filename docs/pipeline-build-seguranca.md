# Documentação do Pipeline de CI/CD e Segurança

## Pipeline de Build e Deploy do Microsserviço de Checkout

Este documento descreve o pipeline de integração contínua (CI) e entrega contínua (CD) implementado para o microsserviço de checkout da Green Food, detalhando cada etapa do processo e as práticas de segurança aplicadas.

## Visão Geral

O pipeline é acionado automaticamente em três cenários:
- Pushes para a branch principal (`main`)
- Criação de tags de versão (formato `v*`, por exemplo: v1.0.0)
- Pull Requests direcionados à branch principal

A execução completa inclui compilação, testes, análise de segurança, construção de imagem Docker e deploy para o registro Docker Hub.

## Fluxo de Execução

![Pipeline CI/CD](https://mermaid.ink/img/pako:eNqFk9tu2zAMhl-F0FUL5AHyKksLrLlZge2il7pItKVEgCMZkp0tDfLuox2nSZcVyYVN8v9Jfj9luhDSUKJT8UOJuuGcKJloyauCKsFshuh3RjUJGdirDbJkwQr9c5YiubUiYVCkeXGuLE-kKfg1qaMpuKiVa5TKRm2JSNFYFBnKUC8UG5bBDdl2Jv5i_s1P9qPQv3FD4pS_P2Hcb3LGOHuvXdgwnh1ntC-gUTxuZOOE-dzfbs5vhl15O4jjE-ebhcKl6uwYGpjiGYHddYdRKJIraRNJlbWAziXuh9dWWeRnWcXG4by1dQHZCgZQU44a4RqRASdQezbQMQLOsJkWP6dtxPQ9liQkUmNk65pnPHpFT8S0LF_d-a-4JFqoMYd5hUrgYFUgL8PnPeQ1rvT7w24XCP6v4R84XOSp9IzXrfQNwwhfbLHsqC8c0AVZmOtI5pA4xb47a_UytaRmMJyUa-1ewgVh_O-6pCOAj7EaqIlczAVaYDURXZOcnqFFH40xJ6isuTTs0QxWT2Ms9nJ-_fVuPi9yXLBcoPOzn8X8g8I?type=png)

## Etapas Detalhadas

### 1. Checkout do Código
- **Ação**: `actions/checkout@v4.2.2`
- **Função**: Clona o repositório para o ambiente de execução do GitHub Actions
- **Importância**: Fornece acesso ao código-fonte para todas as etapas subsequentes

### 2. Configuração do Ambiente Java
- **Ação**: `actions/setup-java@v4`
- **Configuração**:
  - **Distribuição**: Zulu JDK
  - **Versão**: 21
  - **Cache**: Habilitado para Maven
- **Função**: Configura o ambiente Java necessário para compilação e testes
- **Benefícios**: O caching de dependências do Maven reduz significativamente o tempo de build em execuções futuras

### 3. Compilação com Maven
- **Comando**: `mvn -B clean package -DskipTests`
- **Função**: 
  - Limpa builds anteriores
  - Compila o código-fonte
  - Gera o arquivo JAR
  - Pula testes nesta fase para separar erros de compilação de falhas de testes
- **Flags**:
  - `-B`: Modo batch (não interativo)
  - `-DskipTests`: Pula execução de testes

### 4. Execução de Testes
- **Comando**: `mvn -B test`
- **Condição**: Executa apenas se a mensagem de commit não contiver `[skip tests]`
- **Função**: Executa testes unitários e de integração para garantir qualidade do código
- **Vantagem**: A condição permite pular testes em emergências ou pequenas correções

### 5. Extração de Metadados para Docker
- **Ação**: `docker/metadata-action@v5`
- **Função**: Define estratégia de tags para a imagem Docker
- **Estratégia de Versionamento**:
  - Para tags semânticas (ex: v1.2.3):
    - Cria tag com versão completa: `1.2.3`
    - Cria tag com versão major.minor: `1.2`
  - Para branches: tag com nome da branch
  - Para todos os builds: tag com hash curto do commit
- **Benefício**: Estratégia de tags organizada facilita identificação e rollback de versões

### 6. Análise de Segurança com Trivy
- **Ação**: `aquasecurity/trivy-action@master`
- **Função**: Escaneia a imagem Docker em busca de vulnerabilidades
- **Configurações**:
  - **Formato**: Table (tabela para fácil leitura)
  - **Severidades monitoradas**: CRÍTICA, ALTA
  - **Tipos de vulnerabilidade**: Sistema operacional e bibliotecas
  - **Comportamento**: Ignora vulnerabilidades sem correção disponível
- **Política**: Não interrompe o pipeline, mas gera alertas para análise
- **Integrações**: Resultados visíveis nos logs do GitHub Actions

### 7. Login no Docker Hub
- **Ação**: `docker/login-action@v3`
- **Função**: Autentica no Docker Hub para permitir o push da imagem
- **Segurança**: Utiliza secrets do GitHub (`DOCKER_USERNAME` e `DOCKER_TOKEN`) para armazenar credenciais de forma segura

### 8. Configuração do Docker Buildx
- **Ação**: `docker/setup-buildx-action@v3`
- **Função**: Configura o driver de build avançado para Docker
- **Vantagens**: 
  - Suporte a builds multi-plataforma
  - Cache mais eficiente
  - Builds paralelos

### 9. Build e Push da Imagem Docker
- **Ação**: `docker/build-push-action@v6`
- **Função**: Constrói a imagem Docker e a publica no registro
- **Configuração**:
  - **Context**: Diretório atual (`.`)
  - **Push**: Condicional - apenas se não for um Pull Request
  - **Cache**: Configurado para usar GitHub Actions como provedor de cache
- **Segurança**: Imagens de Pull Requests não são publicadas automaticamente
- **Performance**: Sistema de cache reduz o tempo de build em imagens subsequentes

### 10. Notificações no Slack
- **Ação**: `rtCamp/action-slack-notify@v2`
- **Cenários**:
  - Notificação de sucesso: Canal de DevOps com indicador verde
  - Notificação de falha: Canal de DevOps com indicador vermelho
- **Conteúdo**: Informações sobre o build, incluindo tags da imagem quando bem-sucedido
- **Resiliência**: Configurado com `continue-on-error: true` para não falhar o pipeline caso o Slack esteja indisponível

### 11. Deploy no Coolify
- **Job**: `deploy-coolify`
- **Condição**: Executa apenas após o job de build ter sido concluído com sucesso e apenas para pushes na branch `main`
- **Etapas**:
  1. **Acionamento do webhook do Coolify**
     - Envia uma solicitação HTTP POST para o webhook do Coolify
     - Utiliza os secrets `COOLIFY_WEBHOOK` e `COOLIFY_TOKEN` para autenticação
     - Informa o ref (branch ou tag) para o deploy
  
  2. **Verificação do status do deploy**
     - Exibe informações sobre o início do deploy
     - Registra a data/hora do acionamento
     
  3. **Notificação do Coolify no Slack**
     - Envia uma notificação informando que o deploy foi iniciado
     - Permite acompanhamento do processo pela equipe

- **Documentação detalhada**: Para mais informações sobre o processo de deploy no Coolify, consulte [Deploy com Coolify](deploy-coolify.md)

## Práticas de Segurança

### Gestão de Credenciais
- **Secrets no GitHub**: Todas as credenciais são armazenadas como secrets no GitHub
  - `DOCKER_USERNAME`: Usuário do Docker Hub
  - `DOCKER_TOKEN`: Token de acesso ao Docker Hub (não a senha)
  - `SLACK_WEBHOOK`: URL do webhook do Slack para notificações
  - `COOLIFY_WEBHOOK`: URL do webhook do Coolify para deploys
  - `COOLIFY_TOKEN`: Token de autenticação para o Coolify

### Análise de Vulnerabilidades
- **Escaneamento Contínuo**: Trivy verifica vulnerabilidades em cada build
- **Monitoramento**: Foco em vulnerabilidades críticas e altas
- **Visibilidade**: Resultados disponíveis nos logs do pipeline

### Controle de Acesso
- **Restrição de Push**: Apenas pushes para a branch principal resultam em novas imagens publicadas
- **Imagens de PR**: Imagens de Pull Requests são construídas mas não publicadas automaticamente

### Rastreabilidade
- **Tags Consistentes**: Cada imagem é taggeada com informações de versão e commit
- **Notificações**: Alertas de build enviados para a equipe via Slack

### Boas Práticas de Construção de Imagem
- **Multi-stage Builds**: O Dockerfile utiliza builds em múltiplos estágios para minimizar a superfície de ataque
- **Imagens Base Oficiais**: Utilização de imagens base oficiais e atualizadas
- **Princípio do menor privilégio**: A aplicação não roda como root no container

## Manutenção e Aprimoramentos

### Recomendações Futuras
- Implementar análise estática de código (SonarQube)
- Adicionar testes de vulnerabilidade DAST com OWASP ZAP
- Configurar assinatura de imagens com Docker Content Trust (DCT) ou Cosign
- Expandir o escaneamento de vulnerabilidades para incluir também o código-fonte

### Procedimentos para Manutenção
- Atualizar regularmente as versões das actions do GitHub
- Revisar e aplicar patches de segurança para dependências
- Monitorar regularmente os relatórios do Trivy para vulnerabilidades

---

## Apêndice: Recursos e Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Build and Push Action](https://github.com/docker/build-push-action)
- [Trivy Security Scanner](https://github.com/aquasecurity/trivy)
- [Maven Security Best Practices](https://maven.apache.org/guides/mini/guide-security-best-practices.html)
- [Container Security Best Practices](https://docs.docker.com/develop/security-best-practices/)
