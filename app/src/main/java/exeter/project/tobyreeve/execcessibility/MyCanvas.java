package exeter.project.tobyreeve.execcessibility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class MyCanvas extends View {
    Paint paint;
    Context app_context;
    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Bitmap tile = BitmapFactory.decodeResource(getResources(), R.drawable.z15_1_1);
        int width = tile.getWidth()*6;
        int height = tile.getHeight()*4;
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap[] parts = new Bitmap[6];
        parts[0] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_1_1);
        parts[1] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_1_2);
        parts[2] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_2_1);
        parts[3] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_2_2);
        parts[4] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_3_1);
        parts[5] = BitmapFactory.decodeResource(getResources(), R.drawable.z15_3_2);

        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[0], parts[0].getWidth()*2, parts[0].getHeight()*2, false), 0, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[1], parts[1].getWidth()*2, parts[1].getHeight()*2, false), 0, canvas.getHeight()/2, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[2], parts[2].getWidth()*2, parts[2].getHeight()*2, false), canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[3], parts[3].getWidth()*2, parts[3].getHeight()*2, false), canvas.getWidth()/3, canvas.getHeight()/2, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[4], parts[4].getWidth()*2, parts[4].getHeight()*2, false), 2*canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(parts[5], parts[5].getWidth()*2, parts[5].getHeight()*2, false), 2*canvas.getWidth()/3, canvas.getHeight()/2, paint);

       /* canvas.drawBitmap(parts[0], 0, 0, paint);
        canvas.drawBitmap(parts[1], 0, canvas.getHeight()/2, paint);
        canvas.drawBitmap(parts[2], canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(parts[3], canvas.getWidth()/3, canvas.getHeight()/2, paint);
        canvas.drawBitmap(parts[4], 2*canvas.getWidth()/3, 0, paint);
        canvas.drawBitmap(parts[5], 2*canvas.getWidth()/3, canvas.getHeight()/2, paint);*/

    }
}
