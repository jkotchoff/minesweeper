import java.awt.*;
/**
 * Prompts the user for a name which can be retrieved using the
 * getUserName() method
 *
 * @author Jason Kotchoff
 */

class UserName extends JKDialog {
   private String user_name;
   private String message;
   private String prompt;
   private JKTextField name_field;
   private Button okButton;

   /**
    * Constructs a new UserName object.
    * @param parent The parent window
    * @param current_board_type The current Minesweeper Board Type
    * @param is_net_hs The High Score Type
    */
   public UserName(Frame parent, String current_board_type, boolean is_net_hs) {
      super(parent, "Best Time", true);

      String record_type = (is_net_hs)? "a global" : "your";

      message = "You have broken " + record_type + " record for the " + current_board_type + " board.";
      prompt = "Please enter your name:";

      initialiseStuff();
   }

   /**
    * Censor's banned words from String str replacing their
    * characters with an asterisk symbol
    */
   public static String censor(String str) {
      String return_str = str;
      String[] banned_words = {"fuck", "cunt", "shit", "spastic"};
      String lower_case = return_str.toLowerCase();
      for (int i = 0, banned_word_index, banned_word_length; i < banned_words.length; i++) {
         banned_word_index = lower_case.indexOf(banned_words[i]);
         banned_word_length = banned_words[i].length();
         if (banned_word_index != -1) {
            String censored = "";
            for (int j = 0; j < banned_word_length; j++)
               censored += "*";
            return_str = return_str.substring(0, banned_word_index) + censored + return_str.substring(banned_word_index+banned_word_length, return_str.length());
         }
      }
      return return_str;
   }

   /**
    * Returns the User name entered
    */
   public String getUserName() {
      user_name = name_field.getText();
      user_name = censor(user_name);
      if (user_name.compareTo("") == 0) user_name = "Anonymous";
      return user_name;
   }

   /**
    * Adds all the AWT Components to the Dialog
    */
   public void initialiseStuff() {
      surface.setLayout(new BorderLayout());

      Panel msg_panel = new Panel();
      msg_panel.setLayout(new GridLayout(3, 1));
      Label congratulations;
      msg_panel.add(congratulations = new Label("Congratulations!", Label.CENTER));
      congratulations.setFont(new Font("", Font.BOLD, 12));
      msg_panel.add(new Label(message));
      msg_panel.add(new Label(prompt));
      surface.add(msg_panel, BorderLayout.NORTH);

      Panel name_panel = new Panel();
      name_panel.add(name_field = new JKTextField("Anonymous", 17, 20));
      surface.add(name_panel, BorderLayout.SOUTH);
      name_field.selectAll();

      setVisible(true);
   }
}

