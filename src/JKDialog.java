import java.awt.*;
import java.awt.event.*;
/**
 * This class provides extended functionality for the Dialog class
 *
 * @author Jason Kotchoff
 */

class JKDialog extends Dialog {

   protected Panel surface;
   private Frame parent;

   /**
    * Constructs a new JKDialog object.
    * @param parent The parent window
    * @param dialog_title The Window Title
    * @param is_modal Depicts whether the Dialog is Modal
    */
   public JKDialog (Frame parent, String dialog_title, boolean is_modal) {
      super(parent, dialog_title, is_modal);

      this.parent = parent;

      setLayout(new BorderLayout());

      add(surface = new Panel());

      Button ok_button = new Button("  OK  ");
      ok_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            closeDialog();
         }
      });

      Panel ok_holder = new Panel();
      add(ok_holder, BorderLayout.SOUTH);
      ok_holder.add(ok_button);

      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            closeDialog();
         }
      });
   }

   /**
    * Constructs a new JKDialog object.
    * @param parent The parent window
    * @param dialog_title The Window Title
    * @param is_modal Depicts whether the Dialog is Modal
    * @param ok_button If this parameter is included, no OK button will
    *			be added to the Dialog when created
    */
   public JKDialog (Frame parent, String dialog_title, boolean is_modal, boolean ok_button) {
      super(parent, dialog_title, is_modal);
      this.parent = parent;
      add(surface = new Panel());
   }

   /**
    * Closes the dialog
    */
   public void closeDialog() {
      setVisible(false);
      dispose();
      parent.toFront();
   }

   /**
    * Enables the dialog, centering it on the parent frame
    */
   public void setVisible(boolean visible) {
      pack();

      Dimension frameSize = getParent().getSize();
      Point frameLoc = getParent().getLocation();
      Dimension mySize = getSize();
      int x, y;

      x = frameLoc.x + (frameSize.width/2) - (mySize.width/2);
      y = frameLoc.y + (frameSize.height/2) - (mySize.height/2);

      setBounds(x, y, getSize().width, getSize().height);

      super.setVisible(visible);
   }
}
