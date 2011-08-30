import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.image.ImageProducer;
import java.net.*;
import java.io.*;


public class TestApplet {



   public static void main(String []args) throws IOException {
      String script_url = "http://yallara.cs.rmit.edu.au/~jkotchof/minesweeper/set_topscore.php";
      String request = "boardType=Beginner&timeTaken=10&name=jason&dateRecorded=" + URLEncoder.encode("10/10/01");
      String response = postToScript(script_url, request);
      System.out.println(response);
   }


   public static String postToScript(String script_URL, String request) {
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
         System.err.println("MalformedURLException" + mue);
//         errorMessage();
         return null;
      } catch(IOException ioe) {
         System.err.println("IOException" + ioe);
         ioe.printStackTrace();
//         errorMessage();
         return null;
      } catch (Exception e) {
         System.err.println("Exception: " + e);
      }

      return response;
   }


} //class Minesweeper
