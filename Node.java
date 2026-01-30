public class Node {
  char label;
  int x;
  int y;

  public Node(char label){
    this.label = label;
    this.x = 0;
    this.y = 0;
  }
  public Node(int x,int y,char label){
    this.x = x;
    this.y = y;
    this.label = label;
  }
}
