package exeter.project.tobyreeve.execcessibility;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

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
        Cursor nameCursor = helper.getBuildingData(buildingName);
        nameCursor.moveToFirst();
        Cursor featureCursor = helper.getBuildingFeatureData(nameCursor.getInt(0));
        ImageView buildingImage = findViewById(R.id.buildingImage);
        if (buildingName.equals("Harrison")) {
            buildingImage.setImageDrawable(getResources().getDrawable(R.drawable.harrison_building));
        }
        TextView buildingDescription = findViewById(R.id.buildingDescription);
        buildingDescription.setText(Html.fromHtml("<b>Description</b>: "+nameCursor.getString(1)));
        TextView featureList = findViewById(R.id.featureList);
        featureList.setText(Html.fromHtml("<b>Features</b>: <br/>"));
        for (int i = 0; i < featureCursor.getCount(); i++) {
            while(featureCursor.moveToNext()) {
                if (featureCursor.getString(1).equals("")) {
                    featureList.append(Html.fromHtml("&#8226; " + (featureCursor.getString(0)) + "<br/>"));
                } else {
                    featureList.append(Html.fromHtml("&#8226; " + (featureCursor.getString(0)) + " - " + featureCursor.getString(1) + "<br/>"));
                }
            }
        }
    }
}
