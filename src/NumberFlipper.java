import java.awt.*;
/**
 * This class creates a three number counter (created from NumberImage
 * objects that have digits painted onto them).  It can be used for
 * keeping count of things.
 *
 * @author Jason Kotchoff
 */

public class NumberFlipper extends Panel {
   private NumberImage hundreds;
   private NumberImage tens;
   private NumberImage digits;
   public int counter;
   protected int default_value;
   private final int INSET = 1;

   /**
    * Constructs a new NumberFlipper object.
    * @param timer_images is an array of all the images to be drawn
    *			  on this panel
    */
   public NumberFlipper(Image[] color_counter_images, Image[] bw_counter_images, boolean color) {
      setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

      hundreds = new NumberImage(color_counter_images, bw_counter_images, color);
      add(hundreds);

      tens = new NumberImage(color_counter_images, bw_counter_images, color);
      add(tens);

      digits = new NumberImage(color_counter_images, bw_counter_images, color);
      add(digits);
   }

   /**
    * This method accepts an index and flips the number passed as the
    * second variable based on the digit in the index
    * position of the seconds_taken variable.
    * @param index the index of the number to be changed (eg. '1' for
    *              the 'tens' digit).
    * @param number the NumberImage that is being changed
    */
   public void flipImage(int index, NumberImage number) {
      try {
         int index_pos;

         if (number == hundreds && counter < 0) {
            index_pos = -1;
         }
         else {
            String counter_str = String.valueOf(counter);
            int strlen = counter_str.length();

            //the following code handles situations when the number representing
            //the amount of bombs varies in length eg. '1' or '10' or '100'
            if (index == 0)
               index_pos = (counter_str).charAt(index+strlen-1) - 48;  //-48 for ascii value
            else if (index == 1)
               index_pos = (counter_str).charAt(index+strlen-3) - 48;
            else
               index_pos = (counter_str).charAt(index+strlen-5) - 48;
         }
         number.swapTo(index_pos);

      } catch (StringIndexOutOfBoundsException e) { //thrown when there is no hundred or ten digit
         number.swapTo(0);
      }
   }

   /**
    * Used in conjunction with the paint() method to draw the insets
    */
   public Insets getInsets() {
      return new Insets(INSET, INSET, INSET, INSET);
   }

   /**
    * Used in conjunction with the overridden getInsets() method to draw the insets
    */
   public void paint(Graphics g) {
      Dimension size = getSize();

      g.setColor(new Color(132, 130, 132));
      g.fillRect(0, 0, INSET, size.height-INSET);
      g.fillRect(INSET, 0, size.width-INSET*2, INSET);

      g.setColor(Color.white);
      g.fillRect(INSET, size.height-INSET, size.width-INSET*2, INSET);
      g.fillRect(size.width-INSET, INSET, INSET, size.height-INSET);
   }

   /**
    * This method resets the counter to the default value
    */
   public void reset() {
      counter = default_value;
      setCounter();
   }

   /**
    * This method updates the counter (incrementing or decrementing
    * the number images).
    */
   public void setCounter() {
      flipImage(2, hundreds);
      flipImage(1, tens);
      flipImage(0, digits);
   }

   /**
    * Toggles the color mode of this number image between color
    * and black and white
    */
   public void toggleColor() {
      hundreds.toggleColor();
      tens.toggleColor();
      digits.toggleColor();
   }

} //class NumberFlipper
