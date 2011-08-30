import java.util.Random;
import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
/**
 * This class is a panel containing all the blocks/bombs/numbers
 * that make up the game-board.
 *
 * <P>It has methods that are called by the blocks it contains to
 * manipulate the board (thereby providing functionality for the game).
 *
 * @author Jason Kotchoff
 */

public class Board extends Panel {
   protected int type;				//the type of board this is
   protected GenericBlock[][] grid;		//data array with pointers to all blocks
   protected int width = -1;			//number of horizontal blocks
   protected int height = -1;			//number of vertical blocks
   protected int number_bombs = 0;		//total number of bombs on the board
   protected int total_blocks;			//total number of blocks on the board
   protected final int MAX_ADJ_BLOCKS = 8;	//maximum number of adjacent blocks
   protected int blocks_revealed;		//number of blocks that have been revealed
   protected boolean left_clicked;		//indicates state of left mouse btn
   protected boolean right_clicked;		//indicates state of right mouse btn
   protected boolean doubleclicked;		//indicates whether both mouse btn's are pressed
   protected boolean first_initialisation = true;
   protected GenericBlock current_block;	//points to current block being manipulated
   protected boolean on_block = false;		//indicates if the mouse is currently over the current_block
   protected GenericBlock[] parent_block_array;	//stores parents block array
   private boolean color;		//indicates if the board should be drawn in color

   public static final int BEGINNER_WIDTH = 8;
   public static final int BEGINNER_HEIGHT = 8;
   public static final int BEGINNER_BOMBS = 10;
   public static final int INTERMEDIATE_WIDTH = 16;
   public static final int INTERMEDIATE_HEIGHT = 16;
   public static final int INTERMEDIATE_BOMBS = 40;
   public static final int EXPERT_WIDTH = 30;
   public static final int EXPERT_HEIGHT = 16;
   public static final int EXPERT_BOMBS = 99;

   /**
    * Constructs a Board object.  Initialises a 2D array to store
    * information about the board, and calls initBoard() to set up
    * the Blocks that get added to the board itself.
    * @param board_type specifies the size of the board:<BR>
    *                   <I>0</I> Beginner board<BR>
    *                   <I>1</I> Intermediate board<BR>
    *                   <I>2</I> Expert board<BR>
    */
   public Board(int board_type, GenericBlock[] parent_block_array, boolean color) {
      super();

      this.type = board_type;
      this.parent_block_array = parent_block_array;
      this.color = color;

      // Used to change the image on the face to a surprised face
      // if the user clicks within the applet
      addMouseListener (new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            ((MinesweeperPane)getParent()).face.showSurprised();
         }
         public void mouseReleased(MouseEvent e) {
            ((MinesweeperPane)getParent()).face.showNormal();
         }
      });

      switch (board_type) {
         case (Minesweeper.BEGINNER):
            width = BEGINNER_WIDTH;
            height = BEGINNER_HEIGHT;
            number_bombs = BEGINNER_BOMBS;
            break;
         case (Minesweeper.INTERMEDIATE):
            width = INTERMEDIATE_WIDTH;
            height = INTERMEDIATE_HEIGHT;
            number_bombs = INTERMEDIATE_BOMBS;
            break;
         case (Minesweeper.EXPERT):
            width = EXPERT_WIDTH;
            height = EXPERT_HEIGHT;
            number_bombs = EXPERT_BOMBS;
            break;
         default:
            ;
      }

      grid = new GenericBlock[height][width];
      total_blocks = width * height - number_bombs;

      setLayout(new GridLayout(height, width, 0, 0));

      initBoard();
   }

   /**
    * Called when a the minesweeperPane swaps back to this board
    * after using a previous board (Using another board removes
    * blocks from this board therefore they need to be added again).
    * @param number_blocks the number of blocks to add back to this board
    */
   public void addBlocks(int number_blocks) {
      int cnt_blockarr = 0;
      for (int j = 0; j < height && cnt_blockarr < number_blocks; j++) {
         for (int i = 0; i < width && cnt_blockarr < number_blocks; i++) {
            add(grid[j][i]);
         }
      }
   }

   /**
    * Called when a blank block is revealed. reveals all
    * adjacent blank blocks and numbers. recursively calls itself
    * on all adjacent blank blocks.
    * @param origin the block that the method was called from
    */
   public void blankBlocks(GenericBlock origin) {
      GenericBlock[] temp_array = getAdjacentBlocks(origin.getXOrd(), origin.getYOrd());
      origin.setRevealed(true);

      //the following loop shows the adjacent blocks/numbers
      for (int i = 0; i < MAX_ADJ_BLOCKS && temp_array[i] != null; i++) {
         if (!temp_array[i].flagged()) {
            if (temp_array[i].getType() == "Number") {
               if (!temp_array[i].revealed()) {
                  temp_array[i].showBlock();
                  blockRevealed(temp_array[i]);
               }
            }
            else if (temp_array[i].getType() == "Block") {
               if (!temp_array[i].revealed()) {
                  if (!temp_array[i].getChecked()) {
                     temp_array[i].showBlock();
                     blockRevealed(temp_array[i]);
                  }
               }
            }
         }
      }

      //the following code block recursively calls this function
      //if there are any adjacent empty blocks
      for (int i = 0; i < MAX_ADJ_BLOCKS && temp_array[i] != null; i++) {
         if (temp_array[i].getType() == "Block") {
            if (!temp_array[i].flagged()) {
               if (!temp_array[i].revealed()) {
                  if (!temp_array[i].getChecked()) {
                     temp_array[i].setChecked(true);
                     blankBlocks(temp_array[i]);
                     if (temp_array[i].flagged())
                        temp_array[i].setRevealed(false);
                  }
               }
            }
         }
      }
   }

   /**
    * Called whenever any block/number is revealed. if all
    * blocks/numbers (ie. not the bombs) have been revealed,
    * then the game is over and the user has won.
    * @param origin the block that the method was called from
    */
   public void blockRevealed(GenericBlock origin) {
      if (!origin.getAddedToTotal()) {
         origin.setAddedToTotal();
         blocks_revealed++;
         if (blocks_revealed == total_blocks) {
            //put a flag on any bomb that isnt flagged
            for (int i = 0; i < width; i++) {
               for (int j = 0; j < height; j++) {
                  if (grid[j][i].getType() == "Bomb")
                     if (!grid[j][i].flagged())
                        (grid[j][i]).setFlagged();
               }
            }
            //do not allow user to interact with board
            setEnabled(false);

            //call the gameFinished method of the minesweeper class to
            //handle administrative details (top scores etc.)
            ((MinesweeperPane)getParent()).gameFinished(true);
         }
      }
   }

   /**
    * This method takes in the co-ordinate of the current position
    * (could be position '0') and checks all positions next to it
    * (except out of bounds positions) returning the number of
    * adjacent bombs.
    * @param x_ord the x co-ordinate of the position being tested
    * @param y_ord the y co-ordinate of the position being tested
    */
   private int countAdjacentBombs(int x_ord, int y_ord) {
      int adjacent_bombs = 0;
      GenericBlock[] temp_array = getAdjacentBlocks(x_ord, y_ord);
      for (int i = 0; i < MAX_ADJ_BLOCKS; i++)
         if (temp_array[i] != null && temp_array[i].getType() == "Bomb")
            adjacent_bombs++;
      return adjacent_bombs;
   }

   /**
    * Called by Number when a double-click is performed. If the
    * number of adjacent flags is correct, and they are correctly
    * placed, then all adjacent unrevealed blocks will be revealed.
    * @param origin the block that the method was called from
    */
   public void doubleClickPerformed (GenericBlock origin) {
      int x_ord = origin.getXOrd();
      int y_ord = origin.getYOrd();

      //if the origin of the click has been left-clicked before
      if (origin.revealed()) {
         if (origin.getType() == "Number") {
            int num_adj_bombs = origin.getAdjacentBombs();
            int num_flags = 0;
            GenericBlock[] storage = getAdjacentBlocks(x_ord, y_ord);

            //count the number of adjacent_flags
            for (int i = 0; i < MAX_ADJ_BLOCKS && storage[i] != null; i++) {
               if (storage[i].flagged())
                  num_flags++;
            }

            //if the number of adjacent flags is equal to the number
            //of flags put down by the user
            if (num_flags == num_adj_bombs) {
               for (int j = 0; j < MAX_ADJ_BLOCKS && storage[j] != null; j++) {
                  //if any adjacent flags are not on top of bombs, end the game
                  //else show all adjacent unflagged numbers/blocks
                  if (!storage[j].revealed()) {
                     if (storage[j].flagged()) {
                        if (!(storage[j].getType() == "Bomb")) {
                           storage[j].showCross();
                           showBoard();
                           break;	//exit loop so that board doesn't think it's started a new game on the next iteration
                        }
                     }
                     else { //no flag
                        if (storage[j].getType() == "Bomb") {
                           storage[j].highlight();
                           storage[j].showBlock();
                           showBoard();
                           break;	//exit loop so that board doesn't think it's started a new game on the next iteration
                        }
                        else if (storage[j].getType() == "Number") {
                           storage[j].showBlock();
                           blockRevealed(storage[j]);
                        }
                        else { //instance of block
                           storage[j].showBlock();
                           blockRevealed(storage[j]);
                           blankBlocks(storage[j]);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Stores references to all the adjacent blocks in an array
    * (which is returned to the calling method).
    * @param x_ord the x co-ordinate of the position being tested
    * @param y_ord the y co-ordinate of the position being tested
    * @return an array containing pointers to all the adjacent blocks
    */
   private GenericBlock[] getAdjacentBlocks(int x_ord, int y_ord) {
      GenericBlock[] storage = new GenericBlock[MAX_ADJ_BLOCKS];
      int i = 0;

      //check up-left
      if (x_ord > 0 && y_ord > 0) storage[i++] = grid[y_ord - 1][x_ord - 1];
      //check up
      if (y_ord > 0) storage[i++] = grid[y_ord - 1][x_ord];
      //check up-right
      if (x_ord < width - 1 && y_ord > 0) storage[i++] = grid[y_ord - 1][x_ord + 1];
      //check right
      if (x_ord < width - 1) storage[i++] = grid[y_ord][x_ord + 1];
      //check down-right
      if (x_ord < width - 1 && y_ord < height - 1) storage[i++] = grid[y_ord + 1][x_ord + 1];
      //check down
      if (y_ord < height - 1) storage[i++] = grid[y_ord + 1][x_ord];
      //check down-left
      if (x_ord > 0 && y_ord < height - 1) storage[i++] = grid[y_ord + 1][x_ord - 1];
      //check left
      if (x_ord > 0) storage[i++] = grid[y_ord][x_ord - 1];

      return storage;
   }

   /**
    * @return returns a reference to the current block being manipulated
    */
   public GenericBlock getCurrentBlock() {
      return current_block;
   }

   /**
    * Over-ridden in order to draw the border with paint()
    */
   public Insets getInsets() {
      return new Insets(3, 3, 3, 3);
   }

   /**
    * Highlights all the surrounding blocks of the current block
    * Called when a block is entered with both mouse buttons held.
    */
   public void highlightSurrounding() {
      GenericBlock[] temp_array = getAdjacentBlocks(current_block.getXOrd(), current_block.getYOrd());
      for (int i = 0; i < MAX_ADJ_BLOCKS && temp_array[i] != null; i++) {
         if (!temp_array[i].revealed())
            if (!temp_array[i].flagged())
               temp_array[i].highlight();
      }
   }

   /**
    * When called for the first time, this method adds all
    * the blocks to this board and initialises them (ie. sets
    * their types and functionality). In subsequent calls, this
    * method resets the board.
    */
   public void initBoard() {
      blocks_revealed = 0;
      left_clicked = false;
      right_clicked = false;
      doubleclicked = false;
      current_block = null;

      //initialise everything on the board to blank
      int cnt_blockarr = 0;
      for (int i = 0; i < width; i++) {
         for (int j = 0; j < height; j++) {
            if(first_initialisation) {
               grid[j][i] = parent_block_array[cnt_blockarr++];
            }
            grid[j][i].reset(i, j);
         }
      }

      if(first_initialisation) {
         for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
               add(grid[j][i]);
      }

      if (first_initialisation)
         first_initialisation = false;

      //place all the bombs
      for (int i = 0; i < number_bombs; i++)
         placeBomb();

      int adjacentBombs = -1;
      for (int i = 0; i < width; i++) {
         for (int j = 0; j < height; j++) {
            if (!(grid[j][i].getType() == "Bomb")) {
               adjacentBombs = countAdjacentBombs(i, j);
               if (adjacentBombs == 0)
                  grid[j][i].setType("Block");
               else {
                  grid[j][i].setType("Number");
                  grid[j][i].setAdjacentBombs(adjacentBombs);
               }
            }
         }
      }

   }

   /**
    * If this board has been removed and is now being re-used,
    * some of the blocks this board uses may need to be re-added
    * @param previous_board The previous board used (needed to calculate)
    *			    how many blocks will need to be re-added
    */
   public void resetBlocks(int previous_board) {
      switch(this.type) {
         case Minesweeper.BEGINNER:
            addBlocks(64);
            break;
         case Minesweeper.INTERMEDIATE:
            if (previous_board  == Minesweeper.BEGINNER) {
               addBlocks(64);
            }
            else if (previous_board  == Minesweeper.EXPERT)
               addBlocks(256);
            break;
         case Minesweeper.EXPERT:
            if (previous_board  == Minesweeper.BEGINNER)
               addBlocks(64);
            else if (previous_board  == Minesweeper.INTERMEDIATE)
               addBlocks(256);
            break;
      }

   }

   /**
    * Used to start a new game (ie. reinitialise the board)
    */
   public void newGame() {
      initBoard();
      if (!isEnabled()) setEnabled(true);
   }

   /**
    * Over-ridden in order to draw the borders around the board
    */
   public void paint(Graphics g) {
      super.paint(g);
      Dimension size = getSize();

      if (color)
         g.setColor(new Color(132, 130, 132));
      else
         g.setColor(Color.black);
      g.fillRect(0, 0, 3, size.height-3);
      g.fillRect(3, 0, size.width-6, 3);
      g.fillRect(size.width-3, 0, 2, 1);
      g.fillRect(size.width-3, 1, 1, 1);
      g.fillRect(0, size.height-3, 2, 1);
      g.fillRect(0, size.height-2, 1, 1);

      g.setColor(Color.white);
      g.fillRect(3, size.height-3, size.width-6, 3);
      g.fillRect(size.width-3, 3, 3, size.height-3);
      g.fillRect(size.width-1, 1, 1, 1);
      g.fillRect(size.width-2, 2, 2, 1);
      g.fillRect(2, size.height-2, 1, 1);
      g.fillRect(1, size.height-1, 2, 1);
   }

   /**
    * This method randomly selects a position to place a bomb in.
    * <P>If the position is not empty, then it recursively recalls
    * itself to find a new position to place the bomb.
    * <P>If the position is surrounded by bombs (ie. by MAX_ADJ_BLOCKS
    * bombs), it recalls itself.
    * <P>When a valid position is found, that position in the grid array
    * is intialised to a bomb object
    */
   private void placeBomb() {
      Random r = new Random();
      boolean placed = false;
      int x_ord = -1;
      int y_ord = -1;

      while (!placed) {
         x_ord = Math.abs(r.nextInt() % width);
         y_ord = Math.abs(r.nextInt() % height);
         if (grid[y_ord][x_ord].getType() == "") {
            if (countAdjacentBombs(x_ord, y_ord) < MAX_ADJ_BLOCKS) {
               grid[y_ord][x_ord].setType("Bomb");
               placed = true;
            }
         }
      }
   }

   /**
    * Used for testing
    *
   public void printBoard() {
      String currentType = "";
      System.out.println();
      for (int j = 0; j < height; j++) {
         for (int i = 0; i < width; i++) {
            if (grid[j][i].getType() == "") {
               currentType = "-";
            } else if (grid[j][i].getType() == "Bomb") {
               currentType = "b";
            } else if (grid[j][i].getType() == "Block") {
               currentType = "0";
            } else if (grid[j][i].getType() == "Number") {
               currentType = "n";
            }
            System.out.print(currentType + " ");
         }
         System.out.println();
      }
   }

   **
    * Used for testing
    *
   public void printBoardOrds() {
      String currentType = "";
      for (int j = 0; j < height; j++) {
         for (int i = 0; i < width; i++) {
            if (grid[j][i].getType() == "") {
               currentType = "-";
            } else if (grid[j][i].getType() == "Bomb") {
               currentType = "b";
            } else if (grid[j][i].getType() == "Block") {
               currentType = "0";
            } else if (grid[j][i].getType() == "Number") {
               currentType = "n";
            }
            System.out.print(currentType);


            System.out.print("x" + grid[j][i].getXOrd() + "y" + grid[j][i].getYOrd() + " ");
         }
         System.out.println();
      }
   }*/

   /**
    * This method sets the current block variable stored in the
    * board to the parameter passed to the method.
    * @param the new current block
    */
   public void setCurrentBlock(GenericBlock block) {
      current_block = block;
   }

   /**
    * Called when a bomb is clicked on, or a flag is
    * incorrectly placed.  It goes through the whole board,
    * showing what lies in each position.
    */
   public void showBoard() {
      //do not allow user to interact with board
      setEnabled(false);

      //show all mistakes and all bombs
      for (int i = 0; i < width; i++) {
         for (int j = 0; j < height; j++) {
            if (grid[j][i].flagged()) {
               if (!(grid[j][i].getType() == "Bomb"))
                  grid[j][i].showCross();
            }
            else if (grid[j][i].getType() == "Bomb") {
               if (!grid[j][i].revealed())
                  grid[j][i].showBlock();
            }
         }
      }

      //call the gameFinished method of the minesweeper class to
      //handle administrative details (top scores etc.)
      ((MinesweeperPane)getParent()).gameFinished(false);
   }

   /**
    * Shows the current block/number/bomb (showing a
    * red-highlighted bomb if a bomb is the current block).
    */
   public void showCurrentBlock() {
      ((MinesweeperPane)getParent()).face.showNormal();

      if (!current_block.getFlagged() && on_block) {
         if (current_block.getType() == "Number") {
            if (!current_block.revealed()) {
               current_block.showBlock();
               blockRevealed(current_block);
            }
         }
         else if (current_block.getType() == "Bomb") {
            current_block.showBlockRed();
         }
         else { //instance of block
            if (!current_block.revealed()) {
               if (!current_block.getChecked()) {
                  current_block.showBlock();
                  blockRevealed(current_block);
                  blankBlocks(current_block);
               }
            }
         }
      }
   }

   /**
    * Toggles the color-mode of the board between color
    * and black and white
    */
   public void toggleColor() {
      color = !color;
      repaint();
   }

   /**
    * Sets any blocks adjacent to the current block to
    * their unhighlighted state if they are not revealed.
    */
   public void unhighlightSurrounding() {
      GenericBlock[] temp_array = getAdjacentBlocks(current_block.getXOrd(), current_block.getYOrd());
      for (int i = 0; i < MAX_ADJ_BLOCKS && temp_array[i] != null; i++) {
         if (!temp_array[i].revealed())
            if (!temp_array[i].flagged())
               temp_array[i].unhighlight();
      }
   }

} //class Board
