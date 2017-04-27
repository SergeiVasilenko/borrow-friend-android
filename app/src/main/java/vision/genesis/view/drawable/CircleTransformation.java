package vision.genesis.view.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import hugo.weaving.DebugLog;

public class CircleTransformation extends BitmapTransformation {

	public CircleTransformation(Context context) {
		super(context);
	}

	@DebugLog
	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
		int srcWidth = source.getWidth();
		int srcHeight = source.getHeight();
		int srcSize = Math.min(srcWidth, srcHeight);
		int translateX = (srcWidth - srcSize) / 2;
		int translateY = (srcHeight - srcSize) / 2;

		Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
		if (result == null) {
			result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(result);
		BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		Matrix matrix = new Matrix();
		float scale;
		if (srcWidth > srcHeight) {
			scale = (float) outHeight / srcHeight;
		} else {
			scale = (float) outWidth / srcWidth;
		}
		matrix.preScale(scale, scale);
		if (translateX > 0 || translateY > 0) {
			matrix.postTranslate(-translateX, -translateY);
		}
		shader.setLocalMatrix(matrix);
		paint.setShader(shader);

		float r = (float) outWidth / 2f;
		canvas.drawCircle(r, r, r, paint);

		return result;
	}

	@Override
	public String getId() {
		return "CircleTransformation";
	}
}