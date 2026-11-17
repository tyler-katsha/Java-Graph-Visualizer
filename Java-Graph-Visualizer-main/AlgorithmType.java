public enum AlgorithmType {
    NONE("--- Selected a Algorithm ---"),
    DFS("DFS"),
    BFS("BFS"),
    MST_PRIM("Minimum Spanning Tree (PRIM)"),
    DIJKSTRA("Dijskatra one positive weighted graphs"),
    MST_KRUSKAL("Kruskal's MST"),
    ASTAR("A Star"),
    GRAPH_TO_TREE("Convert to Tree / BST");

    private final String displayName;

    AlgorithmType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
