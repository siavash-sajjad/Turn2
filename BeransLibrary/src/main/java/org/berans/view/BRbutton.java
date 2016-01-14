package org.berans.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class BRbutton extends Button {

    public String  fontsPath = "fonts/";
    public String  font      = "iransans";
    public Context context;


    public BRbutton(Context context) {
        super(context);
        initialize(context, null);
    }


    public BRbutton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode())
            return;

        initialize(context, attrs);
    }


    public BRbutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if(isInEditMode())
            return;

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
