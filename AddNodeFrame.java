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
  Random random = new Random();
  JLabel label;
  JTextArea areaNodeField = new JTextArea();
  JButton addButton;
  ArrayList<Node> nodes;
  public Color customDarkGray = new Color(43,45,44);
  private static int nodeWidth = 35;
  private static int nodeHeight = 35;
  private static final int tileSize = 40;
  char data;
  int WIDTH;
  int HEIGHT;
  CustomPanel customPanel;
  int size;
  Graph graph;
  AddNodeFrame(Graph graph,int WIDTH,int HEIGHT,CustomPanel customPanel,int size){
    this.graph = graph;
    this.nodes = graph.nodes;
    this.WIDTH = WIDTH;
    this.HEIGHT = HEIGHT;  
    this.customPanel = customPanel;
    this.size = size;
    label = new JLabel("Enter a character");
    addButton = new JButton("Add");

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
    addButton.setBackground(customDarkGray);
    addButton.setForeground(Color.white);
    add(addButton);
    setVisible(true);
  }

  public boolean overlaps(Node node){
    for(Node n:customPanel.graph.nodes){
      if(node.x == n.x && node.y == n.y){
        return true;
      }
    }
    return false;
    }

    public int[][] grow(int[][] matrix){
      int newRows = matrix.length * 2;
      int newCols = matrix.length * 2;

      int[][] newGrid = new int[newRows][newCols];

      for(int i = 0 ; i < matrix.length;i++){
        for(int j = 0 ; j < matrix[0].length;j++){
          newGrid[i][j] = matrix[i][j];
        }
      }

      return newGrid;
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
      for (Node node : customPanel.graph.nodes) {
        if (node.label == character) {
          JOptionPane.showMessageDialog(customPanel,"Node '" + character + "' already exists","Duplicate Node",JOptionPane.WARNING_MESSAGE);
        return;
    }
}
        int attempts = 0;
        Node newNode;

       if(graph.nodes.size() >= graph.matrix.length){
          graph.matrix = grow(graph.matrix);
       }
        do{
          int maxColumns = Math.max(1,customPanel.getWidth()/ CustomPanel.getTileSize());
          int maxRows = Math.max(1,customPanel.getHeight() / CustomPanel.getTileSize());

          int blockColumn = random.nextInt(maxColumns);
          int blockRow = random.nextInt(maxRows);

          int nodeX = (blockColumn * tileSize) + (tileSize / 2) - (nodeWidth/2);
          int nodeY = (blockRow * tileSize) + (tileSize / 2) - (nodeHeight/2);

          newNode = new Node(nodeX,nodeY,character,blockColumn,blockRow);
          attempts++;
          
        }while(overlaps(newNode) && attempts < 20);

      if(attempts == 20){
        JOptionPane.showMessageDialog(customPanel, "Could not place node (grid full)","Placement error",JOptionPane.ERROR_MESSAGE);
        return;
      }


      customPanel.graph.nodes.add(newNode);
      customPanel.repaint();
      dispose();

      System.out.println("\n\n");
      graph.print();
      
    }
  }
}
