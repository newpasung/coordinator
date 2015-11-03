package com.scut.gof.coordinator.main.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2015/9/8.
 */
public class XRotationAnimation extends Animation {
    private float centerX,centerY;
    private Camera mCamera;
    public XRotationAnimation (){
        mCamera=new Camera();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX=width/2f;
        centerY=height/2f;
        setDuration(1000);
        setInterpolator(new DecelerateInterpolator());
        setFillAfter(true);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix=t.getMatrix();
        mCamera.save();//保存参数，与restore出现成对
        mCamera.rotateX(720 * interpolatedTime);
        mCamera.getMatrix(matrix);//将camera的旋转效果附加到matrix
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);//这两个用于标示中心点，涉及矩阵计算，暂不深究
        mCamera.restore();
    }
}
