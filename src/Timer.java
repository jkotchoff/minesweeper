import java.awt.Image;
/**
 * This class extends NumberFlipper representing a timer clock for the
 * game in progress.  It handles all the timing of Minesweeper games.
 *
 * <P>This class is run on a separate thread to the main program allowing
 * it to increment the clock (timer) at regular intervals (also allowing
 * controlling methods to pause, reset and restart the clock).
 *
 * @author Jason Kotchoff
 */

public class Timer extends NumberFlipper
                           implements Runnable {
   private Thread number_swapper = null;
   private boolean thread_suspended;
   private final int ONESECOND = 1000;

   /**
    * Constructs a new Timer object (initialising the time to '000' and
    * pausing the running of the clock until the start methods have been
    * called).
    */
   public Timer(Image[] color_timer_images, Image[] bw_timer_images, boolean color) {
      super(color_timer_images, bw_timer_images, color);
      counter = default_value = 0;
      reset();
   }

   /**
    * Kills and disposes of the thread that the clock runs on.
    */
   public void destroy() {
      //make sure thread exists before trying to stop it
      if (number_swapper != null)
         number_swapper.destroy();
      //throw away thread object
      number_swapper = null;
   }

   /**
    * This method increments the counter and calls flipImage
    * to swap the images. it is called by the run() method.
    */
   private void incrementTimer() {
      if (counter < 999) {
         counter++;
         setCounter();
      }
   }

   /**
    * This method stops the thread (thereby leaving the current
    * time taken at a stand-still).  The clock is paused when either
    * the game is won(ie. by revealing all blocks and numbers), or
    * lost (ie. by revealing a bomb)
    */
   public int pauseClock() {
      thread_suspended = true;
      return counter;
   }

   /**
    * This method ensures the thread controlling the clock only
    * runs when the game is playing, and it increments the timer
    * evey second
    */
   public void run() {
      while (true) {
         try {
            number_swapper.sleep(ONESECOND);

            if (thread_suspended) {
               synchronized(this) {
                  while (thread_suspended) {
                     wait();
                  }
               }
            }
         } catch (InterruptedException e) {System.err.println("Clock thread interrupted");}
         incrementTimer();
      }
   }

   /**
    * This is the default method for constructing the thread
    */
   public void start() {
      //make sure thread object has been created before trying to start it
      if (number_swapper == null)
         number_swapper = new Thread(this);
      number_swapper.start();
   }

   /**
    * This method starts the clock running (it swaps images every
    * second.  999 is the maximum number, and when the clock reaches
    * it, the thread is paused until it gets started again (when a
    * new game is started).
    */
   public synchronized void startClock() {
      if (thread_suspended)
         notify();
      thread_suspended = false;
   }

} //class Timer
