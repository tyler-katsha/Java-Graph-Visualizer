import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class AddNodeFrame extends JFrame implements ActionListener{

  private final static int NODE_WIDTH = 35;
  private final static int NODE_HEIGHT = 35;
  private final static int TILE_SIZE = 40;

  private final Random random;
  private final JLabel label;
  private final JTextArea areaNodeField;
  private final JButton addButton;
  private final CustomPanel customPanel;
  private final Graph graph;

  public AddNodeFrame(Graph graph,CustomPanel customPanel){
    this.graph = graph;
    this.customPanel = customPanel;
    this.random = new Random();
    areaNodeField = new JTextArea();
    label = new JLabel("Enter a character");
    addButton = new JButton("Add");

  }

  public AddNodeFrame setGui(){

      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(300,250);
      setLocationRelativeTo(null);
      setLayout(null);

      label.setBounds(300/2 - 72, 20, 200, 25);
      label.setFont(new Font("Arial",Font.PLAIN,22));
      areaNodeField.setBounds(300/2 - 45, 65, 80, 25);
      areaNodeField.setFont(new Font("Arial",Font.PLAIN,15));



      addButton.setBounds(300/2 - 45, 100, 80, 30);
      addButton.addActionListener(this);
      add(label);
      add(areaNodeField);

      addButton.setFocusable(false);
      addButton.setBackground(Colors.customDarkGray);
      addButton.setForeground(Color.white);
      add(addButton);
      setVisible(true);

      return this;
  }

  public boolean overlaps(Node node){
    for(Node n:customPanel.getGraph().getNodes()){
      if(node.getX() == n.getX() && node.getY() == n.getY()){
        return true;
      }
    }
    return false;
    }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == addButton){

      String text = areaNodeField.getText().toUpperCase();

      if(text.length() != 1){
        
        JOptionPane.showMessageDialog(customPanel, "Please enter exactly one character", "Invalid input", JOptionPane.ERROR_MESSAGE);
          
        return;
      }
      char character = text.charAt(0);
      for (Node node : customPanel.getGraph().getNodes()) {
        if (node.getData() == character) {
          JOptionPane.showMessageDialog(customPanel,"Node '" + character + "' already exists","Duplicate Node",JOptionPane.WARNING_MESSAGE);
        return;
    }
}
        int attempts = 0;
        Node newNode;
       if(graph.getNodes().size() >= graph.getMatrix().length) graph.setMatrix(graph.grow(graph.getMatrix()));

       do{
          int maxColumns = Math.max(1,customPanel.getWidth()/ customPanel.getTileSize());
          int maxRows = Math.max(1,customPanel.getHeight() / customPanel.getTileSize());

          int blockColumn = random.nextInt(maxColumns);
          int blockRow = random.nextInt(maxRows);

          int nodeX = (blockColumn * TILE_SIZE) + (TILE_SIZE / 2) - (NODE_WIDTH/2);
          int nodeY = (blockRow * TILE_SIZE) + (TILE_SIZE / 2) - (NODE_HEIGHT/2);

          newNode = new Node(nodeX,nodeY,character,blockColumn,blockRow);
          attempts++;
          
        }while(overlaps(newNode) && attempts < 20);

      if(attempts == 20){
        JOptionPane.showMessageDialog(customPanel, "Could not place node (grid full)","Placement error",JOptionPane.ERROR_MESSAGE);
        return;
      }


      customPanel.getGraph().getNodes().add(newNode);
      customPanel.repaint();
      dispose();

      System.out.println("\n\n");
      graph.printG();
      
    }
  }
}
