import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
/**
 * This class is a dialog box that displays High Scores
 *
 * @author Jason Kotchoff
 */

public class BestTimesBox extends JKDialog {

   public BestTimesBox(XMLElement high_scores, Frame parent, String title) {
      super(parent, title, true);

      XMLElement number_scores_node = high_scores.selectSingleNode("topScores/numberScoresRecorded");
      int number_scores = Integer.parseInt(number_scores_node.getContent().trim());
      if (number_scores <= 5) {
         initialiseNonScrolling(number_scores, high_scores);
      }
      else {
         //
      }

      //if number of top scores > 9, display 3 scrollbars, else
      //display dialog


      setVisible(true);
   }

   /**
    * Returns the contents of the nodeName node from the high_scores
    * XMLElement with a board type of board_type and a rank of rank
    * (if not found, uses the default value in the template node)
    */
   public static String getScoreNode(XMLElement high_scores, String board_type, String node_name, int rank) {
      XMLElement parent_node = high_scores.selectSingleNode("topScores/" + board_type);

      XMLElement node = null;

      Enumeration node_children = parent_node.enumerateChildren();
      int counter = 0;
      XMLElement child = null;
      while (node_children.hasMoreElements()) {
         child = (XMLElement) node_children.nextElement();
         if (++counter == rank && child != null) {
            node = child;
            break;
         }
      }

      if (node != null)
         return xmlUnescape(node.selectSingleNode("score/" + node_name).getContent().trim());
      else
         return xmlUnescape(((XMLElement)high_scores.selectSingleNode("topScores/template/" + node_name)).getContent().trim());
   }

   /**
    * Initialise all Components on this Dialog
    */
   private void initialiseNonScrolling(int number_scores, XMLElement high_scores) {
      Panel best_times = new Panel();

      int hz_cells = 5;
      int vt_cells = 3 * number_scores;

      best_times.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.NORTHWEST;

      String current_lbl;
      Label lbl_current;
      Color current_color;
      Insets ins_separator = new Insets (0, 0, 15, 0);
      Insets ins_default = new Insets (0, 0, 0, 0);
      String hd_beginner = "Beginner";
      String hd_intermediate = "Intermediate";
      String hd_expert = "Expert";
      String[] board_types = {hd_beginner, hd_intermediate, hd_expert};
      int score_type_code;
      String board_type = "";
      boolean heading;
      for (int i = 0; i < vt_cells; i++) {
         for (int j = 0; j < hz_cells; j++) {
            current_lbl = "";
            gbc.gridwidth = 1;
            heading = false;

            if (number_scores == 1) {
               score_type_code = i/number_scores;
            }
            else if (i == 0 || i == number_scores || i == number_scores*2) {
               score_type_code = (i+1) / number_scores;
            }
            else {
               score_type_code = -1;
            }

            if (score_type_code == 0)
               board_type = hd_beginner;
            else if (score_type_code == 1)
               board_type = hd_intermediate;
            else if (score_type_code == 2)
               board_type = hd_expert;

            int rank = (i+1)%number_scores;
               if (rank == 0)
                  rank = number_scores;

            switch(j) {
               case (0):
		  if (i == 0 || i == number_scores || i == number_scores*2) {
                     current_lbl = board_types[score_type_code] + ":";
                     heading = true;
                  }
                  break;
               case (1):
                  if (number_scores > 1)
                     current_lbl = Integer.toString(i % number_scores + 1) + ".";
                  else
                     current_lbl = "";
                  break;
               case (2):
                  String node_name;
                  current_lbl = getScoreNode(high_scores, board_type, "timeTaken", rank) + " seconds";
                  break;
               case (3):
                  current_lbl = getScoreNode(high_scores, board_type, "name", rank);
                  break;
               case (4):
                  current_lbl = getScoreNode(high_scores, board_type, "dateRecorded", rank);
                  gbc.gridwidth = GridBagConstraints.REMAINDER; //newline
                  break;
            }
            gbc.insets = ((i+1) % number_scores == 0 && rank != 1) ? ins_separator : ins_default;
            lbl_current = new Label(current_lbl);

            int score_type = i / number_scores;
            int r, g, b;
            r = g = 0;
            if (number_scores > 1)
               b = 255 - i % number_scores * 255 / number_scores;
            else
               b = 0;
            current_color = new Color(r, g, b);
            lbl_current.setForeground(current_color);
            if (heading)
               lbl_current.setFont(new Font("", Font.BOLD, 12));

            best_times.add(lbl_current, gbc);
         }
      }

      add(best_times);
   }

   /**
    * Escapes the content for HTTP posts. See same method in HighScores.java
    */
   public static String xmlUnescape(String content) {
      String escaped_str = content;
      String banned_chars[][] = {{"<", "%3C"}, {">", "%3E"}, {",", "%2C"}, {";", "%3B"}, {" ", "%20"}, {"%", "%25"}};
      for(int i = 0, char_index; i < banned_chars.length; i++) {
         while(escaped_str.indexOf(banned_chars[i][1]) != -1) {
            char_index = escaped_str.indexOf(banned_chars[i][1]);
            escaped_str = escaped_str.substring(0, char_index) + banned_chars[i][0] + escaped_str.substring(char_index+3, escaped_str.length());
         }
      }
      return escaped_str;
   }

}
