package com.example.alan.beziercurve;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by alan on 2017/11/15.
 */

public class HeartAnim extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintAssit = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();
    private double magicNumber = 0.55228475;
    private Point[] pointsData = new Point[4];
    private Point[] pointsControl = new Point[8];
    private int radius;
    private int centerX;
    private int centerY;

    public HeartAnim(Context context) {
        super(context);
        init();

    }

    public HeartAnim(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HeartAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, 600);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 600);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = getMeasuredWidth() * 3 / 8;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        initPoints();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaintAssit.setColor(Color.BLACK);
        mPaintAssit.setTextSize(40);

        for (int i = 0; i < pointsData.length; i++) {
            pointsData[i] = new Point();
        }
        for (int i = 0; i < pointsControl.length; i++) {
            pointsControl[i] = new Point();
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 2000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //绘制出辅助点的位置
//        for (int i = 0; i < pointsData.length; i++) {
//            Point p = pointsData[i];
//            canvas.drawCircle(p.x, p.y, 10, mPaintAssit);
//            canvas.drawText("D" + i, p.x, p.y, mPaintAssit);
//        }
//        for (int i = 0; i < pointsControl.length; i++) {
//            Point p = pointsControl[i];
//            canvas.drawCircle(p.x, p.y, 10, mPaintAssit);
//            canvas.drawText("C" + i, p.x, p.y, mPaintAssit);
//        }

//        mPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
        //绘制四段贝塞尔曲线拟合一个圆
        mPath.moveTo(pointsData[0].x, pointsData[0].y);
        mPath.cubicTo(pointsControl[0].x, pointsControl[0].y, pointsControl[1].x, pointsControl[1].y, pointsData[1].x, pointsData[1].y);
        canvas.drawPath(mPath, mPaint);
        mPath.cubicTo(pointsControl[2].x, pointsControl[2].y, pointsControl[3].x, pointsControl[3].y, pointsData[2].x, pointsData[2].y);
        canvas.drawPath(mPath, mPaint);
        mPath.cubicTo(pointsControl[4].x, pointsControl[4].y, pointsControl[5].x, pointsControl[5].y, pointsData[3].x, pointsData[3].y);
        canvas.drawPath(mPath, mPaint);
        mPath.cubicTo(pointsControl[6].x, pointsControl[6].y, pointsControl[7].x, pointsControl[7].y, pointsData[0].x, pointsData[0].y);
        canvas.drawPath(mPath, mPaint);
    }

    private void initPoints() {
        pointsData[0].x = centerX;
        pointsData[0].y = centerY - radius;
        pointsData[1].x = centerX + radius;
        pointsData[1].y = centerY;
        pointsData[2].x = centerX;
        pointsData[2].y = centerY + radius;
        pointsData[3].x = centerX - radius;
        pointsData[3].y = centerY;

        pointsControl[0].x = (int) (centerX + magicNumber * radius);
        pointsControl[0].y = centerY - radius;
        pointsControl[1].x = centerX + radius;
        pointsControl[1].y = (int) (centerY - magicNumber * radius);
        pointsControl[2].x = centerX + radius;
        pointsControl[2].y = (int) (centerY + magicNumber * radius);
        pointsControl[3].x = (int) (centerX + magicNumber * radius);
        pointsControl[3].y = centerY + radius;
        pointsControl[4].x = (int) (centerX - magicNumber * radius);
        pointsControl[4].y = centerY + radius;
        pointsControl[5].x = centerX - radius;
        pointsControl[5].y = (int) (centerY + magicNumber * radius);
        pointsControl[6].x = centerX - radius;
        pointsControl[6].y = (int) (centerY - magicNumber * radius);
        pointsControl[7].x = (int) (centerX - magicNumber * radius);
        pointsControl[7].y = centerY - radius;
    }


    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(pointsData[0].y, pointsData[0].y + radius / 2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsData[0].y = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ValueAnimator animator2 = ValueAnimator.ofInt(centerX + radius, centerX + radius - radius / 4);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsControl[2].x = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ValueAnimator animator3 = ValueAnimator.ofInt(centerX - radius, centerX - radius + radius / 4);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsControl[5].x = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ValueAnimator animator4 = ValueAnimator.ofInt(centerY + radius, centerY + radius - radius / 4);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointsControl[3].y = (int) animation.getAnimatedValue();
                pointsControl[4].y = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.playTogether(animator, animator2, animator3, animator4);
        mAnimatorSet.start();
    }
}
