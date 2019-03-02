package exeter.project.tobyreeve.execcessibility;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewIncidentTab extends Fragment {
    DatabaseHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incident_new, container, false);

        helper = new DatabaseHelper(MyApp.get());
        helper.getReadableDatabase();
        Bundle bundle = this.getArguments();
        final int vertexId = bundle.getInt("VertexId");

        Button submitReportButton = view.findViewById(R.id.report_new_incident_button);
        //TODO Set onClickListener

        final Spinner spinner = view.findViewById(R.id.incident_type_spinner);
        List<String> incidentTypeList = new ArrayList<String>();
        incidentTypeList.add("Obstruction - Cannot pass");
        incidentTypeList.add("Obstruction - Can pass with difficulty");
        incidentTypeList.add("Public Event - Cannot pass");
        incidentTypeList.add("Public Event - Can pass with difficulty");

        spinner.setAdapter(new ArrayAdapter<String>(MyApp.get(), android.R.layout.simple_spinner_item, incidentTypeList));

        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport(vertexId, (String) spinner.getSelectedItem());
            }
        });

        return view;
    }

    public void submitReport(int vertexId, String description) {
        //TODO http request to submit new report

        Toast.makeText(MyApp.get(), String.valueOf(vertexId) + ":" + description, Toast.LENGTH_SHORT).show();
    }

}
