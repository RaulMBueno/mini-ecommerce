# Security Hotfix — Variáveis de Ambiente e Configuração

Este documento descreve as variáveis de ambiente necessárias após o hotfix de segurança que removeu segredos hardcoded.

## Variáveis de Ambiente Obrigatórias

Configure as seguintes variáveis **antes** de iniciar a aplicação (dev ou produção):

| Variável | Descrição | Obrigatória |
|----------|-----------|-------------|
| `JWT_SECRET` | Segredo para assinatura dos tokens JWT. Use uma string longa e aleatória (ex: 64+ caracteres). | Sim |
| `DB_PASSWORD` | Senha do banco de dados PostgreSQL. Deve ser a mesma usada no container (ou no banco hospedado). | Sim |
| `CLOUDINARY_CLOUD_NAME` | Nome da conta no Cloudinary. | Sim (se usar upload de logos) |
| `CLOUDINARY_API_KEY` | API Key do Cloudinary. | Sim (se usar upload de logos) |
| `CLOUDINARY_API_SECRET` | API Secret do Cloudinary. | Sim (se usar upload de logos) |

### Variáveis opcionais (com defaults)

| Variável | Default | Descrição |
|----------|---------|-----------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/mini_ecommerce` | URL JDBC do PostgreSQL |
| `DB_USER` | `postgres` | Usuário do banco |
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil Spring (dev/prod) |

---

## ⚠️ Aviso de Segurança: Rotacionar Chaves do Cloudinary

**As chaves do Cloudinary estavam expostas no código-fonte.** Se o repositório foi compartilhado ou está público:

1. Acesse o [Dashboard do Cloudinary](https://console.cloudinary.com/)
2. Vá em **Settings** → **API Keys**
3. **Gere novas chaves** (API Key e API Secret)
4. Atualize as variáveis `CLOUDINARY_API_KEY` e `CLOUDINARY_API_SECRET` em todos os ambientes (local, Railway, etc.)

---

## Checklist de Configuração

### Railway (Backend)

- [ ] `JWT_SECRET` — Gere um valor aleatório forte (ex: `openssl rand -base64 64`)
- [ ] `DB_PASSWORD` — Use a mesma senha do banco PostgreSQL provisionado na Railway (ou DATABASE_URL já inclui)
- [ ] `CLOUDINARY_CLOUD_NAME` — Da conta Cloudinary (rotacionada)
- [ ] `CLOUDINARY_API_KEY` — Nova chave após rotação
- [ ] `CLOUDINARY_API_SECRET` — Novo secret após rotação
- [ ] `DATABASE_URL` — Já configurado automaticamente pelo addon PostgreSQL da Railway
- [ ] `SPRING_PROFILES_ACTIVE` — Defina como `prod` em produção

### Vercel (Frontend)

O frontend React usa `import.meta.env` para a URL da API. Em produção, a URL do Railway é usada automaticamente via `import.meta.env.DEV`. Não há segredos sensíveis no frontend desta aplicação. Se você tiver variáveis como `VITE_API_URL`, configure-as conforme necessário.

### Local (Desenvolvimento)

- [ ] Crie um arquivo `.env` na raiz do projeto (ou configure no seu terminal/IDE)
- [ ] Exporte ou defina todas as variáveis obrigatórias
- [ ] Para o banco local: `DB_PASSWORD` deve ser igual a `POSTGRES_PASSWORD` usado no `docker run` (ex: `<SUA_SENHA>`)

Exemplo (PowerShell):

```powershell
$env:JWT_SECRET = "***sua-chave-longa-e-segura***"
$env:DB_PASSWORD = "***sua-senha-postgres***"
$env:CLOUDINARY_CLOUD_NAME = "***"
$env:CLOUDINARY_API_KEY = "***"
$env:CLOUDINARY_API_SECRET = "***"
.\mvnw spring-boot:run
```

---

## O que acontece se faltar variável?

- **JWT_SECRET** ou **DB_PASSWORD** ausentes: A aplicação **não inicia**. Spring Boot falhará com erro de propriedade não resolvida.
- **Cloudinary** ausentes: O upload de logos de marcas falhará com `IllegalStateException` ao tentar fazer upload. A API restante continua funcionando.

Se encontrar erro como `Could not resolve placeholder 'JWT_SECRET'`, configure a variável no ambiente ou em `application.properties` local (nunca commite valores reais).
