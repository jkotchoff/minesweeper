import java.awt.event.*;
import java.net.URL;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.Dimension;
import java.util.LinkedList;
/**
 * This class creates a GenericBlock object.  It is used to represent the
 * three types of block on the minesweeper board (Block, Bomb and Number).
 * It implements a MouseListener that directly handles the functionality
 * of the Block (if it is interacted with by the mouse).
 *
 * @author Jason Kotchoff
 */

import java.applet.Applet;
import java.awt.Event;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.EventQueue;

public class GenericBlock extends JKButton {
   public static final int WIDTH = 16;
   public static final int HEIGHT = 16;
   private String type;
   private int adjacent_bombs;
   private boolean flagged;
   private boolean revealed;
   private static final int LEFT_CLICK = 16;
   private static final int RIGHT_CLICK = 4;
   private int x_ord;
   private int y_ord;
   private boolean checked;
   private boolean added_to_total;
   private boolean left_clicked;
   private boolean right_clicked;
   private Image[] block_images;
   private int current_icon;
   boolean marks;
   boolean marked;

Applet applet1;

   private String browser;

   /**
    * Constructs a new Block object.
    * @param block_images an array of images to be used in drawing the block
    */
   public GenericBlock(Image[] block_images, boolean marks, Minesweeper applet) {
      this.block_images = block_images;
      this.marks = marks;
this.applet1 = applet;
      browser = applet.getBrowser();
/*
      //initialise block and number images
      block_image = block_images[MinesweeperPane.BLOCK];
      block_highlighted = block_images[MinesweeperPane.BLOCK_HIGHLIGHTED];
      cross_image = block_images[MinesweeperPane.CROSS];
      flag_image = block_images[MinesweeperPane.FLAG];
      bomb_image = block_images[MinesweeperPane.BOMB];
      bomb_highlighted = block_images[MinesweeperPane.BOMB_RED];
      number_1 = block_images[MinesweeperPane.ONE];
      number_2 = block_images[MinesweeperPane.TWO];
      number_3 = block_images[MinesweeperPane.THREE];
      number_4 = block_images[MinesweeperPane.FOUR];
      number_5 = block_images[MinesweeperPane.FIVE];
      number_6 = block_images[MinesweeperPane.SIX];
      number_7 = block_images[MinesweeperPane.SEVEN];
      number_8 = block_images[MinesweeperPane.EIGHT];
*/
   }

   /**
    * Alerts the caller whether or not this block is currently flagged.
    */
   public boolean flagged() {
      return flagged;
   }

   /**
    * Used to ensure each block is counted only once
    * when the blankBlocks method is called
    * (used by the 'blockRevealed' method of Board).
    */
   public boolean getAddedToTotal() {
      return added_to_total;
   }

   /**
    * Reveals the number of adjacent bombs.
    * @return number of adjacent bombs
    */
   public int getAdjacentBombs() {
      return adjacent_bombs;
   }

   /**
    * Used by the blankBlocks method of Board.java to see if this block
    * has had the blankBlocks method called on it's surrounding blocks yet.
    */
   public boolean getChecked() {
      return checked;
   }

   /**
    * Used in Board.java to check if this block is currently flagged.
    */
   public boolean getFlagged() {
      return flagged;
   }

   /**
    * Stores the x co-ordinate of this block within the storage matrix
    * of Board.java.
    */
   public int getXOrd() {
      return x_ord;
   }

   /**
    * Stores the y co-ordinate of this block within the storage matrix
    * of Board.java.
    */
   public int getYOrd() {
      return y_ord;
   }

   /**
    * Sets the preferred size of this Component
    */
   public Dimension getPreferredSize() {
      return new Dimension(WIDTH, HEIGHT);
   }

   /**
    * Returns the type of this GenericBlock (Block, Bomb or Number)
    */
   public String getType() {
      return this.type;
   }

   /**
    * Called when a double click is performed and when the button is
    * pressed but not released.  Causes the block to display a highlighted
    * image.
    */
   public void highlight() {
      if (marked)
         current_icon = MinesweeperPane.QMARK_HIGHLIGHTED;
      else
         current_icon = MinesweeperPane.BLOCK_HIGHLIGHTED;
      setIcon(block_images[current_icon]);
   }

   /**
    * Resets all block variables.
    * <p>Called every time a new boad is created or a new game is started
    * @param x_ord horizontal position of block within double array in Board.java
    * @param y_ord vertical position of block within double array in Board.java
    */
   public void reset(int x_ord, int y_ord) {
      this.x_ord = x_ord;
      this.y_ord = y_ord;
      type = "";
      adjacent_bombs = -1;
      flagged = false;
      revealed = false;
      checked = false;
      added_to_total = false;
      left_clicked = false;
      right_clicked = false;
      marked = false;
   }

   /**
    * Alerts the caller whether or not this block has yet been revealed.
    */
   public boolean revealed() {
      return revealed;
   }

   /**
    * Used to ensure each block is counted only once
    * when the blankBlocks method is called
    * (used by the 'blockRevealed' method of Board).
    */
   public void setAddedToTotal() {
      added_to_total = true;
   }

   /**
    * Sets the number of adjacent bombs
    * @param adjacent_bombs the number of adjacent bombs (ie. the number displayed by this block)
    */
   public void setAdjacentBombs(int adjacent_bombs) {
      this.adjacent_bombs = adjacent_bombs;
   }

   /**
    * Used by the blankBlocks method of Board.java.
    */
   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   /**
    * Shows a flag on top of this bomb.
    */
   public void setFlagged() {
      current_icon = MinesweeperPane.FLAG;
      setIcon(block_images[current_icon]);
   }

   /**
    * Shows a question mark on top of this bomb.
    */
   public void setMarked() {
      current_icon = MinesweeperPane.QUESTION_MARK;
      setIcon(block_images[current_icon]);
   }

   /**
    * Used by the blankBlocks method of Board.java.
    */
   public void setRevealed(boolean revealed) {
      this.revealed = revealed;
   }

   /**
    * Sets the type of this GenericBlock (Block, Bomb or Number)
    * @param type the type of block this is
    */
   public void setType(String type) {
      current_icon = MinesweeperPane.BLOCK;
      setIcon(block_images[current_icon]);
      this.type = type;
   }

   /**
    * Called when this block is clicked on, or an adjacent
    * block is pressed
    */
   public void showBlock() {
      marked = false;
      if (this.type == "Block") {
         flagged = false;
         current_icon = MinesweeperPane.BLOCK_HIGHLIGHTED;
         setIcon(block_images[current_icon]);
         ((MinesweeperPane)getParent().getParent()).gameStarted();
      } else if (this.type == "Number") {
         flagged = false;
         current_icon = adjacent_bombs-1;	//HARD-CODED
         setIcon(block_images[current_icon]);
         revealed = true;
         ((MinesweeperPane)getParent().getParent()).gameStarted();
      } else if (this.type == "Bomb") {
         current_icon = MinesweeperPane.BOMB;
         setIcon(block_images[current_icon]);
      }
   }

   /**
    * Called when the left key is pressed down on another block
    * and dragged & released on this one.
    */
   public void showBlockRed() {
      if (!flagged) {
         current_icon = MinesweeperPane.BOMB_RED;
         setIcon(block_images[current_icon]);
         revealed = true;
         ((Board)getParent()).showBoard();
      }
   }

   /**
    * Used when the a mistake is made ending the game.
    */
   public void showCross() {
      current_icon = MinesweeperPane.CROSS;
      setIcon(block_images[current_icon]);
      revealed = true;
   }

   /**
    * Toggles the color mode of this block
    */
   public void toggleColor(Image[] block_images) {
      this.block_images = block_images;
      setIcon(block_images[current_icon]);
   }

   /**
    * Toggles the marks mode of this block
    */
   public void toggleMarks() {
      marks = !marks;
      if (marked)
         unhighlightMark();
      marked = false;
   }

   /**
    * Called when the left mousebutton or a double-click is released.
    */
   public void unhighlight() {
      if(marked)
         current_icon = MinesweeperPane.QUESTION_MARK;
      else
         current_icon = MinesweeperPane.BLOCK;
      setIcon(block_images[current_icon]);
   }

   /**
    * Called when a mark is right clicked
    */
   public void unhighlightMark() {
      current_icon = MinesweeperPane.BLOCK;
      setIcon(block_images[current_icon]);
   }


//**************************************************************************************************
//block mousehandlers
//**************************************************************************************************
   public void blockMousePressed(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showSurprised();

      if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
         if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged()) {
            ((Board)getParent()).current_block.highlight();
         }
         ((Board)getParent()).highlightSurrounding();
         ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked = true;
         ((Board)getParent()).doubleclicked = true;
      }
      else if (e.getModifiers() == LEFT_CLICK) {
         if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.highlight();
         if (((Board)getParent()).right_clicked) { //completing a doubleclick
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            right_clicked = false;
         }
         else
            left_clicked = true;
         ((Board)getParent()).left_clicked = true;
      }
      else if (e.getModifiers() == RIGHT_CLICK) {
         if (((Board)getParent()).left_clicked) { //completing a doubleclick
            if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged())
               ((Board)getParent()).current_block.highlight();
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            left_clicked = false;
         }
         else {
            if (!revealed) {
               if (marks && flagged) {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
                  flagged = false;
                  marked = true;
                  setMarked();
               }
               else if (marks && marked) {
                  marked = false;
                  unhighlightMark();
               }
               else if (!flagged) {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.bombFound();
                  flagged = true;
                  setFlagged();
               }
               else {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
                  flagged = false;
                  unhighlight();
               }
            }
         }
         ((Board)getParent()).right_clicked = true;
      }
   }

   public void blockMouseReleased(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showNormal();

      if (((Board)getParent()).doubleclicked) {
         if (!((Board)getParent()).current_block.revealed() && !((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.unhighlight();
         ((Board)getParent()).unhighlightSurrounding();

         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
            if (!((Board)getParent()).current_block.revealed() && !flagged)
               ((Board)getParent()).current_block.unhighlight();
            ((Board)getParent()).unhighlightSurrounding();
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked =
            ((Board)getParent()).doubleclicked = false;
         }
         else if (e.getModifiers() == RIGHT_CLICK) {
            if (((Board)getParent()).left_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else
               ((Board)getParent()).doubleclicked = false;

            ((Board)getParent()).right_clicked = false;
         }
         else if (e.getModifiers() == LEFT_CLICK) {
            if (((Board)getParent()).right_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else
               ((Board)getParent()).doubleclicked = false;

            ((Board)getParent()).left_clicked = false;
         }
      }
      else if (((Board)getParent()).left_clicked) {
         if (!((Board)getParent()).doubleclicked) {
            ((Board)getParent()).showCurrentBlock();
            ((Board)getParent()).left_clicked = false;
         }
      }

      if (left_clicked) {
         if (!flagged) {
            showBlock();
            ((Board)getParent()).blockRevealed(this);
            ((Board)getParent()).blankBlocks(this);
         }
      }

      if (!((Board)getParent()).doubleclicked) {
         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK)
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked =
            left_clicked = right_clicked = false;
         else if (e.getModifiers() == RIGHT_CLICK)
            ((Board)getParent()).right_clicked = right_clicked = false;
         else if (e.getModifiers() == LEFT_CLICK)
            ((Board)getParent()).left_clicked = left_clicked = false;
      }
   }

   public void blockMouseEntered(MouseEvent e) {
      ((Board)getParent()).setCurrentBlock(this);

      //if a doubleclick with both buttons held enters this block, highlight it
      if(((Board)getParent()).left_clicked && ((Board)getParent()).right_clicked) {
         if (!revealed && !flagged)
            highlight();
         ((Board)getParent()).highlightSurrounding();
      }

      //if a leftclick is held upon entering this block, prepare it for revealing
      if (((Board)getParent()).left_clicked && !((Board)getParent()).doubleclicked)
         if (!revealed && !flagged) {
            highlight();
            left_clicked = true;
         }
   }

   public void blockMouseExited(MouseEvent e) {
      if (right_clicked) right_clicked = false;
      if (left_clicked) left_clicked = false;

      if (((Board)getParent()).doubleclicked) {
         if (!revealed && !flagged)
            unhighlight();
         ((Board)getParent()).unhighlightSurrounding();
      }
      else if (((Board)getParent()).left_clicked)
         if (!revealed && !flagged)
            unhighlight();
   }

//**************************************************************************************************
//GenericBlock mousehandlers
//**************************************************************************************************
   public void numberMousePressed(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showSurprised();

      if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
         if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged()) {
            ((Board)getParent()).current_block.highlight();
         }
         ((Board)getParent()).highlightSurrounding();
         ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked = true;
         ((Board)getParent()).doubleclicked = true;
      }
      else if (e.getModifiers() == LEFT_CLICK) {
         if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.highlight();
         if (((Board)getParent()).right_clicked) { //completing a double click
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            right_clicked = false;
         }
         else {
            left_clicked = true;
         }
         ((Board)getParent()).left_clicked = true;
      }
      else if (e.getModifiers() == RIGHT_CLICK) {
         if (((Board)getParent()).left_clicked) { //completing a double click
            if (!((Board)getParent()).current_block.revealed && !((Board)getParent()).current_block.getFlagged())
               ((Board)getParent()).current_block.highlight();
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            left_clicked = false;
         }
         else {
            if (!revealed) {
               if (marks && flagged) {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
                  flagged = false;
                  marked = true;
                  setMarked();
               }
               else if (marks && marked) {
                  marked = false;
                  unhighlightMark();
               }
               else if (!flagged) {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.bombFound();
                  flagged = true;
                  setFlagged();
               }
               else {
                  ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
                  flagged = false;
                  unhighlight();
               }
            }
            right_clicked = true;
         }
         ((Board)getParent()).right_clicked = true;
      }


   }

   public void numberMouseReleased(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showNormal();

      if (((Board)getParent()).doubleclicked) {
         if (!((Board)getParent()).current_block.revealed() && !((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.unhighlight();
         ((Board)getParent()).unhighlightSurrounding();

         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
            if (revealed)
               ((Board)getParent()).doubleClickPerformed(this);
            if (!((Board)getParent()).current_block.revealed() && !flagged)
               ((Board)getParent()).current_block.unhighlight();
            ((Board)getParent()).unhighlightSurrounding();
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked =
            ((Board)getParent()).doubleclicked = false;
         }
         else if (e.getModifiers() == RIGHT_CLICK) {
            if (((Board)getParent()).left_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else {
               if (revealed)
                  ((Board)getParent()).doubleClickPerformed(this);
               ((Board)getParent()).doubleclicked = false;
            }

            ((Board)getParent()).right_clicked = false;
         }
         else if (e.getModifiers() == LEFT_CLICK) {
            if (((Board)getParent()).right_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else {
               if (revealed)
                  ((Board)getParent()).doubleClickPerformed(this);
               ((Board)getParent()).doubleclicked = false;
            }

            ((Board)getParent()).left_clicked = false;
         }
      }
      else if (((Board)getParent()).left_clicked)
         if (!((Board)getParent()).doubleclicked) {
            ((Board)getParent()).showCurrentBlock();
            ((Board)getParent()).left_clicked = false;
         }

      if (left_clicked) {
         if (right_clicked && revealed) {
            ((Board)getParent()).doubleClickPerformed(this);
         }
         else {
            if (!revealed) {
               if (!flagged) {
                  showBlock();
                  ((Board)getParent()).blockRevealed(this);
               }
            }
         }
      }

      if (!((Board)getParent()).doubleclicked) {
         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK)
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked =
            left_clicked = right_clicked = false;
         else if (e.getModifiers() == RIGHT_CLICK)
            ((Board)getParent()).right_clicked = right_clicked = false;
         else if (e.getModifiers() == LEFT_CLICK)
            ((Board)getParent()).left_clicked = left_clicked = false;
      }
   }

   public void numberMouseEntered(MouseEvent e) {
      ((Board)getParent()).setCurrentBlock(this);

      //if a doubleclick with both buttons held enters this block, highlight it
      if(((Board)getParent()).left_clicked && ((Board)getParent()).right_clicked) {
         if (!revealed && !flagged)
            highlight();
         ((Board)getParent()).highlightSurrounding();
      }

      //if a leftclick is held upon entering this block, prepare it for revealing
      if (((Board)getParent()).left_clicked && !((Board)getParent()).doubleclicked)
         if (!revealed && !flagged) {
            highlight();
            left_clicked = true;
         }
   }

   public void numberMouseExited(MouseEvent e) {
      if (right_clicked) right_clicked = false;
      if (left_clicked) left_clicked = false;

      if (((Board)getParent()).doubleclicked) {
         if (!revealed && !flagged)
            unhighlight();
         ((Board)getParent()).unhighlightSurrounding();
      }
      else if (((Board)getParent()).left_clicked)
         if (!revealed && !flagged)
            unhighlight();
   }

//**************************************************************************************************
//GenericBlock mousehandlers
//**************************************************************************************************
   public void bombMousePressed(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showSurprised();

      if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
         if (!((Board)getParent()).current_block.getFlagged()) {
            ((Board)getParent()).current_block.highlight();
         }
         ((Board)getParent()).highlightSurrounding();
         ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked = true;
         ((Board)getParent()).doubleclicked = true;
      }
      else if (e.getModifiers() == LEFT_CLICK) {
         if (!((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.highlight();
         if (((Board)getParent()).right_clicked) { //completing a double-click
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            right_clicked = false;
         }
         else
            left_clicked = true;
         ((Board)getParent()).left_clicked = true;
      }
      else if (e.getModifiers() == RIGHT_CLICK) {
         if (((Board)getParent()).left_clicked) {  //completing a double-click
            if (!((Board)getParent()).current_block.getFlagged())
               ((Board)getParent()).current_block.highlight();
            ((Board)getParent()).highlightSurrounding();
            ((Board)getParent()).doubleclicked = true;
            left_clicked = false;
         }
         else {
            if (marks && flagged) {
               ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
               flagged = false;
               marked = true;
               setMarked();
            }
            else if (marks && marked) {
               marked = false;
               unhighlightMark();
            }
            else if (!flagged) {
               ((MinesweeperPane)getParent().getParent()).bomb_counter.bombFound();
               flagged = true;
               setFlagged();
            }
            else {
               ((MinesweeperPane)getParent().getParent()).bomb_counter.unFound();
               flagged = false;
               unhighlight();
            }
         }
         ((Board)getParent()).right_clicked = true;
      }

   }

   public void bombMouseReleased(MouseEvent e) {
      ((MinesweeperPane)getParent().getParent()).face.showNormal();

      if (((Board)getParent()).doubleclicked) {
         if (!((Board)getParent()).current_block.revealed() && !((Board)getParent()).current_block.getFlagged())
            ((Board)getParent()).current_block.unhighlight();
         ((Board)getParent()).unhighlightSurrounding();

         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK) {
            if (!((Board)getParent()).current_block.revealed() && !flagged)
               ((Board)getParent()).current_block.unhighlight();
            ((Board)getParent()).unhighlightSurrounding();
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked =
            ((Board)getParent()).doubleclicked = false;
         }
         else if (e.getModifiers() == RIGHT_CLICK) {
            if (((Board)getParent()).left_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else
               ((Board)getParent()).doubleclicked = false;

            ((Board)getParent()).right_clicked = false;
         }
         else if (e.getModifiers() == LEFT_CLICK) {
            if (((Board)getParent()).right_clicked) {
               if (!((Board)getParent()).current_block.revealed() && !flagged)
                  ((Board)getParent()).current_block.unhighlight();
               ((Board)getParent()).unhighlightSurrounding();
            }
            else
               ((Board)getParent()).doubleclicked = false;

            ((Board)getParent()).left_clicked = false;
         }
      }
      else if (((Board)getParent()).left_clicked)
         if (!((Board)getParent()).doubleclicked) {  //needed because sometimes the release of a double click sends 2 mouseReleased calls
            ((Board)getParent()).showCurrentBlock();
         }

      if (left_clicked) {
         if (!flagged) {
            showBlockRed();
            revealed = true;
            ((Board)getParent()).showBoard();
         }
      }

      if (!((Board)getParent()).doubleclicked) {
         if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK)
            ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked = left_clicked = right_clicked = false;
         else if (e.getModifiers() == RIGHT_CLICK)
            ((Board)getParent()).right_clicked = right_clicked = false;
         else if (e.getModifiers() == LEFT_CLICK)
            ((Board)getParent()).left_clicked = left_clicked = false;
      }
   }

   public void bombMouseEntered(MouseEvent e) {
      ((Board)getParent()).setCurrentBlock(this);

      //if a doubleclick with both buttons held enters this block, highlight it
      if(((Board)getParent()).left_clicked && ((Board)getParent()).right_clicked) {
         if (!flagged)
            highlight();
         ((Board)getParent()).highlightSurrounding();
      }

      //if a leftclick is held upon entering this block, prepare it for revealing
      if (((Board)getParent()).left_clicked && !((Board)getParent()).doubleclicked)
         if (!revealed && !flagged) {
            highlight();
            left_clicked = true;
         }
   }

   public void bombMouseExited(MouseEvent e) {
      if (right_clicked) right_clicked = false;
      if (left_clicked) left_clicked = false;

      if (((Board)getParent()).doubleclicked) {
         if (!revealed && !flagged)
            unhighlight();
         ((Board)getParent()).unhighlightSurrounding();
      }
      else if (((Board)getParent()).left_clicked)
         if (!revealed && !flagged)
            unhighlight();
   }

//**************************************************************************************************
//generic mousehandlers
//**************************************************************************************************
   public void mouseExited(MouseEvent e) {
//applet1.showStatus("block " + x_ord + ", " + y_ord + " exited");
      ((Board)getParent()).on_block = false;
      if (this.type == "Block") {
         blockMouseExited(e);
      } else if (this.type == "Number") {
         numberMouseExited(e);
      } else if (this.type == "Bomb") {
         bombMouseExited(e);
      }
   }
   public void mouseReleased(MouseEvent e) {
      //HACK 	to handle IE's event handling which (unlike appletviewer) does NOT
      //	fire a mouseExited event whilst the mouse is pressed
      if (exitListener != null) {
         releaseHighlightedBlocks(e, true);
         removeMouseMotionListener(exitListener);
         exitListener = null;

         if (((Board)getParent()).getCurrentBlock() == null) {
            if (e.getModifiers() == LEFT_CLICK + RIGHT_CLICK)
               ((Board)getParent()).left_clicked = ((Board)getParent()).right_clicked = left_clicked = right_clicked = false;
            else if (e.getModifiers() == RIGHT_CLICK)
               ((Board)getParent()).right_clicked = right_clicked = false;
            else if (e.getModifiers() == LEFT_CLICK)
               ((Board)getParent()).left_clicked = left_clicked = false;

            return;
         }

      }
      //end HACK

//applet1.showStatus("block " + x_ord + ", " + y_ord + " released");
      if (this.type == "Block") {
         blockMouseReleased(e);
      } else if (this.type == "Number") {
         numberMouseReleased(e);
      } else if (this.type == "Bomb") {
         bombMouseReleased(e);
      }
   }

   public void mouseClicked(MouseEvent e) {;}

GenericBlock this_block = this;
private MouseMotionAdapter exitListener;
JKLinkedList hl_blocks = new JKLinkedList();

   public void mousePressed(MouseEvent e) {
if (!(((Board)getParent()).getCurrentBlock() == null && e.getModifiers() == LEFT_CLICK + RIGHT_CLICK))  {

      if (this.type == "Block") {
         blockMousePressed(e);
      } else if (this.type == "Number") {
         numberMousePressed(e);
      } else if (this.type == "Bomb") {
         bombMousePressed(e);
      }
}


      //HACK 	to handle IE's event handling which (unlike appletviewer) does NOT
      //	fire a mouseExited event whilst the mouse is pressed
if (browser != Minesweeper.APPLETVIEWER) {
      addMouseMotionListener(exitListener = new MouseMotionAdapter() {
         public void mouseDragged(MouseEvent event) {
            Board board = (Board)getParent();
            GenericBlock current_block = board.getCurrentBlock();

            //determine which block was just entered
            GenericBlock block_just_entered = null;

            int x = event.getX();
            int y = event.getY();

            int width, height;
            width = WIDTH - 1;
            height = HEIGHT - 1;

            if (x < 0 || x > width || y < 0 || y > width) { //outside this block
               //determine block that we are currently on
               int block_x = (int)Math.ceil((double)x/(double)WIDTH) - 1 + x_ord;
               int block_y = (int)Math.ceil((double)y/(double)HEIGHT) - 1 + y_ord;

               if (block_x >= 0 && block_x < board.width && block_y >= 0 && block_y < board.height) {
                  block_just_entered = board.grid[block_y][block_x];
               }
               else {
                  if (current_block != null) {
                     releaseHighlightedBlocks(event, false);
                     board.setCurrentBlock(null);
                  }
                  return;
               }

               //handle mouse events
               if (current_block == this_block) {	//exited this block
                  releaseHighlightedBlocks(event, false);
                  this_block.mouseExited(event);
                  board.setCurrentBlock(block_just_entered);
                  if (block_just_entered != null) {	//exited onto another block
                     block_just_entered.mouseEntered(event);
                     hl_blocks.add(block_just_entered);
                  }
               }
               else if (block_just_entered != null) {	//exited another block (after already being on that block)
                  if (current_block != null && current_block != block_just_entered) {
                     releaseHighlightedBlocks(event, false);
                  }
                  if (block_just_entered != current_block) {
                     board.setCurrentBlock(block_just_entered);
                     block_just_entered.mouseEntered(event);
                     hl_blocks.add(block_just_entered);
                  }
               }
               else {
                  if (current_block != null) {
                     releaseHighlightedBlocks(event, false);
                     current_block.mouseExited(event);
                     board.setCurrentBlock(null);
                  }
               }
            }
            else if (current_block != this_block) {  //just entered this block
               board.setCurrentBlock(this_block);
               this_block.mouseEntered(event);
            }
         }
      });
}
      //end HACK


   }

   public void releaseHighlightedBlocks(MouseEvent event, boolean from_this_block) {
      GenericBlock temp_block;
      while (!hl_blocks.isEmpty()) {
         temp_block = (GenericBlock) hl_blocks.getFirst();
         if (!from_this_block && temp_block != this_block)
            temp_block.mouseExited(event);
         hl_blocks.remove(temp_block);
      }
   }

   public void mouseEntered(MouseEvent e) {
      ((Board)getParent()).on_block = true;
      if (this.type == "Block") {
         blockMouseEntered(e);
      } else if (this.type == "Number") {
         numberMouseEntered(e);
      } else if (this.type == "Bomb") {
         bombMouseEntered(e);
      }
   }

/*
   protected void processEvent(AWTEvent e) {
      EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();

      System.out.println("event queue: " + eq);

      try {
         Thread.currentThread().sleep(150);
      } catch (InterruptedException ex) {
         ex.printStackTrace();
      }

   }
*/

} //class GenericBlock

