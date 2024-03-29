package exeter.project.tobyreeve.execcessibility;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
    private Map<Integer, Edge> edgeMap;
    private Map<Integer, Vertex> vertexMap;
    private List<Subedge> subedgeList;
    private List<Vertex> calculatedPathList;
    private List<Vertex> incidentVertexList;
    private double minLongitude;
    private double maxLongitude;
    private double minLatitude;
    private double maxLatitude;
    private double userLocationX;
    private double userLocationY;
    private Vertex source;
    private Vertex destination;

    public Graph() {}
    public Graph(Map<Integer, Edge> edgeMap, Map<Integer, Vertex> vertexMap, List<Subedge> subedgeList, double minLongitude,
                 double maxLongitude, double minLatitude, double maxLatitude){
        this.edgeMap = edgeMap;
        this.vertexMap = vertexMap;
        this.subedgeList = subedgeList;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.calculatedPathList = new ArrayList<Vertex>();
    }

    public Map<Integer, Edge> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(Map<Integer, Edge> edgeMap) {
        this.edgeMap = edgeMap;
    }

    public Graph withEdgeMap(Map<Integer, Edge> edgeMap) {
        setEdgeMap(edgeMap);
        return this;
    }

    public double getMinLongitude() {return minLongitude;}

    public double getMaxLongitude() {return maxLongitude;}

    public double getMinLatitude() {return minLatitude;}

    public double getMaxLatitude() {return maxLatitude;}

    public void setSource(Vertex source) {this.source = source;}

    public Vertex getSource() {return source;}

    public void setDestination(Vertex destination) {this.destination = destination;}

    public Vertex getDestination() {return destination;}

    public void setIncidentVertexList(List<Vertex> incidentVertexList) {this.incidentVertexList = incidentVertexList;}

    public List<Vertex> getIncidentVertexList() {return incidentVertexList;}

    public List<Subedge> getSubedgeList() {return subedgeList;}

    public Map<Integer, Vertex> getVertexMap() {
        return vertexMap;
    }

    public void setVertexMap(Map<Integer, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    public Graph withVertexMap(Map<Integer, Vertex> vertexMap) {
        setVertexMap(vertexMap);
        return this;
    }

    public List<Vertex> getCalculatedPathList() {return calculatedPathList;}

    public void setCalculatedPathList(ArrayList<Vertex> calculatedPathList) {
        this.calculatedPathList = calculatedPathList;
    }

    public class VertexComparator implements Comparator<Vertex> {
        public int compare(Vertex nodeFirst, Vertex nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF()) return 1;
            if (nodeSecond.getF() > nodeFirst.getF()) return -1;
            return 0;
        }
    }


    public boolean calculateRoute(Vertex source, Vertex destination, Map<String, Integer> userPreferences) {
        //Routes calculated using A* Search algorithm, with Euclidean distance as the heuristic function
        //Location.distanceBetween returns approximate distance in metres between two locations

        //Dist(source, destination) - Dist(source, genericVertex)

        int stairsPreference = userPreferences.get("AvoidStaircases");
        int incidentsPreference = userPreferences.get("AvoidIncidents");
        int altitudePreference = userPreferences.get("DistanceOverAltitude");

        //TODO incorporate incidents and incidentsPref

        //Calculate approximate distance between source and destination
        float[] sourceDestDistance = new float[1];
        Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination.getLatitude(), destination.getLongitude(), sourceDestDistance);
        Log.d("PLANROUTE", "Estimated Euclidean distance between source and dest: " + String.valueOf(sourceDestDistance[0]));
        Log.d("PLANROUTE", "Start assigning heuristic values to vertices");
        for (Vertex v : this.getVertexMap().values()) {
            //Calculate approximate distance between current vertex and destination, this becomes the current vertex's heuristic value
            float[] vertextToDestinationDistance = new float[1];
            Location.distanceBetween(v.getLatitude(), v.getLongitude(), destination.getLatitude(), destination.getLongitude(), vertextToDestinationDistance);
            v.setHeuristicValue(vertextToDestinationDistance[0]);
        }
        //Create a priority queue using vertices' F value (distance thus far + heuristic value) to decide priority
        PriorityQueue<Vertex> openList = new PriorityQueue<Vertex>(11, new VertexComparator());
        List<Vertex> closedList = new ArrayList<Vertex>();
        Map<Integer, Integer> path = new HashMap<Integer, Integer>();
        source.setG(0);
        openList.add(source);

        Log.d("PLANROUTE", "Start searching graph for best route");
        while (!openList.isEmpty()) {
            Vertex currentVertex = openList.poll(); //Retrieve vertex at head of queue, aka highest priority
            if (currentVertex.getId() == destination.getId()) {
                return path(path, destination);
            }
            closedList.add(currentVertex);

            //For every edge in the graph
            for (Subedge s : subedgeList) {
                //If the edge contains the current vertex, get the neighbouring vertex
                Vertex neighbour = null;
                boolean leftToRight = false;
                if (s.getVertex1Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex2Id());
                } else if (s.getVertex2Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex1Id());
                }

                //If a neighbour has been found that has not already been searched by the algorithm
                if (neighbour != null && !closedList.contains(neighbour)) {
                    //Calculate the approximate distance between the current vertex and the neighbour
                    float[] neighbourDistance = new float[1];
                    Location.distanceBetween(currentVertex.getLatitude(), currentVertex.getLongitude(), neighbour.getLatitude(), neighbour.getLongitude(), neighbourDistance);

                    //TODO Determine if incline or decline, should this affect?
                    int elevationDifference = Math.abs(currentVertex.getElevation() - neighbour.getElevation());
                    //boolean incline = (elevationDifference < 0) ? true : false;
                    //float altitudeScalar = incline ? 2 : 1/2;

                    //Approximate distance of current path from source to neighbour
                    float tentativeG;
                    if ((stairsPreference == 10 && s.isStairs()) || (neighbour.getIncident() != null && incidentsPreference == 1) || (neighbour.getIncident() != null && neighbour.getIncident().getDescription().contains("Cannot pass"))) {
                        tentativeG = Float.MAX_VALUE;
                    } else {
                        tentativeG = (neighbourDistance[0] + (s.isStairs() ? (float) 0.5 * stairsPreference : 0) + (float) 0.5 * altitudePreference * elevationDifference) + currentVertex.getG();
                    }
                    //If the approximated distance between start and neighbour is smaller than the currently stored distance between start and neighbour
                    if (tentativeG < neighbour.getG()) {
                        neighbour.setG(tentativeG); //Update the neighbour
                        path.put(neighbour.getId(), currentVertex.getId()); //Add the connection between the vertices as a possible path step
                        if (!openList.contains(neighbour)) {
                            openList.add(neighbour); //Add the neighbour to the queue if it isn't already there
                        }
                    }
                }
            }
        }
        return path(path, destination);
    }

    public boolean path(Map<Integer, Integer> path, Vertex destination) {
        //Use IDs in path to get list of vertices

        Log.d("PLANROUTE", "Total path cost: " + String.valueOf(destination.getG()));
        final List<Vertex> pathList = new ArrayList<Vertex>();
        pathList.add(destination);
        //Work back from the destination to add the path steps that apply
        while (path.containsKey(destination.getId())) {
            destination = vertexMap.get(path.get(destination.getId()));
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        //Reset all vertices' current distance values
        for (Map.Entry<Integer, Vertex> entry: vertexMap.entrySet()) {
            entry.getValue().setG(Float.MAX_VALUE);
        }
        this.calculatedPathList = pathList;
        return true;
    }

    public double getUserLocationX() {
        return userLocationX;
    }

    public void setUserLocationX(double userLocationX) {
        this.userLocationX = userLocationX;
    }

    public double getUserLocationY() {
        return userLocationY;
    }

    public void setUserLocationY(double userLocationY) {
        this.userLocationY = userLocationY;
    }
}
