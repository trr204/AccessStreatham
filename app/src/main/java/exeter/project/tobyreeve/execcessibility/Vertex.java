package exeter.project.tobyreeve.execcessibility;

public class Vertex implements Comparable<Vertex> {
    private int id;
    private long osmID;
    private double latitude;
    private double longitude;
    private double heuristicValue;
    private int elevation;
    private float g; //Distance from source to this vertex
    private double x;
    private double y;
    private String label;
    private Incident incident;

    public Vertex() {}

    public Vertex(int id, long osmID, double latitude, double longitude, int elevation, String label) {
        this.id = id;
        this.osmID = osmID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.label = label;
        this.g = Float.MAX_VALUE;
    }

    public int compareTo(Vertex otherVertex) {
        return this.getId() - otherVertex.getId();
    }

    public void setLabel(String label) {this.label = label;}

    public String getLabel() {return label;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vertex withId(int id){
        setId(id);
        return this;
    }

    public long getOsmID() {
        return osmID;
    }

    public void setOsmID(long osmID) {
        this.osmID = osmID;
    }

    public Vertex withOsmID(long osmID) {
        setOsmID(osmID);
        return this;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Vertex withLatitude(double latitude) {
        setLatitude(latitude);
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Vertex withLongitude(double longitude) {
        setLongitude(longitude);
        return this;
    }

    public double getHeuristicValue() { return heuristicValue; }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public Vertex withHeuristicValue(double heurisyicValue) {
        setHeuristicValue(heuristicValue);
        return this;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public Vertex withG(float g) {
        setG(g);
        return this;
    }

    public double getF() {
        return g + heuristicValue;
    }

    public double getX() {return x;}

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {return y;}

    public void setY(double y) {
        this.y = y;
    }

    public int getElevation() {return elevation;}

    public void setElevation(int elevation) {this.elevation = elevation;}

    public Incident getIncident() {return incident;}

    public void setIncident(Incident incident) {this.incident = incident;}
}
