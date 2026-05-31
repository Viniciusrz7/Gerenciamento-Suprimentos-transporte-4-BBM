<h1 align="center">
  🚒 BBM-4 — Gerenciamento de Suprimentos e Frota
</h1>

<p align="center">
  Sistema desktop de gerenciamento operacional desenvolvido para o <strong>4º Batalhão de Bombeiros Militar</strong>.
  <br/>
  Controle completo de viaturas, manutenções, suprimentos e processos administrativos.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hibernate-6.4-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.3-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swing-Desktop-007396?style=for-the-badge&logo=java&logoColor=white"/>
</p>

---

## 📋 Sobre o Projeto

O **BBM-4 Gerenciamento de Suprimentos e Frota** é uma aplicação desktop desenvolvida em **Java Swing** para o 4º Batalhão de Bombeiros Militar. O sistema centraliza o controle operacional da frota de viaturas e a gestão de suprimentos, substituindo controles manuais e planilhas por uma solução integrada e acessível em rede local.

O sistema roda diretamente de uma pasta compartilhada na rede interna do batalhão, garantindo que todos os postos de trabalho utilizem sempre a versão mais atualizada sem necessidade de instalação individual.

---

## ✅ Funcionalidades

### 🔐 Controle de Acesso
- Login com autenticação de usuário e senha
- Gerenciamento de usuários do sistema

### 🚒 Gestão de Viaturas
- Cadastro e consulta de viaturas da frota
- Histórico completo por viatura

### 🔧 Manutenção Preventiva e Corretiva
- **Revisões** — Registro e acompanhamento de revisões periódicas
- **Troca de Óleo** — Controle de trocas com km e data
- **Baterias** — Histórico de substituição de baterias
- **Pneus** — Gestão de estoque e substituições

### ⛽ Controle de Abastecimento
- Registro de abastecimentos via **Cartão de Abastecimento**
- Histórico por viatura e por período

### 📋 Processos Administrativos (SEI)
- Gestão de **Modelos SEI** e **Situações de Processo**
- Controle e acompanhamento de processos institucionais

### 📜 Convênios e Contratos
- Cadastro e gerenciamento de **Convênios**

### 🤝 Doações
- Registro e controle de **Doações** recebidas

### 💥 Registro de Acidentes
- Registro detalhado de acidentes envolvendo a frota

### 🛠️ Serviços Realizados
- Histórico de **Serviços Realizados** em cada viatura

---

## 🏗️ Arquitetura do Sistema

```
src/
└── main/
    ├── java/com/bbm4/
    │   ├── Main.java              ← Ponto de entrada da aplicação
    │   ├── model/                 ← Entidades JPA (mapeamento banco de dados)
    │   │   ├── Usuario.java
    │   │   ├── Viatura.java
    │   │   ├── Revisao.java
    │   │   ├── TrocaOleo.java
    │   │   ├── Bateria.java
    │   │   ├── PneuEstoque.java
    │   │   ├── CartaoAbastecimento.java
    │   │   ├── Acidente.java
    │   │   ├── Doacao.java
    │   │   ├── Convenio.java
    │   │   ├── ServicoFeito.java
    │   │   ├── ModeloSei.java
    │   │   └── SituacaoProcessoSei.java
    │   ├── dao/                   ← Camada de acesso a dados (GenericDao + DAOs específicos)
    │   │   ├── GenericDao.java    ← CRUD genérico com JPA/Hibernate
    │   │   ├── UsuarioDao.java
    │   │   ├── ViaturaDao.java
    │   │   └── ...
    │   └── view/                  ← Interface gráfica (Java Swing)
    │       ├── LoginView.java     ← Tela de login
    │       ├── MainView.java      ← Tela principal com todos os módulos
    │       └── AppTheme.java      ← Design system e estilo visual
    └── resources/
        └── META-INF/
            └── persistence.xml    ← Configuração JPA/Hibernate
```

**Padrão arquitetural:** `MVC` (Model-View-Controller) com camada DAO para persistência.

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 17 LTS | Linguagem principal — versão estável e de longo suporte |
| **Java Swing** | — | Framework de interface gráfica desktop |
| **Hibernate ORM** | 6.4.4 | Mapeamento objeto-relacional (ORM), elimina SQL manual |
| **Jakarta Persistence (JPA)** | 3.0 | API padrão de persistência Java |
| **MySQL Connector/J** | 8.3.0 | Driver de conexão com banco de dados MySQL |
| **MySQL Server** | 8.x | Banco de dados relacional — armazenamento de todos os dados |
| **Apache Maven** | 3.9 | Gerenciamento de dependências e build do projeto |
| **jBCrypt** | 0.4 | Hash seguro de senhas dos usuários |
| **SLF4J** | — | Abstração de logging |

---

## ⚙️ Pré-requisitos

### Computador Servidor
- ✅ **MySQL Server 8.x** instalado e rodando
- ✅ Banco de dados `bbm4` criado
- ✅ Porta `3306` liberada no firewall
- ✅ Pasta compartilhada na rede (`\\**.15.**.**\BBM`)
- ✅ Computador ligado durante o horário de expediente

### Computadores Clientes
- ✅ **Java 17** ou superior instalado
  - Download: [https://adoptium.net/](https://adoptium.net/)
- ✅ Conectividade de rede com o servidor (`**.15.**.**`)
- ✅ Acesso à pasta compartilhada

---

## 🚀 Como Executar

### Opção 1 — Execução via Rede (Usuário Final)

Acesse a pasta compartilhada e execute:

```
\\**.15.**.**\\BBM\Executar.bat
```

> Dê um duplo-clique no arquivo `Executar.bat` — o sistema abrirá automaticamente.

### Opção 2 — Execução Local (Desenvolvimento)

```bat
run_app.bat
```

Ou manualmente via linha de comando:

```bash
java -cp "BBM-Gerenciamento.jar;sqlite-jdbc-3.45.3.0.jar;mysql-connector-j-8.3.0.jar;slf4j-stub.jar" com.bbm4.Main
```

### Opção 3 — Build do Projeto (Desenvolvedor)

```bash
# Compilar e empacotar o JAR
mvn clean package

# Ou via script
build_jar.bat
```

---

## 🗄️ Configuração do Banco de Dados

### Informações de Conexão

| Parâmetro | Valor | Descrição |
|---|---|---|
| **Host** | `**.15.**.**` | IP do Servidor |
| **Porta** | `3306` | Porta do Banco de Dados |
| **Banco** | `b*********` | Banco de Dados |
| **Usuário** | `***_***` | Usuário do Banco de Dados |
| **Dialect** | `MySQLDialect` (Hibernate) | 

### Configurar MySQL para conexões remotas

```sql
-- No MySQL Server, verifique o bind-address:
SHOW VARIABLES LIKE 'bind_address';
-- Deve retornar: * ou 0.0.0.0
```

```bat
:: Liberar porta 3306 no Firewall do Windows (execute como Administrador):
netsh advfirewall firewall add rule name="MySQL BBM" dir=in action=allow protocol=TCP localport=3306
```

### Tabelas gerenciadas automaticamente pelo Hibernate

O Hibernate cria e atualiza as tabelas automaticamente via `hbm2ddl.auto=update`:

- `usuario` · `viatura` · `revisao` · `troca_oleo` · `bateria`  
- `pneu_estoque` · `cartao_abastecimento` · `acidente` · `doacao`  
- `convenio` · `servico_feito` · `modelo_sei` · `situacao_processo_sei`

---

## 🔑 Acesso ao Sistema

| Campo | Valor Padrão |
|---|---|
| **Login** | `admin` |
| **Senha** | `admin` |

> ⚠️ **Importante:** Altere a senha padrão após o primeiro acesso em produção.

---

## 🖥️ Implantação em Produção

### Estrutura da Pasta Compartilhada

```
├── BBM-Gerenciamento.jar       ← Aplicativo principal
├── Executar.bat                ← Script de inicialização para usuários
```

### Atualizar o Sistema

1. Feche o aplicativo em **todos** os computadores
2. Substitua o `BBM-Gerenciamento.jar` na pasta compartilhada
3. Pronto — na próxima execução, todos já usam a versão nova

---

## 💾 Backup e Manutenção

### Backup do Banco de Dados

```bash
# Gerar backup
mysqldump -u admin_bombeiros -p bbm4 > backup_bbm4_20260531.sql

# Script automatizado (Windows)
mysqldump -u admin_bombeiros -p bbm4 > backup_bbm4_%date:~-4,4%%date:~-7,2%%date:~-10,2%.sql
```

### Restaurar Backup

```bash
mysql -u admin_bombeiros -p bbm4 < backup_bbm4_20260531.sql
```

> 💡 **Recomendação:** Realize backups semanais e armazene em local seguro separado do servidor.

---

## 📁 Estrutura do Repositório

```
📦 Gerenciamento-Suprimentos-transporte-4-BBM
 ├── 📁 src/                         ← Código-fonte Java
 │   └── main/java/com/bbm4/
 │       ├── Main.java
 │       ├── model/                  ← Entidades de domínio
 │       ├── dao/                    ← Camada de persistência
 │       └── view/                   ← Interface gráfica Swing
 ├── 📁 META-INF/                    ← Manifesto JAR
 ├── 📄 pom.xml                      ← Configuração Maven e dependências
 ├── 📄 run_app.bat                  ← Execução local
 ├── 📄 run_debug.bat                ← Execução em modo debug
 ├── 📄 build_jar.bat                ← Build do projeto
 └── 📄 .gitignore                   ← Arquivos ignorados pelo Git
```

---

## 👤 Autor

**Vinícius Reis Zimmermann**

[![GitHub](https://img.shields.io/badge/GitHub-Viniciusrz7-181717?style=for-the-badge&logo=github)](https://github.com/Viniciusrz7)

---

## 📄 Licença

Este projeto foi desenvolvido para uso institucional do **4º Batalhão de Bombeiros Militar**.  
Todos os direitos reservados © 2026.

---

<p align="center">
  Desenvolvido com ❤️ para o <strong>4º Batalhão de Bombeiros Militar</strong>
</p>
