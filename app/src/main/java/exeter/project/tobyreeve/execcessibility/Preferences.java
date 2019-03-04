package exeter.project.tobyreeve.execcessibility;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Map;

public class Preferences extends AppCompatActivity {
    //TODO: Add more Preferences
    //TODO: Typefacespan?

    DatabaseHelper helper;
    Map<String, Object> prefsWidgetMap;
    public static final String AVOID_STAIRCASES = "AvoidStaircases";
    public static final String AVOID_INCIDENTS = "AvoidIncidents";
    public static final String DISTANCE_OVER_ALTITUDE = "DistanceOverAltitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        helper = new DatabaseHelper(this);
        SeekBar stairsSeekbar = (SeekBar) findViewById(R.id.avoid_staircases_seeker);
        SeekBar distanceOverAltitudeSeekbar = (SeekBar) findViewById(R.id.distance_over_steep_seekbar);
        FloatingActionButton cancelPreferencesButton = (FloatingActionButton) findViewById(R.id.cancel_preferences_button);
        FloatingActionButton savePreferencesButton = (FloatingActionButton) findViewById(R.id.save_preferences_button);
        CheckBox incidentsCheckBox = (CheckBox) findViewById(R.id.avoid_incidents_checkbox);

        prefsWidgetMap = new HashMap<String, Object>();
        prefsWidgetMap.put(AVOID_STAIRCASES, stairsSeekbar);
        prefsWidgetMap.put(DISTANCE_OVER_ALTITUDE, distanceOverAltitudeSeekbar);
        prefsWidgetMap.put(AVOID_INCIDENTS, incidentsCheckBox);
        loadPreferences();

        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
            }
        });

        cancelPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreferences();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadPreferences() {
        Log.d("LOAD PREFS", "Start loading preferences");
        helper.getReadableDatabase();
        Cursor prefCursor = helper.getUserPreferenceData();
        for (int i = 0; i < prefCursor.getCount(); i++) {
            while (prefCursor.moveToNext()) {
                switch (prefCursor.getString(0)) {
                    case AVOID_STAIRCASES:
                        ((SeekBar) prefsWidgetMap.get(AVOID_STAIRCASES)).setProgress(prefCursor.getInt(1));
                        break;
                    case DISTANCE_OVER_ALTITUDE:
                        ((SeekBar) prefsWidgetMap.get(DISTANCE_OVER_ALTITUDE)).setProgress(prefCursor.getInt(1));
                        break;
                    case AVOID_INCIDENTS:
                        ((CheckBox) prefsWidgetMap.get(AVOID_INCIDENTS)).setChecked(prefCursor.getInt(1) == 1 ? true : false);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void savePreferences() {
        Log.d("SAVE PREFS", "Start saving preference values");
        helper.updateUserPreference(helper.getWritableDatabase(), AVOID_STAIRCASES, ((SeekBar) prefsWidgetMap.get(AVOID_STAIRCASES)).getProgress());
        helper.updateUserPreference(helper.getWritableDatabase(), DISTANCE_OVER_ALTITUDE, ((SeekBar) prefsWidgetMap.get(DISTANCE_OVER_ALTITUDE)).getProgress());
        helper.updateUserPreference(helper.getWritableDatabase(), AVOID_INCIDENTS, ((CheckBox) prefsWidgetMap.get(AVOID_INCIDENTS)).isChecked() ? 1 : 0);
        Log.d("SAVE PREFS", "Save successful!");
    }
}
