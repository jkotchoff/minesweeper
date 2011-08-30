import java.awt.event.*;
import java.net.URL;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.Dimension;
import java.applet.Applet;
/**
 * This class creates a button that allows the user to start a new game
 * at any time.  It has it's own MouseListener that directly handles all
 * funtionality of the button (pressing, releasing, entering, exiting etc.).
 *
 * @author Jason Kotchoff
 */

public class Face extends JKButton {
   public static final int WIDTH = 26;
   public static final int HEIGHT = 26;

   protected static final int LEFT_CLICK = 16;
   private boolean face_showing; 		//specifies whether the face is in it's initial state
   private boolean game_over = false; 		//used to ensure that a special face is restored when the mouse exits & enters again after the game is finished
   private boolean game_won = false; 		//used to decide whether to show the game won or game lost face
   private boolean left_clicked = false; 	//used to ensure that correct face is shown when mouseexited
   private boolean left_clicked_2 = false; 	//used to ensure highlighted/unhighlighted state when mouse enters/exits
   private boolean color;

   private static final int FACE = 0, FACE_HIGHLIGHTED = 1, FACE_SURPRISED = 2, FACE_UNHAPPY = 3, FACE_GAMEWON = 4;
   private String[] face_urls = {"face.gif", "face_highlighted.gif", "face_surprised.gif", "face_unhappy.gif", "face_gamewon.gif"};
   private Image[] color_face_images;
   private Image[] bw_face_images;
   private Image[] images;
   private int current_icon;

   private MouseMotionAdapter exitListener;
   private boolean on_block = false; //IE hack which will allow mouse events to fire when mouse is pressed
   private String browser;

   /**
    * Constructs a new Face object.
    * @param color Specifies if Color is enabled for the Face
    * @param applet The Minesweeper Applet
    */
   public Face(boolean color, Minesweeper applet) {
      browser = applet.getBrowser();

      this.color = color;
      face_showing = false;

      color_face_images = new Image[face_urls.length];
      bw_face_images = new Image[face_urls.length];

      for (int x = 0; x < face_urls.length; x++) {
         color_face_images[x] = loadImage(face_urls[x]);
         bw_face_images[x] = loadImage("bw_" + face_urls[x]);
      }

      if (color)
         images = color_face_images;
      else
         images = bw_face_images;

      current_icon = FACE;
      setIcon(images[current_icon]);
   }

   /**
    * Sets the preferred size of this Component
    */
   public Dimension getPreferredSize() {
      return new Dimension(WIDTH, HEIGHT);
   }

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

   /**
    * Same as showNormal() however this is only called when a new
    * game is started. It resets the game variables in the face.
    */
   public void newGame() {
      current_icon = FACE;
      setIcon(images[current_icon]);
      face_showing = true;
      game_over = false;
      game_won = false;
   }

   /**
    * Shows a 'game won' image (called when the board is completed
    * correctly).
    */
   public void showGameWon() {
      current_icon = FACE_GAMEWON;
      setIcon(images[current_icon]);
      game_over = true;
      game_won = true;
   }

   /**
    * Shows the default face.
    */
   public void showNormal() {
      if (!game_over) {
         current_icon = FACE;
         setIcon(images[current_icon]);
         face_showing = true;
      }
   }

   /**
    * Shows the surprised face.  This method is called whenever a user clicks
    * anywhere within the applet.
    */
   public void showSurprised() {
      if (!game_over) {
         current_icon = FACE_SURPRISED;
         setIcon(images[current_icon]);
         face_showing = false;
      }
   }

   /**
    * Shows the unhappy face.  This is called when a bomb is clicked
    * on.
    */
   public void showUnhappy() {
      current_icon = FACE_UNHAPPY;
      setIcon(images[current_icon]);
      game_over = true;
      face_showing = false;
   }

   /**
    * Toggles the color mode of this number image between color
    * and black and white
    */
   public void toggleColor() {
      color = !color;
      if (color)
         images = color_face_images;
      else
         images = bw_face_images;
      setIcon(images[current_icon]);
   }


   public void mouseClicked(MouseEvent e) {;}

   public void mousePressed(MouseEvent e) {
      if (browser != Minesweeper.APPLETVIEWER) {
         //HACK 	to handle IE's event handling which (unlike appletviewer) does NOT
         //	fire a mouseExited event whilst the mouse is pressed
         on_block = true;

         addMouseMotionListener(exitListener = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
               int x = event.getX();
               int y = event.getY();

               if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) { //outside this block
                  if (on_block) {
                     on_block = false;
                     mouseExited(event);
                  }
               } else {
                  if (!on_block) {
                     on_block = true;
                     mouseEntered(event);
                  }
               }
            }
         });
         //end HACK
      }

      if (e.getModifiers() == LEFT_CLICK) {
         current_icon = FACE_HIGHLIGHTED;
         setIcon(images[current_icon]);
         face_showing = false;
         left_clicked = true;
         left_clicked_2 = true;
      }
   }

   public void mouseReleased(MouseEvent e) {
      //HACK 	to handle IE's event handling which (unlike appletviewer) does NOT
      //	fire a mouseExited event whilst the mouse is pressed
      if (exitListener != null) {
         removeMouseMotionListener(exitListener);
         exitListener = null;
      }
      //end HACK

      if (e.getModifiers() == LEFT_CLICK) {
         left_clicked_2 = false;
         if (left_clicked) {
            left_clicked = false;
            newGame();
            ((MinesweeperPane)getParent()).newGame();
         }
      }
   }

   public void mouseEntered(MouseEvent e) {
      if (left_clicked_2) {
         left_clicked = true;
         face_showing = false;
         current_icon = FACE_HIGHLIGHTED;
         setIcon(images[current_icon]);
      }
   }

   public void mouseExited(MouseEvent e) {
      left_clicked = false;
      if (!game_over) {
         if (!face_showing) {
            current_icon = FACE;
            setIcon(images[current_icon]);
            face_showing = true;
         }
      }
      else {
         if (game_won) {
            current_icon = FACE_GAMEWON;
            setIcon(images[current_icon]);
         }
         else {
            current_icon = FACE_UNHAPPY;
            setIcon(images[current_icon]);
         }
      }
   }

} //class Face


