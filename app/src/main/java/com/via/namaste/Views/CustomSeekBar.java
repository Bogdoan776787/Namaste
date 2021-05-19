package com.via.namaste.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.via.namaste.R;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class CustomSeekBar extends SeekBar {
    private TextPaint mTextPaint;
    String[] stringsList;

    private ArrayList<ProgressItem> mProgressItemsList;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initData(ArrayList<ProgressItem> progressItemsList, String[] strings) {
        this.mProgressItemsList = progressItemsList;
        stringsList = strings;
        mTextPaint = new TextPaint();
        mTextPaint.setColor(getResources().getColor(R.color.white));
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.thumb_text_size));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (mProgressItemsList.size() > 0) {
            int progressBarWidth = getWidth();
            int progressBarHeight = getHeight();
            int thumbOffsets = getThumbOffset();
            int lastProgressX = 0;
            int progressItemWidth, progressItemRight;
            for (int i = 0; i < mProgressItemsList.size(); i++) {
                ProgressItem progressItem = mProgressItemsList.get(i);
                @SuppressLint("DrawAllocation") Paint progressPaint = new Paint();
                progressPaint.setColor(getResources().getColor(
                        progressItem.color));

                progressItemWidth = (int) (progressItem.progressItemPercentage
                        * progressBarWidth / 100);

                progressItemRight = lastProgressX + progressItemWidth;

                if (i == mProgressItemsList.size() - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                @SuppressLint("DrawAllocation") Rect progressRect = new Rect();

                progressRect.set(lastProgressX, thumbOffsets / 2,
                        progressItemRight, progressBarHeight - thumbOffsets / 2);
                canvas.drawRect(progressRect, progressPaint);
                String progressText1 = stringsList[i];
                String progressText = String.valueOf(getProgress());
                @SuppressLint("DrawAllocation") Rect bounds = new Rect();
                mTextPaint.getTextBounds(progressText1, 0, progressText1.length(), bounds);

                int leftPadding = getPaddingLeft() - getThumbOffset();
                int rightPadding = getPaddingRight() - getThumbOffset();
                int width = getWidth() - leftPadding - rightPadding;
                float progressRatio = (float) getProgress() / getMax();
                float thumbOffset = 60 * (.7f - progressRatio);
                float thumbX = progressRatio * width + leftPadding + thumbOffset;
                float thumbY = getHeight() / 5f + bounds.height() / 5f;
                canvas.drawText(progressText1, lastProgressX, progressBarHeight - thumbOffsets / 3, mTextPaint);
                canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
                lastProgressX = progressItemRight;
            }
            super.onDraw(canvas);
        }

    }


}
