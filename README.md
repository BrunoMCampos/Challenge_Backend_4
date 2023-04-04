# 🎯 Challenge Backend 4ª Edição - Alura

![Badge laranja do Java 17](https://img.shields.io/badge/Java-17-orange)
![Badge verde do MySQL](https://img.shields.io/badge/MySQL-green)
![Badge verde do Spring Boot 3.0.5](https://img.shields.io/badge/Spring%20Boot-3.0.5-green)
![Badge verde de Gestão de Dependências Maven](https://img.shields.io/badge/Gestão%20De%20Dependências-Maven-green)
![Badge amarela esverdeada de Quantidade de Dependências 13](https://img.shields.io/badge/Depend%C3%AAncias-13-yellowgreen)
![Badge de linguagem utiliaza Java](https://img.shields.io/badge/Linguagem-JAVA-yellow)
![Badge de Status do projeto como em desenvolvimento](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellowgreen)
![Badge de Desenvolvedor com o nome Bruno](https://img.shields.io/badge/Desenvolvedor-Bruno-green)
![Badge de Token JWT verde com Auth0](https://img.shields.io/badge/TokenJWT-Auth0-green)
![Badge de Google Cloud branca com Compute Engine](https://img.shields.io/badge/Google%20Cloud-Compute%20Engine-white)

---

## 📋 Descrição do Projeto
Projeto criado durante um evento da escola online de tecnologia [Alura](https://www.alura.com.br/) onde todos os alunos receberam um conjunto de informações via [Trello](https://trello.com) e instruções via lives e videos gravados para criarem uma API REST para um projeto de gestão financeira. Os cards Trello são dividios semanalmente, sendo disponibilizado um novo quadro a cada semana.

---

## Acesso a Documentação e endereço da API

A api pode ser acessada por meio do IP
```
  http://34.151.195.83:8080
```

Lembrando que todas as requisições exigem o token JWT que deve ser obtido por meio de login com o usuário padrão no endpoint "/login"
```
{ 
  "login": "bruno.campos@financas.com.br",
  "senha": "123456"
}
```

A documentação pode ser acessada por meio do link
```
  http://34.151.195.83:8080/swagger-ui/index.html
```

---

### 📆 Semana 1
Nesta [primeira semana](https://trello.com/b/bQBI8NPo/challenge-backend-4-semana-1) as tarefas foram referentes a criação dos endpoints para o CRUD de receitas e despesas, respeitando as regras de negócio estabelecidas. 

![image](https://user-images.githubusercontent.com/100006703/229295804-e30ee519-5cf3-43ac-aa85-4ef2f087fb0a.png)
![image](https://user-images.githubusercontent.com/100006703/229295816-4b355792-773c-4596-a43c-0093155e28b0.png)

Para a elaboração das tarefas os alunos tem total liberdade de escolha quanto a linguagem de programação ou mesmo os métodos de criação de bancos de dados e classes a serem utilizadas. Abaixo detalhei o que elaborei nesta primeira etapa do projeto:

#### Banco de Dados
Para a elaboração do projeto utilizei MySQL com Flyway, permitindo versionamento do banco de dados e controle das querys lanças ao banco de dados dentro da pasta resources/db/migration.

#### Linguagem e Framework
Por questão de prática e até mesmo para treino e aperfeiçoamento escolhi a linguagem JAVA, que é a que tenho maior interesse, assim como o Framework Spring.

#### Testes
Para realização dos testes nesta primeira semana foi utilizado o POSTMAN.

---

#### 🔨 Forma de elaboração
Haviam inúmeras formas de gerar o banco de dados para os dois endpoints, porém preferi tentar começando com apenas uma tabela no banco de dados, utilizando uma classe Lancamento como entidade e uma tabela lancamento no banco de dados, permitindo assim que todos os dados fossem cadastrados em uma mesma tabela, sendo diferenciados pela coluna adicional tipo, provida por um enum TipoLancamentoEnum. Com isso reaproveitei grande parte do código escrito, apenas separando os controllers para endpoints diferentes, permitindo que alterações futuras possam ser feitas separadamente sem problemas maiores.
Também utilizei de classes Service para poder gerir melhor as validações, realizando verificações mais intensas fora da classe controller.

---

#### 📜 Tarefas da Semana 1
- [x] Criar o banco de dados;
- [x] Criar endpoints para o CRUD de Receitas e Despesas;
- [x] Validar dados; 
- [x] Realizar testes manuais via postman

---

### 📆 Semana 2
Nesta [segunda semana](https://trello.com/b/4V5WcXVi/challenge-backend-4-semana-2) recebemos um novo quadro no Trello com as informações que precisariam ser incluidas ou alteradas no sistema, considerando que ele foi liberado para a utilização e foi recebido o feedback dos clientes da aplicação, sendo necessário realizar adaptações no banco de dados e o desenvolvimento de novas funcionalidades.

![Card Trello](https://user-images.githubusercontent.com/100006703/229295595-2fdbf6df-57e7-4ad7-850e-b25ab370aaf1.png)

#### 🔨 Forma de elaboração
Como descrito no card do Trello, se fez necessário adicionar um novo campo nos lançamentos referentes as despesas, sendo assim achei por bem elaborar duas tabelas separadas, criando as tabelas receitas e despesas e suas respectivas entidades, onde cada uma receberia os dados separadamente, permitindo um futuro crescimento do banco e também de funcionalidades individuais, também utilizei de um ENUM para a categorização das despesas
Após elaborar as entidades, refatorei os controllers, repositorys e criei uma classe service para realizar a etapa de geração de resumos, usando recurso do JAVA 8, como o stream, para realizar a soma por categoria e a geração do relatório mensal.
Por fim criei testes automatizados focando apenas nos controllers com testes que utilizassem o mesmo banco de dados e realizassem a chamada dos repositorys e das services conforme necessidade, realizando o teste de forma mais abrangente e completa.

---

#### 📜 Tarefas da Semana 2
- [x] Alterar o banco de dados para aceitar um novo campo de categorias para as despesas;
- [x] Criar um endpoint para realizar a listagem por mês de despesas e receitas;
- [x] Criar um endpoint para gerar um resumo mensal das despesas e receitas;
- [x] Criar o endpoint para a busca de receitas e despesas por descrição.

---

### 📆 Semanas 3 e 4
Nestas [semanas](https://trello.com/b/kLJ8pbV2/challenge-backend-4-semanas-3-e-4) recebemos as tarefas de implementar autenticação e também o deploy da aplicação.

![image](https://user-images.githubusercontent.com/100006703/229925110-3d31bec9-bf89-45ba-8448-1fe2463f13ba.png)

#### 🔨 Forma de elaboração
Para a implementação da parte de segurança adicionei as dependências do Spring Security e também do [Token JWT fornecido pelo Auth0](https://github.com/auth0/java-jwt), com isso a implementação foi realizada com criptografia BCrypt para as senhas e com a criação de um novo endpoint para login (/login), onde se é possível realizar o acesso com um usuário padrão, sendo enviado em um json os dados abaixo via método POST:

```
{ 
  "login": "bruno.campos@financas.com.br",
  "senha": "123456"
}
```

A partir deste endpoint se recebe um Token de acesso que deverá ser utilizado para realizar as requisições para todos os outros endpoints, que agora estarão bloqueados para requisições sem autorização.

Quanto ao Deploy confesso não ter muita experiência, por isso tive muitas dificuldades, então realizei a disponibilização da API da única forma como consegui, mesmo após algumas pesquisas na internet. Futuramente pretendo realizar uma melhoria se assim for possível.
Utilizei o serviço do Google Cloud com o Compute Engine, criando uma máquina virtual Ubuntu e instalando dentro dela o MySql e o jar da aplicação, com isso executei a aplicação dentro com o comando nohup e encessei a comunicação SSH, permitindo que a aplicação continue rodando continuamente no IP abaixo, que também está sendo usado para acessar a documentação da API.

```
  http://34.151.195.83:8080
```

##### Documentação

Tomei a liberdade de adicionar também uma [documentação](http://34.151.195.83:8080/swagger-ui/index.html) utilizando o Swagger UI.

![image](https://user-images.githubusercontent.com/100006703/229926132-4c230be0-0f86-4313-b26a-35145e2a9226.png)
![image](https://user-images.githubusercontent.com/100006703/229926233-294ee45e-6301-4319-9181-c6367cad1ffc.png)


#### 📜 Tarefas das Semanas 3 e 4
- [x] Aumentar a segurança da API gerando um novo endpoint para login e realizando a verificação stateless para acesso em todas as requisições;
- [x] Realizar o deploy da aplicação;
- [x] Criar Documentação. (EXTRA!)
---
