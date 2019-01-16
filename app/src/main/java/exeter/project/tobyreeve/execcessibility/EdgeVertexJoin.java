package exeter.project.tobyreeve.execcessibility;

public class EdgeVertexJoin implements Comparable<EdgeVertexJoin>{
    private int joinId;
    private int edgeId;
    private int vertexId;
    private int vertexPosition;

    private EdgeVertexJoin() {}

    public EdgeVertexJoin(int joinId, int edgeId, int vertexId, int vertexPosition) {
        this.joinId = joinId;
        this.edgeId = edgeId;
        this.vertexId = vertexId;
        this.vertexPosition = vertexPosition;
    }

    public int compareTo(EdgeVertexJoin otherJoin) {
        return this.getEdgeId() - otherJoin.getEdgeId();
    }

    public int getJoinId() {
        return joinId;
    }

    public void setJoinId(int joinId) {
        this.joinId = joinId;
    }

    public EdgeVertexJoin withJoinId(int joinId) {
        setJoinId(joinId);
        return this;
    }

    public int getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(int edgeId) {
        this.edgeId = edgeId;
    }

    public EdgeVertexJoin withEdgeId(int edgeId) {
        setEdgeId(edgeId);
        return this;
    }

    public int getVertexId() {
        return vertexId;
    }

    public void setVertexId(int vertexId) {
        this.vertexId = vertexId;
    }

    public EdgeVertexJoin withVertexId(int vertexId) {
        setVertexId(vertexId);
        return this;
    }

    public int getVertexPosition() {
        return vertexPosition;
    }

    public void setVertexPosition(int vertexPosition) {
        this.vertexPosition = vertexPosition;
    }

    public EdgeVertexJoin withVertexPosition(int vertexPosition) {
        setVertexPosition(vertexPosition);
        return this;
    }
}
