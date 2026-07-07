package com.schulte.table.ui.home.training;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * A FrameLayout that forces its dimensions to be square.
 * Used for grid cells in Schulte table to ensure proper sizing.
 */
public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get the available width
        int width = MeasureSpec.getSize(widthMeasureSpec);
        
        // Use width as height to make square
        int squareMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        
        // Measure with square dimensions
        super.onMeasure(widthMeasureSpec, squareMeasureSpec);
    }
}