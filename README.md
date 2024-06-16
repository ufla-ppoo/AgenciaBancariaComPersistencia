# Agência Bancária com Persistência

Este projeto apresenta um exemplo simples de persistência em Java.

## Tipos de Persistência

Ele mostra como persistir (salvar e recuperar) dados de contas bancárias a partir de três tipos de persistência:

- em arquivo texto;
- em arquivo binário;
- em banco de dados SQLite.

O projeto demonstra o uso de uma interface (`Persistencia`) como um contrato que permite que
a classe `Agencia` funcione com diferentes tipos de persistência.

- Você pode escolher qual persistência usar comentando/descomentando as linhas que criam o objeto
  concreto de persistência no método `main` da classe `App`.

## Dependências

Para a persistência em banco de dados, o projeto utiliza o driver `sqlite-jdbc` para acessar o banco de dados SQLite.

O arquivo .jar do driver (e de suas dependências) se encontram na pasta lib do projeto.

- Para obter uma versão mais atual do driver, siga as instruções em: https://github.com/xerial/sqlite-jdbc
- Obs.: O arquivo `slf4j-nop-1.7.36.jar` não é realmente necessário.
  - Ele é usado para que não aparecem mensagens de alertas sobre a falta de um arquivo de configuração do SLF4J.
  - Atenção: o ideal seria utilizar uma biblioteca de logging de verdade, para que você possa ver mensagens de log do SQLite.