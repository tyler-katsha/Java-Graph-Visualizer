import java.util.*;

import static java.lang.IO.print;
import static java.lang.IO.println;


public class Graph {
  
  private ArrayList<Node> nodes;
  private int[][] matrix;
  private boolean[][] mstEdges;
  private int mstWeight = -1;
  private List<Integer> output;


  public Graph(int size){
    nodes = new ArrayList<>();
    matrix = new int[size][size];
    output = new ArrayList<>();
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

  public int getMstWeight(){
    return mstWeight;
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

  public boolean isMstEdge(int i,int j) {
    if(mstEdges == null || i >= mstEdges.length || j >= mstEdges.length) return false;
    return mstEdges[i][j];
  }
  public void clearMstEdges(){
    if(matrix != null) mstEdges = new boolean[matrix.length][matrix.length];
    mstWeight = -1;
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

  public int getOutputSize() {
    return output.size();
  }

  public String getOutput() {
    if(this.output.isEmpty()) return "[]";

    int V = output.size();
    StringBuilder arr = new StringBuilder("[");

    for (int i = 0; i < V;i++) {
      int ele = output.get(i);
      if(i == V - 1) arr.append(ele);
      else arr.append(ele).append(", ");
    }
    return arr.append("]").toString();
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

  public void primMSTAnimated(int src, Runnable onStep) {
    if(nodes.isEmpty()) return;

    int V = nodes.size();
    mstEdges = new boolean[matrix.length][matrix.length];
    mstWeight = 0;
    onStep.run();

    PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> Integer.compare(a[0],b[0]));

    boolean[] visited = new boolean[V];

    addToIntArr(pq,0,src,-1);

    while(!pq.isEmpty()) {
      int[] p = pq.poll();
      int wt = p[0];
      int u = p[1];
      int parent = p[2];

      if(visited[u]) continue;

      visited[u] = true;
      this.mstWeight += wt;

      if(parent != -1) {
        mstEdges[parent][u] = true;
        mstEdges[u][parent] = true;
      }

      nodes.get(u).setState(NodeState.PROCESSING);
      onStep.run();

     for(int v = 0; v < V;v++) {
       int edgeWeight = matrix[u][v];
       if(!visited[v] && edgeWeight != 0) {
         addToIntArr(pq,edgeWeight,v,u);
       }
     }

     nodes.get(u).setState(NodeState.PROCESSED);
     onStep.run();
    }

    removeUnmarkedLabels(V,visited,onStep);
  }

  private void addToIntArr(PriorityQueue<int[]> pq,int weight,int current,int parent){
    pq.add(new int[]{weight, current,parent});
  }

  public void dijkstraAnimated(int src, Runnable onStep){
    if(nodes.isEmpty()) return;

    int V = nodes.size();
    int[] dist = new int[V];
    int[] parent = new int[V];
    boolean[] visited = new boolean[V];

    Arrays.fill(dist,Integer.MAX_VALUE);
    Arrays.fill(parent,-1);

    clearMstEdgeAndOutput();

    dist[src] = 0;
    onStep.run();

    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    pq.offer(new int[]{0 , src});

    while(!pq.isEmpty()) {
      int[] curr = pq.poll();
      int d = curr[0];
      int u = curr[1];

      if(visited[u]) continue;
      visited[u] = true;
      output.add(d);


      if(parent[u] != -1) {
        mstEdges[parent[u]][u] = true;
        mstEdges[u][parent[u]] = true;
      }

      nodes.get(u).setState(NodeState.PROCESSING);
      onStep.run();

      for (int v = 0; v < V; v++){
        int edgeWeight = matrix[u][v];
        if(!visited[v] && edgeWeight > 0) {
          int newDist = d + edgeWeight;
          if(newDist < dist[v]) {
            dist[v] = newDist;
            parent[v] = u;
            pq.offer(new int[]{newDist,v});
          }
        }
      }

      nodes.get(u).setState(NodeState.PROCESSED);
      onStep.run();


    }
    removeUnmarkedLabels(V,visited,onStep);
  }

  public void kruskalsAnimated(Runnable onStep){
    if(nodes.isEmpty()) return;

    int V = nodes.size();

    mstEdges = new boolean[matrix.length][matrix.length];
    this.mstWeight = 0;
    onStep.run();

    List<int[]> edges = new ArrayList<>();

    for(int i = 0; i < V;i++) {
      for (int j = i + 1 ; j < V;j++) {
        if(matrix[i][j] != 0) edges.add(new int[]{matrix[i][j], i, j});
      }
    }

    edges.sort(Comparator.comparingInt(a -> a[0]));

    DSU dsu = new DSU(V);
    boolean[] inMst = new boolean[V];
    int edgesIncluded = 0;

    for(int[] edge: edges) {
      int wt = edge[0];
      int u = edge[1];
      int v = edge[2];

      NodeState prevStateU = nodes.get(u).getState();
      NodeState prevStateV = nodes.get(v).getState();

      nodes.get(u).setState(NodeState.PROCESSING);
      nodes.get(v).setState(NodeState.PROCESSING);
      onStep.run();

      if(dsu.find(u) != dsu.find(v)) {
        dsu.union(u,v);

        this.mstWeight += wt;

        mstEdges[u][v] = true;
        mstEdges[v][u] = true;
        inMst[u] = true;
        inMst[v] = true;


        nodes.get(u).setState(NodeState.PROCESSED);
        nodes.get(v).setState(NodeState.PROCESSED);
        onStep.run();

        edgesIncluded++;
        if(edgesIncluded == V - 1) break;
      } else {
        nodes.get(u).setState(prevStateU);
        nodes.get(v).setState(prevStateV);
        onStep.run();
      }
    }

    removeUnmarkedLabels(V,inMst,onStep);
  }

  private void removeUnmarkedLabels(int V, boolean[] set, Runnable onStep){
    for (int i =0 ;i < V; i++) {
      for(int j = 0; j < V;j++) {
        if(!mstEdges[i][j]) matrix[i][j] = 0;
      }
    }

    List<Character> unreachableLabels = new ArrayList<>();

    for(int i =0 ; i < V;i++) {
      if(!set[i]) {
        unreachableLabels.add(nodes.get(i).getData());
      }
    }

    for(char label: unreachableLabels) {
      deleteNode(label);
    }

    int remaining = nodes.size();
    mstEdges = new boolean[matrix.length][matrix.length];

    for (int i =0; i < remaining;i++) {
      for (int j = 0; j < remaining;j++) {
        if(matrix[i][j] != 0) {
          mstEdges[i][j] = true;
        }

      }
    }
    onStep.run();
  }

  private void clearMstEdgeAndOutput(){
    if (matrix != null) mstEdges = new boolean[matrix.length][matrix.length];
    mstWeight = -1;
    output.clear();

  }
}
