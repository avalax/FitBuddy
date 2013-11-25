package de.avalax.fitbuddy.app.swipeBar.progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import de.avalax.fitbuddy.app.R;

public class XmlVerticalProgressBar extends ImageView{
    public XmlVerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //TODO: simplyfy typedArray not needed
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalProgressBar);
        setImageResource(a.getResourceId(R.styleable.VerticalProgressBar_progressBar, R.drawable.primary_progress_bar));
        setImageLevel(a.getInt(R.styleable.VerticalProgressBar_imageLevel, 5000));
        a.recycle();
    }
}
