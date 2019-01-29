package exeter.project.tobyreeve.execcessibility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Map;

public class MyCanvas extends View {
    Paint paint;
    int canvasWidth;
    Graph campus;
    int canvasHeight;
    public MyCanvas(Context context, Graph campus) {
        super(context);
        paint = new Paint();
        this.campus = campus;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Bitmap tile = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        canvasWidth = tile.getWidth();
        canvasHeight = tile.getHeight();
        setMeasuredDimension(canvasWidth, canvasHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap[] parts = new Bitmap[1];
        parts[0] = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        canvas.drawBitmap(parts[0], 0, 0, paint);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 1; i < campus.getEdgeMap().size(); i++) {
            Edge e = campus.getEdgeMap().get(i);
            Vertex v1 = e.getVertexList().get(0);
            Vertex v2 = e.getVertexList().get(e.getVertexList().size()-1);
            canvas.drawCircle((float) v1.getX(), (float) v1.getY(), 10, paint);
            canvas.drawCircle((float) v2.getX(), (float) v2.getY(), 10, paint);
        }
        /*paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        for (int i = 1; i < campus.getEdgeMap().size(); i++) {
            Edge e = campus.getEdgeMap().get(i);
            for (int j = 0; j < e.getVertexList().size()-1; j++) {
                Vertex v1 = e.getVertexList().get(j);
                Vertex v2 = e.getVertexList().get(j+1);
                canvas.drawLine((float) v1.getX(), (float) v1.getY(), (float) v2.getX(), (float) v2.getY(), paint);
            }
        }*/
        paint.setColor(Color.BLUE);
        for (Vertex v : campus.getCalculatedPathList()) {
            canvas.drawCircle((float) v.getX(), (float) v.getY(), 20, paint);
        }

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        for (int i = 1; i < campus.getCalculatedPathList().size(); i++) {
            Vertex v1 = campus.getCalculatedPathList().get(i-1);
            Vertex v2 = campus.getCalculatedPathList().get(i);
            canvas.drawLine((float) v1.getX(), (float) v1.getY(), (float) v2.getX(), (float) v2.getY(), paint);
        }
    }

}
