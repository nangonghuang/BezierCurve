package com.example.alan.beziercurve;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PointF mStartPoint = new PointF();
    private PointF mEndPoint = new PointF();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Random random = new Random();
        HeartView heartView = findViewById(R.id.heart_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndAnimateView(heartView, random, width, height);
            }
        });
    }

    private void addAndAnimateView(HeartView heartView, Random random, int width, int height) {
        ViewGroup viewGroup = (ViewGroup) heartView.getParent();
        HeartView temp = new HeartView(getApplicationContext());
        temp.randomColor();
        viewGroup.addView(temp, heartView.getLayoutParams());

        mStartPoint.x = heartView.getX();
        mStartPoint.y = heartView.getY();
        mEndPoint.x = (random.nextInt(width) - heartView.getWidth()) / 2;
        mEndPoint.y = 0 - heartView.getHeight();

        CubBezierEvaluate bezierEvaluator = new CubBezierEvaluate(new PointF(random.nextInt(width), random.nextInt(height)),
                new PointF(random.nextInt(width), random.nextInt(height)));
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, mStartPoint, mEndPoint);
        anim.setDuration(2000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                temp.updatePosition(point.x, point.y);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.removeView(temp);
                Log.i(TAG, "onAnimationUpdate: onAnimationEnd ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                viewGroup.removeView(temp);
                Log.i(TAG, "onAnimationUpdate: onAnimationCancel ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
