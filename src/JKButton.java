import java.awt.event.*;
import java.awt.*;
/**
 * This class creates an AWT compatible heavyweight class with similar functionality
 * as that of the ImageIcon class in Swing.  The only real difference is that it
 * accepts an Image instead of an ImageIcon as a parameter to setIcon().
 *
 * @author Jason Kotchoff
 */

public abstract class JKButton extends ImageCanvas
                          implements MouseListener {

   /**
    * Constructs a new JKButton object.
    */
   public JKButton() {
      addMouseListener(this);
   }

   /**
    * Neccessary to over-ride all the mousehandlers
    * in the child class
    */
   public abstract void mouseExited(MouseEvent e);
   public abstract void mouseReleased(MouseEvent e);
   public abstract void mouseClicked(MouseEvent e);
   public abstract void mousePressed(MouseEvent e);
   public abstract void mouseEntered(MouseEvent e);

}
