# ECSE223 Project Repository - 2019 Fall - Group 5

<img align="right" width=400 src="https://github.com/McGill-ECSE223-Fall2019/ecse223-project--group-05/blob/master/src/main/resources/UIDocumentation/logo_black.png?raw=true">

This GitHub repository contains a recreation of the Quoridor board game. It is the work of six McGill University Students: Edwin Pan, Alex Masciotra, Thomas Philippon, Matthias Arabian, David Deng, and Daniel Wu. Is was noted as one of the projects with the most bonus features in the Fall 2019 edition of the course.  

## Running our Application  

Our Quoridor Game runs on Java Runtime 11. As such, you will need to have Java Runtime 11 installed in your system in order to be able to run our application. Currently, our application does not compile to an executable, so running the application can be done either by running the QuoridorApplication.java file in _/src/main/java/ca/mcgill/ecse223/quoridor_ folder as a java 11 application or by running the terminal command _gradlew.bat run_ in the root folder of our repository.

Of final note is that we have tested our application on Windows and MacOS, but not Linux. As such, our application might be anomalous when being run on Linux operating systems like Ubuntu or CentOS.

## About Quoridor

The full details about the original Quoridor game on which our Quoridor application is based on can be found in its Wikipedia article: https://en.wikipedia.org/wiki/Quoridor. It essentially boils down to a game where players represented by pawns placed on a nine-by-nine tile board start on opposite ends of each other and must win by reaching their other sides before their opponent(s) as they take turns completing singular actions.

<p style="center"> 
  <img src="https://github.com/McGill-ECSE223-Fall2019/ecse223-project--group-05/blob/master/src/main/resources/UIDocumentation/Picture2.png?raw=true">
</p>

Tools available to players are the ability to leap over opposing players when they are directly adjacent and the ability to place walls to obstruct player movements. Of course, players can always move normally by moving to the a tile directly adjacent to them.

Leaps typically bring players to the opposite sides of their adjacent opponents; however in the case of the opponent being on the edge of the board's tiles or the presence of a wall obstructing this leap, leaps become diagonal movements assuming the tile is in bounds and the path is not obstructed by another wall. 

Walls block players ability to move or leap past them. Players each start with a stock of ten walls that can be placed anywhere so long as they do not intersect with other walls and they do not make it impossible for player to win (ie. a path must always exist; you merely want to extend the shortest path to victory for your opponent).

While the original Quoridor game allows for 4-player gameplay, our application does not support this. As such, there is only ever two players: one playing with the white pawn and the other playing with the black pawn.

