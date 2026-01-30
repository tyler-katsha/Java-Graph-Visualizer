import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class CustomPanel extends JPanel implements MouseListener,MouseMotionListener{
  private static int tileWidth = 15;
  private static int tileHeight = 15;
  private static int nodeWidth = 35;
  private static int nodeHeight = 35;
  private static final int tileSize = 40;
  private static int WIDTH = tileWidth*tileSize;
  private static int HEIGHT = tileHeight*tileSize;

  Graph graph;
  AddNodeFrame anf;
  ArrayList<Character> labels;
  
  private Node draggedNode = null;
  private int offSetX,offSetY;
  public static int size = 6;
  public Color customRed = new Color(225,29,46);
  public Color customGray = new Color(209,213,219);
  public Color customDarkGray = new Color(43,45,44);
  Random random = new Random();

  
  CustomPanel(Graph graph){
    this.graph = graph;

    labels = new ArrayList<>();
    labels.add('A');
    labels.add('B');
    labels.add('C');
    labels.add('D');
    labels.add('E');
    labels.add('F');


    for(char label: labels){
      int cols = WIDTH / tileSize;
      int rows = HEIGHT / tileSize;

      int blockColumn = random.nextInt(cols);
      int blockRow = random.nextInt(rows);

      int nodeX = (blockColumn * tileSize) + (tileSize / 2) - (nodeWidth/2);
      int nodeY = (blockRow * tileSize) + (tileSize / 2) - (nodeHeight/2);
      
      graph.addNode(new Node(nodeX,nodeY,label,blockColumn,blockRow));

    }


    graph.addEdge(0,1);
    graph.addEdge(1,2);
    graph.addEdge(1,3);
    graph.addEdge(1,4);
    graph.addEdge(4,0);
    graph.addEdge(0,2);
    graph.addEdge(5,2);
    graph.print();

    layoutNodesCircle();
    setLayout(null);

      
    
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(WIDTH,HEIGHT));
    addMouseListener(this);
    addMouseMotionListener(this);
    
  }

  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    draw(g);
  }

  public void draw(Graphics g){
    //drawGrid(g);
    drawEdge(g,graph);
    drawNodes(g,graph.nodes);
  }
  public static int getTileSize(){
    return tileSize;
  }
  public int getHeight(){
    return CustomPanel.HEIGHT;
  }
  public int getWidth(){
    return CustomPanel.WIDTH;
  }
  public static int getNodeWidth(){
    return nodeWidth;
  }
  public static int getNodeHeight(){
    return nodeHeight;
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
    for(int i = 0 ; i < graph.nodes.size(); i ++ ){
      for(int j = 0 ; j < graph.nodes.size(); j++){
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
  public void drawGrid(Graphics g){

    for(int i = 0 ; i <= WIDTH/tileSize;i++) g.drawLine(i*tileSize,0,i*tileSize,HEIGHT);
    for(int i = 0 ; i <= HEIGHT/tileSize;i++)  g.drawLine(0, i*tileSize, WIDTH, i*tileSize);

  }
  public void layoutNodesCircle(){
    int centerX = WIDTH / 2;
    int centerY = HEIGHT / 2;
    int radius = Math.min(WIDTH,HEIGHT)/2 - 80;

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
    WIDTH = w;
    HEIGHT = h;
      
    layoutNodesCircle();
  }

  @Override
  public void mouseEntered(MouseEvent e){}

  @Override
  public void mousePressed(MouseEvent e){

    int mouseX = e.getX();
    int mouseY = e.getY();

    for(Node node:graph.nodes){
      int nodeCenterX = node.x + nodeWidth/2;
      int nodeCenterY = node.y + nodeHeight/2;
      double distance = Math.hypot(e.getX() - nodeCenterX,e.getY()-nodeCenterY);
      if(distance <= nodeWidth/2){
        draggedNode = node;
        offSetX = mouseX - node.x;
        offSetY = mouseY - node.y;
        break;
      }
      
      
    }
  }

  @Override
  public void mouseExited(MouseEvent e){}

  @Override
  public void mouseClicked(MouseEvent e){}

  @Override
  public void mouseReleased(MouseEvent e){
    draggedNode = null;  
  }
  

@Override
public void mouseDragged(MouseEvent e) {
  if(draggedNode != null){
    draggedNode.x = e.getX() - offSetX;
    draggedNode.y = e.getY() - offSetY;

    repaint();
  }
}

@Override
public void mouseMoved(MouseEvent e) {}

}

