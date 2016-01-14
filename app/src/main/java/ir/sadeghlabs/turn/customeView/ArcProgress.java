package ir.sadeghlabs.turn.customeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Siavash on 12/12/2015.
 */
public class ArcProgress extends View {
    Paint paint;
    Path path;

    public ArcProgress(Context context) {
        super(context);
        init();
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
// TODO Auto-generated method stub
        super.onDraw(canvas);

        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
/*
* drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
*
* oval - The bounds of oval used to define the shape and size of the arc
* startAngle - Starting angle (in degrees) where the arc begins
* sweepAngle - Sweep angle (in degrees) measured clockwise
* useCenter - If true, include the center of the oval in the arc, and close it if it is being stroked. This will draw a wedge
* paint - The paint used to draw the arc
*/

        canvas.drawArc(oval, 0, 180, false, paint);
    }
}