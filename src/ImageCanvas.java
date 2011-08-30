import java.awt.*;
/**
 * This class creates an AWT compatible heavyweight class with similar functionality
 * as that of the ImageIcon class in Swing.  The only real difference is that it
 * accepts an Image instead of an ImageIcon as a parameter to setIcon().
 *
 * @author Jason Kotchoff
 */
import java.applet.Applet;

public class ImageCanvas extends Canvas {

   private Image image; 	//keeps a reference to the current image being displayed on this canvas

   /**
    * Constructs a new ImageCanvas object.
    */
   public ImageCanvas() {;}

   /**
    * Draws the current image on this button
    */
   public void paint(Graphics g) {
      g.drawImage(image, 0, 0, this);
   }

   /**
    * Sets the current image for this button
    */
   public void setIcon(Image img) {
      if(img != this.image) {
         this.image = img;
         repaint();
      }
   }

}
