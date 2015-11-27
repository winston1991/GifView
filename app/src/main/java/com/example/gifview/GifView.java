package com.example.gifview;

import java.io.InputStream;

import com.example.gifview.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {

	private Movie mMovie = null;
	private long mfristtime = 0;
	private InputStream is;

	private float hscale;
	private float wscale;
	private float scale;

	private Paint mPaint;
	private int mIndex;
	private int mColor;

	public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		setFocusable(true);
		mPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		mPaint.setStyle(Paint.Style.FILL);

		TypedArray obtainStyledAttributes = context.obtainStyledAttributes(
				attrs, R.styleable.GifView, defStyleAttr, 0);
		int resourceId = obtainStyledAttributes.getResourceId(
				R.styleable.GifView_gif, 0);
		mIndex = obtainStyledAttributes
				.getInteger(R.styleable.GifView_index, 0);
		
		mColor = obtainStyledAttributes.getColor(R.styleable.GifView_color, 0);
		if (resourceId != 0) {
			is = context.getResources().openRawResource(resourceId);
			mMovie = Movie.decodeStream(is);
			obtainStyledAttributes.recycle();
		}
	}

	public GifView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public GifView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public void setGif(int resourceId) {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		 super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();
		if (mfristtime == 0) {
			mfristtime = now;
		}
		if (mMovie != null) {
			int duration = mMovie.duration();
			if (duration == 0) {
				duration = 1000;
			}
			int reltimes = (int) ((now - mfristtime) % duration);
			mMovie.setTime(reltimes);
			// scale = Math.max(wscale, hscale);
			canvas.scale(wscale, hscale);

			// mMovie.draw(canvas, (getWidth() - mMovie.width()),
			// (getHeight() - mMovie.height()));

			mMovie.draw(canvas, 0, 0);
			canvas.scale(1 / wscale, 1 / hscale);
			mPaint.setColor(mColor);
			canvas.drawCircle(40, 40, 30, mPaint);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setTextSize(35);
			mPaint.setColor(Color.rgb(255, 255, 255));
			canvas.drawText(String.valueOf(mIndex), 40, 50, mPaint);
			invalidate();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (mMovie != null) {
			// widthMode = MeasureSpec.getMode(widthMeasureSpec);
			// heightMode = MeasureSpec.getMode(heightMeasureSpec);
			// mwidth = MeasureSpec.getSize(widthMeasureSpec);
			// mheight = MeasureSpec.getSize(heightMeasureSpec);
			int w = mMovie.width();
			int h = mMovie.height();
			int pleft = getPaddingLeft();
			int pright = getPaddingRight();
			int ptop = getPaddingTop();
			int pbottom = getPaddingBottom();

			int widthSize;
			int heightSize;
			w += pleft + pright;
			h += ptop + pbottom;

			w = Math.max(w, getSuggestedMinimumWidth());
			h = Math.max(h, getSuggestedMinimumHeight());

			widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
			heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
			wscale = (float) widthSize / w;
			hscale = (float) heightSize / h;

			setMeasuredDimension(widthSize, heightSize);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
