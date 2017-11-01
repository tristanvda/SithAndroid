package com.grietenenknapen.sithandroid.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.view.Gravity;

import com.grietenenknapen.sithandroid.util.BitmapUtil;

import static com.grietenenknapen.sithandroid.util.BitmapUtil.getBitmap;
import static com.grietenenknapen.sithandroid.util.BitmapUtil.replaceBitmapColor;

public class AnimateDrawable extends Drawable implements Animatable {

    private static final int SECOND = 1000;
    private Bitmap[] bitmaps;
    private int numStates = 0;
    private int refreshTime;
    private final Handler animateHandler = new Handler();
    private int currentState = 0;
    private Rect dstRect = new Rect();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private volatile boolean animating;

    public AnimateDrawable(final Context context, final Integer[] drawablesResIds, final int refreshRate) {
        init(context, drawablesResIds, refreshRate, new BitmapUtil.PixelColorReplaceable[]{});
    }

    public AnimateDrawable(final Context context, final Integer[] drawablesResIds, final int refreshRate, BitmapUtil.PixelColorReplaceable[] pixelColorReplaceables) {
        init(context, drawablesResIds, refreshRate, pixelColorReplaceables);
    }

    private void init(final Context context, final Integer[] drawablesResIds, final int refreshRate, BitmapUtil.PixelColorReplaceable[] pixelColorReplaceables) {
        if (drawablesResIds.length == 0) {
            throw new IllegalStateException("AnimateDrawable cannot have 0 drawables");
        }
        this.refreshTime = (SECOND / refreshRate);

        initBitmaps(context, drawablesResIds, pixelColorReplaceables);
    }

    private void initBitmaps(final Context context, final Integer[] drawablesResIds, final BitmapUtil.PixelColorReplaceable[] pixelColorReplaceables) {
        numStates = drawablesResIds.length;

        AsyncTaskCompat.executeParallel(new AsyncTask<Integer, Object, Bitmap[]>() {
            @Override
            protected Bitmap[] doInBackground(final Integer... resIds) {
                Bitmap[] bitmaps = new Bitmap[resIds.length];
                for (int i = 0; i < resIds.length; i++) {
                    final Bitmap bitmap = getBitmap(context, resIds[i]);
                    if (pixelColorReplaceables.length > 0) {
                        replaceBitmapColor(bitmap, pixelColorReplaceables);
                    }
                    bitmaps[i] = bitmap;
                }
                return bitmaps;
            }

            @Override
            protected void onPostExecute(final Bitmap[] bitmapList) {
                super.onPostExecute(bitmapList);
                bitmaps = bitmapList;
                invalidateSelf();
            }
        }, drawablesResIds);
    }

    private Runnable animateRunnable = new Runnable() {
        @Override
        public void run() {
            if (animating) {
                currentState = (currentState == numStates - 1) ? 0 : currentState + 1;
                invalidateSelf();
                animateHandler.postDelayed(animateRunnable, refreshTime);
            }
        }
    };

    public void setState(int state) {
        if (state < 0 || state >= numStates) {
            throw new IllegalStateException("State should be within bounds");
        }

        this.currentState = state;
        invalidateSelf();
    }

    @Override
    public void start() {
        animating = true;
        animateHandler.postDelayed(animateRunnable, refreshTime);
    }

    @Override
    public void stop() {
        animating = false;
        animateHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean isRunning() {
        return animating;
    }

    @Override
    public void draw(@NonNull final Canvas canvas) {
        if (bitmaps == null || currentState >= numStates) {
            return;
        }

        final Bitmap bitmap = bitmaps[currentState];
        Gravity.apply(Gravity.CENTER, bitmap.getWidth(), bitmap.getHeight(), getBounds(), dstRect);
        canvas.drawBitmap(bitmaps[currentState], null, dstRect, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) final int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
