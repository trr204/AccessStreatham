package exeter.project.tobyreeve.execcessibility;

import android.location.Location;
import android.os.Parcelable;

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

    public class VertexComparator implements Comparator<Vertex> {
        public int compare(Vertex nodeFirst, Vertex nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF()) return 1;
            if (nodeSecond.getF() > nodeFirst.getF()) return -1;
            return 0;
        }
    }


    public List<Vertex> calculateRoute(Vertex source, Vertex destination) {

        //Calculate heuristic value for every Vertex
        //Dist(source, destination) - Dist(source, genericVertex)

        //Location.distanceBetween returns approximate distance in metres between two locations
        float[] sourceDestDistance = new float[1];
        Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination.getLatitude(), destination.getLongitude(), sourceDestDistance);
        for (Vertex v : this.getVertexMap().values()) {
            float[] vertextToDestinationDistance = new float[1];
            Location.distanceBetween(v.getLatitude(), v.getLongitude(), destination.getLatitude(), destination.getLongitude(), vertextToDestinationDistance);
            v.setHeuristicValue(vertextToDestinationDistance[0]);
        }
        PriorityQueue<Vertex> openList = new PriorityQueue<Vertex>(11, new VertexComparator());
        List<Vertex> closedList = new ArrayList<Vertex>();
        Map<Integer, Integer> path = new HashMap<Integer, Integer>();
        source.setG(0);
        openList.add(source);

        while (!openList.isEmpty()) {
            Vertex currentVertex = openList.poll();
            if (currentVertex.getId() == destination.getId()) {
                return path(path, destination);
            }
            closedList.add(currentVertex);

            for (Subedge s : subedgeList) {
                Vertex neighbour = null;
                if (s.getVertex1Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex2Id());
                } else if (s.getVertex2Id() == currentVertex.getId()) {
                    neighbour = vertexMap.get(s.getVertex1Id());
                }

                if (neighbour != null && !closedList.contains(neighbour)) {
                    float[] neighbourDistance = new float[1];
                    Location.distanceBetween(currentVertex.getLatitude(), currentVertex.getLongitude(), neighbour.getLatitude(), neighbour.getLongitude(), neighbourDistance);

                    float tentativeG = neighbourDistance[0] + currentVertex.getG();

                    if (tentativeG < neighbour.getG()) {
                        neighbour.setG(tentativeG);
                        path.put(neighbour.getId(), currentVertex.getId());
                        if (!openList.contains(neighbour)) {
                            openList.add(neighbour);
                        }
                    }


                }

            }
        }

        return path(path, destination);
    }

    public List<Vertex> path(Map<Integer, Integer> path, Vertex destination) {
        //Use IDs in path to get list of vertices

        final List<Vertex> pathList = new ArrayList<Vertex>();
        pathList.add(destination);
        while (path.containsKey(destination.getId())) {
            destination = vertexMap.get(path.get(destination.getId()));
            pathList.add(destination);
        }
        for (Vertex v : pathList) {
            v.setG(Float.MAX_VALUE);
        }
        Collections.reverse(pathList);
        this.calculatedPathList = pathList;
        return pathList;
    }
}
