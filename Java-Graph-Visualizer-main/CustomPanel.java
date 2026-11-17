import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class CustomPanel extends JPanel implements MouseListener,MouseMotionListener{

  private final static int SIZE = 6;
  private final static int TILE_WIDTH = 15;
  private final static int TILE_HEIGHT = 15;
  private final static int NODE_WIDTH = 35;
  private final static int NODE_HEIGHT = 35;
  private final static int tileSize = 40;

  private static int WIDTH = TILE_WIDTH*tileSize;
  private static int HEIGHT = TILE_HEIGHT*tileSize;

  private int animationDelay = 600;
  private volatile boolean isRunning = false;

  private final Graph graph;

  private GraphPreset graphPreset = GraphPreset.DEFAULT_HEX;
  private Node draggedNode = null;
  private int offSetX,offSetY;

  public CustomPanel(Graph graph){
    this.graph = graph;
  }

  public CustomPanel setGui(){
    setLayout(null);
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(WIDTH,HEIGHT));
    addMouseListener(this);
    addMouseMotionListener(this);

    PredefinedGraph.loadPreset(graph,GraphPreset.DEFAULT_HEX,WIDTH,HEIGHT);
    return this;
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
    drawNodes(g,graph.getNodes());
    drawMstWeight(g);
  }



  public static int getSizeG(){
    return SIZE;
  }
  public int getTileSize(){
    return tileSize;
  }
  public int getHeight(){
    return CustomPanel.HEIGHT;
  }
  public int getWidth(){
    return CustomPanel.WIDTH;
  }

  public Graph getGraph(){
    return graph;
  }
  private void drawNodes(Graphics g,ArrayList<Node> nodes){

    for(Node node:nodes){

      switch (node.getState()) {
        case PROCESSING -> g.setColor(Colors.customBlue);
        case PROCESSED -> g.setColor(Colors.customGreen);
        default -> g.setColor(Colors.customRed);
      }

      g.fillOval(node.getX(),node.getY(),NODE_WIDTH,NODE_HEIGHT);
    
      g.setColor(Color.WHITE);
      g.drawString(String.valueOf(node.getData()),node.getX() + (NODE_WIDTH / 2) - 4, node.getY() + (NODE_HEIGHT / 2) + 4);
    } 
  }
  private void drawEdge(Graphics g,Graph graph){

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    for(int i = 0 ; i < graph.getNodes().size(); i ++ ){
      for(int j = 0 ; j < graph.getNodes().size(); j++){
        if(graph.checkEdge(i, j)){

          Node from = graph.getNodes().get(i);
          Node to = graph.getNodes().get(j);

          int x1 = from.getX() + NODE_WIDTH / 2;
          int y1 = from.getY() + NODE_HEIGHT / 2;


          int x2 = to.getX() + NODE_WIDTH / 2;
          int y2 = to.getY() + NODE_HEIGHT / 2;

          int midpointX = (x1 + x2) / 2;
          int midpointY = (y1 + y2) / 2;


          if(graph.isMstEdge(i,j)) {
            g2d.setColor(Colors.customBrightGreen);
            g2d.setStroke(new BasicStroke(3.5f));
          } else {
            g2d.setColor(Colors.customGray);
            g2d.setStroke(new BasicStroke(2.0f));
          }

          g2d.drawLine(x1,y1,x2,y2);

          int w = graph.getEdgeWeight(i,j);

          String weightText = String.valueOf(w);

          FontMetrics fm = g2d.getFontMetrics();
          int textWidth = fm.stringWidth(weightText);
          int textHeight = fm.getAscent();

          int textX = midpointX - (textWidth / 2);
          int textY = midpointY - (textHeight / 4);

          g2d.setColor(Color.WHITE);
          g2d.drawString(weightText, textX,textY);
        }
      }
    }

  }
  private void drawGrid(Graphics g){

    for(int i = 0 ; i <= WIDTH/tileSize;i++) g.drawLine(i*tileSize,0,i*tileSize,HEIGHT);
    for(int i = 0 ; i <= HEIGHT/tileSize;i++)  g.drawLine(0, i*tileSize, WIDTH, i*tileSize);

  }
  private void drawMstWeight(Graphics g) {
    if(graph.getMstWeight() < 0) return;

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    String text = String.format("Total MST Weight: %d", graph.getMstWeight());

    Font originalFont = g2d.getFont();
    g2d.setFont(originalFont.deriveFont(Font.BOLD,16f));

    FontMetrics fm = g2d.getFontMetrics();
    int textWidth = fm.stringWidth(text);

    int topRightX = getWidth() - textWidth - 20;
    int topRightY = 30;
    g2d.setColor(Colors.customBrightGreen);
    g2d.drawString(text,topRightX,topRightY);

    g2d.setFont(originalFont);

  }

  public void setAnimationDelay(int delay) {
    this.animationDelay = delay;
  }

  public void resetNodes(){
    for (Node node: graph.getNodes()) {
      node.setState(NodeState.UNVISITED);
    }

    graph.clearMstEdges();

    repaint();
  }

  private void stepPause(){
    repaint();
    try {
      Thread.sleep(animationDelay);
    } catch(InterruptedException e) {
      String title = "Thread interrupted";
      String reason = e.getMessage();
      Popups.showError(reason,title);
    }

  }

  public void runAlgorithm(AlgorithmType type, int startNodeIndex) {
    if(isRunning || type == AlgorithmType.NONE) return;

    Runnable task = () -> {

      try {
        isRunning = true;
        resetNodes();
        stepPause();

        switch (type) {
          case BFS -> graph.bfsAnimated(startNodeIndex, this::stepPause);
          case DFS -> graph.dfsAnimated(startNodeIndex,this::stepPause);
          case MST_PRIM -> graph.primMSTAnimated(startNodeIndex,this::stepPause);
          case MST_KRUSKAL -> graph.kruskalsAnimated(this::stepPause);
          default -> {}
        }
      } finally {
        isRunning = false;
      }

    };
    Thread.ofVirtual().start(task);
  }

  public void loadPreset(GraphPreset preset) {
    if(isRunning) return;
    graph.clearMstEdges();
    this.graphPreset = preset;
    PredefinedGraph.loadPreset(graph,preset,WIDTH,HEIGHT);
    repaint();
  }

  @Override
  public void setBounds(int x, int y, int w, int h){
    super.setBounds(x, y, w, h);
    WIDTH = w;
    HEIGHT = h;
      
    PredefinedGraph.loadPreset(graph,graphPreset,WIDTH,HEIGHT);
  }

  @Override
  public void mouseEntered(MouseEvent e){}

  @Override
  public void mousePressed(MouseEvent e){

    int mouseX = e.getX();
    int mouseY = e.getY();

    for(Node node:graph.getNodes()){
      int nodeCenterX = node.getX() + NODE_WIDTH/2;
      int nodeCenterY = node.getY() + NODE_HEIGHT/2;
      double distance = Math.hypot(e.getX() - nodeCenterX,e.getY()-nodeCenterY);
      if(distance <= (double) NODE_WIDTH /2){
        draggedNode = node;
        offSetX = mouseX - node.getX();
        offSetY = mouseY - node.getY();
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
  public void mouseMoved(MouseEvent e) {}

@Override
public void mouseDragged(MouseEvent e) {
  if(draggedNode != null){
    draggedNode.setX(e.getX() - offSetX);
    draggedNode.setY(e.getY() - offSetY);
    repaint();
  }
}
}

