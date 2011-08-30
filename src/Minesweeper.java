import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.image.ImageProducer;

//import netscape.javascript.*;


/**
 * This class draws the applet that will contain the launcher
 * button (that launches the minesweeper application).
 *
 * <P>It initialises and handles the menu and creates an instance
 * of the Minesweeper frame.
 *
 * @author Jason Kotchoff
 */

public class Minesweeper extends Applet {
   public static final int BEGINNER = 0;
   public static final int INTERMEDIATE = 1;
   public static final int EXPERT = 2;

   public static final String APPLETVIEWER = "appletviewer";

   private int current_board_type = INTERMEDIATE;
   private boolean marks = false;
   private boolean color = true;
   private boolean sound = false;
   public static final String EMAIL = "cornflakesuperstar@hotmail.com";

   private Button launchButton = new Button("Launch Minesweeper");
   private Frame frame = new Frame("Minesweeper by Jas");
   private AboutBox aboutMinesweeper;
   private MenuBar mbar;
   private MenuItem newItem, beginnerItem, intermediateItem, expertItem, customItem, usersBestTimesItem, netBestTimesItem, exitItem, helpItem, aboutItem;
   private CheckboxMenuItem marksItem, colorItem, soundItem;
   private MenuItemListener menuItemListener = new MenuItemListener();
   private MinesweeperPane board;

   private String browser;

   HighScores high_scores;

   public String getBrowser() {
      return browser;
   }

   Image about_pic;

   public void init() {
      //retrieve applet parameters
      String bg_color = getParameter("bgcolor");
      Color background;
      try { background = new Color(Integer.parseInt(bg_color, 16)); }
      catch (Exception e) { background = null; }
      setBackground(background);

      browser = getParameter("browser");
      browser = (browser == null) ? "applet" : APPLETVIEWER;

      about_pic = loadImage("mugshot.jpg");

      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            frame.dispose();
         }
         public void windowClosed(WindowEvent event) {
            try {
               high_scores.saveUsersHighScores();
               launchButton.setEnabled(true);
            } catch (Exception e) {
               //applet was unloaded from web-page
            }
         }
      });

      frame.setBackground(new Color(191, 191, 191));
      frame.setResizable(false);
      frame.setIconImage(loadImage("minesweeper.gif"));
      high_scores = new HighScores(frame, this);
      board = new MinesweeperPane(current_board_type, high_scores, color, marks, sound, this);
      frame.add(board);

      launchButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            launchButton.setEnabled(false);
            showStatus(null);

            //start stoopid browser hack to avoid dissapearing menu
            setupMenu();
            menuEnablement(current_board_type);
            frame.pack();
            if (browser != "appletviewer")
               frame.pack();	//stoopid browser hack to ensure correct size

            Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
            int frame_width = frame.getSize().width;
            int frame_height = frame.getSize().height;
            frame.setLocation(screen_size.width/2 - frame_width/2, screen_size.height/2 - frame_height/2);
            //end stoopid browser hack


            frame.show();
            showStatus("Minesweeper launched");
         }
      });
      add(launchButton);
   }

   public String getAppletInfo() {
      return "Minesweeper in Java written by Jason Kotchoff (" + EMAIL + ")";
   }

   //sets up the menu on the minesweeper frame
   public void setupMenu() {
      MenuBar mbar = new MenuBar();

      Menu fileMenu = new Menu("File", true);
      fileMenu.add(newItem = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N)));
      fileMenu.addSeparator();
      fileMenu.add(beginnerItem = new MenuItem("Beginner"));
      fileMenu.add(intermediateItem = new MenuItem("Intermediate"));
      fileMenu.add(expertItem = new MenuItem("Expert"));
      //fileMenu.add(customItem = new MenuItem("Custom..."));
      fileMenu.addSeparator();
      fileMenu.add(marksItem = new CheckboxMenuItem("Marks (?)", marks));
      fileMenu.add(colorItem = new CheckboxMenuItem("Color", color));
//      fileMenu.add(soundItem = new CheckboxMenuItem("Sound", sound));
      fileMenu.addSeparator();
      fileMenu.add(usersBestTimesItem = new MenuItem("Your Best Times..."));
      fileMenu.add(netBestTimesItem = new MenuItem("Minesweepers Best Times..."));
      fileMenu.addSeparator();
      fileMenu.add(exitItem = new MenuItem("Exit"));

      Menu helpMenu = new Menu("Help", true);
//      helpMenu.add(helpItem = new MenuItem("Help Topics..."));
      helpMenu.add(aboutItem = new MenuItem("About Minesweeper..."));

      mbar.add(fileMenu);
      mbar.add(helpMenu);
      mbar.setHelpMenu(helpMenu);

      frame.setMenuBar(mbar);

      newItem.addActionListener(menuItemListener);
      beginnerItem.addActionListener(menuItemListener);
      intermediateItem.addActionListener(menuItemListener);
      expertItem.addActionListener(menuItemListener);
      //customItem.addActionListener(menuItemListener);
      usersBestTimesItem.addActionListener(menuItemListener);
      netBestTimesItem.addActionListener(menuItemListener);
      exitItem.addActionListener(menuItemListener);

      marksItem.addItemListener(menuItemListener);
      colorItem.addItemListener(menuItemListener);
//      soundItem.addItemListener(menuItemListener);

//      helpItem.addActionListener(menuItemListener);
      aboutItem.addActionListener(menuItemListener);
   }

   static Frame getFrame(Component c) {
      Frame frame = null;
      while((c = c.getParent()) != null) {
         if(c instanceof Frame)
            frame = (Frame)c;
      }
      return frame;
   }


   public void menuEnablement(int selected_board) {
      MenuItem[] boardMenuItems = {beginnerItem, intermediateItem, expertItem};
      for (int i = BEGINNER; i <= EXPERT; i++) {
         if (i == selected_board)
            boardMenuItems[i].setEnabled(false);
         else if (!boardMenuItems[i].isEnabled())
            boardMenuItems[i].setEnabled(true);
      }
   }

   class MenuItemListener implements ActionListener, ItemListener  {
      public void itemStateChanged(ItemEvent event) {
         MenuItem item = (MenuItem) event.getSource();

         if (item == marksItem) {
            marks = !marks;
            board.toggleMarks();
         }
         else if (item == colorItem) {
            color = !color;
            board.toggleColor();
         }
/*
         else if (item == soundItem) {
            sound = !sound;
            board.toggleSound();
         }
*/
      }

      public void actionPerformed(ActionEvent event) {
         MenuItem item = (MenuItem) event.getSource();

         if (item == newItem) {
            board.newGame();
         }
         else if (item == beginnerItem) {
            swapToBoardType(BEGINNER);
         }
         else if (item == intermediateItem) {
            swapToBoardType(INTERMEDIATE);
         }
         else if (item == expertItem) {
            swapToBoardType(EXPERT);
         }
/*
         else if (item == customItem) {
            frame.setEnabled(false);
            CustomGame cg = new CustomGame(current_board_type, frame);
            frame.setEnabled(true);
            if (!cg.getCancelled()) {
System.out.println("custom Game\n width: " + cg.getWidth() + "\n height: " + cg.getHeight() + "\n mines: " + cg.getMines());
//               createCustomGame(cg.getWidth(), cg.getHeight(), cg.getMines());
            }
         }
*/
         else if (item == usersBestTimesItem) {
            frame.setEnabled(false);	//this may kill the UserName dlg
            high_scores.displayUsersHighScores();
            frame.setEnabled(true);
         }
         else if (item == netBestTimesItem) {
            frame.setEnabled(false);
            high_scores.displayNetHighScores();
            frame.setEnabled(true);
         }
         else if (item == exitItem) {
            frame.dispose();
         }
/*
         else if (item == helpItem) {
            frame.setEnabled(false);
            MessageBox mb = new MessageBox(frame, "Help has not yet been implemented");
            frame.setEnabled(true);
         }
*/
         else if (item == aboutItem) {
            frame.setEnabled(false);
            aboutMinesweeper = new AboutBox(frame, about_pic);
            frame.setEnabled(true);
         }
      }

      public void swapToBoardType(int board_type) {
         if (current_board_type != board_type) {
            board.swapBoardType(board_type);
            frame.pack();
            current_board_type = board_type;
            menuEnablement(board_type);
         }
      }

   }


   /**
    * Set the cookie for the current document
    *
   public void setCookie(String str) {
      java.util.Calendar c = java.util.Calendar.getInstance();
      c.add(java.util.Calendar.YEAR, 1);
      String expires = "; expires=" + c.getTime().toString();

      String cookie = str + expires;

      JSObject myBrowser = JSObject.getWindow(applet);

      JSObject myDocument =  (JSObject) myBrowser.getMember("document");

//      cookie = myDocument.call("escape", cookie);

      myDocument.setMember("cookie", cookie);
   }

   /**
    * Get all cookies for the current document
    *
   public String getCookie() {
      try {
         JSObject myBrowser = (JSObject) JSObject.getWindow(applet);
         JSObject myDocument =  (JSObject) myBrowser.getMember("document");
         String myCookie = (String)myDocument.getMember("cookie");
         if (myCookie.length() > 0)
//            myCookie = myDocument.call("unescape", myCookie);
            return myCookie;
         }
      catch (Exception e){
         System.err.println("Could not retrieve Cookie");
         e.printStackTrace();
      }
      return "";
   }
*/


   public Image loadImage(String img_url) {
      Image img = null;
      URL url = getClass().getResource(img_url);
      try {
         img = createImage((ImageProducer)url.getContent());
         if (img == null)
            System.out.println("null image: " + img_url);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return img;
   }

} //class Minesweeper
