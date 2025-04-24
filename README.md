# Trabalho

## TODO
- [ ] API
  - [X] Cliente
      - [X] Listar Clientes
      - [X] Buscar Clientes
      - [X] Remover Cliente
      - [X] Cadastrar Cliente
  - [ ] Pacote
      - [x] Listar Pacotes
      - [x] Buscar Pacotes
      - [ ] Remover Pacote (apenas se nao tem clientes relacionados)
      - [x] Cadastrar Pacote
  - [ ] Servico de um Pacote
      - [ ] Listar Servicos
      - [ ] Buscar Servicos
      - [ ] Remover Servico
      - [ ] Incluir Servico
  - [ ] Listar Pacotes de um Cliente
  - [ ] Listar Clientes que contrataram um Pacote

## Sistema para Agência de Viagens

1. Visão Geral

O sistema tem como objetivo gerenciar o cadastro de clientes e pacotes de viagens
em uma agência de turismo. Ele deve permitir a gestão de clientes nacionais e
estrangeiros, bem como o cadastro de pacotes de viagens com diferentes
características. Além disso, deve possibilitar o relacionamento entre clientes e
pacotes de viagem, permitindo que um cliente contrate mais de um pacote.
Outro aspecto controlado pelo sistema é a possibilidade de inclusão de serviços
adicionais: translado, passeios, aluguel de veículos, etc...

2. Requisitos do Sistema

- Funcionalidades Principais
    - Cadastro de Clientes
        - Clientes podem ser nacionais ou estrangeiros.
        - Clientes possuem informações como nome, CPF (para nacionais),
          passaporte (para estrangeiros), telefone e e-mail.
        - Deve ser possível listar, buscar e excluir clientes cadastrados.
    - Cadastro de Pacotes de Viagem
        - Um pacote de viagem deve conter minimamente:
            - Nome do pacote
            - Destino
            - Duração (número de dias)
            - Preço
            - Tipo de pacote (ex: aventura, luxo, cultural, etc.)
            - Cada tipo de pacote apresentará detalhes específicos
        - Deve ser possível listar, buscar e excluir pacotes cadastrados.
    - Serviços Opcionais
        - Deve ser possível incluir, listar, buscar e excluir serviços.
    - Relacionamento Cliente-Pacote
        - Um cliente pode contratar mais de um pacote de viagem.
        - Deve ser possível visualizar os pacotes contratados por um cliente.
        - Deve ser possível listar os clientes que contrataram determinado pacote.

    - Os Serviços estarão relacionados ao Pedido do Cliente
        - Uma contratação de pacote pode conter vários serviços adicionais
- Regras de Negócio
    - Clientes estrangeiros devem obrigatoriamente informar o número do passaporte, nacionais o CPF.
    - O sistema deve garantir que um pacote não seja cadastrado sem preço e destino.
    - Deve ser possível remover pacotes apenas se não houver clientes associados a ele.

3. Estrutura do Sistema

- Classes Principais
    - Classe Cliente
      ```java
      public abstract class Cliente {
      }
      ```
    - Classe PacoteViagem
        ```java
        public abstract class PacoteViagem {
        }
        ```
    - Classe ServicoAdicional
        ```java
        public abstract class ServicoAdicional {
        }
        ```

4. Tecnologias Utilizadas

- Linguagem: Java 11+
- Banco de Dados: MySQL

5. Critérios de Avaliação

- Organização do código e utilização de conceitos de programação orientado
  a objeto;
- Elaboração do Modelo de Dados do sistema;
- Criação de um banco de dados;
- Manual de utilização do sistema;
- Funcionamento do sistema conforme descrito nas instruções;
- Script para popular o banco de dados para eventuais testes.

6. Considerações Finais

Este projeto visa criar um sistema flexível e escalável para o gerenciamento de agências de viagens, permitindo futuras
melhorias.