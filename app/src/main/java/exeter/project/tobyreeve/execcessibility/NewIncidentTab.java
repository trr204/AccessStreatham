package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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
        JSONObject json = new JSONObject();
        String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(Calendar.getInstance().getTime());

        try {
            json = new JSONObject().put("vertexId", vertexId).put("incidentDescription", description).put("incidentReportTime", timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, "http://192.168.0.25:3000/incident/report", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("IncidntReport HTTP RESP", response.toString());
                Toast.makeText(MyApp.get(), "Report submitted!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ORIGIN", "NewIncidentTab");
                getActivity().setResult(RESULT_OK, returnIntent);
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("IncidntReport HTTP ERR", "No connection could be made");
                } else {
                    Log.e("IncidntReport HTTP ERR", error.getMessage());
                }
                Toast.makeText(MyApp.get(), "Failed to submit report.", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-auth", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QifQ.eIRXuuKEmcqrZ2GHGcu0fGp5ypHcNy1gxTJBZ11Dz-I");
                return headers;
            }
        };
        RequestQueueHandler.getInstance().addToRequestQueue(jor);
        Log.d("HTTP REQUEST", "IncidntReport REQ SENT");
    }


}
