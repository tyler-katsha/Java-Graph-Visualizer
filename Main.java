import javax.swing.JFrame;

public class Main{

  public static void main(String[] args){
    
    CustomPanel customPanel = new CustomPanel();

    JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(customPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
  }
}