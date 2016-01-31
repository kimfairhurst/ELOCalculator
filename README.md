# ELOCalculator

Calculates the "ELO" and associated rankings of players based on an adjacency matrix to a customized-scale

Based on an input of adjacency files in a specified directory, calculates the ELO and associated rankings of players for each file, 
then outputs the rankings to a specified text file. 

"Tester" is an example input file, where the first line indicates the number of players and the rest is the game-board. 
A 1 in location [x, y] indicates x has beaten y in a game. There are no self-edges in a valid game board. 

The ELO-ranking system's chart was based on the one found here: http://gobase.org/studying/articles/elo/
