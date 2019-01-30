package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        Cursor vCursor = helper.getVertexData();
        List<String> vertices = new ArrayList<String>();
        for (int i = 0; i < vCursor.getCount(); i++) {
            while (vCursor.moveToNext()) {
                vertices.add(String.valueOf(vCursor.getInt(0)));
            }
        }
        source.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vertices));
        destination.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vertices));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
               returnIntent.putExtra("SOURCE", source.getSelectedItem().toString());
               returnIntent.putExtra("DESTINATION", destination.getSelectedItem().toString());
               setResult(RESULT_OK, returnIntent);
               finish();
           }
                                           }

        );
    }
}
