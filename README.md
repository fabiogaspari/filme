# Projeto Texo IT - Pior Filme do Golden Raspberry Awards
## API RESTful para possibilitar a leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards

- Passos para rodar o projeto:
1. Clone o projeto do github com o comando: git clone https://github.com/fabiogaspari/filme.git
2. Rode os testes com o comando: mvn test
3. Rode o projeto com o comando: mvn spring-boot:run
4. Para testar o método para obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido, basta acessar o navegador e digitar na barra de navegação o seguinte endereço: localhost:8080/filmes/intervalo-premios

### Observações:
1. Em uma análise da base de dados, foi descoberta a string "Brian Robbinsand Sharla Sumpter Bridgett" (referindo-se a "Brian Robbins and Sharla Sumpter Bridgett"), onde "Brian Robbinsand" está unido à conjunção "and". Não foi feito tratamento nesse caso, pois separando os produtores apenas pela string "and " poderia gerar erros em outras bases de dados. Nesse caso em especifico, sugere-se tratar apenas o registro.
