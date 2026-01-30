import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
public class CustomPanel extends JPanel{
  private int width = 600,height = 600, nodeWidth = 40, nodeHeight = 40;


  Graph graph = new Graph(5);
  char[] labels = {'A','B','C','D','E'};
  int x = 100;
  int y = 100;
  Color customRed = new Color(225,29,46);
  Color customGray = new Color(209,213,219);
  CustomPanel(){
    

    for(char label: labels){
      graph.addNode(new Node(x,y,label));

      
      if(x > width - 55){
        x = 100;
        y += 75;
      } else{
        x += 75;
      }
    }

    graph.addEdge(0,1);
    graph.addEdge(1,2);
    graph.addEdge(2,3);
    graph.addEdge(1,4);
    graph.addEdge(4,0);
    graph.addEdge(4,2);

    graph.print();

    layoutNodesCircle();
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(width,height));
    
  }

  @Override
  public void paintComponent(Graphics g){

    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g){
    drawEdge(g,graph);
    drawNodes(g,graph.nodes);
  }
  public void drawNodes(Graphics g,ArrayList<Node> nodes){


    for(Node node:nodes){
      g.setColor(customRed);
      g.fillOval(node.x,node.y,nodeWidth,nodeHeight);
    
      g.setColor(Color.WHITE);
      g.drawString(String.valueOf(node.label),node.x + (nodeWidth / 2) - 4, node.y + (nodeHeight / 2) + 4);


    }
    
    
  }
  public void drawEdge(Graphics g,Graph graph){
    g.setColor(customGray);
    for(int i = 0 ; i < graph.matrix.length; i ++ ){
      for(int j = 0 ; j < graph.matrix.length; j++){
        if(graph.checkEdge(i, j)){

          Node from = graph.nodes.get(i);
          Node to = graph.nodes.get(j);

          int x1 = from.x + nodeWidth / 2;
          int y1 = from.y + nodeHeight / 2;


          int x2 = to.x + nodeWidth / 2;
          int y2 = to.y + nodeHeight / 2;


          g.drawLine(x1,y1,x2,y2);
        } else{
          continue;
        }
      }
    }

  }

  public void layoutNodesCircle(){
    int centerX = width / 2;
    int centerY = height / 2;
    int radius = Math.min(width,height)/2 - 80;

    int n = graph.nodes.size();

    for(int i = 0; i < n; i++){
      double angle = 2 * Math.PI * i / n;

      int x = (int)(centerX + radius * Math.cos((angle))) - nodeWidth/2;
      int y = (int)(centerY + radius * Math.sin((angle))) - nodeHeight/2;

      graph.nodes.get(i).x = x;
      graph.nodes.get(i).y = y;
    }
  }

  @Override
  public void setBounds(int x, int y, int w, int h){
    super.setBounds(x, y, w, h);
    width = w;
    height = h;
    layoutNodesCircle();
  }
}
