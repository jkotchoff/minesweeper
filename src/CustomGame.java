import java.awt.*;
import java.awt.event.*;

class CustomGame extends JKDialog {
   private int width, height, number_bombs;
   private TextField height_field;
   private TextField width_field;
   private TextField mines_field;
   private boolean cancelled = false;

   public CustomGame(int current_board_type, Frame parent) {
      super(parent, "Custom Game", true, false);

      switch (current_board_type) {
         case (Minesweeper.BEGINNER):
            width = Board.BEGINNER_WIDTH;
            height = Board.BEGINNER_HEIGHT;
            number_bombs = Board.BEGINNER_BOMBS;
            break;
         case (Minesweeper.INTERMEDIATE):
            width = Board.INTERMEDIATE_WIDTH;
            height = Board.INTERMEDIATE_HEIGHT;
            number_bombs = Board.INTERMEDIATE_BOMBS;
            break;
         case (Minesweeper.EXPERT):
            width = Board.EXPERT_WIDTH;
            height = Board.EXPERT_HEIGHT;
            number_bombs = Board.EXPERT_BOMBS;
            break;
         default:
            ;
      }

      initialiseStuff();
   }

   public CustomGame(Frame parent, int width, int height, int mines) {
      super(parent, "Custom Game", true, false);

      this.width = width;
      this.height = height;
      this.number_bombs = mines;

      initialiseStuff();
   }

   public boolean getCancelled() {
      return cancelled;
   }

   public int getHeight() {
      return Integer.parseInt(height_field.getText());
   }

   public int getMines() {
      return Integer.parseInt(mines_field.getText());
   }

   public int getWidth() {
      return Integer.parseInt(width_field.getText());
   }

   public boolean validateFields() {
      boolean valid_data = true;
      TextField fields[] = new TextField {height_field, width_field, mines_field};
      for (int i = 0; i < fields.length; i++) {
         try {
            Integer.parseInt(fields[i].getText());
         } catch (NumberFormatException nfe) {
            valid_data = false;
            fields[i].
         }
      }
      return valid_data;
   }

   public void initialiseStuff() {

      Panel fields = new Panel();
      fields.setLayout(new GridLayout(3, 2));
      fields.add(new Label("Height:"));
      fields.add(height_field = new TextField(Integer.toString(height), 3));
      fields.add(new Label("Width:"));
      fields.add(width_field = new TextField(Integer.toString(width), 3));
      fields.add(new Label("Mines:"));
      fields.add(mines_field = new TextField(Integer.toString(number_bombs), 3));

      Panel buttons = new Panel();
      buttons.setLayout(new GridLayout(2, 1, 0, 10));
      Button okButton = new Button("OK");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            if (validateFields())
               closeDialog();
         }
      });
      buttons.add(okButton);
      Button cancelButton = new Button("Cancel");
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            cancelled = true;
            closeDialog();
         }
      });
      buttons.add(cancelButton);

      surface.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
      surface.add(fields);
      surface.add(buttons);

      height_field.selectAll();

      setVisible(true);
   }
}

