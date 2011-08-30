import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 * This class creates a Canvas that has a number drawn on it (The
 * number must be in the range 0-9 (or a negative symbol: "-"),
 * and it is specified in the constructor).
 *
 * @author Jason Kotchoff
 */


public class NumberImage extends Canvas {
   public static final int NUMBER_WIDTH = 13;
   public static final int NUMBER_HEIGHT = 23;
   private final int UNINITIALISED = -1000;  //note: this value needs to be larger than the maximum number of blocks possible
   private int value;
   private Image number;
   private Image[] images, color_images, bw_images;
   private boolean color;

   /**
    * Constructs a new NumberImage object.
    * @param timer_images An array of the images that will be used when drawing the number
    */
   public NumberImage(Image[] color_number_images, Image[] bw_number_images, boolean color) {
      this.color_images = color_number_images;
      this.bw_images = bw_number_images;
      this.color = color;
      if (color)
         this.images = color_number_images;
      else
         this.images = bw_number_images;
      this.value = UNINITIALISED;
   }

   /**
    * Sets the preferred size of this Component
    */
   public Dimension getPreferredSize() {
      return new Dimension(NUMBER_WIDTH, NUMBER_HEIGHT);
   }

   /**
    * Standard over-riding of paint method
    * @param g The default graphics object
    */
   public void paint (Graphics g) {
      if (value != UNINITIALISED)
         g.drawImage(number, 0, 0, this);
   }

   /**
    * Constructs a new NumberImage object.
    * @param number the value of the number to be represented
    */
   public void swapTo(int number) {
      if (this.value != number) {
         this.value = number;
         if (number == -1)
            this.number = images[MinesweeperPane.NEGATIVE];
         else if (number <= 0)
            this.number = images[MinesweeperPane.ZERO];
         else
            this.number = images[number-1];
         repaint();
      }
   }

   /**
    * Toggles the color mode of this number image between color
    * and black and white
    */
   public void toggleColor() {
      color = !color;
      if (color)
         this.images = color_images;
      else
         this.images = bw_images;
      //need to repeat the functionality of swapTo() because that
      //method wont repaint the image if the number is not swapped
      if (value == -1)
         this.number = images[MinesweeperPane.NEGATIVE];
      else if (value <= 0)
         this.number = images[MinesweeperPane.ZERO];
      else
         this.number = images[value-1];
      repaint();
   }

} //class NumberImage
