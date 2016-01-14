package ir.sadeghlabs.turn.customeView;

/**
 * Created by Siavash on 12/10/2015.
 */
import java.io.InputStream;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import ir.sadeghlabs.turn.R;

public class gifImageView extends View {
    public Movie mMovie;
    public long movieStart;

    public gifImageView(Context context) {
        super(context);
        initializeView();
    }

    public gifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode())
            return;

        initializeView();
    }

    public gifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if(isInEditMode())
            return;

        initializeView();
    }

    private void initializeView() {
        InputStream is = getContext().getResources().openRawResource(gifId);
        mMovie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
            this.invalidate();
        }
    }
    private int gifId = R.mipmap.search_loading;

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView();
    }

    public int getGIFResource() {
        return this.gifId;
    }
}