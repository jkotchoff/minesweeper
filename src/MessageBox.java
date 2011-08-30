import java.awt.*;
/**
 * This class is a dialog box that displays error messages
 *
 * @author Jason Kotchoff
 */


public class MessageBox extends JKDialog {
   String message = "";

   /**
    * Constructs a new MessageBox object.
    * @param parent The parent window
    * @param message The message to display on this Dialog
    */
   public MessageBox(Frame parent, String message) {
      super(parent, "Minesweeper Message", true);
      this.message = message;
      initialiseStuff();
      setVisible(true);
   }

   /**
    * Initialises AWT components on the MessageBox
    */
   private void initialiseStuff() {
      Label mesg = new Label(message);
      add(mesg);
   }
}
