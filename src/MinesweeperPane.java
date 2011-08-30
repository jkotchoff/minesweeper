import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.image.ImageProducer;

import java.applet.Applet;


import java.applet.AudioClip;
/**
 * This is the class that is referenced in the swing applet.  It accepts
 * a parameter specifying the size of the board to set up.  The parameter
 * it expects is one of the public static int's specified as:<BR>
 * - Minesweeper.BEGINNER<BR>
 * - Minesweeper.INTERMEDIATE<BR>
 * - Minesweeper.EXPERT
 *
 * <P>The following method is called from the parent window using
 * javascript:
 * <P><B>newGame()</B> this method sets up a new board and starts
 *			a new game if the user selects 'new game'
 *			from the menu
 *
 * @author Jason Kotchoff
 */

public class MinesweeperPane extends Panel {
   public int board_type;
   private Board beginner_board = null;
   private Board intermediate_board = null;
   private Board expert_board = null;
   private Board board = null;   	//panel that stores the board
   protected Face face;
   private boolean initialised = false; //doesn't allow the menu to start a game until it has been initialised
   protected BombCounter bomb_counter;
   protected Timer timer;
   private boolean clock_running = false;
   private GridBagConstraints gbc = new GridBagConstraints();
   private boolean color;
   private boolean marks;
   private boolean sound;

   private GenericBlock[] block_array;	//contains all the GenericBlock's that will be used by Board objects
   private Image[] block_images;	//contains pre-initialised images to be used on GenericBlock's
   private Image[] color_block_images;	//contains pre-initialised images to be used on GenericBlock's
   private Image[] bw_block_images;	//contains pre-initialised images to be used on GenericBlock's
   private Image[] color_timer_images;	//contains pre-initialised images to be used on the Timer
   private Image[] bw_timer_images;	//contains pre-initialised images to be used on the Timer

   private AudioClip game_won_sound = null;
   private AudioClip game_lost_sound = null;

   int max_board_size = 16*30;               	//16*30 is the maximum board size: Expert Board

   //positions of namesake images in block_images and timer_images arrays
   public static final int ONE = 0, TWO = 1, THREE = 2, FOUR = 3, FIVE = 4, SIX = 5, SEVEN = 6;

   //positions of namesake images in block_images array
   public static final int BLOCK = 7, BLOCK_HIGHLIGHTED = 8, CROSS = 9, FLAG = 10, BOMB = 11, BOMB_RED = 12,
                           QUESTION_MARK = 13, QMARK_HIGHLIGHTED = 14;

   //positions of namesake images in timer_images array
   public static final int EIGHT = 7, NINE = 8, ZERO = 9, NEGATIVE = 10;

   private String[] block_urls = {"one.gif", "two.gif", "three.gif", "four.gif", "five.gif", "six.gif", "seven.gif",
                                  "block.gif", "block_highlighted.gif", "cross.gif", "flag.gif", "bomb.gif", "bomb_red.gif",
                                  "question_mark.gif", "question_mark_hl.gif"};

   private String[] timer_urls = {"timer_one.gif", "timer_two.gif", "timer_three.gif", "timer_four.gif", "timer_five.gif",
                             	  "timer_six.gif", "timer_seven.gif", "timer_eight.gif", "timer_nine.gif",
                             	  "timer_zero.gif", "timer_negative.gif"};


   private int beginner_board_after = -1;	//lists the largest board that has been used after the beginner board
   private int intermediate_board_after = -1;	//lists the largest board that has been used after the intermediate board
   private int expert_board_after = -1;		//lists the largest board that has been used after the expert board

   private HighScores high_scores;

   /**
    * Constructs a new MinesweeperPane Panel that contains
    * the bomb-counter, face, timer and board.
    * @param board_type specifies the type of board to be
    *			initialised with
    */
Minesweeper applet1;
//AudioClip winning_sound, losing_sound;
   public MinesweeperPane(int board_type, HighScores high_scores, boolean color, boolean marks, boolean sound, Minesweeper applet) {
      this.high_scores = high_scores;
      this.color = color;
      this.marks = marks;
      this.sound = sound;

      createImages();

      applet1 = applet;

      createSounds();

      createBlocks(color, marks);

      setBackground(new Color(191, 191, 191));
      setLayout(new GridBagLayout());
      gbc.weightx = 100;	//probably over-exaggerated

      //initialise and add the bomb counter
      bomb_counter = new BombCounter(board_type, color_timer_images, bw_timer_images, color);
      gbc.anchor = GridBagConstraints.WEST;
      gbc.insets = new Insets(6, 7, 11, 0);
      add(bomb_counter, gbc);

      //initialise and add the face button that resets the game
      face = new Face(color, applet);

      gbc.anchor = GridBagConstraints.CENTER;
      gbc.insets = new Insets(6, 0, 11, 0);
      add(face, gbc);

      //initialise and add the timer clock
      timer = new Timer(color_timer_images, bw_timer_images, color);
      gbc.anchor = GridBagConstraints.EAST;
      gbc.insets = new Insets(6, 0, 11, 9);
      gbc.gridwidth = GridBagConstraints.REMAINDER; //newline
      add(timer, gbc);

      gbc.insets = new Insets(0, 0, 0, 0);
      gbc.gridwidth = 3;
      addBoard(board_type);

      //initialises timer clock thread
      timer.start();
      timer.pauseClock();

      // Used to change the image on the face to a surprised face
      // if the user clicks within the applet
      addMouseListener (new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            face.showSurprised();
         }
         public void mouseReleased(MouseEvent e) {
            face.showNormal();
         }
      });

      //will now allow the menu to start a new game
      initialised = true;
   }

   /**
    * Adds the relevant board_type to the Panel (initialising
    * the board if it has not already been initialised)
    *
    * @param board_type the type of board to display
    */
   public void addBoard(int board_type) {
      this.board_type = board_type;

      if (board != null) {
         remove(board);
      }

      bomb_counter.setBoardType(board_type);

      boolean first_initialisation = false;
      //initialise board if not already initialised
      Board[] boards = {beginner_board, intermediate_board, expert_board};
      if (boards[board_type] == null) {
         boards[board_type] = new Board(board_type, block_array, color);
         first_initialisation = true;
      }

      board = boards[board_type];

      updateOverriddenBlocks(board_type, first_initialisation);

      newGame();

      add(board, gbc);
   }

   /**
    * Initialises all the blocks before any of the boards are created.
    * <p>This ensures the performance is optimised for when boards are
    * switched (The blocks do not need to be created again).
    */
   public void createBlocks(boolean color, boolean marks) {
      block_array = new GenericBlock[max_board_size];

      Image[] block_images = (color) ? color_block_images : bw_block_images;


      for (int x = 0; x < max_board_size; x++) {
         block_array[x] = new GenericBlock(block_images, marks, applet1);
      }
   }

   /**
    * Initialises all the images that will be used by the minesweeper board.
    * <p>This helps optimise performance (All images are initialised only once).
    */
   public void createImages() {
      //initialise images for blocks
      color_block_images = new Image[block_urls.length];
      bw_block_images = new Image[block_urls.length];

      for (int x = 0; x < block_urls.length; x++) {
         color_block_images[x] = loadImage(block_urls[x]);
         bw_block_images[x] = loadImage("bw_" + block_urls[x]);
      }

      //initialise images for clock and bomb counter
      color_timer_images = new Image[timer_urls.length];
      bw_timer_images = new Image[timer_urls.length];

      for (int x = 0; x < timer_urls.length; x++) {
         color_timer_images[x] = loadImage(timer_urls[x]);
         bw_timer_images[x] = loadImage("bw_" + timer_urls[x]);
      }
   }

   /**
    * Initialises all the images that will be used by the minesweeper board.
    * <p>This helps optimise performance (All images are initialised only once).
    */
   public void createSounds() {
      game_won_sound = applet1.getAudioClip(applet1.getCodeBase(), "cool!.wav");
      game_lost_sound = applet1.getAudioClip(applet1.getCodeBase(), "doh.au");
   }

   /**
    * This method is called when a game is finished.
    * @param won <I>false</I>: signifies that a bomb was clicked on to end the game<BR>
    *            <I>true</I>: signifies that the board was successfully completed
    */
   public void gameFinished(boolean won) {
      timer.pauseClock();
      clock_running = false;

      if (won) {
         face.showGameWon();
         bomb_counter.gameWon();

         if (sound) {
            game_won_sound.play();
         }

         String game_type = "";
         switch(board_type) {
            case(Minesweeper.BEGINNER): game_type = "Beginner"; break;
            case(Minesweeper.INTERMEDIATE): game_type = "Intermediate"; break;
            case(Minesweeper.EXPERT): game_type = "Expert"; break;
         }

         high_scores.checkHighScore(timer.counter, game_type);

         System.out.println("\n\n\nCongratulations!  You won the " + game_type + " game in " + timer.counter + " seconds.\n\n\n");
      }
      else {
         face.showUnhappy();

         if (sound) {
            game_lost_sound.play();
/*
            try {
               applet1.play(new URL(applet1.getCodeBase(), "doh.au"));
            } catch(java.net.MalformedURLException me) {
               System.out.println("Could not play sound");
            }
*/
         }
      }
   }

   /**
    * Called when a block or number is revealed.  This method
    * starts the clock running.
    */
   public void gameStarted() {
      if(!clock_running) {
         timer.reset();
         timer.startClock();
         clock_running = true;
      }
   }

   /**
    * Over-ridden in order to draw the border with paint()
    */
   public Insets getInsets() {
      return new Insets(9, 9, 7, 5);
   }

   /**
    * This method will call the initBoard() method of the board
    * to set up a new Board.  It is called from the window menu.
    */
   public void newGame() {
      //ensure the menu hasnt started the game before all buttons
      //have been initialised in the constructors
      if (initialised) {
         //ensure that the timer has been stopped (if the user clicks on the face
         //in the middle of a game, the gameFinished method is never called
         if (clock_running) {
            timer.pauseClock();
            clock_running = false;
         }

         bomb_counter.reset();
         face.newGame();
         timer.reset();
         ((Board)board).newGame();
      }
   }

   /**
    * Over-ridden in order to draw the borders around the panel
    */
   public void paint(Graphics g) {
      super.paint(g);
      Dimension size = getSize();

      g.setColor(Color.white);
      g.fillRect(0, 0, 3, size.height);
      g.fillRect(2, 0, size.width-2, 3);
      g.fillRect(10, 45, 1, 1);
      g.fillRect(11, 44, size.width-11-5, 2);
      g.fillRect(size.width-7, 11, 2, 33);

      if (color) {
         g.setColor(new Color(132, 130, 132));
      }
      else {
         g.setColor(Color.black);
      }
      g.fillRect(9, 9, size.width-9-7, 2);
      g.fillRect(size.width-7, 9, 1, 1);
      g.fillRect(9, 11, 2, 33);
      g.fillRect(9, 44, 1, 1);
   }

   /**
    * Swaps the board type currently being displayed
    */
   public void swapBoardType(int new_type) {
      addBoard(new_type);
   }

   /**
    * Toggles the color-mode of the board between color
    * and black and white
    */
   public void toggleColor() {
      color = !color;
      timer.toggleColor();
      face.toggleColor();
      bomb_counter.toggleColor();
      if (color)
         block_images = color_block_images;
      else
         block_images = bw_block_images;
      for (int x = 0; x < max_board_size; x++)
         block_array[x].toggleColor(block_images);
      board.toggleColor();
      repaint();
   }

   /**
    * Toggles the Marks mode of the board to allow the user
    * the facility of placing question marks on blocks
    */
   public void toggleMarks() {
      marks = !marks;
      for (int x = 0; x < max_board_size; x++)
         block_array[x].toggleMarks();
   }

   /**
    * Toggles the sound played in the game
    */
   public void toggleSound() {
      sound = !sound;
   }

   /**
    * Checks that the board hasn't been swapped from one of the other boards so that
    * lost blocks can be re-added
    */
   public void updateOverriddenBlocks(int board_type, boolean first_initialisation) {
      switch(board_type) {
         case Minesweeper.BEGINNER:
            if (beginner_board_after != -1 && !first_initialisation) {
               ((Board)beginner_board).resetBlocks(beginner_board_after);
            }
            if (intermediate_board_after == -1)
               intermediate_board_after = Minesweeper.BEGINNER;
            if (expert_board_after == -1)
               expert_board_after = Minesweeper.BEGINNER;
            beginner_board_after = -1;
            break;
         case Minesweeper.INTERMEDIATE:
            if (intermediate_board_after != -1 && !first_initialisation) {
               ((Board)intermediate_board).resetBlocks(intermediate_board_after);
            }
            if (beginner_board_after == -1)
               beginner_board_after = Minesweeper.INTERMEDIATE;
            if (expert_board_after == -1 || expert_board_after == Minesweeper.BEGINNER)
               expert_board_after = Minesweeper.INTERMEDIATE;
            intermediate_board_after = -1;
            break;
         case Minesweeper.EXPERT:
            if (expert_board_after != -1 && !first_initialisation) {
               ((Board)expert_board).resetBlocks(expert_board_after);
            }
            if (beginner_board_after == -1)
               beginner_board_after = Minesweeper.EXPERT;
            if (intermediate_board_after == -1 || intermediate_board_after == Minesweeper.BEGINNER)
               intermediate_board_after = Minesweeper.EXPERT;
            expert_board_after = -1;
            break;
      }
   }


   public Image loadImage(String img_url) {
      Image img = null;
      URL url = getClass().getResource(img_url);
      try {
         img = createImage((ImageProducer)url.getContent());
         if (img == null)
            System.out.println("null image: " + img_url);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return img;
   }




} //class MinesweeperPane
