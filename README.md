# 🎯 Challenge Backend 4ª Edição - Alura

![Badge laranja do Java 17](https://img.shields.io/badge/Java-17-orange)
![Badge verde do MySQL](https://img.shields.io/badge/MySQL-green)
![Badge verde do Spring Boot 3.0.5](https://img.shields.io/badge/Spring%20Boot-3.0.5-green)
![Badge verde de Gestão de Dependências Maven](https://img.shields.io/badge/Gestão%20De%20Dependências-Maven-green)
![Badge amarela esverdeada de Quantidade de Dependências 9](https://img.shields.io/badge/Depend%C3%AAncias-9-yellowgreen)
![Badge de linguagem utiliaza Java](https://img.shields.io/badge/Linguagem-JAVA-yellow)
![Badge de Status do projeto como em desenvolvimento](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellowgreen)
![Badge de Desenvolvedor com o nome Bruno](https://img.shields.io/badge/Desenvolvedor-Bruno-green)

---

## 📋 Descrição do Projeto
Projeto criado durante um evento da escola online de tecnologia Alura (https://www.alura.com.br/) onde todos os alunos receberam um conjunto de informações via Trello (https://trello.com/b/bQBI8NPo/challenge-backend-4-semana-1) e instruções via lives ao vivo e videos gravados para criarem uma API REST para um projeto de gestão financeira. Os cards Trello são dividios semanalmente, sendo disponibilizado um novo quadro a cada semana.
Nesta primeira semana as tarefas foram referentes a criação dos endpoints para o CRUD de receitas e despesas, respeitando as regras de negócio estabelecidas.

Para a elaboração das tarefas os alunos tem total liberdade de escolha quanto a linguagem de programação ou mesmo os métodos de criação de bancos de dados e classes a serem utilizadas. Abaixo detalhei o que elaborei nesta primeira etapa do projeto:

### Banco de Dados
Para a elaboração do projeto utilizei MySQL com Flyway, permitindo versionamento do banco de dados e controle das querys lanças ao banco de dados dentro da pasta resources/db/migration.

### Linguagem e Framework
Por questão de prática e até mesmo para treino e aperfeiçoamento escolhi a linguagem JAVA, que é a que tenho maior interesse, assim como o Framework Spring.

### Testes
Para realização dos testes nesta primeira semana foi utilizado o POSTMAN.

---

### Forma de elaboração

Haviam inúmeras formas de gerar o banco de dados para os dois endpoints, porém preferi tentar começando com apenas uma tabela no banco de dados, utilizando uma classe Lancamento como entidade e uma tabela lancamento no banco de dados, permitindo assim que todos os dados fossem cadastrados em uma mesma tabela, sendo diferenciados pela coluna adicional tipo, provida por um enum TipoLancamentoEnum. Com isso reaproveitei grande parte do código escrito, apenas separando os controllers para endpoints diferentes, permitindo que alterações futuras possam ser feitas separadamente sem problemas maiores.
Também utilizei de classes Service para poder gerir melhor as validações, realizando verificações mais intensas fora da classe controller.

---

### 📜 Tarefas da Semana 1

- [x] Criar o banco de dados;
- [x] Criar endpoints para o CRUD de Receitas e Despesas;
- [x] Validar dados; 
- [x] Realizar testes manuais via postman

---
