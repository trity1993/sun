package cc.trity.sun.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import cc.trity.sun.activities.ForecastActivity;
import cc.trity.sun.activities.h5.H5Activity;

/**
 * 通过封装好的ScaleGestureDetector的放大和缩小的手势进行实现相关的效果
 * Created by TryIT on 2016/3/6.
 */
public class ZoomTouchImplListener implements View.OnTouchListener,ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG="ZoomTouchImplListener";
    private ScaleGestureDetector scaleGestureDetector;
    private Context context;
    public ZoomTouchImplListener(Context context){
        this.context=context;
        scaleGestureDetector=new ScaleGestureDetector(context,this);
    }
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //一定要返回true才会进入onScale()这个函数
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        float scale=detector.getScaleFactor();
        if(scale>1){//放大
            H5Activity.toH5Act(context);
        }else{//缩小
            ForecastActivity.showWeatherForecastAct(context,-1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event);
    }
}
