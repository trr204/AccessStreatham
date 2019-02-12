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
    private double minLongitude;
    private double maxLongitude;
    private double minLatitude;
    private double maxLatitude;

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


    public boolean calculateRoute(Vertex source, Vertex destination) {
        //Routes calculated using A* Search algorithm, with Euclidean distance as the heuristic function
        //Location.distanceBetween returns approximate distance in metres between two locations

        //Dist(source, destination) - Dist(source, genericVertex)

        //Calculate approximate distance between source and destination
        float[] sourceDestDistance = new float[1];
        Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination.getLatitude(), destination.getLongitude(), sourceDestDistance);

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
                if (s.getVertex1Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex2Id());
                } else if (s.getVertex2Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex1Id());
                }

                //If a neighbour has been found that has not already been searched by the algorithm
                if (neighbour != null && !closedList.contains(neighbour)) {
                    //Calculate the approximate distance between the current vertex and the neighbour
                    //TODO Convert to edge weight instead of Euclidean distance
                    float[] neighbourDistance = new float[1];
                    Location.distanceBetween(currentVertex.getLatitude(), currentVertex.getLongitude(), neighbour.getLatitude(), neighbour.getLongitude(), neighbourDistance);

                    //Approximate distance of current path from source to neighbour
                    float tentativeG = neighbourDistance[0] + currentVertex.getG();

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

        final List<Vertex> pathList = new ArrayList<Vertex>();
        pathList.add(destination);
        //Work back from the destination to add the path steps that apply
        while (path.containsKey(destination.getId())) {
            destination = vertexMap.get(path.get(destination.getId()));
            pathList.add(destination);
        }
        //Reset all vertices' current distance values
        for (Vertex v : pathList) {
            v.setG(Float.MAX_VALUE);
        }
        Collections.reverse(pathList);
        this.calculatedPathList = pathList;
        return true;
    }
}
