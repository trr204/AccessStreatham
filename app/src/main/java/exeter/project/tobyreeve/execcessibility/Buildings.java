package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Integer.MAX_VALUE;

public class Buildings extends AppCompatActivity {
    DatabaseHelper helper;
    private PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);

        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor cursor = helper.getBuildingNames();
        ArrayList<String > buildings = new ArrayList<String>();
        for (int i = 0; i < cursor.getCount(); i++) {
            while(cursor.moveToNext()) {
                buildings.add(cursor.getString(0));
            }
        }
        ListAdapter buildingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);

        ListView buildingsListView = (ListView) findViewById(R.id.buildingsListView);
        buildingsListView.setAdapter(buildingsAdapter);

        buildingsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String building = String.valueOf(parent.getItemAtPosition(position));

                        Intent buildingIntent = new Intent(Buildings.this, Feature.class);
                        buildingIntent.putExtra("name",building);
                        Buildings.this.startActivity(buildingIntent);
                    }
                }
        );
    }

}
