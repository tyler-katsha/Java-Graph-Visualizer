public class Cell{

    private int parent_i, parent_j;
    private double f,g,h;

    public Cell(CellBuilder builder) {
        this.parent_i = builder.parent_i;
        this.parent_j = builder.parent_j;
        this.f = builder.f;
        this.h = builder.h;
        this.g = builder.g;
    }

    public void setParentI(int parent_i) {
        this.parent_i = parent_i;
    }
    public void setParentJ(int parent_j) {
        this.parent_j = parent_j;
    }
    public void setF(double f) {
        this.f = f;
    }
    public void setH(double h) {
        this.h = h;
    }
    public void setG(double g) {
        this.g = g;
    }

    public int getParentI(){
        return this.parent_i;
    }
    public int getParentJ(){
        return parent_j;
    }
    public double getF(){
        return f;
    }
    public double getG(){
        return g;
    }
    public double getH(){
        return h;
    }

    public static class CellBuilder{
        private int parent_i, parent_j;
        private double f,g,h;

        public CellBuilder parentI(int parent_i) {
            this.parent_i = parent_i;
            return this;
        }
        public CellBuilder parentJ(int parent_j) {
            this.parent_j = parent_j;
            return this;
        }
        public CellBuilder f(double f) {
            this.f = f;
            return this;
        }
        public CellBuilder h(double h) {
            this.h = h;
            return this;
        }
        public CellBuilder g(double g) {
            this.g = g;
            return this;
        }


        public Cell build(){
            return new Cell(this);
        }
    }
}
