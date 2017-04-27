package vision.genesis.ui.order;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import vision.genesis.R;


class ListItemDecorator extends RecyclerView.ItemDecoration {

	private Paint mPaint;
	private int   mStrokeWidth;
	private int   mStartPadding;

	public ListItemDecorator(Context context) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources res = context.getResources();
		Resources.Theme theme = context.getTheme();
		int color = ResourcesCompat.getColor(res, R.color.grayD9, theme);
		mPaint.setColor(color);
		mStrokeWidth = res.getDimensionPixelSize(R.dimen.list_divider_stroke_width);
		mStartPadding = 0;
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int left = parent.getPaddingLeft() + mStartPadding;
		int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 1; i < childCount - 1; i++) {
			View child = parent.getChildAt(i);

			int top = child.getBottom();
			int bottom = child.getBottom() + mStrokeWidth;
			c.drawRect(left, top, right, bottom, mPaint);
		}
	}
}