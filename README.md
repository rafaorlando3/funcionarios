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

## Como Executar

Para executar o projeto, siga os seguintes passos:

1. Certifique-se de ter o Java instalado em seu computador.
2. Clone o repositório para sua máquina.
3. Abra o projeto em sua IDE Java de preferência.
4. Execute a classe `Principal` para ver os resultados das operações.

O projeto oferece uma visão completa das funcionalidades implementadas, facilitando o entendimento e a utilização das operações de gerenciamento de funcionários.
