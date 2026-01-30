import java.util.ArrayList;


public class Graph {
  
  ArrayList<Node> nodes;
  int[][] matrix;


  Graph(int size){
    nodes = new ArrayList<>();
    matrix = new int[size][size];
  }

  public void addNode(Node node){
    nodes.add(node);
  }
  public void addEdge(int src,int dst){
    matrix[src][dst] = 1;
  }
  public boolean checkEdge(int src,int dst){
    if(matrix[src][dst] == 1){
      return true;
    }
    return false;
  }
  public void print(){
    for(int i = 0 ; i < nodes.size();i++){
      Node node = nodes.get(i);
      System.out.print(node.label + ": ");
      for(int j = 0 ; j < nodes.size();j++){
        if(matrix[i][j] != 0){
          System.out.print(j + " ");
        }
      }
      System.out.println();
    }
  }
  public boolean deleteNode(char label){

    int index = -1;

    for(int i = 0 ; i < nodes.size();i++){
      if(nodes.get(i).label == label){
        index = i;
        break;
      }
    }

    if(index == -1) return false;

    nodes.remove(index);

    //shrink matrix
    int n = matrix.length - 1;
    int[][] newMatrix = new int[n][n];

    int row = 0;
    for(int i = 0 ; i < matrix.length;i++){
      if(i == index) continue;
      int col = 0;
      for(int j = 0 ; j < matrix.length;j++){
        if(j == index) continue;
        newMatrix[row][col] = matrix[i][j];
        col++;
      }
      row++;
    }

    matrix = newMatrix;
    return true;
  }
}
