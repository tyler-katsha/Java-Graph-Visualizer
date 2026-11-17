public class DSU {

    private final int[] rank, parent;

    public DSU(int V) {
        parent = new int[V];
        rank = new int[V];

        for(int i = 0; i < V;i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int find(int i) {

        if(parent[i] != i) {
           parent[i] = find(parent[i]);
        }

        return parent[i];
    }

    public void union(int x, int y) {
        int s1 = find(x);
        int s2 = find(y);

        if(s1 != s2) {
            if(rank[s1] < rank[s2]) {
                parent[s1] = s2;
            } else if(rank[s1] > rank[s2]) {
                parent[s2] = s1;
            } else {
                parent[s2] = s1;
                rank[s1]++;
            }
        }
    }
}
