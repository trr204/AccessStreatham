package exeter.project.tobyreeve.execcessibility;

import java.util.Map;

public class Edge implements Comparable<Edge>{

    private int id;
    private long osmId;
    private float weight;
    private Map<Integer, Vertex> vertexList;

    public Edge() {}

    public Edge(int id, long osmId, Map<Integer, Vertex> vertexList) {
        this.id = id;
        this.osmId = osmId;
        this.vertexList = vertexList;
    }

    public int compareTo(Edge otherEdge) {
        return this.getId() - otherEdge.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Edge withId(int id) {
        setId(id);
        return this;
    }

    public long getOsmId() {
        return osmId;
    }

    public void setOsmId(long osmId) {
        this.osmId = osmId;
    }

    public Edge withOsmId(long osmId) {
        setOsmId(osmId);
        return this;
    }
    public Map<Integer, Vertex> getVertexList() {
        return vertexList;
    }

    public void setVertexList(Map<Integer, Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public Edge withVertexList(Map<Integer, Vertex> vertexList) {
        setVertexList(vertexList);
        return this;
    }

    public float getWeight() { return weight; }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Edge withWeight(float weight) {
        setWeight(weight);
        return this;
    }
}

