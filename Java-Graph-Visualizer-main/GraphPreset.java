public enum GraphPreset {
    DEFAULT_HEX("Default (6-Node Circle)"),
    SIMPLE_TRIANGLE("Simple Triangle (3 Nodes - Basic)"),
    BINARY_TREE("Binary Tree (7 Nodes - Hierarchical)"),
    GRID_MESH("Grid Mesh (9 Nodes - 3x3 Lattice)"),
    STAR_HUB("Star Network (8 Nodes - Central Hub)"),
    DENSE_WEB("Crazy Web (10 Nodes - Double Ring + Crossings)"),
    DISCONNECTED_ISLANDS("Two Islands (8 Nodes - Disconnected Test)");

    private final String displayName;

    GraphPreset(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
