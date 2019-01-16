package exeter.project.tobyreeve.execcessibility;

public class Subedge {

    private int parentEdgeId;
    private int vertex1Id;
    private int vertex2Id;

    public Subedge() {}
    public Subedge(int parentEdgeId, int vertex1Id, int vertex2Id) {
        this.parentEdgeId = parentEdgeId;
        this.vertex1Id = vertex1Id;
        this.vertex2Id = vertex2Id;
    }

    public int getParentEdgeId() {
        return parentEdgeId;
    }

    public void setParentEdgeId(int parentEdgeId) {
        this.parentEdgeId = parentEdgeId;
    }

    public int getVertex1Id() {
        return vertex1Id;
    }

    public void setVertex1Id(int vertex1Id) {
        this.vertex1Id = vertex1Id;
    }

    public int getVertex2Id() {
        return vertex2Id;
    }

    public void setVertex2Id(int vertex2Id) {
        this.vertex2Id = vertex2Id;
    }
}
