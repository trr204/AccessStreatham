package exeter.project.tobyreeve.execcessibility;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper helper;
    Graph campus;
    MyCanvas canvas;
    float canvasWidth;
    float canvasHeight;
    Vertex start;
    Vertex end;
    Bitmap tile;

    final double maxLatitude = 50.7416700; //TODO STORE THESE IN DATABASE
    final double minLatitude = 50.7316700;
    final double maxLongitude = -3.5238600;
    final double minLongitude = -3.5458300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue queue = RequestQueueHandler.getInstance().getRequestQueue();
        checkForGraphUpdate();

        canvas = new MyCanvas(this);
        canvas.getSettings().setBuiltInZoomControls(true);
        canvas.getSettings().setDisplayZoomControls(true);
        canvas.loadUrl("file:///android_res/drawable/full_map_19.png");
        setContentView(R.layout.activity_main);
        RelativeLayout layout = findViewById(R.id.activity_main);
        layout.addView(canvas);/*
        if (tile == null) {
            tile = BitmapFactory.decodeResource(getResources(), R.drawable.full_map_19); //TODO CHANGE THIS
        }
        canvasWidth = tile.getWidth();
        canvasHeight = tile.getHeight();*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        canvas.setScreenHeight(displayMetrics.heightPixels);
        canvas.setScreenWidth(displayMetrics.widthPixels);

        Drawable d = getResources().getDrawable(R.drawable.full_map_19);
        canvasHeight = d.getIntrinsicHeight();
        canvasWidth = d.getIntrinsicWidth();
        canvas.setInitialScaleFactor(canvas.getScale());
        //getSupportActionBar().hide();
        FloatingActionButton planRouteFab = findViewById(R.id.plan_route_button);
        planRouteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PLANROUTE", "Planning a route fab clicked!");
                Intent intentPlan = new Intent(MainActivity.this, RouteSpecification.class);
                if (campus.getSource() != null) {
                    intentPlan.putExtra("Source", campus.getSource().getId());
                }
                if (campus.getDestination() != null) {
                    intentPlan.putExtra("Destination", campus.getDestination().getId());
                }
                startActivityForResult(intentPlan, 1);
                //TODO:
                //list of specific sources and destinations (including multiple entrances for some buildings) attributed to specific nodes on the graph (plus current user location if enabled)

            }
        });

        /*TODO:
            Alternative route calculation:
                User selects a point
                User then clicks plan route button
                Selected point is initial starting point (shown as "currently selected location" or something)
                Proceed as previously
        */
        FloatingActionButton clearRouteFab = findViewById(R.id.clear_route_button);
        clearRouteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLEARROUTE", "Clearing route fab clicked!");
                clearRoute();
            }
        });
        Log.d("CANVAS DIMENS", "Canvas height: " + String.valueOf(canvasHeight) + ", Canvas width: " + String.valueOf(canvasWidth));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intentAbout = new Intent(this, SimpleContentActivity.class).putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/about.html");
                this.startActivity(intentAbout);
                return true;
            case R.id.preferences:
                Intent intentPreferences = new Intent(this, Preferences.class);
                this.startActivity(intentPreferences);
                return true;
            case R.id.buildings:
                Intent intentBuildings = new Intent(this, Buildings.class);
                this.startActivity(intentBuildings);
                return true;
            case R.id.incidents:
                getIncidents();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setUpGraph() {
        Log.d("SETUP", "Set up start");
        List<Vertex> vertexList = new ArrayList<Vertex>();
        List<Edge> edgeList = new ArrayList<Edge>();
        List<EdgeVertexJoin> joinList = new ArrayList<EdgeVertexJoin>();

        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor vCursor = helper.getVertexData();
        Cursor eCursor = helper.getEdgeData();
        Cursor jCursor = helper.getEdgeVertexJoinData();

        for (int i = 0; i < vCursor.getCount(); i++) {
            while(vCursor.moveToNext()) {
                vertexList.add(new Vertex(vCursor.getInt(0), vCursor.getLong(1), vCursor.getFloat(2), vCursor.getFloat(3), vCursor.getInt(4), vCursor.getString(8)));

            }
        }
        Log.d("SETUPGRAPH", "vertexList populated");
        for (int j = 0; j < jCursor.getCount(); j++) {
            while (jCursor.moveToNext()) {
                joinList.add(new EdgeVertexJoin(jCursor.getInt(0), jCursor.getInt(1), jCursor.getInt(2), jCursor.getInt(3)));
            }
        }
        Log.d("SETUPGRAPH", "joinList populated");
        for (int i = 0; i < eCursor.getCount(); i++) {
            while(eCursor.moveToNext()) {
                edgeList.add(new Edge(eCursor.getInt(0), eCursor.getLong(1), new HashMap<Integer, Vertex>(), eCursor.getInt(4) == 1 ? true : false));
            }
        }
        Log.d("SETUPGRAPH", "edgeList populated");

        Collections.sort(edgeList);
        Collections.sort(vertexList);
        Collections.sort(joinList);
        Map<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();
        for (Vertex v : vertexList) {
            v.setY((1-(v.getLatitude() - minLatitude)/(maxLatitude - minLatitude)) * canvasHeight);
            v.setX(((v.getLongitude() - minLongitude)/(maxLongitude - minLongitude))*canvasWidth);
            vertexMap.put(v.getId(), v);
            if (v.getId() == 251) {
                Log.d("tempdebug", String.valueOf(v.getOsmID()));
            }
        }
        Log.d("SETUPGRAPH", "vertexMap populated");

        int i = 0;
        for (int j = 0; j < joinList.size(); j++) {
            if (joinList.get(j).getEdgeId() == edgeList.get(i).getId()) {
                    edgeList.get(i).getVertexList().put(joinList.get(j).getVertexPosition(), vertexMap.get(joinList.get(j).getVertexId()));
            }
            if ((j+1 < joinList.size()) && joinList.get(j).getEdgeId() != joinList.get(j+1).getEdgeId()) {
                i++;
            }
        }
        Log.d("SETUPGRAPH", "edgeList vertexLists populated");

        List<Subedge> subedgeList = new ArrayList<Subedge>();
        for (Edge e : edgeList) {
            for (int s = 0; s < e.getVertexList().size() - 1; s++) {
                subedgeList.add(new Subedge(e.getId(), e.getVertexList().get(s).getId(), e.getVertexList().get(s+1).getId(), e.isStairs()));
            }
        }
        Log.d("SETUPGRAPH", "subedgeList populated");

        Map<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
        for (Edge e : edgeList) {
            edgeMap.put(e.getId(), e);
        }
        Log.d("SETUPGRAPH", "edgeMap populated");

        campus = new Graph(edgeMap, vertexMap, subedgeList,minLongitude , maxLongitude,minLatitude ,maxLatitude);
        //TODO
        canvas.setGraph(campus);
        getIncidents();
        canvas.postInvalidate();
        Log.d("SETUPGRAPH", "Finished. Vertex total: " + String.valueOf(campus.getVertexMap().size()) + ", Edge total: " + String.valueOf(campus.getEdgeMap().size()));
    }

    public void clearRoute() {
        campus.setCalculatedPathList(new ArrayList<Vertex>());
        canvas.postInvalidate();
        campus.setSource(null);
        campus.setDestination(null);
        Log.d("CLEARROUTE", "Route cleared");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                switch (intent.getStringExtra("ORIGIN")) {
                    case "RouteSpecification":
                        Vertex source = campus.getVertexMap().get(Integer.valueOf(intent.getStringExtra("SOURCE")));
                        Vertex destination = campus.getVertexMap().get(Integer.valueOf(intent.getStringExtra("DESTINATION")));
                        clearRoute();
                        Log.d("tempdebug", "source/destination: " + String.valueOf(source.getOsmID()) + "/" + String.valueOf(destination.getOsmID()));
                        Log.d("PLANROUTE", "Source and destination acquired on MainActivity from RouteSpecification");
                        planRoute(source, destination);
                        break;
                    case "NewIncidentTab":
                        getIncidents();
                        break;
                    case "CurrentIncidentTab":
                        getIncidents();
                        break;
                }
            }
        }
    }

    public void planRoute(Vertex source, Vertex destination) {
        Cursor upCursor = helper.getUserPreferenceData();
        Map<String, Integer> userPreferences = new HashMap<String, Integer>();
        for (int i = 0; i < upCursor.getCount(); i++) {
            while (upCursor.moveToNext()) {
                userPreferences.put(upCursor.getString(0), upCursor.getInt(1));
            }
        }
        Log.d("PLANROUTE", userPreferences.toString());
        campus.calculateRoute(source, destination, userPreferences);
        canvas.postInvalidate();
        if (campus.getCalculatedPathList().size() == 1) {
            Toast.makeText(this, "Sorry, a path could not be found at the current time", Toast.LENGTH_LONG).show();
        }
        Log.d("PLANROUTE", "Route calculation finished, path size: " + String.valueOf(campus.getCalculatedPathList().size()));
    }




    public void checkForGraphUpdate() {
        final DatabaseHelper helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor versionCursor = helper.getVersionNum();
        versionCursor.moveToNext();
        final int currentClientVersion = versionCursor.getInt(0);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.25:3000/version", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Version HTTP RESP", response.toString());
                try {
                    if (currentClientVersion < response.getInt("VersionNum")) {
                        getUpdatedGraphData(response.getInt("VersionNum"), helper);
                    } else {
                        setUpGraph();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("Version HTTP ERR", "No connection could be made");
                } else {
                    Log.e("Version HTTP ERR", error.getMessage());
                }
                setUpGraph();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QifQ.eIRXuuKEmcqrZ2GHGcu0fGp5ypHcNy1gxTJBZ11Dz-I");
                return headers;
            }
        };
        RequestQueueHandler.getInstance().addToRequestQueue(jor);
        Log.d("HTTP REQUEST", "VERSION REQ SENT");
    }

    public void getUpdatedGraphData(final int newVersionNum, final DatabaseHelper helper) {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.25:3000/graphdata", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("GraphUpdate HTTP RESP", response.toString());
                helper.updateGraphData(helper.getWritableDatabase(), newVersionNum, response);
                setUpGraph();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("GraphUpdate HTTP ERR", "No connection could be made");
                } else {
                    Log.e("GraphUpdate HTTP ERR", error.getMessage());
                }
                setUpGraph();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QifQ.eIRXuuKEmcqrZ2GHGcu0fGp5ypHcNy1gxTJBZ11Dz-I");
                return headers;
            }
        };
        RequestQueueHandler.getInstance().addToRequestQueue(jor);
        Log.d("HTTP REQUEST", "UPDATE REQ SENT");

    }

    public void showVertexDialog(final Vertex vertex) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_vertex, null);
        myBuilder.setView(mView);
        final AlertDialog dialog = myBuilder.create();

        dialog.show();
        dialog.getWindow().setLayout(500, 360);
        Button sourceButton = (Button) mView.findViewById(R.id.vertex_select_source_button);
        sourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campus.setSource(vertex);
                canvas.postInvalidate();
                dialog.dismiss();
            }
        });
        Button destinationButton = mView.findViewById(R.id.vertex_select_destination_button);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campus.setDestination(vertex);
                canvas.postInvalidate();
                dialog.dismiss();
            }
        });
        Button incidentButton = mView.findViewById(R.id.vertex_select_incident_button);
        incidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentIncident = new Intent(MainActivity.this, IncidentReporting.class);
                intentIncident.putExtra("VertexId", vertex.getId());
                startActivityForResult(intentIncident, 1);
            }
        });

    }

    public void getIncidents() {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, "http://192.168.0.25:3000/incident/list", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("IncidentList HTTP RESP", response.toString());
                helper.updateIncidents(helper.getWritableDatabase(), response);
                updateGraphWithIncidents();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("IncidentList HTTP ERR", "No connection could be made");
                } else {
                    Log.e("IncidentList HTTP ERR", error.getMessage());
                }
                Toast.makeText(MyApp.get(), "Failed to retrieve incidents data", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QifQ.eIRXuuKEmcqrZ2GHGcu0fGp5ypHcNy1gxTJBZ11Dz-I");
                return headers;
            }
        };
        RequestQueueHandler.getInstance().addToRequestQueue(jor);
        Log.d("HTTP REQUEST", "IncidentList REQ SENT");

    }

    public void updateGraphWithIncidents() {
        List<Incident> incidents = helper.getIncidents();
        campus.setIncidentVertexList(new ArrayList<Vertex>());
        for (Map.Entry<Integer, Vertex> e : campus.getVertexMap().entrySet()) {
            e.getValue().setIncident(null);
        }
        if (incidents.size() > 0) {
            for (Incident i : incidents) {
                campus.getIncidentVertexList().add(campus.getVertexMap().get(i.getVertexId()));
                campus.getVertexMap().get(i.getVertexId()).setIncident(i);
            }
        }
        canvas.postInvalidate();
    }
}
