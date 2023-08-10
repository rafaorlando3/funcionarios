# Projeto de Gerenciamento de Funcionários

Este projeto Java foi desenvolvido com o objetivo de criar uma aplicação para gerenciar os funcionários de uma indústria. Através deste sistema, é possível realizar diversas operações com os funcionários, como inserção, remoção, atualização e visualização de informações. 

## Requisitos Implementados

O projeto atende aos seguintes requisitos:

1. A classe `Pessoa` que possui os atributos `nome` (String) e `dataNascimento` (LocalDate).
2. A classe `Funcionário` que herda da classe `Pessoa`, contendo os atributos adicionais `salário` (BigDecimal) e `função` (String).
3. A classe `Principal` para executar ações como inserir, remover, imprimir e atualizar funcionários, além de realizar operações de agrupamento, ordenação e cálculos.
4. Na edição de cada funcionário é possível dar aumento de qualquer porcentagem.
5. Funcionários são agrupados por função em um MAP.
6. Impressão dos funcionários agrupados por função.
7. Impressão dos funcionários que fazem aniversário nos meses 10 e 12.
8. Impressão do funcionário mais velho, com nome e idade.
9. Impressão da lista de funcionários em ordem alfabética.
10. Impressão do total dos salários dos funcionários.
11. Impressão da quantidade de salários mínimos ganhos por cada funcionário.

## Tecnologias Utilizadas

O projeto utiliza as seguintes tecnologias:

- Java: Linguagem de programação orientada a objetos.
- Spring Boot: Framework para facilitar a criação de aplicativos Java.
- Thymeleaf: Motor de template que permite a integração de HTML com Java.
- HTML, CSS e JavaScript: Para criar a interface do usuário.
- Bootstrap: Framework de estilo para melhorar o design e a usabilidade.

O sistema foi desenvolvido de forma a cumprir todos os requisitos especificados, permitindo o gerenciamento eficiente dos funcionários da indústria.

## Como Executar e Testar o Projeto

Para executar e testar o projeto, siga os seguintes passos:

1. **Baixar e Instalar o Spring Tools Suite:**

   - Baixe o Spring Tools Suite (STS) da página oficial: [Spring Tools Suite](https://spring.io/tools).
   - Siga as instruções de instalação específicas para o seu sistema operacional.

2. **Clonar o Repositório:**

   - Abra o Spring Tools Suite.
   - No menu superior, selecione "File" > "Import...".
   - Na janela de importação, escolha "Git" > "Projects from Git" e clique em "Next".
   - Selecione "Clone URI" e clique em "Next".
   - No campo "URI", insira o URL do repositório (URL do seu repositório Git) e clique em "Next".
   - Siga as etapas para configurar o repositório, como selecionar o branch principal (normalmente, "main" ou "master").
   - Escolha o diretório de destino e clique em "Next".
   - Selecione "Import existing Eclipse projects" e clique em "Next".
   - Selecione o projeto e clique em "Finish".

3. **Executar o Projeto:**

   - No Spring Tools Suite, navegue até a classe `Principal` no pacote que contém a lógica do projeto.
   - Clique com o botão direito na classe `Principal` e selecione "Run As" > "Java Application".
   - Isso executará a classe e mostrará os resultados das operações no console.

4. **Testar no Navegador:**

   - Abra um navegador da web.
   - Digite a URL: `http://localhost:8080` (por padrão, o servidor incorporado do Spring Boot roda na porta 8080).
   - Você será direcionado para a página inicial do projeto, onde poderá interagir com as operações implementadas.

Também coloquei o arquivo Sistema_Funcionarios_Exportado.jar caso seja conveniente.
