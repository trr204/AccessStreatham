package exeter.project.tobyreeve.execcessibility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String EDGE_TABLE_NAME = "Edge";
    public static final String EDGE_TABLE_COLUMN_ONE = "ID";
    public static final String EDGE_TABLE_COLUMN_TWO = "OSMID";
    public static final String EDGE_TABLE_COLUMN_THREE = "StartVertexId";
    public static final String EDGE_TABLE_COLUMN_FOUR = "EndVertexId";
    public static final String EDGE_VERTEX_JOIN_TABLE_NAME = "Edge_Vertex";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_ONE = "ID";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO = "EdgeId";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE = "VertexId";
    public static final String EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR = "VertexPosition";
    public static final String USER_PREFERENCES_TABLE_NAME = "UserPreferences";
    public static final String USER_PREFERENCES_TABLE_COLUMN_ONE = "PrefKey";
    public static final String USER_PREFERENCES_TABLE_COLUMN_TWO = "PrefValue";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 22);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        newDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        newDB(db);
    }

    public void newDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + BUILDING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FEATURE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VERTEX_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PREFERENCES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EDGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EDGE_VERTEX_JOIN_TABLE_NAME);
        String sql = "CREATE TABLE " + BUILDING_TABLE_NAME + " (" +
                BUILDING_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                BUILDING_TABLE_COLUMN_TWO + " TEXT," + //NAME
                BUILDING_TABLE_COLUMN_THREE + " TEXT)"; //DESCRIPTION
        db.execSQL(sql);
        sql = "CREATE TABLE " + USER_PREFERENCES_TABLE_NAME + " (" +
                USER_PREFERENCES_TABLE_COLUMN_ONE + " TEXT PRIMARY KEY," + //PREFKEY
                USER_PREFERENCES_TABLE_COLUMN_TWO + " INTEGER)"; //PREFVALUE
        db.execSQL(sql);
        sql = "CREATE TABLE " + FEATURE_TABLE_NAME + " (" +
                FEATURE_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                FEATURE_TABLE_COLUMN_TWO + " INTEGER," + //BUILDINGID
                FEATURE_TABLE_COLUMN_THREE + " TEXT," + //NAME
                FEATURE_TABLE_COLUMN_FOUR + " TEXT," + //DESCRIPTION
                "FOREIGN KEY("+FEATURE_TABLE_COLUMN_TWO+") REFERENCES "+BUILDING_TABLE_NAME+"("+BUILDING_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);
        sql = "CREATE TABLE " + VERTEX_TABLE_NAME + " (" +
                VERTEX_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                VERTEX_TABLE_COLUMN_TWO + " TEXT," + //VERTEX'S OSM ID
                VERTEX_TABLE_COLUMN_THREE + " REAL," + //LATITUDE
                VERTEX_TABLE_COLUMN_FOUR + " REAL)"; //LONGITUDE
        db.execSQL(sql);
        sql = "CREATE TABLE " + EDGE_TABLE_NAME + " (" +
                EDGE_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                EDGE_TABLE_COLUMN_TWO + " TEXT," + //EDGE'S OSM ID
                EDGE_TABLE_COLUMN_THREE + " INTEGER," + //START VERTEX'S ID
                EDGE_TABLE_COLUMN_FOUR + " INTEGER," + //END VERTEX'S ID
                "FOREIGN KEY("+EDGE_TABLE_COLUMN_THREE+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+")," +
                "FOREIGN KEY("+EDGE_TABLE_COLUMN_FOUR+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);
        sql = "CREATE TABLE " + EDGE_VERTEX_JOIN_TABLE_NAME + " (" +
                EDGE_VERTEX_JOIN_TABLE_COLUMN_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + //ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO + " INTEGER," + //EDGE'S ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE + " INTEGER," + //VERTEX'S ID
                EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR + " INTEGER," + //VERTEX POSITION
                "FOREIGN KEY("+EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO+") REFERENCES "+EDGE_TABLE_NAME+"("+EDGE_TABLE_COLUMN_ONE+")," +
                "FOREIGN KEY("+EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE+") REFERENCES "+VERTEX_TABLE_NAME+"("+VERTEX_TABLE_COLUMN_ONE+"))";
        db.execSQL(sql);

        populateUserPreferencesData(db, "AvoidStaircases", 0);
        populateUserPreferencesData(db, "DistanceOverAltitude", 0);

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
        populateBuildingData(db, "Harrison","");
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
        populateBuildingData(db, "Newman Lecture Theatres","Same as Peter Chalk");

        populateFeatureData(db, "Computer Science","", 1);
        populateFeatureData(db, "Engineering","", 1);
        populateFeatureData(db, "Mathematical Science","", 1);
        populateFeatureData(db, "Geography","", 2);
        populateFeatureData(db, "Student Services Centre","", 3);
        populateFeatureData(db, "Costa Coffee","", 3);

        populateVertexData(db, "210545946", "50.7359912", "-3.5359704");
        populateVertexData(db, "210545948", "50.7361631", "-3.5352804");
        populateVertexData(db, "210545950", "50.7360794", "-3.5348629");
        populateVertexData(db, "210547297", "50.7382002", "-3.5300247");
        populateVertexData(db, "210547298", "50.7377647", "-3.5299250");
        populateVertexData(db, "210547299", "50.7371976", "-3.5298186");
        populateVertexData(db, "210547300", "50.7368187", "-3.5297334");
        populateVertexData(db, "210547301", "50.7366828", "-3.5296697");
        populateVertexData(db, "210547303", "50.7365958", "-3.5299521");
        populateVertexData(db, "210547304", "50.7363691", "-3.5310554");
        populateVertexData(db, "210547306", "50.7361843", "-3.5314258");
        populateVertexData(db, "210547307", "50.7357611", "-3.5319808");
        populateVertexData(db, "210547308", "50.7355148", "-3.5322844");
        populateVertexData(db, "210549158", "50.7362635", "-3.5347118");
        populateVertexData(db, "210549160", "50.7369169", "-3.5340227");
        populateVertexData(db, "210549161", "50.7370824", "-3.5332955");
        populateVertexData(db, "210549163", "50.7372304", "-3.5322084");
        populateVertexData(db, "210549164", "50.7373983", "-3.5311473");
        populateVertexData(db, "210549166", "50.7374769", "-3.5309249");
        populateVertexData(db, "210549168", "50.7378252", "-3.5303737");
        populateVertexData(db, "210549169", "50.7378442", "-3.5300484");
        populateVertexData(db, "286500033", "50.7378544", "-3.5299344");
        populateVertexData(db, "286500034", "50.7387658", "-3.5303316");
        populateVertexData(db, "286500035", "50.7390441", "-3.5305298");
        populateVertexData(db, "292639157", "50.7356036", "-3.5335953");
        populateVertexData(db, "292646138", "50.7361182", "-3.5351755");
        populateVertexData(db, "292646139", "50.7361135", "-3.5354034");
        populateVertexData(db, "292646217", "50.7361131", "-3.5350634");
        populateVertexData(db, "292654207", "50.7355914", "-3.5341895");
        populateVertexData(db, "293996326", "50.7378375", "-3.5320398");
        populateVertexData(db, "293996327", "50.7379460", "-3.5319284");
        populateVertexData(db, "293996328", "50.7380080", "-3.5318583");
        populateVertexData(db, "293996330", "50.7380800", "-3.5318275");
        populateVertexData(db, "293996331", "50.7381581", "-3.5318019");
        populateVertexData(db, "293996333", "50.7382227", "-3.5317924");
        populateVertexData(db, "293996334", "50.7383004", "-3.5317772");
        populateVertexData(db, "293996335", "50.7383808", "-3.5317845");
        populateVertexData(db, "293996344", "50.7382381", "-3.5325753");
        populateVertexData(db, "293996345", "50.7378268", "-3.5321433");
        populateVertexData(db, "293996351", "50.7378987", "-3.5315629");
        populateVertexData(db, "293996352", "50.7378985", "-3.5314676");
        populateVertexData(db, "293996353", "50.7378758", "-3.5312740");
        populateVertexData(db, "293996354", "50.7377735", "-3.5309893");
        populateVertexData(db, "293996356", "50.7376618", "-3.5307283");
        populateVertexData(db, "293996357", "50.7380556", "-3.5315641");
        populateVertexData(db, "293996374", "50.7376839", "-3.5294554");
        populateVertexData(db, "293996390", "50.7378113", "-3.5296098");
        populateVertexData(db, "293996391", "50.7378725", "-3.5297307");
        populateVertexData(db, "293996403", "50.7357691", "-3.5291099");
        populateVertexData(db, "293996404", "50.7357306", "-3.5290641");
        populateVertexData(db, "293996408", "50.7366361", "-3.5294770");
        populateVertexData(db, "299823091", "50.7395546", "-3.5315834");
        populateVertexData(db, "299823093", "50.7395860", "-3.5308443");
        populateVertexData(db, "299823094", "50.7394499", "-3.5306676");
        populateVertexData(db, "299825172", "50.7381664", "-3.5312155");
        populateVertexData(db, "299825175", "50.7380042", "-3.5313440");
        populateVertexData(db, "299825176", "50.7379008", "-3.5314102");
        populateVertexData(db, "299825584", "50.7396490", "-3.5312937");
        populateVertexData(db, "299825611", "50.7394572", "-3.5319773");
        populateVertexData(db, "299828466", "50.7371545", "-3.5328525");
        populateVertexData(db, "299828468", "50.7371211", "-3.5328292");
        populateVertexData(db, "299828469", "50.7370216", "-3.5327040");
        populateVertexData(db, "299828470", "50.7371570", "-3.5317335");
        populateVertexData(db, "299828471", "50.7373059", "-3.5316134");
        populateVertexData(db, "299833294", "50.7359274", "-3.5305221");
        populateVertexData(db, "299833295", "50.7359510", "-3.5305803");
        populateVertexData(db, "299833296", "50.7361173", "-3.5310019");
        populateVertexData(db, "299833297", "50.7362439", "-3.5313303");
        populateVertexData(db, "299835269", "50.7369745", "-3.5329676");
        populateVertexData(db, "299835270", "50.7368515", "-3.5327719");
        populateVertexData(db, "299835273", "50.7367386", "-3.5325065");
        populateVertexData(db, "299835446", "50.7365005", "-3.5317768");
        populateVertexData(db, "299835586", "50.7367675", "-3.5326402");
        populateVertexData(db, "299835587", "50.7367491", "-3.5323095");
        populateVertexData(db, "299835590", "50.7367316", "-3.5321919");
        populateVertexData(db, "299835594", "50.7366815", "-3.5320615");
        populateVertexData(db, "299835714", "50.7363782", "-3.5313286");
        populateVertexData(db, "299836557", "50.7363817", "-3.5311785");
        populateVertexData(db, "299836558", "50.7363751", "-3.5314325");
        populateVertexData(db, "299836559", "50.7363087", "-3.5316073");
        populateVertexData(db, "299836636", "50.7362732", "-3.5316070");
        populateVertexData(db, "299836637", "50.7362318", "-3.5314957");
        populateVertexData(db, "299838074", "50.7371033", "-3.5358440");
        populateVertexData(db, "299838075", "50.7374986", "-3.5352150");
        populateVertexData(db, "299838079", "50.7378797", "-3.5342991");
        populateVertexData(db, "299839044", "50.7377431", "-3.5342860");
        populateVertexData(db, "299839046", "50.7375911", "-3.5341944");
        populateVertexData(db, "299839047", "50.7378341", "-3.5345536");
        populateVertexData(db, "299839098", "50.7379046", "-3.5339454");
        populateVertexData(db, "299839100", "50.7381992", "-3.5328597");
        populateVertexData(db, "299842370", "50.7379136", "-3.5334812");
        populateVertexData(db, "299842881", "50.7361094", "-3.5355137");
        populateVertexData(db, "301171092", "50.7374840", "-3.5341272");
        populateVertexData(db, "301171093", "50.7373168", "-3.5339868");
        populateVertexData(db, "301171094", "50.7371016", "-3.5338366");
        populateVertexData(db, "301171095", "50.7369709", "-3.5337565");
        populateVertexData(db, "306472711", "50.7379106", "-3.5335267");
        populateVertexData(db, "306472713", "50.7380497", "-3.5333848");
        populateVertexData(db, "306472714", "50.7381127", "-3.5332653");
        populateVertexData(db, "306473559", "50.7374227", "-3.5330456");
        populateVertexData(db, "306473562", "50.7371461", "-3.5329498");
        populateVertexData(db, "306473569", "50.7378635", "-3.5334555");
        populateVertexData(db, "306473570", "50.7377781", "-3.5333963");
        populateVertexData(db, "306473571", "50.7376872", "-3.5332353");
        populateVertexData(db, "306474499", "50.7356089", "-3.5324177");
        populateVertexData(db, "306474501", "50.7357679", "-3.5324380");
        populateVertexData(db, "306474503", "50.7358490", "-3.5325567");
        populateVertexData(db, "306474505", "50.7359449", "-3.5326418");
        populateVertexData(db, "306474507", "50.7360375", "-3.5327168");
        populateVertexData(db, "306474509", "50.7361342", "-3.5329000");
        populateVertexData(db, "306474511", "50.7362854", "-3.5331898");
        populateVertexData(db, "306474513", "50.7364178", "-3.5332026");
        populateVertexData(db, "306474515", "50.7364721", "-3.5331769");
        populateVertexData(db, "306474517", "50.7365699", "-3.5329966");
        populateVertexData(db, "306474519", "50.7366297", "-3.5329194");
        populateVertexData(db, "306480257", "50.7358203", "-3.5292887");
        populateVertexData(db, "306493110", "50.7376507", "-3.5320967");
        populateVertexData(db, "306493111", "50.7375529", "-3.5320436");
        populateVertexData(db, "306493113", "50.7372682", "-3.5319338");
        populateVertexData(db, "306493114", "50.7377766", "-3.5321330");
        populateVertexData(db, "343129417", "50.7371217", "-3.5330881");
        populateVertexData(db, "343130093", "50.7361145", "-3.5350144");
        populateVertexData(db, "571485408", "50.7373059", "-3.5304852");
        populateVertexData(db, "612974485", "50.7366113", "-3.5343470");
        populateVertexData(db, "612974486", "50.7365042", "-3.5331177");
        populateVertexData(db, "612974487", "50.7370520", "-3.5332855");
        populateVertexData(db, "612974488", "50.7369858", "-3.5334250");
        populateVertexData(db, "612974489", "50.7368839", "-3.5334652");
        populateVertexData(db, "612974490", "50.7365512", "-3.5332560");
        populateVertexData(db, "612974491", "50.7365376", "-3.5331836");
        populateVertexData(db, "612974492", "50.7364629", "-3.5333553");
        populateVertexData(db, "612974493", "50.7365189", "-3.5334947");
        populateVertexData(db, "612974494", "50.7365342", "-3.5336825");
        populateVertexData(db, "612974495", "50.7365155", "-3.5339158");
        populateVertexData(db, "612974496", "50.7365054", "-3.5339802");
        populateVertexData(db, "922765956", "50.7365358", "-3.5303482");
        populateVertexData(db, "922765966", "50.7370974", "-3.5305772");
        populateVertexData(db, "1168215417", "50.7366459", "-3.5297036");
        populateVertexData(db, "1170216059", "50.7361550", "-3.5351482");
        populateVertexData(db, "1184366141", "50.7377674", "-3.5353387");
        populateVertexData(db, "1184366796", "50.7358297", "-3.5351052");
        populateVertexData(db, "1184366850", "50.7372570", "-3.5367760");
        populateVertexData(db, "1205762777", "50.7359832", "-3.5320080");
        populateVertexData(db, "1205762779", "50.7361251", "-3.5320317");
        populateVertexData(db, "1205762780", "50.7364376", "-3.5324642");
        populateVertexData(db, "1205762781", "50.7365903", "-3.5326842");
        populateVertexData(db, "1615754377", "50.7366870", "-3.5328032");
        populateVertexData(db, "1792436354", "50.7362404", "-3.5341495");
        populateVertexData(db, "1792436356", "50.7369125", "-3.5358611");
        populateVertexData(db, "1792436363", "50.7380857", "-3.5333366");
        populateVertexData(db, "1792436383", "50.7361198", "-3.5342545");
        populateVertexData(db, "1792436385", "50.7370246", "-3.5358316");
        populateVertexData(db, "1792436391", "50.7368338", "-3.5348473");
        populateVertexData(db, "1792436394", "50.7380036", "-3.5333983");
        populateVertexData(db, "1792436395", "50.7379345", "-3.5334131");
        populateVertexData(db, "1792436425", "50.7366326", "-3.5354170");
        populateVertexData(db, "1792436434", "50.7364186", "-3.5350699");
        populateVertexData(db, "1792436447", "50.7359517", "-3.5341204");
        populateVertexData(db, "1792436454", "50.7367852", "-3.5358209");
        populateVertexData(db, "1792436482", "50.7362374", "-3.5347341");
        populateVertexData(db, "1792436494", "50.7370729", "-3.5357900");
        populateVertexData(db, "1792436525", "50.7376830", "-3.5348890");
        populateVertexData(db, "1792436526", "50.7376339", "-3.5342518");
        populateVertexData(db, "1792436528", "50.7368514", "-3.5359443");
        populateVertexData(db, "1792436542", "50.7381398", "-3.5331819");
        populateVertexData(db, "1792436552", "50.7369431", "-3.5358611");
        populateVertexData(db, "1792436562", "50.7361537", "-3.5339970");
        populateVertexData(db, "1792436572", "50.7360001", "-3.5346702");
        populateVertexData(db, "1792436584", "50.7366353", "-3.5344924");
        populateVertexData(db, "1792436644", "50.7359329", "-3.5344139");
        populateVertexData(db, "1792436669", "50.7369804", "-3.5358665");
        populateVertexData(db, "1792436671", "50.7377102", "-3.5333100");
        populateVertexData(db, "1792436730", "50.7365836", "-3.5343761");
        populateVertexData(db, "1853359417", "50.7361217", "-3.5306159");
        populateVertexData(db, "1853359427", "50.7358162", "-3.5294572");
        populateVertexData(db, "1853359594", "50.7360912", "-3.5299588");
        populateVertexData(db, "1853359637", "50.7359333", "-3.5297388");
        populateVertexData(db, "1853359653", "50.7358111", "-3.5293821");
        populateVertexData(db, "1853359667", "50.7359605", "-3.5299534");
        populateVertexData(db, "1853359692", "50.7359452", "-3.5296772");
        populateVertexData(db, "1853359694", "50.7361557", "-3.5308412");
        populateVertexData(db, "1853359702", "50.7360402", "-3.5303611");
        populateVertexData(db, "1853359705", "50.7359944", "-3.5299561");
        populateVertexData(db, "1853359712", "50.7361014", "-3.5304979");
        populateVertexData(db, "1853359717", "50.7358501", "-3.5294867");
        populateVertexData(db, "1853359720", "50.7359044", "-3.5297925");
        populateVertexData(db, "1853359729", "50.7359350", "-3.5295940");
        populateVertexData(db, "1853359747", "50.7361285", "-3.5307474");
        populateVertexData(db, "1853359749", "50.7361625", "-3.5310075");
        populateVertexData(db, "1853359757", "50.7360623", "-3.5299588");
        populateVertexData(db, "1853359760", "50.7361453", "-3.5311033");
        populateVertexData(db, "1853359762", "50.7364188", "-3.5308573");
        populateVertexData(db, "1853359798", "50.7361710", "-3.5309110");
        populateVertexData(db, "1853359828", "50.7359112", "-3.5295350");
        populateVertexData(db, "1853359850", "50.7362277", "-3.5322286");
        populateVertexData(db, "1853359858", "50.7366009", "-3.5318968");
        populateVertexData(db, "1853359861", "50.7370184", "-3.5324445");
        populateVertexData(db, "1853359863", "50.7374461", "-3.5309955");
        populateVertexData(db, "1853359884", "50.7372311", "-3.5316601");
        populateVertexData(db, "1853359895", "50.7366598", "-3.5315493");
        populateVertexData(db, "1853359900", "50.7370745", "-3.5320842");
        populateVertexData(db, "1853359906", "50.7366072", "-3.5307930");
        populateVertexData(db, "1853359909", "50.7371119", "-3.5318477");
        populateVertexData(db, "1853359933", "50.7366972", "-3.5308681");
        populateVertexData(db, "1853359952", "50.7364487", "-3.5307272");
        populateVertexData(db, "1853359956", "50.7367210", "-3.5310048");
        populateVertexData(db, "1853359960", "50.7367159", "-3.5309378");
        populateVertexData(db, "1853380525", "50.7355535", "-3.5337045");
        populateVertexData(db, "1853380568", "50.7359316", "-3.5332794");
        populateVertexData(db, "1853380573", "50.7358960", "-3.5341591");
        populateVertexData(db, "1853380578", "50.7355547", "-3.5335775");
        populateVertexData(db, "1853380580", "50.7358682", "-3.5341579");
        populateVertexData(db, "1853380594", "50.7360267", "-3.5341135");
        populateVertexData(db, "1853380596", "50.7359554", "-3.5341430");
        populateVertexData(db, "1853380600", "50.7362125", "-3.5339821");
        populateVertexData(db, "1853380602", "50.7360980", "-3.5340733");
        populateVertexData(db, "1853380604", "50.7355912", "-3.5341456");
        populateVertexData(db, "1853380605", "50.7363288", "-3.5338748");
        populateVertexData(db, "1853380606", "50.7363900", "-3.5340239");
        populateVertexData(db, "1853380609", "50.7362338", "-3.5336522");
        populateVertexData(db, "1853380632", "50.7363614", "-3.5339498");
        populateVertexData(db, "1853380635", "50.7363394", "-3.5339049");
        populateVertexData(db, "1853380661", "50.7362440", "-3.5339526");
        populateVertexData(db, "1853380682", "50.7360352", "-3.5332874");
        populateVertexData(db, "1853380685", "50.7361574", "-3.5335074");
        populateVertexData(db, "1853380686", "50.7359961", "-3.5332713");
        populateVertexData(db, "2179853920", "50.7392099", "-3.5306441");
        populateVertexData(db, "2179853972", "50.7396643", "-3.5311591");
        populateVertexData(db, "2179854004", "50.7395318", "-3.5307641");
        populateVertexData(db, "2179854173", "50.7391066", "-3.5305877");
        populateVertexData(db, "2179854178", "50.7396487", "-3.5310269");
        populateVertexData(db, "2179854218", "50.7393241", "-3.5306443");
        populateVertexData(db, "2179854249", "50.7396191", "-3.5309252");
        populateVertexData(db, "2179870767", "50.7395603", "-3.5308063");
        populateVertexData(db, "2179870779", "50.7372464", "-3.5315818");
        populateVertexData(db, "2179870810", "50.7372637", "-3.5316397");
        populateVertexData(db, "2179870821", "50.7386726", "-3.5302715");
        populateVertexData(db, "2179870822", "50.7383926", "-3.5301073");
        populateVertexData(db, "2179870824", "50.7374138", "-3.5308016");
        populateVertexData(db, "2179870848", "50.7377959", "-3.5305028");
        populateVertexData(db, "2179870864", "50.7385450", "-3.5301836");
        populateVertexData(db, "2179870868", "50.7371534", "-3.5312497");
        populateVertexData(db, "2180367248", "50.7381417", "-3.5312226");
        populateVertexData(db, "2696494906", "50.7372636", "-3.5304190");
        populateVertexData(db, "2696494907", "50.7371539", "-3.5301607");
        populateVertexData(db, "2696494910", "50.7372907", "-3.5302070");
        populateVertexData(db, "2696494911", "50.7370195", "-3.5311512");
        populateVertexData(db, "2696495063", "50.7371564", "-3.5311976");
        populateVertexData(db, "2696495151", "50.7363519", "-3.5340559");
        populateVertexData(db, "2696495155", "50.7365333", "-3.5344288");
        populateVertexData(db, "2857941399", "50.7355637", "-3.5338242");
        populateVertexData(db, "3670438697", "50.7396335", "-3.5309747");
        populateVertexData(db, "3729706012", "50.7356870", "-3.5350419");
        populateVertexData(db, "3729706014", "50.7355765", "-3.5348082");
        populateVertexData(db, "3729706015", "50.7355544", "-3.5346229");
        populateVertexData(db, "3729706016", "50.7355731", "-3.5344483");
        populateVertexData(db, "3729706017", "50.7355918", "-3.5343113");
        populateVertexData(db, "3729706021", "50.7359284", "-3.5345960");
        populateVertexData(db, "3729706022", "50.7358961", "-3.5344510");
        populateVertexData(db, "3729706023", "50.7358227", "-3.5341832");
        populateVertexData(db, "3729706024", "50.7358144", "-3.5333742");
        populateVertexData(db, "3729707331", "50.7356950", "-3.5350129");
        populateVertexData(db, "3729707332", "50.7357596", "-3.5349720");
        populateVertexData(db, "3729707333", "50.7358517", "-3.5351114");
        populateVertexData(db, "3729707334", "50.7360930", "-3.5350434");
        populateVertexData(db, "3729707344", "50.7362309", "-3.5340105");
        populateVertexData(db, "3729707345", "50.7360304", "-3.5342442");
        populateVertexData(db, "3729707346", "50.7359930", "-3.5342495");
        populateVertexData(db, "3729707347", "50.7360151", "-3.5343006");
        populateVertexData(db, "3729707348", "50.7359488", "-3.5343650");
        populateVertexData(db, "3729707349", "50.7359267", "-3.5342388");
        populateVertexData(db, "3729707350", "50.7359012", "-3.5340938");
        populateVertexData(db, "3729707351", "50.7358910", "-3.5339890");
        populateVertexData(db, "3729707352", "50.7359446", "-3.5339779");
        populateVertexData(db, "3729707353", "50.7362031", "-3.5339468");
        populateVertexData(db, "3729707364", "50.7356954", "-3.5324080");
        populateVertexData(db, "3729707365", "50.7356568", "-3.5324293");
        populateVertexData(db, "3729707372", "50.7361077", "-3.5319823");
        populateVertexData(db, "3729707373", "50.7360845", "-3.5318847");
        populateVertexData(db, "3729707374", "50.7360662", "-3.5317931");
        populateVertexData(db, "3729707375", "50.7360633", "-3.5316268");
        populateVertexData(db, "3729707376", "50.7359107", "-3.5318175");
        populateVertexData(db, "3729738804", "50.7361466", "-3.5354066");
        populateVertexData(db, "3729738808", "50.7360492", "-3.5357208");
        populateVertexData(db, "3731660186", "50.7360834", "-3.5349205");
        populateVertexData(db, "4225974511", "50.7368140", "-3.5303509");
        populateVertexData(db, "4225974512", "50.7368539", "-3.5300639");
        populateVertexData(db, "4225974513", "50.7369660", "-3.5304045");
        populateVertexData(db, "4225974514", "50.7370063", "-3.5301131");
        populateVertexData(db, "4225974516", "50.7371635", "-3.5311450");
        populateVertexData(db, "4225974518", "50.7375109", "-3.5304903");
        populateVertexData(db, "4225974519", "50.7375355", "-3.5302731");
        populateVertexData(db, "4432912551", "50.7373181", "-3.5331965");
        populateVertexData(db, "4432912552", "50.7373584", "-3.5330881");
        populateVertexData(db, "4432912553", "50.7373703", "-3.5330845");
        populateVertexData(db, "4432912555", "50.7374011", "-3.5330929");
        populateVertexData(db, "4432912556", "50.7374688", "-3.5333058");
        populateVertexData(db, "4432912557", "50.7375133", "-3.5331234");
        populateVertexData(db, "4432912559", "50.7376118", "-3.5330785");
        populateVertexData(db, "4432912560", "50.7376763", "-3.5331060");
        populateVertexData(db, "4432912561", "50.7376795", "-3.5331425");
        populateVertexData(db, "4823218149", "50.7358093", "-3.5350655");
        populateVertexData(db, "5186492089", "50.7372606", "-3.5366457");
        populateVertexData(db, "5186492090", "50.7373068", "-3.5364226");
        populateVertexData(db, "5186492091", "50.7374643", "-3.5362337");
        populateVertexData(db, "5186492092", "50.7375648", "-3.5360707");
        populateVertexData(db, "5186492093", "50.7376788", "-3.5358947");
        populateVertexData(db, "5186492094", "50.7377793", "-3.5357402");
        populateVertexData(db, "5186492095", "50.7378800", "-3.5355469");
        populateVertexData(db, "5186492096", "50.7378961", "-3.5354484");
        populateVertexData(db, "5186492097", "50.7378744", "-3.5353883");
        populateVertexData(db, "5186492098", "50.7378337", "-3.5353282");
        populateVertexData(db, "5186492099", "50.7378934", "-3.5352209");
        populateVertexData(db, "5186492100", "50.7379260", "-3.5351265");
        populateVertexData(db, "5186492101", "50.7379151", "-3.5350321");
        populateVertexData(db, "5186492102", "50.7378635", "-3.5349592");
        populateVertexData(db, "5186492103", "50.7378011", "-3.5348562");
        populateVertexData(db, "5186492104", "50.7377432", "-3.5347553");
        populateVertexData(db, "5186503212", "50.7358224", "-3.5350471");
        populateVertexData(db, "5186544819", "50.7378500", "-3.5352403");
        populateVertexData(db, "5186544820", "50.7378975", "-3.5351115");
        populateVertexData(db, "5186550821", "50.7378907", "-3.5350471");
        populateVertexData(db, "5186550822", "50.7378024", "-3.5348905");
        populateVertexData(db, "5186550823", "50.7378500", "-3.5349806");
        populateVertexData(db, "5186550824", "50.7377440", "-3.5348411");
        populateVertexData(db, "5454709604", "50.7357288", "-3.5349130");
        populateVertexData(db, "5763358013", "50.7358194", "-3.5338582");
        populateVertexData(db, "5763358014", "50.7360376", "-3.5348929");
        populateVertexData(db, "5789855967", "50.7360272", "-3.5347360");
        populateVertexData(db, "5789860462", "50.7362269", "-3.5330333");
        populateVertexData(db, "5789867356", "50.7363996", "-3.5305657");
        populateVertexData(db, "5789869661", "50.7360524", "-3.5348823");
        populateVertexData(db, "5789869662", "50.7360893", "-3.5349989");
        populateEdgeData(db, "19988638","210545950","286500033");
        populateEdgeVertexJoinData(db, "19988638", "210545950",0);
        populateEdgeVertexJoinData(db, "19988638", "1792436482",1);
        populateEdgeVertexJoinData(db, "19988638", "210549158",2);
        populateEdgeVertexJoinData(db, "19988638", "2696495155",3);
        populateEdgeVertexJoinData(db, "19988638", "1792436730",4);
        populateEdgeVertexJoinData(db, "19988638", "612974485",5);
        populateEdgeVertexJoinData(db, "19988638", "210549160",6);
        populateEdgeVertexJoinData(db, "19988638", "301171095",7);
        populateEdgeVertexJoinData(db, "19988638", "210549161",8);
        populateEdgeVertexJoinData(db, "19988638", "343129417",9);
        populateEdgeVertexJoinData(db, "19988638", "306473562",10);
        populateEdgeVertexJoinData(db, "19988638", "299828466",11);
        populateEdgeVertexJoinData(db, "19988638", "210549163",12);
        populateEdgeVertexJoinData(db, "19988638", "306493113",13);
        populateEdgeVertexJoinData(db, "19988638", "299828471",14);
        populateEdgeVertexJoinData(db, "19988638", "210549164",15);
        populateEdgeVertexJoinData(db, "19988638", "1853359863",16);
        populateEdgeVertexJoinData(db, "19988638", "210549166",17);
        populateEdgeVertexJoinData(db, "19988638", "293996356",18);
        populateEdgeVertexJoinData(db, "19988638", "2179870848",19);
        populateEdgeVertexJoinData(db, "19988638", "210549168",20);
        populateEdgeVertexJoinData(db, "19988638", "210549169",21);
        populateEdgeVertexJoinData(db, "19988638", "286500033",22);
        populateEdgeData(db, "26683918","5789869661","292646139");
        populateEdgeVertexJoinData(db, "26683918", "5789869661",0);
        populateEdgeVertexJoinData(db, "26683918", "5789869662",1);
        populateEdgeVertexJoinData(db, "26683918", "292646217",2);
        populateEdgeVertexJoinData(db, "26683918", "292646138",3);
        populateEdgeVertexJoinData(db, "26683918", "292646139",4);
        populateEdgeData(db, "26799949","293996326","293996335");
        populateEdgeVertexJoinData(db, "26799949", "293996326",0);
        populateEdgeVertexJoinData(db, "26799949", "293996327",1);
        populateEdgeVertexJoinData(db, "26799949", "293996328",2);
        populateEdgeVertexJoinData(db, "26799949", "293996330",3);
        populateEdgeVertexJoinData(db, "26799949", "293996331",4);
        populateEdgeVertexJoinData(db, "26799949", "293996333",5);
        populateEdgeVertexJoinData(db, "26799949", "293996334",6);
        populateEdgeVertexJoinData(db, "26799949", "293996335",7);
        populateEdgeData(db, "26799950","293996345","293996356");
        populateEdgeVertexJoinData(db, "26799950", "293996345",0);
        populateEdgeVertexJoinData(db, "26799950", "293996326",1);
        populateEdgeVertexJoinData(db, "26799950", "293996351",2);
        populateEdgeVertexJoinData(db, "26799950", "293996352",3);
        populateEdgeVertexJoinData(db, "26799950", "299825176",4);
        populateEdgeVertexJoinData(db, "26799950", "293996353",5);
        populateEdgeVertexJoinData(db, "26799950", "293996354",6);
        populateEdgeVertexJoinData(db, "26799950", "293996356",7);
        populateEdgeData(db, "26799951","293996357","293996351");
        populateEdgeVertexJoinData(db, "26799951", "293996357",0);
        populateEdgeVertexJoinData(db, "26799951", "293996351",1);
        populateEdgeData(db, "26799954","293996404","1853359594");
        populateEdgeVertexJoinData(db, "26799954", "293996404",0);
        populateEdgeVertexJoinData(db, "26799954", "293996403",1);
        populateEdgeVertexJoinData(db, "26799954", "306480257",2);
        populateEdgeVertexJoinData(db, "26799954", "1853359653",3);
        populateEdgeVertexJoinData(db, "26799954", "1853359427",4);
        populateEdgeVertexJoinData(db, "26799954", "1853359717",5);
        populateEdgeVertexJoinData(db, "26799954", "1853359828",6);
        populateEdgeVertexJoinData(db, "26799954", "1853359729",7);
        populateEdgeVertexJoinData(db, "26799954", "1853359692",8);
        populateEdgeVertexJoinData(db, "26799954", "1853359637",9);
        populateEdgeVertexJoinData(db, "26799954", "1853359720",10);
        populateEdgeVertexJoinData(db, "26799954", "1853359667",11);
        populateEdgeVertexJoinData(db, "26799954", "1853359705",12);
        populateEdgeVertexJoinData(db, "26799954", "1853359757",13);
        populateEdgeVertexJoinData(db, "26799954", "1853359594",14);
        populateEdgeData(db, "26799956","210547301","293996408");
        populateEdgeVertexJoinData(db, "26799956", "210547301",0);
        populateEdgeVertexJoinData(db, "26799956", "293996408",1);
        populateEdgeData(db, "27314459","299825172","299825176");
        populateEdgeVertexJoinData(db, "27314459", "299825172",0);
        populateEdgeVertexJoinData(db, "27314459", "2180367248",1);
        populateEdgeVertexJoinData(db, "27314459", "299825175",2);
        populateEdgeVertexJoinData(db, "27314459", "299825176",3);
        populateEdgeData(db, "27314726","210547306","210547308");
        populateEdgeVertexJoinData(db, "27314726", "210547306",0);
        populateEdgeVertexJoinData(db, "27314726", "3729707375",1);
        populateEdgeVertexJoinData(db, "27314726", "3729707376",2);
        populateEdgeVertexJoinData(db, "27314726", "210547307",3);
        populateEdgeVertexJoinData(db, "27314726", "210547308",4);
        populateEdgeData(db, "27315059","299828466","299828471");
        populateEdgeVertexJoinData(db, "27315059", "299828466",0);
        populateEdgeVertexJoinData(db, "27315059", "299828468",1);
        populateEdgeVertexJoinData(db, "27315059", "299828469",2);
        populateEdgeVertexJoinData(db, "27315059", "1853359861",3);
        populateEdgeVertexJoinData(db, "27315059", "1853359900",4);
        populateEdgeVertexJoinData(db, "27315059", "1853359909",5);
        populateEdgeVertexJoinData(db, "27315059", "299828470",6);
        populateEdgeVertexJoinData(db, "27315059", "1853359884",7);
        populateEdgeVertexJoinData(db, "27315059", "2179870810",8);
        populateEdgeVertexJoinData(db, "27315059", "299828471",9);
        populateEdgeData(db, "27315757","299833294","299833297");
        populateEdgeVertexJoinData(db, "27315757", "299833294",0);
        populateEdgeVertexJoinData(db, "27315757", "299833295",1);
        populateEdgeVertexJoinData(db, "27315757", "299833296",2);
        populateEdgeVertexJoinData(db, "27315757", "1853359760",3);
        populateEdgeVertexJoinData(db, "27315757", "299833297",4);
        populateEdgeData(db, "27316124","343129417","299835446");
        populateEdgeVertexJoinData(db, "27316124", "343129417",0);
        populateEdgeVertexJoinData(db, "27316124", "299835269",1);
        populateEdgeVertexJoinData(db, "27316124", "299835270",2);
        populateEdgeVertexJoinData(db, "27316124", "299835586",3);
        populateEdgeVertexJoinData(db, "27316124", "299835273",4);
        populateEdgeVertexJoinData(db, "27316124", "299835587",5);
        populateEdgeVertexJoinData(db, "27316124", "299835590",6);
        populateEdgeVertexJoinData(db, "27316124", "299835594",7);
        populateEdgeVertexJoinData(db, "27316124", "1853359858",8);
        populateEdgeVertexJoinData(db, "27316124", "299835446",9);
        populateEdgeData(db, "27316183","210547304","210547306");
        populateEdgeVertexJoinData(db, "27316183", "210547304",0);
        populateEdgeVertexJoinData(db, "27316183", "299836557",1);
        populateEdgeVertexJoinData(db, "27316183", "299835714",2);
        populateEdgeVertexJoinData(db, "27316183", "299836558",3);
        populateEdgeVertexJoinData(db, "27316183", "299836559",4);
        populateEdgeVertexJoinData(db, "27316183", "299836636",5);
        populateEdgeVertexJoinData(db, "27316183", "299836637",6);
        populateEdgeVertexJoinData(db, "27316183", "210547306",7);
        populateEdgeData(db, "27316609","299838079","299839044");
        populateEdgeVertexJoinData(db, "27316609", "299838079",0);
        populateEdgeVertexJoinData(db, "27316609", "299839044",1);
        populateEdgeData(db, "27429399","286500033","210547301");
        populateEdgeVertexJoinData(db, "27429399", "286500033",0);
        populateEdgeVertexJoinData(db, "27429399", "210547298",1);
        populateEdgeVertexJoinData(db, "27429399", "210547299",2);
        populateEdgeVertexJoinData(db, "27429399", "210547300",3);
        populateEdgeVertexJoinData(db, "27429399", "210547301",4);
        populateEdgeData(db, "27432272","299839046","301171095");
        populateEdgeVertexJoinData(db, "27432272", "299839046",0);
        populateEdgeVertexJoinData(db, "27432272", "301171092",1);
        populateEdgeVertexJoinData(db, "27432272", "301171093",2);
        populateEdgeVertexJoinData(db, "27432272", "301171094",3);
        populateEdgeVertexJoinData(db, "27432272", "301171095",4);
        populateEdgeData(db, "27432291","299839044","299839046");
        populateEdgeVertexJoinData(db, "27432291", "299839044",0);
        populateEdgeVertexJoinData(db, "27432291", "1792436526",1);
        populateEdgeVertexJoinData(db, "27432291", "299839046",2);
        populateEdgeData(db, "27918718","4432912552","4432912552");
        populateEdgeVertexJoinData(db, "27918718", "4432912552",0);
        populateEdgeVertexJoinData(db, "27918718", "4432912553",1);
        populateEdgeVertexJoinData(db, "27918718", "4432912555",2);
        populateEdgeVertexJoinData(db, "27918718", "4432912557",3);
        populateEdgeVertexJoinData(db, "27918718", "4432912556",4);
        populateEdgeVertexJoinData(db, "27918718", "4432912551",5);
        populateEdgeVertexJoinData(db, "27918718", "4432912552",6);
        populateEdgeData(db, "27918722","299842370","306473559");
        populateEdgeVertexJoinData(db, "27918722", "299842370",0);
        populateEdgeVertexJoinData(db, "27918722", "306473569",1);
        populateEdgeVertexJoinData(db, "27918722", "306473570",2);
        populateEdgeVertexJoinData(db, "27918722", "1792436671",3);
        populateEdgeVertexJoinData(db, "27918722", "306473571",4);
        populateEdgeVertexJoinData(db, "27918722", "4432912561",5);
        populateEdgeVertexJoinData(db, "27918722", "4432912560",6);
        populateEdgeVertexJoinData(db, "27918722", "4432912559",7);
        populateEdgeVertexJoinData(db, "27918722", "306473559",8);
        populateEdgeData(db, "27918950","306474499","299835586");
        populateEdgeVertexJoinData(db, "27918950", "306474499",0);
        populateEdgeVertexJoinData(db, "27918950", "3729707365",1);
        populateEdgeVertexJoinData(db, "27918950", "3729707364",2);
        populateEdgeVertexJoinData(db, "27918950", "306474501",3);
        populateEdgeVertexJoinData(db, "27918950", "306474503",4);
        populateEdgeVertexJoinData(db, "27918950", "306474505",5);
        populateEdgeVertexJoinData(db, "27918950", "306474507",6);
        populateEdgeVertexJoinData(db, "27918950", "306474509",7);
        populateEdgeVertexJoinData(db, "27918950", "5789860462",8);
        populateEdgeVertexJoinData(db, "27918950", "306474511",9);
        populateEdgeVertexJoinData(db, "27918950", "306474513",10);
        populateEdgeVertexJoinData(db, "27918950", "306474515",11);
        populateEdgeVertexJoinData(db, "27918950", "612974486",12);
        populateEdgeVertexJoinData(db, "27918950", "306474517",13);
        populateEdgeVertexJoinData(db, "27918950", "306474519",14);
        populateEdgeVertexJoinData(db, "27918950", "1615754377",15);
        populateEdgeVertexJoinData(db, "27918950", "299835586",16);
        populateEdgeData(db, "27920982","306493110","306493113");
        populateEdgeVertexJoinData(db, "27920982", "306493110",0);
        populateEdgeVertexJoinData(db, "27920982", "306493111",1);
        populateEdgeVertexJoinData(db, "27920982", "306493113",2);
        populateEdgeData(db, "27920983","306493114","306493110");
        populateEdgeVertexJoinData(db, "27920983", "306493114",0);
        populateEdgeVertexJoinData(db, "27920983", "306493110",1);
        populateEdgeData(db, "27920984","293996345","306493114");
        populateEdgeVertexJoinData(db, "27920984", "293996345",0);
        populateEdgeVertexJoinData(db, "27920984", "306493114",1);
        populateEdgeData(db, "41469624","293996374","286500033");
        populateEdgeVertexJoinData(db, "41469624", "293996374",0);
        populateEdgeVertexJoinData(db, "41469624", "293996390",1);
        populateEdgeVertexJoinData(db, "41469624", "293996391",2);
        populateEdgeVertexJoinData(db, "41469624", "286500033",3);
        populateEdgeData(db, "41470025","210547301","299833297");
        populateEdgeVertexJoinData(db, "41470025", "210547301",0);
        populateEdgeVertexJoinData(db, "41470025", "1168215417",1);
        populateEdgeVertexJoinData(db, "41470025", "210547303",2);
        populateEdgeVertexJoinData(db, "41470025", "922765956",3);
        populateEdgeVertexJoinData(db, "41470025", "1853359952",4);
        populateEdgeVertexJoinData(db, "41470025", "1853359762",5);
        populateEdgeVertexJoinData(db, "41470025", "210547304",6);
        populateEdgeVertexJoinData(db, "41470025", "299833297",7);
        populateEdgeData(db, "42227872","210545950","1792436644");
        populateEdgeVertexJoinData(db, "42227872", "210545950",0);
        populateEdgeVertexJoinData(db, "42227872", "5789855967",1);
        populateEdgeVertexJoinData(db, "42227872", "1792436572",2);
        populateEdgeVertexJoinData(db, "42227872", "1792436644",3);
        populateEdgeData(db, "42227879","210545946","210545950");
        populateEdgeVertexJoinData(db, "42227879", "210545946",0);
        populateEdgeVertexJoinData(db, "42227879", "3729738808",1);
        populateEdgeVertexJoinData(db, "42227879", "299842881",2);
        populateEdgeVertexJoinData(db, "42227879", "3729738804",3);
        populateEdgeVertexJoinData(db, "42227879", "210545948",4);
        populateEdgeVertexJoinData(db, "42227879", "1170216059",5);
        populateEdgeVertexJoinData(db, "42227879", "343130093",6);
        populateEdgeVertexJoinData(db, "42227879", "3731660186",7);
        populateEdgeVertexJoinData(db, "42227879", "210545950",8);
        populateEdgeData(db, "44380909","299825611","286500033");
        populateEdgeVertexJoinData(db, "44380909", "299825611",0);
        populateEdgeVertexJoinData(db, "44380909", "299823091",1);
        populateEdgeVertexJoinData(db, "44380909", "299825584",2);
        populateEdgeVertexJoinData(db, "44380909", "2179853972",3);
        populateEdgeVertexJoinData(db, "44380909", "2179854178",4);
        populateEdgeVertexJoinData(db, "44380909", "3670438697",5);
        populateEdgeVertexJoinData(db, "44380909", "2179854249",6);
        populateEdgeVertexJoinData(db, "44380909", "299823093",7);
        populateEdgeVertexJoinData(db, "44380909", "2179870767",8);
        populateEdgeVertexJoinData(db, "44380909", "2179854004",9);
        populateEdgeVertexJoinData(db, "44380909", "299823094",10);
        populateEdgeVertexJoinData(db, "44380909", "2179854218",11);
        populateEdgeVertexJoinData(db, "44380909", "2179853920",12);
        populateEdgeVertexJoinData(db, "44380909", "2179854173",13);
        populateEdgeVertexJoinData(db, "44380909", "286500035",14);
        populateEdgeVertexJoinData(db, "44380909", "286500034",15);
        populateEdgeVertexJoinData(db, "44380909", "2179870821",16);
        populateEdgeVertexJoinData(db, "44380909", "2179870864",17);
        populateEdgeVertexJoinData(db, "44380909", "2179870822",18);
        populateEdgeVertexJoinData(db, "44380909", "210547297",19);
        populateEdgeVertexJoinData(db, "44380909", "286500033",20);
        populateEdgeData(db, "45069950","210549166","571485408");
        populateEdgeVertexJoinData(db, "45069950", "210549166",0);
        populateEdgeVertexJoinData(db, "45069950", "2179870824",1);
        populateEdgeVertexJoinData(db, "45069950", "571485408",2);
        populateEdgeData(db, "48230336","210549161","612974486");
        populateEdgeVertexJoinData(db, "48230336", "210549161",0);
        populateEdgeVertexJoinData(db, "48230336", "612974487",1);
        populateEdgeVertexJoinData(db, "48230336", "612974488",2);
        populateEdgeVertexJoinData(db, "48230336", "612974489",3);
        populateEdgeVertexJoinData(db, "48230336", "612974490",4);
        populateEdgeVertexJoinData(db, "48230336", "612974491",5);
        populateEdgeVertexJoinData(db, "48230336", "612974486",6);
        populateEdgeData(db, "48230337","612974491","612974485");
        populateEdgeVertexJoinData(db, "48230337", "612974491",0);
        populateEdgeVertexJoinData(db, "48230337", "612974492",1);
        populateEdgeVertexJoinData(db, "48230337", "612974493",2);
        populateEdgeVertexJoinData(db, "48230337", "612974494",3);
        populateEdgeVertexJoinData(db, "48230337", "612974495",4);
        populateEdgeVertexJoinData(db, "48230337", "612974496",5);
        populateEdgeVertexJoinData(db, "48230337", "612974485",6);
        populateEdgeData(db, "78689229","922765956","922765966");
        populateEdgeVertexJoinData(db, "78689229", "922765956",0);
        populateEdgeVertexJoinData(db, "78689229", "922765966",1);
        populateEdgeData(db, "104509178","210547307","1205762779");
        populateEdgeVertexJoinData(db, "104509178", "210547307",0);
        populateEdgeVertexJoinData(db, "104509178", "1205762777",1);
        populateEdgeVertexJoinData(db, "104509178", "1205762779",2);
        populateEdgeData(db, "167907356","1792436482","1792436425");
        populateEdgeVertexJoinData(db, "167907356", "1792436482",0);
        populateEdgeVertexJoinData(db, "167907356", "1792436434",1);
        populateEdgeVertexJoinData(db, "167907356", "1792436425",2);
        populateEdgeData(db, "167907359","1792436391","1792436730");
        populateEdgeVertexJoinData(db, "167907359", "1792436391",0);
        populateEdgeVertexJoinData(db, "167907359", "1792436584",1);
        populateEdgeVertexJoinData(db, "167907359", "1792436730",2);
        populateEdgeData(db, "167907360","1853380606","1792436644");
        populateEdgeVertexJoinData(db, "167907360", "1853380606",0);
        populateEdgeVertexJoinData(db, "167907360", "2696495151",1);
        populateEdgeVertexJoinData(db, "167907360", "1792436354",2);
        populateEdgeVertexJoinData(db, "167907360", "1792436383",3);
        populateEdgeVertexJoinData(db, "167907360", "1792436644",4);
        populateEdgeData(db, "167907368","1792436454","293996344");
        populateEdgeVertexJoinData(db, "167907368", "1792436454",0);
        populateEdgeVertexJoinData(db, "167907368", "1792436528",1);
        populateEdgeVertexJoinData(db, "167907368", "1792436356",2);
        populateEdgeVertexJoinData(db, "167907368", "1792436552",3);
        populateEdgeVertexJoinData(db, "167907368", "1792436669",4);
        populateEdgeVertexJoinData(db, "167907368", "1792436385",5);
        populateEdgeVertexJoinData(db, "167907368", "1792436494",6);
        populateEdgeVertexJoinData(db, "167907368", "299838074",7);
        populateEdgeVertexJoinData(db, "167907368", "299838075",8);
        populateEdgeVertexJoinData(db, "167907368", "1792436525",9);
        populateEdgeVertexJoinData(db, "167907368", "5186492104",10);
        populateEdgeVertexJoinData(db, "167907368", "299839047",11);
        populateEdgeVertexJoinData(db, "167907368", "299838079",12);
        populateEdgeVertexJoinData(db, "167907368", "299839098",13);
        populateEdgeVertexJoinData(db, "167907368", "306472711",14);
        populateEdgeVertexJoinData(db, "167907368", "299842370",15);
        populateEdgeVertexJoinData(db, "167907368", "1792436395",16);
        populateEdgeVertexJoinData(db, "167907368", "1792436394",17);
        populateEdgeVertexJoinData(db, "167907368", "306472713",18);
        populateEdgeVertexJoinData(db, "167907368", "1792436363",19);
        populateEdgeVertexJoinData(db, "167907368", "306472714",20);
        populateEdgeVertexJoinData(db, "167907368", "1792436542",21);
        populateEdgeVertexJoinData(db, "167907368", "299839100",22);
        populateEdgeVertexJoinData(db, "167907368", "293996344",23);
        populateEdgeData(db, "174685239","1853359895","1853359952");
        populateEdgeVertexJoinData(db, "174685239", "1853359895",0);
        populateEdgeVertexJoinData(db, "174685239", "1853359956",1);
        populateEdgeVertexJoinData(db, "174685239", "1853359960",2);
        populateEdgeVertexJoinData(db, "174685239", "1853359933",3);
        populateEdgeVertexJoinData(db, "174685239", "1853359906",4);
        populateEdgeVertexJoinData(db, "174685239", "1853359952",5);
        populateEdgeData(db, "174685240","1853359760","1853359702");
        populateEdgeVertexJoinData(db, "174685240", "1853359760",0);
        populateEdgeVertexJoinData(db, "174685240", "1853359749",1);
        populateEdgeVertexJoinData(db, "174685240", "1853359798",2);
        populateEdgeVertexJoinData(db, "174685240", "1853359694",3);
        populateEdgeVertexJoinData(db, "174685240", "1853359747",4);
        populateEdgeVertexJoinData(db, "174685240", "1853359417",5);
        populateEdgeVertexJoinData(db, "174685240", "1853359712",6);
        populateEdgeVertexJoinData(db, "174685240", "1853359702",7);
        populateEdgeData(db, "174687138","1853380609","1853380568");
        populateEdgeVertexJoinData(db, "174687138", "1853380609",0);
        populateEdgeVertexJoinData(db, "174687138", "1853380685",1);
        populateEdgeVertexJoinData(db, "174687138", "1853380682",2);
        populateEdgeVertexJoinData(db, "174687138", "1853380686",3);
        populateEdgeVertexJoinData(db, "174687138", "1853380568",4);
        populateEdgeData(db, "174687143","1853380606","1853380632");
        populateEdgeVertexJoinData(db, "174687143", "1853380606",0);
        populateEdgeVertexJoinData(db, "174687143", "1853380632",1);
        populateEdgeData(db, "174687145","1853380635","1853380661");
        populateEdgeVertexJoinData(db, "174687145", "1853380635",0);
        populateEdgeVertexJoinData(db, "174687145", "1853380605",1);
        populateEdgeVertexJoinData(db, "174687145", "1853380661",2);
        populateEdgeData(db, "174687146","1853380632","1853380635");
        populateEdgeVertexJoinData(db, "174687146", "1853380632",0);
        populateEdgeVertexJoinData(db, "174687146", "1853380635",1);
        populateEdgeData(db, "174687147","1853380600","1853380580");
        populateEdgeVertexJoinData(db, "174687147", "1853380600",0);
        populateEdgeVertexJoinData(db, "174687147", "1853380602",1);
        populateEdgeVertexJoinData(db, "174687147", "1853380594",2);
        populateEdgeVertexJoinData(db, "174687147", "1853380596",3);
        populateEdgeVertexJoinData(db, "174687147", "1853380573",4);
        populateEdgeVertexJoinData(db, "174687147", "1853380580",5);
        populateEdgeData(db, "174687148","1853380661","1853380600");
        populateEdgeVertexJoinData(db, "174687148", "1853380661",0);
        populateEdgeVertexJoinData(db, "174687148", "1853380600",1);
        populateEdgeData(db, "263968211","571485408","2696494906");
        populateEdgeVertexJoinData(db, "263968211", "571485408",0);
        populateEdgeVertexJoinData(db, "263968211", "2696494906",1);
        populateEdgeData(db, "263968212","2696494907","2696494907");
        populateEdgeVertexJoinData(db, "263968212", "2696494907",0);
        populateEdgeVertexJoinData(db, "263968212", "922765966",1);
        populateEdgeVertexJoinData(db, "263968212", "2696494911",2);
        populateEdgeVertexJoinData(db, "263968212", "2696495063",3);
        populateEdgeVertexJoinData(db, "263968212", "4225974516",4);
        populateEdgeVertexJoinData(db, "263968212", "2696494906",5);
        populateEdgeVertexJoinData(db, "263968212", "2696494910",6);
        populateEdgeVertexJoinData(db, "263968212", "2696494907",7);
        populateEdgeData(db, "263968241","2179870810","2696495063");
        populateEdgeVertexJoinData(db, "263968241", "2179870810",0);
        populateEdgeVertexJoinData(db, "263968241", "2179870779",1);
        populateEdgeVertexJoinData(db, "263968241", "2179870868",2);
        populateEdgeVertexJoinData(db, "263968241", "2696495063",3);
        populateEdgeData(db, "263968251","2696495155","2696495151");
        populateEdgeVertexJoinData(db, "263968251", "2696495155",0);
        populateEdgeVertexJoinData(db, "263968251", "2696495151",1);
        populateEdgeData(db, "369155876","3729706021","3729706021");
        populateEdgeVertexJoinData(db, "369155876", "3729706021",0);
        populateEdgeVertexJoinData(db, "369155876", "3729706022",1);
        populateEdgeVertexJoinData(db, "369155876", "3729706023",2);
        populateEdgeVertexJoinData(db, "369155876", "5763358013",3);
        populateEdgeVertexJoinData(db, "369155876", "3729706024",4);
        populateEdgeVertexJoinData(db, "369155876", "292639157",5);
        populateEdgeVertexJoinData(db, "369155876", "1853380578",6);
        populateEdgeVertexJoinData(db, "369155876", "1853380525",7);
        populateEdgeVertexJoinData(db, "369155876", "2857941399",8);
        populateEdgeVertexJoinData(db, "369155876", "1853380604",9);
        populateEdgeVertexJoinData(db, "369155876", "292654207",10);
        populateEdgeVertexJoinData(db, "369155876", "3729706017",11);
        populateEdgeVertexJoinData(db, "369155876", "3729706016",12);
        populateEdgeVertexJoinData(db, "369155876", "3729706015",13);
        populateEdgeVertexJoinData(db, "369155876", "3729706014",14);
        populateEdgeVertexJoinData(db, "369155876", "3729706012",15);
        populateEdgeVertexJoinData(db, "369155876", "3729707331",16);
        populateEdgeVertexJoinData(db, "369155876", "3729707332",17);
        populateEdgeVertexJoinData(db, "369155876", "4823218149",18);
        populateEdgeVertexJoinData(db, "369155876", "1184366796",19);
        populateEdgeVertexJoinData(db, "369155876", "3729707333",20);
        populateEdgeVertexJoinData(db, "369155876", "3729707334",21);
        populateEdgeVertexJoinData(db, "369155876", "5763358014",22);
        populateEdgeVertexJoinData(db, "369155876", "3729706021",23);
        populateEdgeData(db, "369155878","3729707344","3729707344");
        populateEdgeVertexJoinData(db, "369155878", "3729707344",0);
        populateEdgeVertexJoinData(db, "369155878", "3729707345",1);
        populateEdgeVertexJoinData(db, "369155878", "3729707346",2);
        populateEdgeVertexJoinData(db, "369155878", "3729707347",3);
        populateEdgeVertexJoinData(db, "369155878", "3729707348",4);
        populateEdgeVertexJoinData(db, "369155878", "3729707349",5);
        populateEdgeVertexJoinData(db, "369155878", "3729707350",6);
        populateEdgeVertexJoinData(db, "369155878", "3729707351",7);
        populateEdgeVertexJoinData(db, "369155878", "3729707352",8);
        populateEdgeVertexJoinData(db, "369155878", "1792436447",9);
        populateEdgeVertexJoinData(db, "369155878", "1792436562",10);
        populateEdgeVertexJoinData(db, "369155878", "3729707353",11);
        populateEdgeVertexJoinData(db, "369155878", "3729707344",12);
        populateEdgeData(db, "369155880","3729707375","1615754377");
        populateEdgeVertexJoinData(db, "369155880", "3729707375",0);
        populateEdgeVertexJoinData(db, "369155880", "3729707374",1);
        populateEdgeVertexJoinData(db, "369155880", "3729707373",2);
        populateEdgeVertexJoinData(db, "369155880", "3729707372",3);
        populateEdgeVertexJoinData(db, "369155880", "1205762779",4);
        populateEdgeVertexJoinData(db, "369155880", "1853359850",5);
        populateEdgeVertexJoinData(db, "369155880", "1205762780",6);
        populateEdgeVertexJoinData(db, "369155880", "1205762781",7);
        populateEdgeVertexJoinData(db, "369155880", "1615754377",8);
        populateEdgeData(db, "422869689","2696494910","4225974519");
        populateEdgeVertexJoinData(db, "422869689", "2696494910",0);
        populateEdgeVertexJoinData(db, "422869689", "4225974519",1);
        populateEdgeData(db, "422869693","4225974514","4225974513");
        populateEdgeVertexJoinData(db, "422869693", "4225974514",0);
        populateEdgeVertexJoinData(db, "422869693", "4225974513",1);
        populateEdgeData(db, "422869694","4225974518","2696494906");
        populateEdgeVertexJoinData(db, "422869694", "4225974518",0);
        populateEdgeVertexJoinData(db, "422869694", "2696494906",1);
        populateEdgeData(db, "422869695","210547299","2696494907");
        populateEdgeVertexJoinData(db, "422869695", "210547299",0);
        populateEdgeVertexJoinData(db, "422869695", "2696494907",1);
        populateEdgeData(db, "422869696","2696494907","4225974511");
        populateEdgeVertexJoinData(db, "422869696", "2696494907",0);
        populateEdgeVertexJoinData(db, "422869696", "4225974514",1);
        populateEdgeVertexJoinData(db, "422869696", "4225974512",2);
        populateEdgeVertexJoinData(db, "422869696", "4225974511",3);
        populateEdgeData(db, "445948835","4432912555","306473559");
        populateEdgeVertexJoinData(db, "445948835", "4432912555",0);
        populateEdgeVertexJoinData(db, "445948835", "306473559",1);
        populateEdgeData(db, "445948836","4432912552","306473562");
        populateEdgeVertexJoinData(db, "445948836", "4432912552",0);
        populateEdgeVertexJoinData(db, "445948836", "306473562",1);
        populateEdgeData(db, "534920394","1184366850","5186492104");
        populateEdgeVertexJoinData(db, "534920394", "1184366850",0);
        populateEdgeVertexJoinData(db, "534920394", "5186492089",1);
        populateEdgeVertexJoinData(db, "534920394", "5186492090",2);
        populateEdgeVertexJoinData(db, "534920394", "5186492091",3);
        populateEdgeVertexJoinData(db, "534920394", "5186492092",4);
        populateEdgeVertexJoinData(db, "534920394", "5186492093",5);
        populateEdgeVertexJoinData(db, "534920394", "5186492094",6);
        populateEdgeVertexJoinData(db, "534920394", "5186492095",7);
        populateEdgeVertexJoinData(db, "534920394", "5186492096",8);
        populateEdgeVertexJoinData(db, "534920394", "5186492097",9);
        populateEdgeVertexJoinData(db, "534920394", "5186492098",10);
        populateEdgeVertexJoinData(db, "534920394", "5186492099",11);
        populateEdgeVertexJoinData(db, "534920394", "5186492100",12);
        populateEdgeVertexJoinData(db, "534920394", "5186492101",13);
        populateEdgeVertexJoinData(db, "534920394", "5186492102",14);
        populateEdgeVertexJoinData(db, "534920394", "5186492103",15);
        populateEdgeVertexJoinData(db, "534920394", "5186492104",16);
        populateEdgeData(db, "534921335","5763358014","4823218149");
        populateEdgeVertexJoinData(db, "534921335", "5763358014",0);
        populateEdgeVertexJoinData(db, "534921335", "5186503212",1);
        populateEdgeVertexJoinData(db, "534921335", "4823218149",2);
        populateEdgeData(db, "534928176","1184366141","5186550824");
        populateEdgeVertexJoinData(db, "534928176", "1184366141",0);
        populateEdgeVertexJoinData(db, "534928176", "5186544819",1);
        populateEdgeVertexJoinData(db, "534928176", "5186544820",2);
        populateEdgeVertexJoinData(db, "534928176", "5186550821",3);
        populateEdgeVertexJoinData(db, "534928176", "5186550823",4);
        populateEdgeVertexJoinData(db, "534928176", "5186550822",5);
        populateEdgeVertexJoinData(db, "534928176", "5186550824",6);
        populateEdgeData(db, "608283677","210545950","5789869661");
        populateEdgeVertexJoinData(db, "608283677", "210545950",0);
        populateEdgeVertexJoinData(db, "608283677", "5789869661",1);
        populateEdgeData(db, "611575552","5454709604","5789855967");
        populateEdgeVertexJoinData(db, "611575552", "5454709604",0);
        populateEdgeVertexJoinData(db, "611575552", "5789855967",1);
        populateEdgeData(db, "611577274","922765956","1853359694");
        populateEdgeVertexJoinData(db, "611577274", "922765956",0);
        populateEdgeVertexJoinData(db, "611577274", "5789867356",1);
        populateEdgeVertexJoinData(db, "611577274", "1853359694",2);
        populateEdgeData(db, "611577275","299833297","210547306");
        populateEdgeVertexJoinData(db, "611577275", "299833297",0);
        populateEdgeVertexJoinData(db, "611577275", "210547306",1);
        populateEdgeData(db, "611577468","5789869661","5763358014");
        populateEdgeVertexJoinData(db, "611577468", "5789869661",0);
        populateEdgeVertexJoinData(db, "611577468", "5763358014",1);


    }

    public boolean populateBuildingData(SQLiteDatabase db, String name, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUILDING_TABLE_COLUMN_TWO, name);
        contentValues.put(BUILDING_TABLE_COLUMN_THREE, description);
        long result = db.insert(BUILDING_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateUserPreferencesData(SQLiteDatabase db, String prefKey, int prefValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PREFERENCES_TABLE_COLUMN_ONE, prefKey);
        contentValues.put(USER_PREFERENCES_TABLE_COLUMN_TWO, prefValue);
        long result = db.insert(USER_PREFERENCES_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateFeatureData(SQLiteDatabase db, String name, String description, int buildingId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FEATURE_TABLE_COLUMN_TWO, buildingId);
        contentValues.put(FEATURE_TABLE_COLUMN_THREE, name);
        contentValues.put(FEATURE_TABLE_COLUMN_FOUR, description);
        long result = db.insert(FEATURE_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public boolean populateVertexData(SQLiteDatabase db, String osmID, String latitude, String longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VERTEX_TABLE_COLUMN_TWO, osmID);
        contentValues.put(VERTEX_TABLE_COLUMN_THREE, latitude);
        contentValues.put(VERTEX_TABLE_COLUMN_FOUR, longitude);
        long result = db.insert(VERTEX_TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public void populateEdgeData(SQLiteDatabase db, String osmID, String startVertex, String endVertex) {
        String sql = "INSERT INTO "+EDGE_TABLE_NAME+"("+EDGE_TABLE_COLUMN_TWO+","+EDGE_TABLE_COLUMN_THREE+","+EDGE_TABLE_COLUMN_FOUR+") VALUES ("+
                osmID+",(SELECT "+VERTEX_TABLE_COLUMN_ONE+" FROM "+VERTEX_TABLE_NAME+" WHERE "+VERTEX_TABLE_COLUMN_TWO+"='"+startVertex+"'),"+
                "(SELECT "+VERTEX_TABLE_COLUMN_ONE+" FROM "+VERTEX_TABLE_NAME+" WHERE "+VERTEX_TABLE_COLUMN_TWO+"='"+endVertex+"'))";
        db.execSQL(sql);
    }

    public void populateEdgeVertexJoinData(SQLiteDatabase db, String edgeOSMID, String vertexOSMID, int vertexPosition) {
        String sql = "INSERT INTO "+EDGE_VERTEX_JOIN_TABLE_NAME+"("+EDGE_VERTEX_JOIN_TABLE_COLUMN_TWO+","+EDGE_VERTEX_JOIN_TABLE_COLUMN_THREE+","+EDGE_VERTEX_JOIN_TABLE_COLUMN_FOUR+") VALUES ("+
                "(SELECT "+EDGE_TABLE_COLUMN_ONE+" FROM "+EDGE_TABLE_NAME+" WHERE "+EDGE_TABLE_COLUMN_TWO+"='"+edgeOSMID+"'),"+
                "(SELECT "+VERTEX_TABLE_COLUMN_ONE+" FROM "+VERTEX_TABLE_NAME+" WHERE "+VERTEX_TABLE_COLUMN_TWO+"='"+vertexOSMID+"'),"+
                vertexPosition+")";
        db.execSQL(sql);
    }

    public Cursor getBuildingNames() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + BUILDING_TABLE_COLUMN_TWO + " from " + BUILDING_TABLE_NAME, null);
        return res;
    }

    public Cursor getBuildingId(String buildingName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + BUILDING_TABLE_COLUMN_ONE + " from " + BUILDING_TABLE_NAME + " WHERE " + BUILDING_TABLE_COLUMN_TWO + " = '" + buildingName + "'", null);
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


}
