package exeter.project.tobyreeve.execcessibility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.webkit.WebView;

public class MyCanvas extends WebView {
    Paint paint;
    Graph campus;

    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaleFactor = getScale()/((float)2.625);
        Log.d("CANVAS ONDRAW", "Current scale: " + String.valueOf(getScale()) + ", Current scale factor: " + String.valueOf(scaleFactor) + ", Current ScrollX value: " + String.valueOf(getScrollX()) + ", Current ScrollY value:" + String.valueOf(getScrollY()));
        if (campus != null) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            Log.d("CANVAS ONDRAW", "Start drawing start (v1) and end (v2) vertex for each edge");
            for (int i = 1; i < campus.getEdgeMap().size(); i++) {
                Edge e = campus.getEdgeMap().get(i);
                Vertex v1 = e.getVertexList().get(0);
                Vertex v2 = e.getVertexList().get(e.getVertexList().size()-1);
                canvas.drawCircle(scaleFactor*((float) v1.getX())-getScrollX(), (float) scaleFactor*((float) v1.getY())-getScrollY(), scaleFactor*10, paint);
                canvas.drawCircle((float) scaleFactor*((float) v2.getX())-getScrollX(), (float) scaleFactor*((float) v2.getY())-getScrollY(), scaleFactor*10, paint);
            }

            Log.d("CANVAS ONDRAW", "Check if calculated path needs to be drawn");
            if (campus.getCalculatedPathList().size() > 0) {
                Log.d("PLANROUTE", "Start drawing calculated route vertices");
                paint.setColor(Color.BLUE);
                for (Vertex v : campus.getCalculatedPathList()) {
                    canvas.drawCircle((float) scaleFactor*((float) v.getX())-getScrollX(), (float) scaleFactor*((float) v.getY())-getScrollY(), scaleFactor*20, paint);
                }

                Log.d("PLANROUTE", "Start drawing calculated route edges between vertices");
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(scaleFactor*10);
                for (int i = 1; i < campus.getCalculatedPathList().size(); i++) {
                    Vertex v1 = campus.getCalculatedPathList().get(i - 1);
                    Vertex v2 = campus.getCalculatedPathList().get(i);
                    canvas.drawLine((float) scaleFactor*((float) v1.getX())-getScrollX(), (float) scaleFactor*((float) v1.getY())-getScrollY(), (float) scaleFactor*((float) v2.getX())-getScrollX(), (float) scaleFactor*((float) v2.getY())-getScrollY(), paint);
                }
            }
        }
    }

    public void setGraph(Graph campus) {
        this.campus = campus;
    }

}
