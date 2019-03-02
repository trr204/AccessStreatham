package exeter.project.tobyreeve.execcessibility;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentIncidentTab extends Fragment {
    DatabaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incident_current, container, false);
        helper = new DatabaseHelper(MyApp.get());
        helper.getReadableDatabase();
        final int vertexId = this.getArguments().getInt("VertexId");
        Incident incident = helper.getIncidentData(vertexId);
        TextView descriptionText = view.findViewById(R.id.currentIncidentDescription);
        TextView reportedAtText = view.findViewById(R.id.currentIncidentReportedAt);
        TextView reportLabel = view.findViewById(R.id.reported_at_label);

        Button removeReportButton = view.findViewById(R.id.remove_current_incident_button);
        if (incident.getId() > 0) {
            descriptionText.setText(incident.getDescription());
            reportedAtText.setText(incident.getReportedAtTime());
            reportedAtText.setVisibility(View.VISIBLE);
            removeReportButton.setVisibility(View.VISIBLE);
            reportLabel.setVisibility(View.VISIBLE);
        } else {
            descriptionText.setText(incident.getDescription());
            reportedAtText.setVisibility(View.INVISIBLE);
            removeReportButton.setVisibility(View.INVISIBLE);
            reportLabel.setVisibility(View.INVISIBLE);
        }

        removeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeReport(vertexId);
            }
        });

        return view;
    }

    public void removeReport(int vertexId) {
        //TODO http request to remove current report
        Toast.makeText(MyApp.get(), String.valueOf(vertexId), Toast.LENGTH_SHORT).show();
    }
}
