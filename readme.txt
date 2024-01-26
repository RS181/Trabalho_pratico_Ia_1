Instruções para executar o programa

O código deve ser invocado da seguinte maneira:
java Main <strategy> < config
Onde "code" é a forma de invocar o seu código (nome de executável ou outra forma), 
"<strategy>" poderá ser uma de "DFS", "BFS", "IDFS", "A*-misplaced", "A*-Manhattan, "Greedy-misplaced", "Greedy-Manhattan" 
e "config" é um ficheiro com as duas configurações inicial que está incluído nesta pasta.

Nota: 
o IDFS tem outro parametro (que e o limite maximo de profundidade).Para customizar 
o limite de profundidade e necessario ir a linha 695 (dentro do metodo Start) e modificar
o terceiro parametro do metodo (DFS_Iterative_helper(n, Final, LIMITE_MAXIMO_PROFUNDIDADE))
Como executar o programa:

1.Faça o download do arquivo de instalação do programa 
2.Abra a pasta do programa
3.Abra o terminal no diretório onde se encontra a pasta
4.Compilar o programa usando o seguinte comando no terminal : "javac Main.java"
5.Executar o programa no terminar da forma : "java Main <strategy> < config"; como explicado em cima

Requisitos de Sistema:

Ter instalada a seguinte versão do java: openjdk 17.0.6
Versão do Ubuntu 20.04 LTS ou semelhante 
311,2 kB de espaço livre em disco.
