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

  public void aStarAnimated(int[][] aStarGrid, int[] src, int[] dest, Runnable onStep) {

    int ROW = aStarGrid.length;
    int COL = aStarGrid[0].length;
    boolean isFound = false;
    if(!AStarHelper.isValid(src[0],src[1],ROW,COL) || !AStarHelper.isValid(dest[0],dest[1], ROW,COL)) {
      Popups.showWarning("Source or Destination is invalid","Invalid I/O");
      return;
    }

    if(!AStarHelper.isUnblocked(aStarGrid,src[0],src[1]) || !AStarHelper.isUnblocked(aStarGrid,dest[0],dest[1])) {
      Popups.showWarning("Source or Destination is blocked","Blocked I/O");
      return;
    }

    if(AStarHelper.isDestination(dest, src[0],src[1])) {
      Popups.showInfo("We are already at then destination", "Destination");
      return;
    }

    clearMstEdgeAndOutput();

    boolean[][] closedList = new boolean[ROW][COL];
    Cell[][] cellDetails = new Cell[ROW][COL];

    for (int i = 0; i < ROW;i++) {
      for(int j = 0; j < COL;j++) {

        double inf = Double.POSITIVE_INFINITY;
        cellDetails[i][j] = new Cell.CellBuilder()
                .parentI(-1)
                .parentJ(-1)
                .f(inf)
                .g(inf)
                .h(inf)
                .build();

      }
    }

    int i = src[0];
    int j = src[1];

    cellDetails[i][j].setF(0);
    cellDetails[i][j].setH(0);
    cellDetails[i][j].setG(0);
    cellDetails[i][j].setParentI(i);
    cellDetails[i][j].setParentJ(j);

    PriorityQueue<double[]> openList = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));

    openList.offer(new double[]{0 , i , j});

    while(!openList.isEmpty()) {

      double[] p = openList.poll();
      i = (int) p[1];
      j = (int) p[2];

      if(closedList[i][j]) continue;
      closedList[i][j] = true;

      int nodeIdx = searchByCoords(i,j);

      if(nodeIdx != -1) {
        nodes.get(nodeIdx).setState(NodeState.PROCESSING);
        onStep.run();
      }

      int[][] directions = { {-1,0},  // North
                             {1,0},   // South
                             {0,-1},  // West
                             {0,1},   // East
                             {-1,-1}, // North-West
                             {-1,1},  // North-East
                             {1,1},   // South-East
                             {1, -1}  // South-West
                            };

      for(int[] dir : directions) {
        int newI = i + dir[0];
        int newJ = j + dir[1];
        isFound = AStarHelper.processNeighbour(i,j,newI,newJ,aStarGrid,cellDetails,closedList,openList,dest,ROW,COL);

        if(isFound){
          if(nodeIdx != -1) nodes.get(nodeIdx).setState(NodeState.PROCESSED);
          highlightAStarPath(cellDetails,dest,onStep);
          return;
        }
      }

      if(nodeIdx != -1) {
        nodes.get(nodeIdx).setState(NodeState.PROCESSED);
        onStep.run();
      }
    }

    if(!isFound) {
      Popups.showInfo("Failed to find the destination cell","Failed Search");
      println("Failed to find the destination cell");
    }


  }

  private void highlightAStarPath(Cell[][] cellDetails,int[] dest,Runnable onStep) {
    int V = nodes.size();
    boolean[] inPath = new boolean[V];

    int r = dest[0];
    int c = dest[1];

    while(!(r == cellDetails[r][c].getParentI() && c == cellDetails[r][c].getParentJ())) {
      int pr = cellDetails[r][c].getParentI();
      int pc = cellDetails[r][c].getParentJ();

      int u = searchByCoords(r,c);
      int v = searchByCoords(pr,pc);

      if(u != -1 && v != -1) {
        inPath[v] = true;
        inPath[u] = true;
        mstEdges[u][v] = true;
        mstEdges[v][u] = true;
        nodes.get(u).setState(NodeState.PROCESSED);
        onStep.run();
      }

      r = pr;
      c = pc;
    }

    int startIdx = searchByCoords(r,c);
    if(startIdx != -1) {
      inPath[startIdx] = true;
      nodes.get(startIdx).setState(NodeState.PROCESSED);
      onStep.run();
    }

    removeAStarUnmarked(V,inPath,onStep);
  }

  private void removeAStarUnmarked(int V,boolean[] set,Runnable onStep) {

    for (int i =0 ;i < V; i++) {
      for(int j = 0; j < V;j++) {
        if(!mstEdges[i][j]) matrix[i][j] = 0;
      }
    }

    for(int i = V - 1; i >= 0;i--) {
      if(!set[i]) {
        deleteNodeByIdx(i);
      }
    }

    int remaining = nodes.size();
    mstEdges = new boolean[matrix.length][matrix.length];

    for (int i = 0; i < remaining; i++) {
      for (int j = 0; j < remaining; j++) {
        if (matrix[i][j] != 0) {
          mstEdges[i][j] = true;
        }
      }
    }

    onStep.run();
  }

  public static Graph buildGraphFromGrid(int[][] grid, int[] src, int[] dest, int startX, int startY, int spacing) {
    int rows = grid.length;
    int cols = grid[0].length;

    // Count open cells so the adjacency matrix is sized exactly
    int openCount = 0;
    int[][] cellToIndex = new int[rows][cols];
    for (int r = 0; r < rows; r++) {
      Arrays.fill(cellToIndex[r], -1);
      for (int c = 0; c < cols; c++) {
        if (grid[r][c] == 1) {
          cellToIndex[r][c] = openCount++;
        }
      }
    }

    Graph graph = new Graph(openCount);

    // Add a Node for every open cell at its (x, y) grid position
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (grid[r][c] == 1) {
          int x = startX + c * spacing;
          int y = startY + r * spacing;

          char label = '.';
          if (r == src[0] && c == src[1]) label = 'S';
          else if (r == dest[0] && c == dest[1]) label = 'D';

          graph.addNode(new Node(x, y,label, c,r));
        }
      }
    }

    // Connect adjacent open cells (Right & Down avoids duplicate edges)
    // If your A* allows diagonal moves, use this instead:
    int[][] dirs = { {0, 1}, {1, 0}, {1, 1}, {1, -1} };

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int u = cellToIndex[r][c];
        if (u == -1) continue;

        for (int[] d : dirs) {
          int nr = r + d[0];
          int nc = c + d[1];
          if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
            int v = cellToIndex[nr][nc];
            if (v != -1) {
              graph.addEdge(u, v, 1);
            }
          }
        }
      }
    }

    return graph;
  }


  private int searchByCoords(int row,int col) {
    for (int i = 0; i < nodes.size();i++) {
      Node n = nodes.get(i);
      if(n.getRow() == row && n.getCol() == col) {
        return i;
      }
    }
    return -1;
  }

  private void deleteNodeByIdx(int index) {
    if(index < 0 || index >= nodes.size()) return;

    nodes.remove(index);

    int n = matrix.length - 1;
    int[][] newMatrix = new int[n][n];

    int r = 0;
    for(int i = 0; i < matrix.length; i++) {
      if(i == index) continue;
      int c = 0;
      for(int j = 0; j < matrix.length;j++) {
        if(j == index) continue;
        newMatrix[r][c] = matrix[i][j];
        c++;
      }
      r++;
    }

    matrix = newMatrix;
  }
}
