# Minesweeper Java Applet

Minesweeper is a single player video game. The object of the game is to clear an abstract minefield without detonating a mine - [Wikipedia] [1]

## Example
To see a web example of this codebase, go to http://www.kangarooit.com/minesweeper/ (with Java Applet support in your browser) and click the 'Launch Minesweeper' Button

## Project inspiration
This Minesweeper applet was built from scratch during uni summer holidays in 2000 to play with Java & AWT (Java Swing was not a package installed by default in web browsers). All the button implementations, game logic and everything is coded into the applet - the Board.java algorithms were lots of fun!

The graphics were all cut-up screenshots of Minesweeper on the Windows 2000 box. 

## Running this codebase
Hasn't been done in 10 years but probably something along the lines of:

    $ cd src
    $ javac -classpath .;"C:\Program Files\Netscape\Communicator\Program\java\classes\java40.jar"; HighScores.java
    $ javac Minesweeper.java
    $ jar -cvf Minesweeper.jar *.class *.gif *.jpg *.wav *.au

## Codebase thoughts
In the year 2000 the GUI capabilities of Java Applets were well ahead of their time.

This codebase is understandably a bit messy - being a university project and having been built with very little programming experience.

  [1]: http://en.wikipedia.org/wiki/Minesweeper_(video_game) "Wikipedia"  