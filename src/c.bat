del Minesweeper.jar
javac -classpath .;"C:\Program Files\Netscape\Communicator\Program\java\classes\java40.jar"; HighScores.java
javac Minesweeper.java
jar -cvf Minesweeper.jar *.class *.gif *.jpg
del *.class
