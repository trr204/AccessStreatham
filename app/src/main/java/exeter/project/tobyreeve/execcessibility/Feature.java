package exeter.project.tobyreeve.execcessibility;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Feature extends AppCompatActivity {
    DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        String buildingName = getIntent().getStringExtra("name");
        setTitle(buildingName);
        helper = new DatabaseHelper(this);
        helper.getReadableDatabase();
        Cursor nameCursor = helper.getBuildingId(buildingName);
        nameCursor.moveToFirst();
        Cursor featureCursor = helper.getBuildingFeatureData(nameCursor.getInt(0));

        TextView featureList = (TextView)  findViewById(R.id.textView);
        featureList.setText(Html.fromHtml("Features: <br/>"));
        for (int i = 0; i < featureCursor.getCount(); i++) {
            while(featureCursor.moveToNext()) {
                featureList.append(Html.fromHtml("&#8226; " + (featureCursor.getString(0)) + "<br/>"));
            }
        }
    }
}
