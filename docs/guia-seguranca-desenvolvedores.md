# Guia de Segurança para Desenvolvedores

Este guia fornece instruções para executar localmente as mesmas verificações de segurança que são realizadas no pipeline de CI/CD.

## Análise de Vulnerabilidades com Trivy

### Instalação do Trivy

**Linux/macOS:**
```bash
brew install trivy
```

**Windows (com Chocolatey):**
```powershell
choco install trivy
```

**Alternativa para Windows (com scoop):**
```powershell
scoop install trivy
```

**Docker:**
```bash
docker pull aquasec/trivy
```

### Executar análise da imagem localmente

```powershell
# Construa a imagem primeiro
docker build -t ms-gf-checkout-service:local .

# Execute o scan
trivy image ms-gf-checkout-service:local
```

### Executar análise de código-fonte localmente

```powershell
# Analisa vulnerabilidades no código-fonte e dependências
trivy fs .
```

### Verificar vulnerabilidades de dependências Maven

```powershell
# Analisa especificamente o arquivo pom.xml
trivy fs --security-checks vuln pom.xml
```

### Usando a configuração do projeto

O arquivo `trivy.yaml` na raiz do projeto contém as mesmas configurações utilizadas no pipeline:

```powershell
# Usando o arquivo de configuração
trivy image --config trivy.yaml ms-gf-checkout-service:local
```

## Interpretando Resultados

Os resultados do scan são organizados por:

1. **Severidade**: CRÍTICA, ALTA, MÉDIA, BAIXA
2. **Tipo**: Biblioteca, Sistema Operacional
3. **ID da Vulnerabilidade**: Identificador único (CVE, etc.)
4. **Biblioteca afetada**: Nome e versão do componente vulnerável
5. **Versão corrigida**: Versão onde a vulnerabilidade foi corrigida
6. **Descrição**: Detalhes sobre a vulnerabilidade

### Exemplo de saída:

```
ms-gf-checkout-service:local (debian 11.7)
===========================================
Total: 27 (CRITICAL: 2, HIGH: 10, MEDIUM: 15, LOW: 0, UNKNOWN: 0)

┌──────────┬────────────────────┬──────────┬────────────┬───────────────────┬───────────────┐
│ Library  │ Vulnerability ID   │ Severity │ Installed  │ Fixed Version     │ Title         │
│          │                    │          │ Version    │                   │               │
├──────────┼────────────────────┼──────────┼────────────┼───────────────────┼───────────────┤
│ zlib     │ CVE-2022-37434     │ CRITICAL │ 1:1.2.11.. │ 1:1.2.11.dfsg-2+.. │ zlib: heap-   │
│          │                    │          │            │                   │ based buffer  │
│          │                    │          │            │                   │ overflow      │
└──────────┴────────────────────┴──────────┴────────────┴───────────────────┴───────────────┘
```

## O que fazer com as vulnerabilidades

1. **Vulnerabilidades CRÍTICAS e ALTAS** devem ser corrigidas antes do merge para a branch principal
2. **Vulnerabilidades MÉDIAS** devem ser avaliadas e, se possível, corrigidas
3. **Vulnerabilidades BAIXAS** devem ser documentadas para correção futura

### Estratégias de correção:

- Atualizar a biblioteca afetada para versão corrigida
- Substituir biblioteca por alternativa segura
- Implementar mitigações se a atualização não for possível
- Documentar vulnerabilidades aceitas no arquivo `.trivyignore`

## Falsos Positivos

Se você identificar um falso positivo, você pode:

1. Adicionar o ID da vulnerabilidade ao arquivo `.trivyignore`
2. Documentar por que é um falso positivo no arquivo

Exemplo de `.trivyignore`:
```
# Falso positivo - não aplicável à nossa implementação
CVE-2022-12345

# Vulnerabilidade em código de teste, não usado em produção
CVE-2022-67890
```

## Integração com IDE

O Trivy pode ser integrado com várias IDEs:

- **VS Code**: Extensão "Trivy Vulnerability Scanner"
- **IntelliJ IDEA**: Plugin "Trivy"

## Outras Verificações de Segurança Recomendadas

- **OWASP Dependency Check**: Análise mais detalhada de dependências Java
- **SpotBugs**: Análise estática de código Java
- **SonarQube**: Análise de qualidade e segurança do código

## Recursos Adicionais

- [Documentação oficial do Trivy](https://aquasecurity.github.io/trivy/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Guia de Segurança para Java](https://www.oracle.com/java/technologies/javase/seccodeguide.html)
