# Desafio Fullstack Integrado – Solução BIP

## 1. Objetivo

Entregar uma aplicação **fullstack em camadas** (DB, EJB, Backend e Frontend), com:

- **Bug de transferência** corrigido no EJB.
- **Fluxos CRUD** de benefício funcionando do backend ao frontend.
- **Ambiente completo orquestrado em Docker**.

---

## 2. Clonando o repositório

```bash
git clone https://github.com/<seu-usuario>/bip-teste-integrado.git
cd bip-teste-integrado
```

> Troque `<seu-usuario>` pelo usuário ou origem correta do repositório (fork etc.).

---

## 3. Rodando tudo com Docker (caminho recomendado)

### 3.1. Build dos módulos Java (opcional mas recomendado)

Na raiz do projeto:

```bash
mvn clean package -DskipTests
```

Isso garante que os artefatos (JAR/WAR) já estejam gerados antes do Docker montar as imagens.

### 3.2. Subir toda a stack

Na raiz do projeto:

```bash
docker-compose up --build
```

- O Docker vai subir:
  - `db` (PostgreSQL + schema.sql + seed.sql)
  - `ejb` (WildFly + ejb-app.war)
  - `backend` (Spring Boot)
  - `frontend` (Angular em Nginx)

Para subir em background:

```bash
docker-compose up --build -d
```

### 3.3. Parar a stack

```bash
docker-compose down
```

---

## 4. Endereços principais (via Docker)

Assumindo as portas padrão do `docker-compose.yml`:

- **Frontend (Angular/Nginx)**`http://localhost:4200`
- **Backend (Spring Boot + Swagger)**

  - API: `http://localhost:8080`
  - Swagger UI: `http://localhost:8080/swagger-ui.html`
  - OpenAPI JSON: `http://localhost:8080/api-docs`
- **EJB (WildFly / REST)**

  - Base (exemplo): `http://localhost:8081/ejb-app/api`
  - Transferência: `POST http://localhost:8081/ejb-app/api/transferencias`

*(A URL exata do EJB pode variar conforme o `docker-compose.yml`; ajustar se necessário.)*

---

## 5. Rodando localmente sem Docker (opcional)

Se preferir subir tudo “na mão”:

### 5.1. Banco de Dados (PostgreSQL)

1. Suba um PostgreSQL local.
2. Crie o banco (se necessário):

   ```bash
   createdb bip_beneficios
   ```
3. Execute os scripts:

   ```bash
   psql -d bip_beneficios -f db/schema.sql
   psql -d bip_beneficios -f db/seed.sql
   ```
4. Ajuste as credenciais no `application.properties` / `application.yml` do `backend-module`.

---

### 5.2. EJB + WildFly

#### 5.2.1. Build do EJB

Na raiz do projeto:

```bash
mvn -pl ejb-module,ejb-app -am clean package
```

Isso gera o `ejb-app.war` em:

- `ejb-app/target/ejb-app.war`

#### 5.2.2. Deploy no WildFly

Copie o WAR para a pasta de deploy do seu WildFly, por exemplo:

```bash
cp ejb-app/target/ejb-app.war /path/para/wildfly/standalone/deployments/
```

Inicie o WildFly (se ainda não estiver rodando):

```bash
/path/para/wildfly/bin/standalone.sh
```

Depois valide o endpoint (ajuste host/porta se necessário):

```bash
curl -X POST http://localhost:8080/ejb-app/api/transferencias \
  -H "Content-Type: application/json" \
  -d '{"fromId":1,"toId":2,"valor":100.00}'
```

---

### 5.3. Backend (Spring Boot)

Na raiz do projeto ou dentro de `backend-module/`:

#### 5.3.1. Rodando direto via Maven

```bash
cd backend-module
mvn clean spring-boot:run
```

#### 5.3.2. Rodando via JAR

```bash
cd backend-module
mvn clean package
java -jar target/backend-module-*.jar
```

A API ficará disponível em:

- `http://localhost:8080`

Endpoints principais:

- `GET /api/beneficios`
- `GET /api/beneficios/ativos`
- `GET /api/beneficios/{id}`
- `POST /api/beneficios`
- `PUT /api/beneficios/{id}`
- `DELETE /api/beneficios/{id}`
- `POST /api/beneficios/transferir`

Swagger:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/api-docs`

---

### 5.4. Frontend (Angular)

Na pasta `frontend/`:

#### 5.4.1. Instalar dependências

```bash
cd frontend
npm install
```

#### 5.4.2. Rodar em modo desenvolvimento

```bash
npm run start
```

Ou, dependendo da configuração, o padrão Angular:

```bash
ng serve
```

Normalmente acessível em:

- `http://localhost:4200`

#### 5.4.3. Build de produção

```bash
npm run build
```

Os arquivos finais ficarão em `frontend/dist/` (ou conforme configurado).

---

## 6. Módulo EJB – Detalhes da Correção

Arquivos principais:

- `ejb-module/src/main/java/com/example/ejb/Beneficio.java`
- `ejb-module/src/main/java/com/example/ejb/BeneficioEjbService.java`

Implementado:

- Validação de parâmetros (`fromId`, `toId`, `valor`).
- Verificação de ativo/inativo.
- Verificação de saldo suficiente (antes de debitar).
- Lock pessimista com `LockModeType.PESSIMISTIC_WRITE`.
- Campo de versão para apoio ao optimistic locking.
- Método transacional com rollback em caso de exceção.

Endpoint exposto via `ejb-app`:

- `POST /ejb-app/api/transferencias`

---

## 7. Testes Automatizados

### 7.1. Rodar testes dos módulos principais

Na raiz do projeto:

```bash
mvn -pl ejb-module,backend-module -am test
```

### 7.2. Teste de serviço (Backend)

Arquivo principal:

- `backend-module/src/test/java/com/example/service/BeneficioServiceTest.java`

Características:

- JUnit 5.
- Stub simples para o client do EJB (evita problemas com Mockito em Java 25).
- Verifica delegação para o EJB e mapeamento de respostas.

---

## 8. Como validar rapidamente o desafio

1. Rodar testes:

   ```bash
   mvn -pl ejb-module,backend-module -am test
   ```
2. Subir tudo com Docker:

   ```bash
   docker-compose up --build
   ```
3. Chamar a transferência via backend:

   ```bash
   curl -X POST http://localhost:8080/api/beneficios/transferir \
     -H "Content-Type: application/json" \
     -d '{"fromId":1,"toId":2,"valor":100.00}'
   ```
4. Conferir Swagger:

   ```text
   http://localhost:8080/swagger-ui.html
   ```
5. Testar via frontend:

   - Abrir `http://localhost:4200`
   - Preencher o formulário de transferência.
   - Validar que o saldo é atualizado corretamente e não há erro.

---

## 9. Resumo

- **Bug de transferência** ajustado com validações, controle de saldo e locking.
- **Backend** expõe CRUD completo + operação de transferência integrada ao EJB.
- **Frontend** oferece uma UI simples para disparar a transferência.
- **Docker Compose** permite subir tudo com apenas um comando:

  ```bash
  docker-compose up --build
  ```
