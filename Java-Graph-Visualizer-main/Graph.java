import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.IO.print;
import static java.lang.IO.println;


public class Graph {
  
  private ArrayList<Node> nodes;
  private int[][] matrix;


  public Graph(int size){
    nodes = new ArrayList<>();
    matrix = new int[size][size];
  }

  public void setMatrix(int[][] matrix) {
    this.matrix = matrix;
  }

  public void setNodes(ArrayList<Node> nodes){
    this.nodes = nodes;
  }
  public ArrayList<Node> getNodes() {
    return nodes;
  }

  public int[][] getMatrix() {
    return matrix;
  }

  public void addNode(Node node){
    nodes.add(node);
  }

  public void addEdge(int src,int dst, int weight){
    matrix[src][dst] = weight;
    matrix[dst][src] =  weight;
  }

  public boolean checkEdge(int src,int dst){
    return matrix[src][dst] != 0;
  }
  public int getEdgeWeight(int src,int dst) {
    return matrix[src][dst];
  }
  public void printG(){
    for(int i = 0 ; i < nodes.size();i++){
      Node node = nodes.get(i);
      print(node.getData() + " -> ");
      for(int j = 0 ; j < nodes.size();j++){
        if(matrix[i][j] != 0){
          print(j + " (w: " + matrix[i][j] + ") -> ");
        }
      }
      println();
    }
  }

  public int[][] grow(int[][] matrix){
    int newRows = matrix.length * 2;
    int newCols = matrix.length * 2;

    int[][] newGrid = new int[newRows][newCols];

    for(int i = 0 ; i < matrix.length;i++){
      System.arraycopy(matrix[i], 0, newGrid[i], 0, matrix[0].length);
    }

    return newGrid;
  }
  public boolean deleteNode(char label){

    int index = -1;

    for(int i = 0 ; i < nodes.size();i++){
      if(nodes.get(i).getData() == label){
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


    public int search(char label) {
        int index = -1;

      for(int i = 0 ; i < nodes.size();i++){
        if(nodes.get(i).getData() == label){
          index = i;
          break;
        }
      }

      return index;
    }

    public boolean searchBool(char label) {
        for (Node node : nodes) {
            if (node.getData() == label) {
                return true;
            }
        }

      return false;
    }


    private void dfsHelper(int src, boolean[] visited,Runnable onStep) {
        if(visited[src]) return;

        visited[src] = true;

        nodes.get(src).setState(NodeState.PROCESSING);
        onStep.run();
        for (int i = 0; i < matrix[src].length; i++) {
          if(!visited[i] && matrix[src][i] != 0) dfsHelper(i,visited,onStep);
        }

      nodes.get(src).setState(NodeState.PROCESSED);
      onStep.run();
    }

  public void bfsAnimated(int src, Runnable onStep) {
    if(nodes.isEmpty()) return;

    Queue<Integer> queue = new LinkedList<>();

    boolean[] visited = new boolean[nodes.size()];

    queue.offer(src);
    visited[src] = true;

    nodes.get(src).setState(NodeState.PROCESSING);
    onStep.run();

    while(!queue.isEmpty()) {
      int current = queue.poll();

      nodes.get(current).setState(NodeState.PROCESSING);
      onStep.run();

      for (int i = 0; i < matrix[src].length;i++) {
        if(!visited[i] && matrix[current][i] != 0) {
          queue.offer(i);
          visited[i] = true;

          nodes.get(i).setState(NodeState.PROCESSING);
          onStep.run();
        }


      }

      nodes.get(current).setState(NodeState.PROCESSED);
      onStep.run();

    }
  }

  public void dfsAnimated(int src, Runnable onStep) {
    if(nodes.isEmpty()) return;
    boolean[] visited = new boolean[nodes.size()];
    dfsHelper(src,visited,onStep);

  }
}
