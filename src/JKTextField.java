import java.awt.event.*;
import java.awt.TextField;
/**
 * This class provides extended functionality for the TextField class
 * including a max_chars limit on the number of characters entered
 *
 * @author Jason Kotchoff
 */

class JKTextField extends TextField {
   private int max_chars;

   /**
    * Constructs a new JKTextField object.
    * @param default_text The text to display in the Text Field
    * @param max_chars The Maximum number of characters allowed in this
    *			TextField
    */
   public JKTextField(String default_text, int cols, int max_chars) {
      super(default_text, cols);
      this.max_chars = max_chars;
      addKeyListener(new JKKeyListener());
      addFocusListener(new JKFocusListener());
   }

   /**
    * Provides Key Listening for the JKTextField
    */
   class JKKeyListener extends KeyAdapter  {
      public void keyPressed(KeyEvent event) {
         TextField field = (TextField)event.getSource();
         String contents = field.getText();

         if (field.getSelectedText().compareTo("") == 0 && contents.length() > max_chars-1) {
            if(!event.isActionKey()) {
               boolean valid_key = false;
               int[] valid_keys = {KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_TAB};
               for (int i = 0; i < valid_keys.length && valid_key == false; i++)
                  if (event.getKeyCode() == valid_keys[i])
                     valid_key = true;
               if (!valid_key)
                  event.consume();
            }
         }
      }
   }

//STILL UNDER DEVELOPMENT
   class JKFocusListener extends FocusAdapter {
      public void focusLost(FocusEvent event) {
//System.out.println("focus lost");
         select(0, 0);	//unhighlight any text selection
      }
   }
}
