package exeter.project.tobyreeve.execcessibility;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preferences extends AppCompatActivity {
    //TODO: Add more Preferences
    //TODO: Typefacespan?

    DatabaseHelper helper;
    Map<String, SeekBar> seekBarMap;
    public static final String AVOID_STAIRCASES = "AvoidStaircases";
    public static final String DISTANCE_OVER_ALTITUDE = "DistanceOverAltitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        helper = new DatabaseHelper(this);
        SeekBar stairsSeekbar = (SeekBar) findViewById(R.id.avoid_staircases_seeker);
        SeekBar distanceOverAltitudeSeekbar = (SeekBar) findViewById(R.id.distance_over_steep_seekbar);
        ImageButton cancelPreferencesButton = (ImageButton) findViewById(R.id.cancel_preferences_button);
        ImageButton savePreferencesButton = (ImageButton) findViewById(R.id.save_preferences_button);

        seekBarMap = new HashMap<String, SeekBar>();
        seekBarMap.put(AVOID_STAIRCASES, stairsSeekbar);
        seekBarMap.put(DISTANCE_OVER_ALTITUDE, distanceOverAltitudeSeekbar);
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
        helper.getReadableDatabase();
        Cursor prefCursor = helper.getUserPreferenceData();
        for (int i = 0; i < prefCursor.getCount(); i++) {
            while (prefCursor.moveToNext()) {
                switch (prefCursor.getString(0)) {
                    case AVOID_STAIRCASES:
                        seekBarMap.get(AVOID_STAIRCASES).setProgress(prefCursor.getInt(1));
                        break;
                    case DISTANCE_OVER_ALTITUDE:
                        seekBarMap.get(DISTANCE_OVER_ALTITUDE).setProgress(prefCursor.getInt(1));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void savePreferences() {
        //Snackbar.make(view, "Saving Preferences!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        for (Map.Entry<String, SeekBar> e : seekBarMap.entrySet()) {
            helper.updateUserPreference(helper.getWritableDatabase(), e.getKey(), e.getValue().getProgress());
        }
        Toast.makeText(this, "Save successful!", Toast.LENGTH_LONG).show();
    }
}
