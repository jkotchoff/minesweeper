import java.awt.*;
/**
 * This class is a dialog box that displays information
 * about my Minesweeper Applet
 *
 * @author Jason Kotchoff
 */


public class AboutBox extends JKDialog {

   public AboutBox(Frame parent, Image pic) {
      super(parent, "About Minesweeper", true);

      Label gameLabel = new Label();
      ImageCanvas picOfMe = new ImageCanvas();

      surface.setLayout(new BorderLayout());

      gameLabel.setText("Minesweeper written in Java");
      gameLabel.setFont(new Font("", Font.BOLD, 12));
      surface.add(gameLabel, BorderLayout.NORTH);

      Panel contactDetails = new Panel();
      contactDetails.setLayout(new GridLayout(2, 1));
      Label name = new Label("by Jason Kotchoff");
      name.setAlignment(Label.CENTER);
      Label email = new Label(Minesweeper.EMAIL);
      email.setAlignment(Label.LEFT);
      email.setForeground(Color.blue);
      contactDetails.add(name);
      contactDetails.add(email);
      Panel details_together = new Panel();
      details_together.add(contactDetails);
      surface.add(details_together, BorderLayout.CENTER);

      picOfMe.setIcon(pic);
      picOfMe.setSize(new Dimension(200, 134));
      surface.add(picOfMe, BorderLayout.SOUTH);

      setVisible(true);
   }

}


