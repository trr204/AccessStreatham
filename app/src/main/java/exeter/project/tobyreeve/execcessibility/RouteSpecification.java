package exeter.project.tobyreeve.execcessibility;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteSpecification extends AppCompatActivity {

    DatabaseHelper helper;
    //TODO: RouteSpecification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_specification);

        final Spinner source = findViewById(R.id.spinner_source);
        final Spinner destination = findViewById(R.id.spinner_destination);

        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        /*Cursor bCursor = helper.getBuildingNames();
        List<String> buildings = new ArrayList<String>();
        for (int i = 0; i < bCursor.getCount(); i++) {
           while (bCursor.moveToNext()) {
               buildings.add(bCursor.getString(0));
           }
        }
        Collections.sort(buildings);
        source.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings));
        destination.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings));*/
        Cursor vCursor = helper.getVertexLabelData();
        List<String> sourceVertexLabels = new ArrayList<String>();
        List<Integer> sourceVertexIds = new ArrayList<Integer>();
        List<String> destinationVertexLabels = new ArrayList<String>();
        List<Integer> destinationVertexIds = new ArrayList<Integer>();
        Map<String, Integer> vertexMap = new HashMap<String, Integer>();
        for (int i = 0; i < vCursor.getCount(); i++) {
            while (vCursor.moveToNext()) {
                vertexMap.put(vCursor.getString(8), vCursor.getInt(0));
            }
        }
        List<String> vertices = new ArrayList<>(vertexMap.keySet());
        Collections.sort(vertices);

        for (String entry : vertices) {
            sourceVertexIds.add(vertexMap.get(entry));
            sourceVertexLabels.add(entry);
            destinationVertexIds.add(vertexMap.get(entry));
            destinationVertexLabels.add(entry);

        }
        int customSourceId = getIntent().getIntExtra("Source", 0);
        int customDestinationId = getIntent().getIntExtra("Destination", 0);
        int sourcePos = 0;
        int destinationPos = 0;
        if (customSourceId != 0) {
            boolean exists = false;
            int pos = 0;
            for (int i : sourceVertexIds) {
                if (i == customSourceId) {
                    exists = true;
                } else if (!exists) {
                    pos++;
                }
            }
            if (exists) {
                sourcePos = pos;
            } else {
                sourceVertexIds.add(customSourceId);
                sourcePos = sourceVertexIds.size()-1;
                sourceVertexLabels.add("User-selected source");
            }
        }
        if (customDestinationId != 0) {
            boolean exists = false;
            int pos = 0;
            for (int i : destinationVertexIds) {
                if (i == customDestinationId) {
                    exists = true;
                } else if (!exists) {
                    pos++;
                }
            }
            if (exists) {
                destinationPos = pos;
            } else {
                destinationVertexIds.add(customDestinationId);
                destinationPos = destinationVertexIds.size()-1;
                destinationVertexLabels.add("User-selected destination");
            }
        }
        final List<Integer> sourceVertexIdsFinal = sourceVertexIds;
        final List<Integer> destinationVertexIdsFinal = destinationVertexIds;
        //TODO Persist source and destination for easy recalculation?
        source.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceVertexLabels));
        destination.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destinationVertexLabels));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        source.setSelection(sourcePos);
        destination.setSelection(destinationPos);

        ImageButton swapButton = findViewById(R.id.swap_button);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currSource = source.getSelectedItemPosition();
                int currDest = destination.getSelectedItemPosition();
                source.setSelection(currDest);
                destination.setSelection(currSource);
            }
        });

        Button calculateButton = findViewById(R.id.route_specified_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent returnIntent = new Intent();
               returnIntent.putExtra("ORIGIN", "RouteSpecification");
               returnIntent.putExtra("SOURCE", sourceVertexIdsFinal.get(source.getSelectedItemPosition()).toString());
               returnIntent.putExtra("DESTINATION", destinationVertexIdsFinal.get(destination.getSelectedItemPosition()).toString());
               setResult(RESULT_OK, returnIntent);
               finish();
           }
        });
    }
}
