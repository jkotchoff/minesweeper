import java.net.*;
import java.io.*;
import java.awt.Frame;
import java.util.Enumeration;
import java.util.Calendar;
import java.applet.Applet;
import netscape.javascript.*;
/**
 * This class is enables the back-end interaction with the client
 * browser and the web-server to log High Scores
 *
 * @author Jason Kotchoff
 */

class HighScores {
   private Minesweeper applet;
   private Frame parent;
   private XMLElement users_scores_xml;
   private XMLElement net_scores_xml;
   private String browser;

   /**
    * Constructs a new MessageBox object.
    * @param parent The parent window
    * @param applet The Minesweeper Applet
    */
   public HighScores(Frame parent, Minesweeper applet) {
      this.parent = parent;
      this.applet = applet;
      browser = applet.getBrowser();
      if (browser != Minesweeper.APPLETVIEWER) {
         users_scores_xml = getUsersHighScores(applet);
         net_scores_xml = getNetHighScores();
      }
      else {
         users_scores_xml = null;
         net_scores_xml = null;
      }
   }

   /**
    * Checks whether the time is a High Score for the User and/or
    * for the global high scores list and if so, prompts the user
    * for a name and logs the score.
    */
   public void checkHighScore(int time, String board_type) {
      boolean is_user_hs, is_net_hs;
      is_user_hs = isHighScore(time, board_type, users_scores_xml);
      is_net_hs = isHighScore(time, board_type, net_scores_xml);

      if (!is_user_hs && !is_net_hs) {
         return;
      }

      String name = getUserName(board_type, is_net_hs);
      name = xmlEscape(name);
      String date = getDate();

      if (is_net_hs) {
         if(updateNetHighScore(board_type, time, name, date)) {
            net_scores_xml = getNetHighScores();
         }
         else {
            //MessageBox mb = new MessageBox(parent, "The Global High Score you made was no longer a High score by the time it made it to the High Scores List");
         }
      }

      if (is_user_hs) {
         updateHighScoresXML(name, time, date, board_type, users_scores_xml);
      }

      if (is_net_hs)
         displayNetHighScores();
      else
         displayUsersHighScores();
   }

   /**
    * Displays the Global best Times from the web
    */
   public void displayNetHighScores() {
      if (net_scores_xml == null) {
         errorMessage();
         return;
      }

      BestTimesBox btb = new BestTimesBox(net_scores_xml, parent, "Fastest Mine Sweepers");
   }

   /**
    * Displays the Users best Times
    */
   public void displayUsersHighScores() {
      if (users_scores_xml == null) {
         errorMessage();
         return;
      }

      BestTimesBox btb = new BestTimesBox(users_scores_xml, parent, "Your Best Times");
   }

   /**
    * Displays an Error Message
    */
   public void errorMessage() {
      MessageBox mb = new MessageBox(parent, "Error in Internet Connection to High Scores Server: Will not be able to set or retrieve High Scores");
   }

   /**
    * Get all cookies for the current document
    */
   public String getCookie(Minesweeper applet) {
      try {
         JSObject myBrowser = (JSObject) JSObject.getWindow(applet);
         JSObject myDocument =  (JSObject) myBrowser.getMember("document");
         String myCookie = (String)myDocument.getMember("cookie");
         if (myCookie.length() > 0)
            myCookie = myCookie.substring(12, myCookie.length());
            return myCookie;
         }
      catch (Exception e){
         System.err.println("Could not retrieve Cookie");
      }
      return "";
   }

   /**
    * Returns the current date in the format dd/mm/yy
    */
   public String getDate() {
      Calendar c = Calendar.getInstance();
      return c.get(c.DAY_OF_MONTH) + "/" + c.get(c.MONTH)+1 + "/" + c.get(c.YEAR);
   }

   /**
    * Retrieves the Global High Scores from the Web Server
    */
   public XMLElement getNetHighScores() {
      String script_url = "http://jkotchoff:90/minesweeper/get_topscores.asp";
      String xml = postToScript(script_url, "");

      if (xml == "") {
         errorMessage();
         return null;
      }

      XMLElement high_scores_xml = new XMLElement();
      high_scores_xml.parseString(xml);

      return high_scores_xml;
   }

   /**
    * Return the User Name (tag) of the player
    */
   public String getUserName(String board_type, boolean is_net_hs) {
      UserName user_name_dlg = new UserName(parent, board_type, is_net_hs);
      return user_name_dlg.getUserName();
   }

   /**
    * Retrieves the High Scores for the current User from their browser cookie
    */
   public XMLElement getUsersHighScores(Minesweeper applet) {
      boolean cookie_found = false;

      String cookie = getCookie(applet);

      if (cookie != "") cookie_found = true;

      XMLElement high_scores_xml = new XMLElement();

      if (cookie_found) {
         try {
            high_scores_xml.parseString(cookie);
         } catch (Exception e) {
            high_scores_xml = new XMLElement();
            cookie_found = false;
         }
      }

      if (!cookie_found) high_scores_xml.parseString(getUserTemplateXML());

      return high_scores_xml;
   }

   /**
    * Sets the High Scores Template for the client High Scores
    */
   public String getUserTemplateXML() {
      String xml;
      xml = "<topScores>";
      xml += "   <numberScoresRecorded>1</numberScoresRecorded>";
      xml += "   <template>";
      xml += "      <timeTaken>999</timeTaken>";
      xml += "      <name>Anonymous</name>";
      xml += "      <dateRecorded>-</dateRecorded>";
      xml += "   </template>";
      xml += "   <Beginner/>";
      xml += "   <Intermediate/>";
      xml += "   <Expert/>";
      xml += "</topScores>";
      return xml;
   }

   /**
    * Returns true if the time is a High Score for the high_scores_xml
    */
   public boolean isHighScore(int time, String board_type, XMLElement high_scores_xml) {
      if (browser != Minesweeper.APPLETVIEWER) {
         //get the lowest score in the xml, if time is > lowest score, return true
         int lowest_score_rank = Integer.parseInt(high_scores_xml.selectSingleNode("topScores/numberScoresRecorded").getContent().trim());
         int lowest_score = Integer.parseInt(BestTimesBox.getScoreNode(high_scores_xml, board_type, "timeTaken", lowest_score_rank));
         return time < lowest_score;
      }
      else
         errorMessage();
      return false;
   }

   /**
    * Posts to the script_URL in the same directory as the applet
    * If response is empty or "failed", returns null.
    */
   public String postToScript(String script_URL, String request) {
      URL url;
      URLConnection urlConn;
      DataOutputStream printout;
      BufferedReader input;

      String response = "", str;

      try {
         url = new URL (script_URL);

         urlConn = url.openConnection();
         urlConn.setDoInput (true);
         urlConn.setDoOutput (true);
         urlConn.setUseCaches (false);
         urlConn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
         printout = new DataOutputStream (urlConn.getOutputStream ());
         printout.writeBytes (request);
         printout.flush ();
         printout.close ();

         // Get response data.
         input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

         while (null != ((str = input.readLine()))) {
            response += str + "\n";
         }
         input.close();

      } catch(MalformedURLException mue) {
         System.err.println(mue);
         errorMessage();
         return null;
      } catch(IOException ioe) {
         System.err.println(ioe);
         errorMessage();
         return null;
      }

      return response;
   }

   /**
    * Saves the Users High Scores to a Cookie on the Client Browser
    */
   public void saveUsersHighScores() {
      if (browser != Minesweeper.APPLETVIEWER)
         setCookie(users_scores_xml.toString(), applet);
      else
         errorMessage();
   }

   /**
    * Set the cookie for the current document
    */
   public void setCookie(String str, Minesweeper applet) {
      Calendar c = Calendar.getInstance();
      c.add(Calendar.YEAR, 1);
      String expires = "; expires=" + c.getTime();

      String cookie = "Minesweeper=" + str + expires;

      JSObject myBrowser = JSObject.getWindow(applet);

      JSObject myDocument =  (JSObject) myBrowser.getMember("document");

      myDocument.setMember("cookie", cookie);
   }

   /**
    * Inserts the new score in the scores_xml
    */
   public void updateHighScoresXML(String name, int time, String date, String board_type, XMLElement scores_xml) {
      XMLElement new_score_node = new XMLElement();
      String new_score_xml;
      new_score_xml = "<score>";
      new_score_xml += "<timeTaken>" + Integer.toString(time) + "</timeTaken>";
      new_score_xml += "<name>" + name + "</name>";
      new_score_xml += "<dateRecorded>" + date + "</dateRecorded>";
      new_score_xml += "</score>";
      new_score_node.parseString(new_score_xml);

      XMLElement board_node = scores_xml.selectSingleNode("topScores/" + board_type);

      Enumeration scores = board_node.enumerateChildren();
      int index = 0, child_score = -1;
      XMLElement child = null;
      boolean score_recorded = false;

      while (scores.hasMoreElements() && !score_recorded) {
         child = (XMLElement) scores.nextElement();
         child_score = Integer.parseInt(child.selectSingleNode("score/timeTaken").getContent());
         if (time < child_score) {
            board_node.addChild(index, new_score_node);
            score_recorded = true;
         }
         index++;
      }

      if (!score_recorded) {
         board_node.addChild(new_score_node);
      }

      int num_scores = Integer.parseInt(scores_xml.selectSingleNode("topScores/numberScoresRecorded").getContent());
      if (board_node.countChildren() > num_scores) {
         board_node.removeLastChild();
      }
   }

   /**
    * Returns false if the user's score is no longer a net high score by the
    * time it reaches the scores script on the web-server
    */
   public boolean updateNetHighScore(String board_type, int time_taken, String name, String date_recorded) {
//      String script_url = "http://jkotchoff:90/minesweeper/set_topscore.asp";
//      String request = "?boardType=" + board_type + "&timeTaken=" + time_taken + "&name=" + URLEncoder.encode(name) + "&dateRecorded=" + URLEncoder.encode(date_recorded);
      String script_url = "http://jkotchoff:90/minesweeper/set_topscore.asp?boardType=" + board_type + "&timeTaken=" + time_taken + "&name=" + URLEncoder.encode(name) + "&dateRecorded=" + URLEncoder.encode(date_recorded);
      String xml = postToScript(script_url, "");

//      String xml = postToScript(script_url, request);

      if (xml == "" || xml == "failed") {
         errorMessage();
         return false;
      }

      return true;
   }

   /**
    * Performs URL encoding for banned characters in a HTTP Post. Should
    * consider testing the URLEncoder.encode() method for this when I get time
    */
   public String xmlEscape(String content) {
      String escaped_str = content;
      String banned_chars[][] = {{"%", "%25"}, {"<", "%3C"}, {">", "%3E"}, {",", "%2C"}, {";", "%3B"}, {" ", "%20"}};
      for(int i = 0, char_index; i < banned_chars.length; i++) {
         while(escaped_str.indexOf(banned_chars[i][0]) != -1) {
            char_index = escaped_str.indexOf(banned_chars[i][0]);
            escaped_str = escaped_str.substring(0, char_index) + banned_chars[i][1] + escaped_str.substring(char_index+1, escaped_str.length());
         }
      }
      return escaped_str;
   }

}
















