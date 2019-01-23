package exeter.project.tobyreeve.execcessibility;

public class Vertex implements Comparable<Vertex> {
    private int id;
    private long osmID;
    private double latitude;
    private double longitude;
    private double heuristicValue;
    private float g;
    private double x;
    private double y;

    public Vertex() {}

    public Vertex(int id, long osmID, double latitude, double longitude) {
        this.id = id;
        this.osmID = osmID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.g = Float.MAX_VALUE;
    }

    public int compareTo(Vertex otherVertex) {
        return this.getId() - otherVertex.getId();
    }

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
}
