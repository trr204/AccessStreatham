package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap tile = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        canvasWidth = tile.getWidth();
        canvasHeight = tile.getHeight();
        setUpGraph();
        scrollView = new ScrollView(this);
        hScrollView = new HorizontalScrollView(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        hScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        canvas = new MyCanvas(this, campus);
        canvas.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(canvas);
        scrollView.scrollTo(0, tile.getHeight());
        hScrollView.addView(scrollView);
        hScrollView.scrollTo(tile.getWidth(), 0);

        setContentView(hScrollView);

        //Toast.makeText(this, String.valueOf(canvasWidth) + "/" + String.valueOf(canvasHeight), Toast.LENGTH_LONG).show();
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
                Intent intentAbout = new Intent(this, About.class);
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
            case R.id.temproute:
                planRoute(start,end);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpGraph() {

        List<Vertex> vertexList = new ArrayList<Vertex>();
        List<Edge> edgeList = new ArrayList<Edge>();
        List<EdgeVertexJoin> joinList = new ArrayList<EdgeVertexJoin>();

        double maxLatitude = 50.7380400;
        double minLatitude = 50.7360000;
        double maxLongitude = -3.5295900;
        double minLongitude = -3.5350400;

        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor vCursor = helper.getVertexData();
        Cursor eCursor = helper.getEdgeData();
        Cursor jCursor = helper.getEdgeVertexJoinData();

        for (int i = 0; i < vCursor.getCount(); i++) {
            while(vCursor.moveToNext()) {
                vertexList.add(new Vertex(vCursor.getInt(0), vCursor.getLong(1), vCursor.getFloat(2), vCursor.getFloat(3)));
                if (vCursor.getLong(1) == 299835590) {
                    start = vertexList.get(vertexList.size()-1);
                }
                if (vCursor.getLong(1) == 2179870824L) {
                    end = vertexList.get(vertexList.size()-1);
                }

            }
        }
        for (int j = 0; j < jCursor.getCount(); j++) {
            while (jCursor.moveToNext()) {
                joinList.add(new EdgeVertexJoin(jCursor.getInt(0), jCursor.getInt(1), jCursor.getInt(2), jCursor.getInt(3)));
            }
        }
        for (int i = 0; i < eCursor.getCount(); i++) {
            while(eCursor.moveToNext()) {
                edgeList.add(new Edge(eCursor.getInt(0), eCursor.getLong(1), new HashMap<Integer, Vertex>()));
            }
        }

        Collections.sort(edgeList);
        Collections.sort(vertexList);
        Collections.sort(joinList);
        Map<Integer, Vertex> vertexMap = new HashMap<Integer, Vertex>();
        for (Vertex v : vertexList) {
            v.setY((1-(v.getLatitude() - minLatitude)/(maxLatitude - minLatitude)) * canvasHeight);
            v.setX(((v.getLongitude() - minLongitude)/(maxLongitude - minLongitude))*canvasWidth);
            vertexMap.put(v.getId(), v);
        }


        int i = 0;
        for (int j = 0; j < joinList.size(); j++) {
            if (joinList.get(j).getEdgeId() == edgeList.get(i).getId()) {
                    edgeList.get(i).getVertexList().put(joinList.get(j).getVertexPosition(), vertexMap.get(joinList.get(j).getVertexId()));
            }
            if ((j+1 < joinList.size()) && joinList.get(j).getEdgeId() != joinList.get(j+1).getEdgeId()) {
                i++;
            }
        }

        List<Subedge> subedgeList = new ArrayList<Subedge>();
        for (Edge e : edgeList) {
            for (int s = 0; s < e.getVertexList().size() - 1; s++) {
                subedgeList.add(new Subedge(e.getId(), e.getVertexList().get(s).getId(), e.getVertexList().get(s+1).getId()));
            }
        }

        Map<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
        for (Edge e : edgeList) {
            edgeMap.put(e.getId(), e);
        }

        campus = new Graph(edgeMap, vertexMap, subedgeList,minLongitude , maxLongitude,minLatitude ,maxLatitude);
       /* TextView mainText = (TextView)  findViewById(R.id.mainText);
        mainText.setText(Html.fromHtml("Vertices per Edge: <br/>"));
        for (int k = 0; k < edgeList.size(); k++) {
            mainText.append(Html.fromHtml("&#8226; Edge " + edgeList.get(k).getId() + ": " + edgeList.get(k).getVertexList().size() + "<br/>"));
        }*/
    }

    public void planRoute(Vertex source, Vertex destination) {

        List<Vertex> path = campus.calculateRoute(source, destination);
        //Toast.makeText(this, String.valueOf(path.size()), Toast.LENGTH_LONG).show();
        canvas.postInvalidate();
    }
}
