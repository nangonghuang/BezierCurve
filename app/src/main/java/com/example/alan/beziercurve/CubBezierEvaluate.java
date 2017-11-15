package com.example.alan.beziercurve;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by alan on 2017/11/15.
 */

public class CubBezierEvaluate implements TypeEvaluator<PointF> {
    private PointF control1;
    private PointF control2;
    private PointF point = new PointF();

    public CubBezierEvaluate(PointF control1, PointF control2) {
        this.control1 = control1;
        this.control2 = control2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float temp = 1 - fraction;
        point.x = startValue.x * temp * temp * temp + 3 * control1.x * fraction * temp * temp +
                3 * control2.x * fraction * fraction * temp + endValue.x * fraction * fraction * fraction;

        point.y = startValue.y * temp * temp * temp + 3 * control1.y * fraction * temp * temp +
                3 * control2.y * fraction * fraction * temp + endValue.y * fraction * fraction * fraction;
        return point;
    }
}
