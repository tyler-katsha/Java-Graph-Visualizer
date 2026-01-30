import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main{

  public static void main(String[] args){
    
    Graph graph = new Graph(CustomPanel.size);
    CustomPanel customPanel = new CustomPanel(graph);
    
    ControlPanel controlPanel = new ControlPanel(customPanel,graph);
    JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        frame.setLayout(new BorderLayout());

        frame.add(customPanel,BorderLayout.CENTER);
        frame.add(controlPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
  }
}