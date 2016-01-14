package org.berans.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


public class BRedittext extends EditText {

    public String  fontsPath = "fonts/";
    public String  font      = "iransans";
    public Context context;

    public BRedittext(Context context) {
        super(context);
        initialize(context, null);
    }

    public BRedittext(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode()){
            return;
        }

        initialize(context, attrs);
    }

    public BRedittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(isInEditMode()){
            return;
        }

        initialize(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BRedittext(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }


    public void initialize(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray attrFontName = getContext().obtainStyledAttributes(attrs, R.styleable.app);
        String fontName = attrFontName.getString(R.styleable.app_fontName);
        if (fontName!=null) {
            setFont(fontName);
        }else{
            setFont(font);
        }
        attrFontName.recycle();
    }


    public void setFont(String font) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontsPath + font + ".ttf");
        setTypeface(tf);
    }

}
