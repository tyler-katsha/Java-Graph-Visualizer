public class Node {
  private final char data;
  private int x;
  private int y;
  private final int col;
  private final int row;
  private NodeState state;

  public Node(int x,int y,char data,int col,int row){
    this.x = x;
    this.y = y;
    this.data = data;
    this.col = col;
    this.row = row;
    state = NodeState.UNVISITED;
  }


  public NodeState getState(){
    return state;
  }

  public void setState(NodeState state) {
    this.state = state;
  }
  public char getData(){
    return data;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }
  public int getX() {
    return x;
  }

  public int getY(){
    return y;
  }

  public int getCol() {
    return col;
  }

  public int getRow(){
    return row;
  }
  
}
