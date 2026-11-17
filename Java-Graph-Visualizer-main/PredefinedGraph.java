import java.util.ArrayList;

public class PredefinedGraph {

    private final static int NODE_OFFSET = 17;

    public static void loadPreset(Graph graph, GraphPreset preset, int panelWidth, int panelHeight) {
        int w = panelWidth > 0 ? panelWidth : 600;
        int h = panelHeight > 0 ? panelHeight : 600;

        switch (preset) {
            case SIMPLE_TRIANGLE -> buildSimpleTriangle(graph,w,h);
            case BINARY_TREE -> buildBinaryTree(graph, w, h);
            case GRID_MESH -> buildGridMesh(graph, w, h);
            case STAR_HUB -> buildStarHub(graph, w, h);
            case DENSE_WEB -> buildDenseWeb(graph, w, h);
            case DISCONNECTED_ISLANDS -> buildDisconnectedIslands(graph, w, h);
            case DEFAULT_HEX -> buildDefaultHex(graph, w, h);
        }

    }

    private static void resetGraph(Graph graph, int size) {
        graph.setNodes(new ArrayList<>());
        graph.setMatrix(new int[size][size]);
    }

    private static void addNodeAt(Graph graph, char data, int centerX, int centerY) {
        graph.addNode(new Node(centerX - NODE_OFFSET,centerY - NODE_OFFSET,data,0,0));
    }

    private static void buildDefaultHex(Graph graph, int w, int h) {
        resetGraph(graph,6);
        char[] labels = {'A','X','Y','B','C','G'};
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.min(w,h) / 2 - 80;

        for(int i = 0; i < labels.length; i++) {
            double angle = 2 * Math.PI * i / labels.length;
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            addNodeAt(graph,labels[i],x,y);
        }

        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 30);
        graph.addEdge(1, 3, 4);
        graph.addEdge(1, 4, 5);
        graph.addEdge(4, 0, 1);
        graph.addEdge(0, 2, 5);
        graph.addEdge(5, 2, 60);
    }

    private static void buildSimpleTriangle(Graph graph, int w, int h) {
        resetGraph(graph,3);
        int cx = w / 2;
        int cy = h / 2;

        addNodeAt(graph,'A',cx,cy - 120);
        addNodeAt(graph,'Z',cx - 140, cy + 100);
        addNodeAt(graph,'H',cx + 140, cy + 100);

        graph.addEdge(0,1,5);
        graph.addEdge(1,2,10);
        graph.addEdge(2,0,15);
    }

    private static void buildBinaryTree(Graph graph, int w ,int h) {
        resetGraph(graph,7);
        int cx = w / 2;


        // L1 - root
        addNodeAt(graph,'A',cx,90);

        // L2
        addNodeAt(graph,'G',cx - 140,220);
        addNodeAt(graph,'B',cx + 140,220);

        // L3
        addNodeAt(graph, 'N', cx - 210, 370);
        addNodeAt(graph, 'L', cx - 70, 370);
        addNodeAt(graph, 'F', cx + 70, 370);
        addNodeAt(graph, 'G', cx + 210, 370);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 8);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 4, 6);
        graph.addEdge(2, 5, 3);
        graph.addEdge(2, 6, 9);
    }

    private  static void buildGridMesh(Graph graph, int w, int h) {
        resetGraph(graph,9);
        int startX = w / 2 - 150;
        int startY = h / 2 - 150;
        int spacing = 150;

        char label = 'A';

        for(int row = 0; row < 3;row++) {
            for(int col=0; col < 3;col++) {
                addNodeAt(graph,label++,startX + col * spacing, startY + row * spacing);
            }
        }

        graph.addEdge(0, 1, 12); graph.addEdge(1, 2, 8);
        graph.addEdge(3, 4, 15); graph.addEdge(4, 5, 6);
        graph.addEdge(6, 7, 10); graph.addEdge(7, 8, 14);

        graph.addEdge(0, 3, 7);  graph.addEdge(3, 6, 11);
        graph.addEdge(1, 4, 9);  graph.addEdge(4, 7, 5);
        graph.addEdge(2, 5, 13); graph.addEdge(5, 8, 4);

        // Diagonal cross-edges
        graph.addEdge(0, 4, 25);
        graph.addEdge(2, 4, 18);
        graph.addEdge(4, 8, 22);


    }

    private static void buildStarHub(Graph graph, int w, int h) {
        resetGraph(graph, 8);
        int cx = w / 2, cy = h / 2;
        int radius = Math.min(w, h) / 2 - 90;

        // Center hub is 'A'
        addNodeAt(graph, 'A', cx, cy);

        // 7 outer nodes
        for (int i = 1; i < 8; i++) {
            double angle = 2 * Math.PI * (i - 1) / 7;
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            addNodeAt(graph, (char) ('A' + i), x, y);

            // Connect hub to outer spoke
            graph.addEdge(0, i, i * 5);
        }

        // Connect outer rim into a cycle
        for (int i = 1; i < 8; i++) {
            int next = (i == 7) ? 1 : i + 1;
            graph.addEdge(i, next, (i * 3) + 2);
        }
    }

    private static void buildDenseWeb(Graph graph, int w, int h) {
        resetGraph(graph, 10);
        int cx = w / 2, cy = h / 2;
        int outerRadius = Math.min(w, h) / 2 - 65;
        int innerRadius = outerRadius / 2;

        // Outer ring: A - E (indices 0 - 4)
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
            int x = (int) (cx + outerRadius * Math.cos(angle));
            int y = (int) (cy + outerRadius * Math.sin(angle));
            addNodeAt(graph, (char) ('A' + i), x, y);
        }

        // Inner ring: F - J (indices 5 - 9), rotated slightly so edges don't overlap
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 - Math.PI / 2 + (Math.PI / 5);
            int x = (int) (cx + innerRadius * Math.cos(angle));
            int y = (int) (cy + innerRadius * Math.sin(angle));
            addNodeAt(graph, (char) ('F' + i), x, y);
        }

        int[] weights = {14, 27, 9, 42, 31, 18, 55, 6, 39, 23, 12, 48, 7, 19, 33};
        int wIdx = 0;

        for (int i = 0; i < 5; i++) {
            // Outer ring cycle
            graph.addEdge(i, (i + 1) % 5, weights[wIdx++ % weights.length]);
            // Inner star connections (pentagram)
            graph.addEdge(5 + i, 5 + ((i + 2) % 5), weights[wIdx++ % weights.length]);
            // Bridges between outer and inner rings
            graph.addEdge(i, 5 + i, weights[wIdx++ % weights.length]);
            graph.addEdge(i, 5 + ((i + 4) % 5), weights[wIdx++ % weights.length]);
            // Long chord across the web
            graph.addEdge(i, (i + 2) % 5, weights[wIdx++ % weights.length] + 15);
        }
    }

    private static void buildDisconnectedIslands(Graph graph, int w, int h) {
        resetGraph(graph, 8);
        int leftCenter = w / 4 + 20;
        int rightCenter = (w * 3) / 4 - 20;
        int cy = h / 2;
        int offset = 85;

        // Left Island (A, B, C, D)
        addNodeAt(graph, 'A', leftCenter - offset, cy - offset);
        addNodeAt(graph, 'B', leftCenter + offset, cy - offset);
        addNodeAt(graph, 'C', leftCenter + offset, cy + offset);
        addNodeAt(graph, 'D', leftCenter - offset, cy + offset);

        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 20);
        graph.addEdge(2, 3, 30);
        graph.addEdge(3, 0, 40);
        graph.addEdge(0, 2, 15);

        // Right Island (E, F, G, H)
        addNodeAt(graph, 'E', rightCenter - offset, cy - offset);
        addNodeAt(graph, 'F', rightCenter + offset, cy - offset);
        addNodeAt(graph, 'G', rightCenter + offset, cy + offset);
        addNodeAt(graph, 'H', rightCenter - offset, cy + offset);

        graph.addEdge(4, 5, 8);
        graph.addEdge(5, 6, 12);
        graph.addEdge(6, 7, 16);
        graph.addEdge(7, 4, 24);
        graph.addEdge(5, 7, 6);
    }
}
