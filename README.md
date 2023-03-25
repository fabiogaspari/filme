# Projeto Texo IT - Pior Filme do Golden Raspberry Awards
## API RESTful para possibilitar a leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards

- Passos para rodar o projeto:
1. Clone o projeto do github com o comando: git clone https://github.com/fabiogaspari/filme.git
2. Instale as dependências do maven com o comando (deve ser rodado com um prompt na raiz do projeto, onde se encontra o arquivo pom.xml): mvn dependency:copy-dependencies
3. Rode os testes com o comando: mvn test
4. Rode o projeto com o comando: mvn spring-boot:run

### Observações:
1. Em uma análise da base de dados, foi descoberta a string Brian Robbinsand Sharla Sumpter Bridgett (referindo-se a Brian Robbins and Sharla Sumpter Bridgett), onde Brian Robbinsand está unido ao artigo and. Não foi feito tratamento nesse caso, pois para separar os produtores apenas pela string "and " poderia gerar erros em outras bases de dados. Nesse caso em especifico, sugere-se tratar apenas o registro.
