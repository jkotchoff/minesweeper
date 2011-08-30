import java.awt.Image;
/**
 * This panel keeps a count of the number of flags that have been placed so far.
 *
 * <P>The number of flags represented on the BombCounter cannot be lower
 * than -99 (it will still store the number of flags but will not graphically
 * represent it).
 *
 * @author Jason Kotchoff
 */

public class BombCounter extends NumberFlipper {

   /**
    * Constructs a new BombCounter object.
    * @param board_type specifies the board type of the current game.<BR>
    *                   <I>0</I>: beginner board<BR>
    *                   <I>1</I>: intermediate board<BR>
    *                   <I>2</I>: expert board
    * @param color_counter_images An array of Color images to be displayed on this numberflipper
    * @param bw_counter_images An array of Black & White images to be displayed on this numberflipper
    * @param color Specifies whether the BombCounter is to be drawn in Color or not
    */
   public BombCounter(int board_type, Image[] color_counter_images, Image[] bw_counter_images, boolean color) {
      super(color_counter_images, bw_counter_images, color);
      setBoardType(board_type);
      setCounter();
   }

   /**
    * This method decrements the bomb counter and then calls the
    * setCounter() method to graphically update the counter.
    */
   public void bombFound() {
      counter--;
      if (counter >= -99) {
         setCounter();
      }
   }

   /**
    * This method is called when a game is won to ensure that the
    * bomb counter shows '000' (if all non-bombs are revealed, but
    * not all flags are placed)
    */
   public void gameWon() {
      if (counter != 0) {
         counter = 0;
         setCounter();
      }
   }

   /**
    * This method re-initialises the number of bombs
    * (called when a board-type is swapped)
    */
   public void setBoardType(int board_type) {
      switch(board_type) {
         case(Minesweeper.BEGINNER):
            counter = default_value = 10;
            break;
         case(Minesweeper.INTERMEDIATE):
            counter = default_value = 40;
            break;
         case(Minesweeper.EXPERT):
            counter = default_value = 99;
            break;
      }
   }

   /**
    * This method increments the bomb counter and then calls the
    * setCounter() method to graphically update the counter.
    */
   public void unFound() {
      counter++;
      if (counter >= -99)
         setCounter();
   }

} //class BombCounter

