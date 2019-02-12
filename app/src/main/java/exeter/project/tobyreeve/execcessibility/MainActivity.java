package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
    ScrollView scrollView;
    HorizontalScrollView hScrollView;
    int canvasWidth;
    int canvasHeight;
    Vertex start;
    Vertex end;

    final double maxLatitude = 50.7380400;
    final double minLatitude = 50.7360000;
    final double maxLongitude = -3.5295900;
    final double minLongitude = -3.5350400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap tile = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        RequestQueue queue = RequestQueueHandler.getInstance().getRequestQueue();
        canvasWidth = tile.getWidth();
        canvasHeight = tile.getHeight();
        checkForGraphUpdate();

        canvas = new MyCanvas(this);
        canvas.getSettings().setBuiltInZoomControls(true);
        canvas.getSettings().setDisplayZoomControls(true);
        canvas.loadUrl("file:///android_res/drawable/test1.png");
        setContentView(R.layout.activity_main);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
        layout.addView(canvas);

        FloatingActionButton planRouteFab = findViewById(R.id.plan_route_button);
        planRouteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PLANROUTE", "Planning a route fab clicked!");
                Intent intentPlan = new Intent(MainActivity.this, RouteSpecification.class);
                startActivityForResult(intentPlan, 1);


                //TODO:
                //click button
                //new popup?
                //list of specific sources and destinations (including multiple entrances for some buildings) attributed to specific nodes on the graph (plus current user location if enabled)
                //choose source - populate spinner from database
                //choose destination - populate spinner from database
                //make source and destination interchangeable
                //click calculate button = planRoute();
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
                vertexList.add(new Vertex(vCursor.getInt(0), vCursor.getLong(1), vCursor.getFloat(2), vCursor.getFloat(3)));

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
                edgeList.add(new Edge(eCursor.getInt(0), eCursor.getLong(1), new HashMap<Integer, Vertex>()));
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
                subedgeList.add(new Subedge(e.getId(), e.getVertexList().get(s).getId(), e.getVertexList().get(s+1).getId()));
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
        canvas.postInvalidate();
        Log.d("SETUPGRAPH", "Finished. Vertex total: " + String.valueOf(campus.getVertexMap().size()) + ", Edge total: " + String.valueOf(campus.getEdgeMap().size()));
    }

    public void clearRoute() {
        campus.setCalculatedPathList(new ArrayList<Vertex>());
        canvas.postInvalidate();
        Log.d("CLEARROUTE", "Route cleared");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Vertex source = campus.getVertexMap().get(Integer.valueOf(intent.getStringExtra("SOURCE")));
                Vertex destination = campus.getVertexMap().get(Integer.valueOf(intent.getStringExtra("DESTINATION")));
                clearRoute();
                Log.d("PLANROUTE", "Source and destination acquired on MainActivity from RouteSpecification");
                planRoute(source, destination);
            }
        }
    }

    public void planRoute(Vertex source, Vertex destination) {

        List<Vertex> path = campus.calculateRoute(source, destination);
        //Toast.makeText(this, String.valueOf(path.size()), Toast.LENGTH_LONG).show();
        canvas.postInvalidate();
        Log.d("PLANROUTE", "Route calculation finished");
    }




    public void checkForGraphUpdate() {
        final DatabaseHelper helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor versionCursor = helper.getVersionNum();
        versionCursor.moveToNext();
        final int currentClientVersion = versionCursor.getInt(0);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:3000/version", null, new Response.Listener<JSONObject>() {
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
        Log.d("HTTP REQUEST", "SENT");
    }

    public void getUpdatedGraphData(final int newVersionNum, final DatabaseHelper helper) {
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:3000/graphdata", null, new Response.Listener<JSONObject>() {
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
        Log.d("HTTP REQUEST", "SENT");

    }
}
