package com.grietenenknapen.sithandroid.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.util.MathUtils;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class LightSaberLoadingView extends View {

    public static final int COLOR_RED = 0;
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_RANDOM = 50;
    private static final int FORCE_TIME = 30;

    @Retention(SOURCE)
    @IntDef({ANIMATION_MODE_NORMAL, ANIMATION_MODE_FORCE})
    private @interface AnimationMode {
    }

    private static final int ANIMATION_MODE_NORMAL = 0;
    private static final int ANIMATION_MODE_FORCE = 1;
    @AnimationMode
    private int animationMode = ANIMATION_MODE_NORMAL;

    private static final String ROTATE_ANGLE = "rotateAngle";
    private static final int ROTATE_DURATION = 1500;
    private static final int DESIRED_WIDTH_DP = 56;
    private static final int DESIRED_HEIGHT_DP = 56;
    private static final float TAIL_SIZE_PERCENTAGE = 0.03f;
    private VectorDrawableCompat lightSaberDrawable;
    private float rotateAngle = 0;
    private RectF tailCircle;
    private Paint tailPaint;
    private AnimatorSet saberAnimatorSet;
    private float tailSize;

    public LightSaberLoadingView(final Context context) {
        this(context, null, 0);
    }

    public LightSaberLoadingView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightSaberLoadingView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LightSaberLoadingView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(final AttributeSet attrs, final int defStyle) {
        int lightSaberColor = 0;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LightSaberLoadingView, defStyle, 0);
            lightSaberColor = a.getInt(R.styleable.LightSaberLoadingView_saberColor, 0);
            a.recycle();
        }

        if (lightSaberColor == COLOR_RANDOM) {
            lightSaberColor = MathUtils.generateRandomInteger(0, 1);
        }

        tailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initLightSaberColor(lightSaberColor);
        tailPaint.setAlpha(160);
        tailPaint.setStyle(Paint.Style.STROKE);
        tailPaint.setStrokeCap(Paint.Cap.ROUND);

        setWillNotDraw(true);
    }

    private void initLightSaberColor(final int lightSaberColor) {
        switch (lightSaberColor) {
            case COLOR_RED:
                setLightSaberColor(R.drawable.light_saber_red, R.color.light_saber_red);
                break;
            case COLOR_GREEN:
                setLightSaberColor(R.drawable.light_saber_green, R.color.light_saber_green);
                break;
            default:
                setLightSaberColor(R.drawable.light_saber_red, R.color.light_saber_red);
        }
    }

    private void setLightSaberColor(@DrawableRes final int drawableRes,
                                    @ColorRes final int drawableColor) {

        lightSaberDrawable = VectorDrawableCompat.create(getResources(), drawableRes, null);
        tailPaint.setColor(ContextCompat.getColor(getContext(), drawableColor));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        handleStartStopAnimation();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, final int visibility) {
        if (isInEditMode()) {
            return;
        }

        super.onVisibilityChanged(changedView, visibility);
        handleStartStopAnimation();
    }

    @Override
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        //handleStartStopAnimation();
    }

    public void startForceAnimation() {
        if (saberAnimatorSet != null) {
            saberAnimatorSet.cancel();
            saberAnimatorSet = null;
        }
        animationMode = ANIMATION_MODE_FORCE;
        handleStartStopAnimation();
    }

    private void handleStartStopAnimation() {
        if (getVisibility() == View.VISIBLE && (saberAnimatorSet == null || !saberAnimatorSet.isRunning())) {
            if (saberAnimatorSet != null) {
                saberAnimatorSet.cancel();
            }
            setRotateAngle(0);
            final ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(this, ROTATE_ANGLE, 360);
            rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);
            switch (animationMode) {
                case ANIMATION_MODE_FORCE:
                    rotateAnimator.setInterpolator(new LinearInterpolator());
                    rotateAnimator.setDuration(ROTATE_DURATION / 3);
                    rotateAnimator.setRepeatCount(FORCE_TIME);
                    animationMode = ANIMATION_MODE_NORMAL;
                    rotateAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(final Animator animation) {
                            saberAnimatorSet = null;
                            handleStartStopAnimation();
                        }
                    });
                    break;
                case ANIMATION_MODE_NORMAL:
                default:
                    rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    rotateAnimator.setDuration(ROTATE_DURATION);
                    rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
            }

            saberAnimatorSet = new AnimatorSet();
            saberAnimatorSet.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(final Animator animation) {
                    setWillNotDraw(false);
                }
            });
            saberAnimatorSet.playTogether(rotateAnimator);
            saberAnimatorSet.start();
        } else if (saberAnimatorSet != null && getVisibility() != View.VISIBLE) {
            saberAnimatorSet.cancel();
        }
    }

    public void setRotateAngle(final float rotateAngle) {
        this.rotateAngle = rotateAngle;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public float getRotateAngle() {
        return rotateAngle;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        int desiredWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DESIRED_WIDTH_DP, getResources().getDisplayMetrics());
        int desiredHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DESIRED_HEIGHT_DP, getResources().getDisplayMetrics());

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        tailSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h * TAIL_SIZE_PERCENTAGE, getResources().getDisplayMetrics());
        tailPaint.setStrokeWidth(tailSize);

        final float halfTailSize = tailSize / 2;
        tailCircle = new RectF(halfTailSize, halfTailSize, w - halfTailSize, h - halfTailSize);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final float tailAngle = (float) ((rotateAngle > 180 ? 360 - rotateAngle : rotateAngle) / (1.25 - (Math.abs(180 - rotateAngle) / 720)));
        final float extraMarginAngle = tailSize / 2; //This is extra margin to make make sure the the light doesn't appear before the saber
        canvas.drawArc(tailCircle, rotateAngle - tailAngle - 90 - extraMarginAngle, tailAngle, false, tailPaint);
        canvas.save();
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        lightSaberDrawable.setBounds(0, 0, getWidth(), getHeight());
        lightSaberDrawable.draw(canvas);
        canvas.restore();
    }
}
