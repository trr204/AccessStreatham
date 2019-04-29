package exeter.project.tobyreeve.execcessibility;

public class Incident {

    private int id;
    private int vertexId;
    private String description;
    private String reportedAtTime;

    public Incident() {}
    public Incident(int id, int vertexId, String description, String reportedAtTime) {
        this.id = id;
        this.vertexId = vertexId;
        this.description = description;
        this.reportedAtTime = reportedAtTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVertexId() {return vertexId;}

    public void setVertexId(int vertexId) {this.vertexId = vertexId;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedAtTime() {
        return reportedAtTime;
    }

    public void setReportedAtTime(String reportedAtTime) {
        this.reportedAtTime = reportedAtTime;
    }
}
