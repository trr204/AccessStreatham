package exeter.project.tobyreeve.execcessibility;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyCanvas extends WebView {
    Paint paint;
    Graph campus;
    float initialScaleFactor;
    int screenHeight;
    int screenWidth;
    int canvasHeight;
    boolean onLoad;
    int canvasWidth;
    int count = 0;
    private long startClickTime;
    MainActivity mainActivity;

    public MyCanvas(Context context) {
        super(context);
        this.mainActivity = (MainActivity) context;
        paint = new Paint();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final float scaleFactor = getScale()/initialScaleFactor;
        //TODO Consider only drawing stuff that would be visible onscreen
        Log.d("CANVAS ONDRAW", "Current scale: " + String.valueOf(getScale()) + ", Current scale factor: " + String.valueOf(scaleFactor) + ", Current ScrollX value: " + String.valueOf(getScrollX()) + ", Current ScrollY value:" + String.valueOf(getScrollY()));
        if (campus != null) {
            if (campus.getUserLocationX() != 0 && campus.getUserLocationY() != 0 && scaleFactor * campus.getUserLocationX() >= getScrollX() && scaleFactor * campus.getUserLocationX() <= getScrollX() + screenWidth && scaleFactor * campus.getUserLocationY() >= getScrollY() && scaleFactor * campus.getUserLocationY() <= getScrollY() + screenHeight) {

                paint.setColor(Color.rgb(95, 12, 140));
                canvas.drawRect(scaleFactor * (float) (campus.getUserLocationX() - 20), scaleFactor * (float) (campus.getUserLocationY() - 20), scaleFactor * (float) (campus.getUserLocationX() + 20), scaleFactor * (float) (campus.getUserLocationY() + 20), paint);
            }
            paint.setStyle(Paint.Style.FILL);
            Log.d("CANVAS ONDRAW", "Start drawing start (v1) and end (v2) vertex for each edge");
            paint.setColor(Color.RED);
            if (campus.getIncidentVertexList() != null && campus.getIncidentVertexList().size() > 0) {
                for (Vertex v : campus.getIncidentVertexList()) {
                    if (scaleFactor * v.getX() >= getScrollX() && scaleFactor * v.getX() <= getScrollX() + screenWidth && scaleFactor * v.getY() >= getScrollY() && scaleFactor * v.getY() <= getScrollY() + screenHeight) {
                        canvas.drawCircle(scaleFactor * ((float) v.getX()), (float) scaleFactor * ((float) v.getY()), scaleFactor * 15, paint);
                    }
                }
            }
            /*paint.setColor(Color.YELLOW);
            for (int i = 1; i < campus.getEdgeMap().size(); i++) {
                Edge e = campus.getEdgeMap().get(i);
                if (e != null) {
                    Vertex v1 = e.getVertexList().get(0);
                    Vertex v2 = e.getVertexList().get(e.getVertexList().size() - 1);
                    if (scaleFactor *v1.getX() >= getScrollX() && scaleFactor *v1.getX() <= getScrollX()+screenWidth && scaleFactor *v1.getY() >= getScrollY() && scaleFactor *v1.getY() <= getScrollY()+screenHeight) {
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle(scaleFactor * ((float) v1.getX()), (float) scaleFactor * ((float) v1.getY()), scaleFactor * 10, paint);
                    }

                    if (scaleFactor *v2.getX() >= getScrollX() && scaleFactor *v2.getX() <= getScrollX()+screenWidth && scaleFactor *v2.getY() >= getScrollY() && scaleFactor *v2.getY() <= getScrollY()+screenHeight) {
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle((float) scaleFactor * ((float) v2.getX()), (float) scaleFactor * ((float) v2.getY()), scaleFactor * 10, paint);
                    }
                    float temp = paint.getStrokeWidth();
                    if (e.isStairs()) {
                        paint.setColor(Color.CYAN);
                        paint.setStrokeWidth(scaleFactor * 10);
                        if ((scaleFactor *v1.getX() >= getScrollX() && scaleFactor *v1.getX() <= getScrollX()+screenWidth && scaleFactor *v1.getY() >= getScrollY() && scaleFactor *v1.getY() <= getScrollY()+screenHeight) || (scaleFactor *v2.getX() >= getScrollX() && scaleFactor *v2.getX() <= getScrollX() + screenWidth && scaleFactor *v2.getY() >= getScrollY() && scaleFactor *v2.getY() <= getScrollY() + screenHeight)) {
                            canvas.drawLine((float) scaleFactor * ((float) v1.getX()), (float) scaleFactor * ((float) v1.getY()), (float) scaleFactor * ((float) v2.getX()), (float) scaleFactor * ((float) v2.getY()), paint);
                        }
                    }
                    paint.setStrokeWidth(temp);
                }
            }*/

            Log.d("CANVAS ONDRAW", "Check if calculated path needs to be drawn");
            if (campus.getCalculatedPathList().size() > 1) {
                Log.d("PLANROUTE", "Start drawing calculated route vertices");
                paint.setColor(Color.BLUE);
                for (Vertex v : campus.getCalculatedPathList()) {
                    if (scaleFactor * v.getX() >= getScrollX() && scaleFactor * v.getX() <= getScrollX() + screenWidth && scaleFactor * v.getY() >= getScrollY() && scaleFactor * v.getY() <= getScrollY() + screenHeight) {
                        canvas.drawCircle((float) scaleFactor * ((float) v.getX()), (float) scaleFactor * ((float) v.getY()), scaleFactor * 15, paint);
                    }
                }
                Log.d("PLANROUTE", "Start drawing calculated route edges between vertices");
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(scaleFactor * 10);
                for (int i = 1; i < campus.getCalculatedPathList().size(); i++) {
                    Vertex v1 = campus.getCalculatedPathList().get(i - 1);
                    Vertex v2 = campus.getCalculatedPathList().get(i);
                    if ((scaleFactor * v1.getX() >= getScrollX() && scaleFactor * v1.getX() <= getScrollX() + screenWidth && scaleFactor * v1.getY() >= getScrollY() && scaleFactor * v1.getY() <= getScrollY() + screenHeight) || (scaleFactor * v2.getX() >= getScrollX() && scaleFactor * v2.getX() <= getScrollX() + screenWidth && scaleFactor * v2.getY() >= getScrollY() && scaleFactor * v2.getY() <= getScrollY() + screenHeight)) {
                        canvas.drawLine((float) scaleFactor * ((float) v1.getX()), (float) scaleFactor * ((float) v1.getY()), (float) scaleFactor * ((float) v2.getX()), (float) scaleFactor * ((float) v2.getY()), paint);
                    }
                }
            }

            if (campus.getSource() != null) {
                Vertex s = campus.getSource();
                paint.setColor(Color.rgb(216, 150, 52));
                canvas.drawCircle((float) scaleFactor * ((float) s.getX()), (float) scaleFactor * ((float) s.getY()), scaleFactor * 15, paint);
            }
            if (campus.getDestination() != null) {
                Vertex d = campus.getDestination();
                paint.setColor(Color.rgb(52, 164, 216));
                canvas.drawCircle((float) scaleFactor * ((float) d.getX()), (float) scaleFactor * ((float) d.getY()), scaleFactor * 15, paint);
            }
        }
    }

    public void setGraph(Graph campus) {
        this.campus = campus;
    }

    public void setInitialScaleFactor(float initialScaleFactor) {
        this.initialScaleFactor = initialScaleFactor;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setOnLoad(boolean onLoad) {this.onLoad = onLoad;}

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public float getInitialScaleFactor() {return initialScaleFactor;}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        float scaleFactor = getScale()/initialScaleFactor;
        Log.d("COORDS", this.getScrollX() + ":" + this.getScrollY());
        switch  (action) {
            case MotionEvent.ACTION_DOWN:
                startClickTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                    float downX = (event.getX()+getScrollX())/scaleFactor;
                    float downY = (event.getY()+getScrollY())/scaleFactor;
                    for (Vertex v : campus.getVertexMap().values()) {
                        if (Math.pow((downX - v.getX()), 2) + Math.pow((downY - v.getY()), 2) < Math.pow(scaleFactor * 40, 2)) {
                            mainActivity.showVertexDialog(v);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void scrollToUserLocation() {
        if (campus.getUserLocationX() > 0 && campus.getUserLocationY() > 0 && campus.getUserLocationX() < canvasWidth & campus.getUserLocationY() < canvasHeight) {
            float scaleFactor = getScale() / initialScaleFactor;
            this.scrollTo((int) Math.round(scaleFactor * campus.getUserLocationX()) - screenWidth/2, (int) Math.round(scaleFactor * campus.getUserLocationY()) - screenHeight/2);
        }
    }

    public void scrollToPathStart() {
        if (campus.getCalculatedPathList() != null && campus.getCalculatedPathList().get(0) != null) {
            float scaleFactor = getScale() / initialScaleFactor;
            this.scrollTo((int) Math.round(scaleFactor * campus.getCalculatedPathList().get(0).getX()) - screenWidth/2, (int) Math.round(scaleFactor * campus.getCalculatedPathList().get(0).getY()) - screenHeight/2);
        }
    }
}
