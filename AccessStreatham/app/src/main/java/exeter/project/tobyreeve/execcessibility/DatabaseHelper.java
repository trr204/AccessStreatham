package exeter.project.tobyreeve.execcessibility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "EXccessibility.db";
    public static final String BUILDING_TABLE_NAME = "Building";
    public static final String BUILDING_TABLE_COLUMN_ONE = "ID";
    public static final String BUILDING_TABLE_COLUMN_TWO = "Name";
    public static final String BUILDING_TABLE_COLUMN_THREE = "Description";
    public static final String FEATURE_TABLE_NAME = "Feature";
    public static final String FEATURE_TABLE_COLUMN_ONE = "ID";
    public static final String FEATURE_TABLE_COLUMN_TWO = "BuildingID";
    public static final String FEATURE_TABLE_COLUMN_THREE = "Name";
    public static final String FEATURE_TABLE_COLUMN_FOUR = "Description";
    public static final String VERTEX_TABLE_NAME = "Vertex";
    public static final String VERTEX_TABLE_COLUMN_ONE = "ID";
    public static final String VERTEX_TABLE_COLUMN_TWO = "OSMID";
    public static final String VERTEX_TABLE_COLUMN_THREE = "Latitude";
    public static final String VERTEX_TABLE_COLUMN_FOUR = "Longitude";
    public static final String VERTEX_TABLE_COLUMN_FIVE = "Elevation";
    public static final String VERTEX_TABLE_COLUMN_SIX = "Label";
    public static final String INCIDENT_TABLE_NAME = "Incident";
    public static final String INCIDENT_TABLE_COLUMN_ONE = "ReportID";
    public static final String INCIDENT_TABLE_COLUMN_TWO = "VertexID";
    public static final String INCIDENT_TABLE_COLUMN_THREE = "Description";
    public static final String INCIDENT_TABLE_COLUMN_FOUR = "ReportedAt";
    public static final String EDGE_TABLE_NAME = "Edge";
    public static final String EDGE_TABLE_COLUMN_ONE = "ID";
    public static final String EDGE_TABLE_COLUMN_TWO = "OSMID";
    public static final String EDGE_TABLE_COLUMN_THREE = "StartVertexId";
    public static final String EDGE_TABLE_COLUMN_FOUR = "EndVertexId";
    public static final String EDGE_TABLE_COLUMN_FIVE = "Stairs";
    public static final String EDGE_VERTEX_JOIN_TABLE_NAME = "Edge_Vertex";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_ONE = "ID";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO = "EdgeId";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE = "VertexId";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR = "VertexPosition";
    public static final String USER_PREFERENCES_TABLE_NAME = "UserPreferences";
    public static final String USER_PREFERENCES_TABLE_COLUMN_ONE = "PrefKey";
    public static final String USER_PREFERENCES_TABLE_COLUMN_TWO = "PrefValue";
    public static final String DATABASE_VERSION_TABLE_NAME = "Dataset_Version";
    public static final String DATABASE_VERSION_TABLE_COLUMN_ONE = "VersionNum";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 48);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE CREATE", "Start creating database tables");
        Log.d("DATABASE CREATE", "Create Building table");
        String sql = "CREATE TABLE " + BUILDING_TABLE_NAME + " (" +
                BUILDING_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                BUILDING_TABLE_COLUMN_TWO + " TEXT," + //NAME
                BUILDING_TABLE_COLUMN_THREE + " TEXT)"; //DESCRIPTION
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create UserPreferences table");
        sql = "CREATE TABLE " + USER_PREFERENCES_TABLE_NAME + " (" +
                USER_PREFERENCES_TABLE_COLUMN_ONE + " TEXT PRIMARY KEY," + //PREFKEY
                USER_PREFERENCES_TABLE_COLUMN_TWO + " INTEGER)"; //PREFVALUE
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create Feature table");
        sql = "CREATE TABLE " + FEATURE_TABLE_NAME + " (" +
                FEATURE_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                FEATURE_TABLE_COLUMN_TWO + " INTEGER," + //BUILDINGID
                FEATURE_TABLE_COLUMN_THREE + " TEXT," + //NAME
                FEATURE_TABLE_COLUMN_FOUR + " TEXT," + //DESCRIPTION
                "FOREIGN KEY("+FEATURE_TABLE_COLUMN_TWO+") REFERENCES "+BUILDING_TABLE_NAME+"("+BUILDING_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create Vertex table");
        sql = "CREATE TABLE " + VERTEX_TABLE_NAME + " (" +
                VERTEX_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                VERTEX_TABLE_COLUMN_TWO + " TEXT," + //VERTEX'S OSM ID
                VERTEX_TABLE_COLUMN_THREE + " REAL," + //LATITUDE
                VERTEX_TABLE_COLUMN_FOUR + " REAL," + //LONGITUDE
                VERTEX_TABLE_COLUMN_FIVE + " REAL," + //ELEVATION
                VERTEX_TABLE_COLUMN_SIX + " TEXT)"; //LABEL
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create Incident table");
        sql = "CREATE TABLE " + INCIDENT_TABLE_NAME + " (" +
                INCIDENT_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                INCIDENT_TABLE_COLUMN_TWO + " INTEGER," + //VERTEXID
                INCIDENT_TABLE_COLUMN_THREE + " TEXT," + //DESCRIPTION
                INCIDENT_TABLE_COLUMN_FOUR + " TEXT)"; //REPORTEDAT
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create Edge table");
        sql = "CREATE TABLE " + EDGE_TABLE_NAME + " (" +
                EDGE_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                EDGE_TABLE_COLUMN_TWO + " TEXT," + //EDGE'S OSM ID
                EDGE_TABLE_COLUMN_THREE + " INTEGER," + //START VERTEX'S ID
                EDGE_TABLE_COLUMN_FOUR + " INTEGER," + //END VERTEX'S ID
                EDGE_TABLE_COLUMN_FIVE + " INTEGER," + //STAIRS T/F
                "FOREIGN KEY("+EDGE_TABLE_COLUMN_THREE+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+")," +
                "FOREIGN KEY("+EDGE_TABLE_COLUMN_FOUR+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create Edge_Vertex_Association table");
        sql = "CREATE TABLE " + EDGE_VERTEX_JOIN_TABLE_NAME + " (" +
                EDGE_VERTEX_JOIN_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO + " INTEGER," + //EDGE'S ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE + " INTEGER," + //VERTEX'S ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR + " INTEGER," + //VERTEX POSITION
                "FOREIGN KEY("+EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO+") REFERENCES "+EDGE_TABLE_NAME+"("+EDGE_TABLE_COLUMN_ONE+")," +
                "FOREIGN KEY("+EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Create DatabaseVersion table");
        sql = "CREATE TABLE " + DATABASE_VERSION_TABLE_NAME + " (" +
                DATABASE_VERSION_TABLE_COLUMN_ONE + " INTEGER)"; //ID
        db.execSQL(sql);
        Log.d("DATABASE CREATE", "Start inserting static data");

        Log.d("DATABASE CREATE", "Insert initial DatabaseVersion data");
        populateVersionNum(db, 1);

        Log.d("DATABASE CREATE", "Insert initial USerPreference data");
        populateUserPreferencesData(db, "AvoidStaircases", 0);
        populateUserPreferencesData(db, "DistanceOverAltitude", 0);
        populateUserPreferencesData(db, "AvoidIncidents", 0);

        Log.d("DATABASE CREATE", "Insert Building data");

        populateBuildingData(db, "Amory","");
        populateBuildingData(db, "Bill Douglas Cinema Museum","");
        populateBuildingData(db, "Building: One","");
        populateBuildingData(db, "Byrne House","");
        populateBuildingData(db, "Birks Grange Vilage","");
        populateBuildingData(db, "Alexander Building, Thornlea","Not visible on map (bottom left)");
        populateBuildingData(db, "Clayden","");
        populateBuildingData(db, "Clydesdale Court","");
        populateBuildingData(db, "Clydesdale House","");
        populateBuildingData(db, "Clydesdale Rise","");
        populateBuildingData(db, "Cornwall House","");
        populateBuildingData(db, "Devonshire House","");
        populateBuildingData(db, "Family Centre","");
        populateBuildingData(db, "Forum","");
        populateBuildingData(db, "Garden Hill House","Not visible on map (top right)");
        populateBuildingData(db, "Geoffrey Pope","");
        populateBuildingData(db, "Harrison","Part of the CEMPS faculty, named after former Vice-Chancellor Sir David Harrison.");
        populateBuildingData(db, "Great Hall","");
        populateBuildingData(db, "Hatherly Labs","");
        populateBuildingData(db, "Henry Wellcome","");
        populateBuildingData(db, "Higher Hoopern Cottage","Not visible on map (top right)");
        populateBuildingData(db, "Higher Hoopern Farm","Not visible on map (top right)");
        populateBuildingData(db, "Holland Hall","");
        populateBuildingData(db, "Holland Hall Studios","");
        populateBuildingData(db, "Hope Hall","");
        populateBuildingData(db, "Innovation Centre","");
        populateBuildingData(db, "Innovation Centre Phase 2","");
        populateBuildingData(db, "Institute of Arabic and Islamic Studies","");
        populateBuildingData(db, "INTO International Study Centre","");
        populateBuildingData(db, "Kay","");
        populateBuildingData(db, "Knightley","");
        populateBuildingData(db, "Lafrowda","");
        populateBuildingData(db, "Lafrowda Cottage","");
        populateBuildingData(db, "Lafrowda House","");
        populateBuildingData(db, "Laver","");
        populateBuildingData(db, "Lazenby","");
        populateBuildingData(db, "Library","Same as Forum");
        populateBuildingData(db, "Living Systems Institute","");
        populateBuildingData(db, "Lopes Hall","");
        populateBuildingData(db, "Mardon Hall","");
        populateBuildingData(db, "Mary Harris Memorial Chapel","");
        populateBuildingData(db, "Mood Disorders Centre","");
        populateBuildingData(db, "Nash Grove","");
        populateBuildingData(db, "Northcote House","");
        populateBuildingData(db, "Northcott Theatre","");
        populateBuildingData(db, "Old Library","");
        populateBuildingData(db, "Pennsylvania Court","");
        populateBuildingData(db, "Peter Chalk Centre","");
        populateBuildingData(db, "Physics Tower","");
        populateBuildingData(db, "Queens","");
        populateBuildingData(db, "Ransom Pickard","");
        populateBuildingData(db, "Reed Hall","");
        populateBuildingData(db, "Roborough Studios","");
        populateBuildingData(db, "Rowe House","");
        populateBuildingData(db, "Sports Park","");
        populateBuildingData(db, "St German's","");
        populateBuildingData(db, "Streatham Court","");
        populateBuildingData(db, "Streatham Farm","");
        populateBuildingData(db, "Student Health Centre","");
        populateBuildingData(db, "Washington Singer","");
        populateBuildingData(db, "White House, Thronlea","Not visible on map (bottom left)");
        populateBuildingData(db, "XFi","");
        populateBuildingData(db, "Estate Service Centre","");
        populateBuildingData(db, "Duryard","");
        populateBuildingData(db, "Newman Lecture Theatres","Located inside the Peter Chalk Centre");

        Log.d("DATABASE CREATE", "Insert Feature data");
        populateFeatureData(db, "Computer Science"," Windows Lab, Linux Lab, Mac Lab", "Harrison");
        populateFeatureData(db, "Engineering","", "Harrison");
        populateFeatureData(db, "Mathematical Science","", "Harrison");
        populateFeatureData(db, "Geography","", "Amory");
        populateFeatureData(db, "Student Services Centre","", "Forum");
        populateFeatureData(db, "Costa Coffee","", "Forum");
        populateFeatureData(db, "Newman Red","Lecture Theatre", "Newman Lecture Theatres");
        populateFeatureData(db, "Newman Blue","Lecture Theatre", "Newman Lecture Theatres");
        populateFeatureData(db, "Newman Green","Lecture Theatre", "Newman Lecture Theatres");
        populateFeatureData(db, "Newman Purple","Lecture Theatre", "Newman Lecture Theatres");
        populateFeatureData(db, "Newman Collaborative","Lecture Theatre", "Newman Lecture Theatres");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void updateGraphData(SQLiteDatabase db, int versionNum, JSONObject newData) {
        Log.d("DB VERSION UPDATE", "Begin database data update, new version number: " + String.valueOf(versionNum));
        Log.d("DB VERSION UPDATE", "Drop current data from Vertex, Edge, Edge_Vertex_Association and DatabaseVersion tables");
        db.execSQL("DELETE FROM " + VERTEX_TABLE_NAME);
        db.execSQL("DELETE FROM " + EDGE_TABLE_NAME);
        db.execSQL("DELETE FROM " + EDGE_VERTEX_JOIN_TABLE_NAME);

        Log.d("DB VERSION UPDATE", "Start extracting data from JSON object: " + newData.toString());
        try {
            JSONArray vertexArray = newData.getJSONArray("vertices");
            JSONArray edgeArray = newData.getJSONArray("edges");
            JSONArray joinsArray = newData.getJSONArray("joins");
            boolean successful = true;
            Log.d("DB VERSION UPDATE", "Start inserting new Vertex data");
            for (int i = 0; i < vertexArray.length(); i++) {
                JSONObject v = vertexArray.getJSONObject(i);
                boolean temp = populateVertexData(db, v.getInt("VertexId"), v.getString("OsmId"), v.getString("Latitude"), v.getString("Longitude"), v.getInt("Elevation"), v.getString("Label"));
                if (!temp) {
                    successful = false;
                }
            }
            Log.d("DB VERSION UPDATE", "Vertex data insert fully successful? " + String.valueOf(successful));
            Log.d("DB VERSION UPDATE", "Start inserting new Edge data");
            for (int i = 0; i < edgeArray.length(); i++) {
                JSONObject e = edgeArray.getJSONObject(i);
                boolean temp = populateEdgeData(db, e.getInt("EdgeId"), e.getString("OsmId"), e.getInt("StartVertexId"), e.getInt("EndVertexId"), e.getBoolean("Stairs") == true ? 1 : 0);
                if (!temp) {
                    successful = false;
                }
            }
            Log.d("DB VERSION UPDATE", "Edge data insert fully successful? " + String.valueOf(successful));
            Log.d("DB VERSION UPDATE", "Start inserting new Edge_Vertex_Association data");
            for (int i = 0; i < joinsArray.length(); i++) {
                JSONObject j = joinsArray.getJSONObject(i);
                boolean temp = populateEdgeVertexJoinData(db, j.getInt("AssociationId"), j.getInt("EdgeId"), j.getInt("VertexId"), j.getInt("VertexPos"));
                if (!temp) {
                    successful = false;
                }
            }
            Log.d("DB VERSION UPDATE", "Edge_Vertex_Association data insert fully successful? " + String.valueOf(successful));

            if (successful) {
                Log.d("DB VERSION UPDATE", "Insert new version number");
                db.execSQL("DELETE FROM " + DATABASE_VERSION_TABLE_NAME);
                populateVersionNum(db, versionNum);
            }
        } catch (JSONException e) {
            Log.e("DatHelp JSON ERROR", e.getMessage());
            e.printStackTrace();
        }

    }

    public boolean populateBuildingData(SQLiteDatabase db, String name, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUILDING_TABLE_COLUMN_TWO, name);
        contentValues.put(BUILDING_TABLE_COLUMN_THREE, description);
        long result = db.insert(BUILDING_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateVersionNum(SQLiteDatabase db, int versionNum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATABASE_VERSION_TABLE_COLUMN_ONE, versionNum);
        long result = db.insert(DATABASE_VERSION_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateUserPreferencesData(SQLiteDatabase db, String prefKey, int prefValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PREFERENCES_TABLE_COLUMN_ONE, prefKey);
        contentValues.put(USER_PREFERENCES_TABLE_COLUMN_TWO, prefValue);
        long result = db.insert(USER_PREFERENCES_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public void populateFeatureData(SQLiteDatabase db, String name, String description, String buildingName) {
        String sql = "INSERT INTO " + FEATURE_TABLE_NAME + " (" + FEATURE_TABLE_COLUMN_TWO + "," + FEATURE_TABLE_COLUMN_THREE + "," + FEATURE_TABLE_COLUMN_FOUR + ") VALUES (" +
                "(SELECT " + BUILDING_TABLE_COLUMN_ONE + " FROM " + BUILDING_TABLE_NAME + " WHERE " + BUILDING_TABLE_COLUMN_TWO + " = '" + buildingName + "'), '" + name + "', '" + description + "')";
        db.execSQL(sql);
    }

    public boolean populateVertexData(SQLiteDatabase db, int id, String osmID, String latitude, String longitude, int elevation, String label) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VERTEX_TABLE_COLUMN_ONE, id);
        contentValues.put(VERTEX_TABLE_COLUMN_TWO, osmID);
        contentValues.put(VERTEX_TABLE_COLUMN_THREE, latitude);
        contentValues.put(VERTEX_TABLE_COLUMN_FOUR, longitude);
        contentValues.put(VERTEX_TABLE_COLUMN_FIVE, elevation);
        contentValues.put(VERTEX_TABLE_COLUMN_SIX, label);
        long result = db.insert(VERTEX_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateEdgeData(SQLiteDatabase db, int id, String osmID, int startVertex, int endVertex, int stairs) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EDGE_TABLE_COLUMN_ONE, id);
        contentValues.put(EDGE_TABLE_COLUMN_TWO, osmID);
        contentValues.put(EDGE_TABLE_COLUMN_THREE, startVertex);
        contentValues.put(EDGE_TABLE_COLUMN_FOUR, endVertex);
        contentValues.put(EDGE_TABLE_COLUMN_FIVE, stairs);
        long result = db.insert(EDGE_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateEdgeVertexJoinData(SQLiteDatabase db, int id, int edgeId, int vertexId, int vertexPosition) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EDGE_VERTEX_JOIN_TABLE_COLUMN_ONE, id);
        contentValues.put(EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO, edgeId);
        contentValues.put(EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE, vertexId);
        contentValues.put(EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR, vertexPosition);
        long result = db.insert(EDGE_VERTEX_JOIN_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public Cursor getBuildingNames() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + BUILDING_TABLE_COLUMN_TWO + " from " + BUILDING_TABLE_NAME, null);
        return res;
    }

    public Cursor getBuildingData(String buildingName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + BUILDING_TABLE_COLUMN_ONE + "," + BUILDING_TABLE_COLUMN_THREE + " from " + BUILDING_TABLE_NAME + " WHERE " + BUILDING_TABLE_COLUMN_TWO + " = '" + buildingName + "'", null);
        return res;
    }

    public Cursor getBuildingFeatureData(int buildingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + FEATURE_TABLE_COLUMN_THREE + ", " + FEATURE_TABLE_COLUMN_FOUR + " from " + FEATURE_TABLE_NAME + " WHERE " + FEATURE_TABLE_COLUMN_TWO + " = " + buildingId, null);
        return res;
    }

    public Cursor getVertexCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + VERTEX_TABLE_NAME, null);
        return res;
    }

    public Cursor getVertexData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + VERTEX_TABLE_NAME, null);
        return res;
    }

    public Cursor getVertexLabelData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + VERTEX_TABLE_NAME + " WHERE " + VERTEX_TABLE_COLUMN_SIX + " IS NOT NULL AND " + VERTEX_TABLE_COLUMN_SIX + " != ''", null);
        return res;
    }

    public Cursor getEdgeCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + EDGE_TABLE_NAME, null);
        return res;
    }

    public Cursor getEdgeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EDGE_TABLE_NAME, null);
        return res;
    }

    public Cursor getVersionNum() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DATABASE_VERSION_TABLE_NAME, null);
        return res;
    }

    public Cursor getUserPreferenceData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + USER_PREFERENCES_TABLE_COLUMN_ONE + "," + USER_PREFERENCES_TABLE_COLUMN_TWO + " FROM " + USER_PREFERENCES_TABLE_NAME, null);
        return res;
    }

    public Cursor getEdgeVertexJoinCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + EDGE_VERTEX_JOIN_TABLE_NAME , null);
        return res;
    }

    public Cursor getEdgeVertexJoinData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EDGE_VERTEX_JOIN_TABLE_NAME , null);
        return res;
    }

    public void updateUserPreference(SQLiteDatabase db, String prefKey, int prefValue) {
        String sql = "UPDATE "+USER_PREFERENCES_TABLE_NAME+" SET "+USER_PREFERENCES_TABLE_COLUMN_TWO+"="+prefValue+" WHERE "+USER_PREFERENCES_TABLE_COLUMN_ONE+"='"+prefKey+"'";
        db.execSQL(sql);
    }

    public Incident getIncidentData(int vertexId) {
        String sql = "SELECT * FROM " + INCIDENT_TABLE_NAME + " WHERE " + INCIDENT_TABLE_COLUMN_TWO + " = " + String.valueOf(vertexId);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(sql, null);
        Incident incident = new Incident();
        if (res.getCount()>0 && res.moveToFirst() && res.getInt(0) > 0) {
            incident.setId(res.getInt(0));
            incident.setDescription(res.getString(2));
            incident.setReportedAtTime(res.getString(3));
        } else {
            incident.setId(-1); //No Incident found
            incident.setDescription("No incident reported here.");
        }
        return incident;
    }

    public void updateIncidents(SQLiteDatabase db, JSONObject response) {
        String sql = "DELETE FROM " + INCIDENT_TABLE_NAME;
        db.execSQL(sql);
        try {
            JSONArray incidents = response.getJSONArray("incidents");
            for (int i = 0; i < incidents.length(); i++) {
                JSONObject o = incidents.getJSONObject(i);
                populateIncidentData(db, o.getInt("ReportId"), o.getInt("VertexId"), o.getString("Description"), o.getString("ReportedAt"));
            }
        } catch (JSONException j) {
            j.printStackTrace();
        }

    }

    public void populateIncidentData(SQLiteDatabase db, int reportId, int vertexId, String description, String reportedAt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCIDENT_TABLE_COLUMN_ONE, reportId);
        contentValues.put(INCIDENT_TABLE_COLUMN_TWO, vertexId);
        contentValues.put(INCIDENT_TABLE_COLUMN_THREE, description);
        contentValues.put(INCIDENT_TABLE_COLUMN_FOUR, reportedAt);
        long result = db.insert(INCIDENT_TABLE_NAME, null, contentValues);
    }

    public List<Incident> getIncidents() {
        String sql = "SELECT * FROM " + INCIDENT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(sql, null);
        List<Incident> incidents = new ArrayList<Incident>();
        if (res.getCount()>0) {
            while (res.moveToNext()) {
                    incidents.add(new Incident(res.getInt(0), res.getInt(1), res.getString(2), res.getString(3)));
            }
        }
        return incidents;
    }

    public void removeIncidentData(SQLiteDatabase db, int vertexId) {
        String sql = "DELETE FROM " + INCIDENT_TABLE_NAME + " WHERE " + INCIDENT_TABLE_COLUMN_TWO + " = " + String.valueOf(vertexId);
        db.execSQL(sql);
    }


}
