package exeter.project.tobyreeve.execcessibility;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.DELETE, "http://192.168.0.25:3000/incident/remove/"+String.valueOf(vertexId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("IncidntRemove HTTP RESP", response.toString());
                Toast.makeText(MyApp.get(), "Report removed!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ORIGIN", "CurrentIncidentTab");
                getActivity().setResult(RESULT_OK, returnIntent);
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Log.e("IncidntRemove HTTP ERR", "No connection could be made");
                } else {
                    Log.e("IncidntRemove HTTP ERR", error.getMessage());
                }
                Toast.makeText(MyApp.get(), "Failed to remove report.", Toast.LENGTH_LONG).show();
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
        Log.d("HTTP REQUEST", "IncidntRemove REQ SENT");
    }
}
