package exeter.project.tobyreeve.execcessibility;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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

        Spinner source = findViewById(R.id.spinner_source);
        Spinner destination = findViewById(R.id.spinner_destination);

        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor bCursor = helper.getBuildingNames();
        List<String> buildings = new ArrayList<String>();
        for (int i = 0; i < bCursor.getCount(); i++) {
           while (bCursor.moveToNext()) {
               buildings.add(bCursor.getString(0));
           }
        }
        Collections.sort(buildings);
        source.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings));
        destination.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
