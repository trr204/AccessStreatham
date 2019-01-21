package exeter.project.tobyreeve.execcessibility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MyCanvas extends View {
    Paint paint;
    Canvas thisCanvas;
    Context app_context;
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

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        double heightScale;
        double widthScale;
        for (int i = 1; i <= campus.getVertexMap().size(); i++) {
            Vertex thisV = campus.getVertexMap().get(i);
            thisV.setHeightScale(1-(thisV.getLatitude() - campus.getMinLatitude())/(campus.getMaxLatitude() - campus.getMinLatitude()));
            thisV.setWidthScale((thisV.getLongitude() - campus.getMinLongitude())/(campus.getMaxLongitude() - campus.getMinLongitude()));

            canvas.drawCircle((float) thisV.getWidthScale()*canvasWidth, (float) thisV.getHeightScale()*canvasHeight, 20, paint);
        }

        for (Subedge s : campus.getSubedgeList()) {
            Vertex v1 = campus.getVertexMap().get(s.getVertex1Id());
            Vertex v2 = campus.getVertexMap().get(s.getVertex2Id());

            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(15);
            canvas.drawLine((float) v1.getWidthScale()*canvasWidth, (float) v1.getHeightScale()*canvasHeight, (float) v2.getWidthScale()*canvasWidth, (float) v2.getHeightScale()*canvasHeight, paint);
        }
        /*parts[0] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_1_1);
        parts[1] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_1_2);
        parts[2] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_2_1);
        parts[3] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_2_2);
        parts[4] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_3_1);
        parts[5] = BitmapFactory.decodeResource(getResources(), R.drawable.z17_3_2);

        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[0], parts[0].getWidth()*2, parts[0].getHeight()*2, false), 0, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[1], parts[1].getWidth()*2, parts[1].getHeight()*2, false), 0, canvas.getHeight()/2, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[2], parts[2].getWidth()*2, parts[2].getHeight()*2, false), canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[3], parts[3].getWidth()*2, parts[3].getHeight()*2, false), canvas.getWidth()/3, canvas.getHeight()/2, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[4], parts[4].getWidth()*2, parts[4].getHeight()*2, false), 2*canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[5], parts[5].getWidth()*2, parts[5].getHeight()*2, false), 2*canvas.getWidth()/3, canvas.getHeight()/2, paint);
*/
       /* canvas.drawBitmap(Bitmap.createScaledBitmap(parts[0], parts[0].getWidth()*2, parts[0].getHeight()*2, false), 0, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[1], parts[1].getWidth()*2, parts[1].getHeight()*2, false), 0, canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[2], parts[2].getWidth()*2, parts[2].getHeight()*2, false), 0, 2*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[3], parts[3].getWidth()*2, parts[3].getHeight()*2, false), 0, 3*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[4], parts[4].getWidth()*2, parts[4].getHeight()*2, false), canvas.getWidth()/4, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[5], parts[5].getWidth()*2, parts[5].getHeight()*2, false), canvas.getWidth()/4, canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[6], parts[6].getWidth()*2, parts[6].getHeight()*2, false), canvas.getWidth()/4, 2*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[7], parts[7].getWidth()*2, parts[7].getHeight()*2, false), canvas.getWidth()/4, 3*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[8], parts[8].getWidth()*2, parts[8].getHeight()*2, false), 2*canvas.getWidth()/4, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[9], parts[9].getWidth()*2, parts[9].getHeight()*2, false), 2*canvas.getWidth()/4, canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[10], parts[10].getWidth()*2, parts[10].getHeight()*2, false), 2*canvas.getWidth()/4, 2*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[11], parts[11].getWidth()*2, parts[11].getHeight()*2, false), 2*canvas.getWidth()/4, 3*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[12], parts[12].getWidth()*2, parts[12].getHeight()*2, false), 3*canvas.getWidth()/4, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[13], parts[13].getWidth()*2, parts[13].getHeight()*2, false), 3*canvas.getWidth()/4, canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[14], parts[14].getWidth()*2, parts[14].getHeight()*2, false), 3*canvas.getWidth()/4, 2*canvas.getHeight()/4, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[15], parts[15].getWidth()*2, parts[15].getHeight()*2, false), 3*canvas.getWidth()/4, 3*canvas.getHeight()/4, paint);*/

        /*canvas.drawBitmap(parts[0], 0, 0, paint);
        canvas.drawBitmap(parts[1], 0, canvas.getHeight()/2, paint);
        canvas.drawBitmap(parts[2], canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(parts[3], canvas.getWidth()/3, canvas.getHeight()/2, paint);
        canvas.drawBitmap(parts[4], 2*canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(parts[5], 2*canvas.getWidth()/3, canvas.getHeight()/2, paint);*/
    }
}
