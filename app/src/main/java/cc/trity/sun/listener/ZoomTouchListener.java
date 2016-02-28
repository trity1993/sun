package cc.trity.sun.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import cc.trity.sun.activities.h5.H5Activity;

/**
 * 封装缩小放大的手势
 * Created by TryIT on 2016/2/28.
 */
public class ZoomTouchListener implements View.OnTouchListener {
    private float oldDist;
    private boolean isZoom=false;
    private Context context;
    private static final int DIST_NUM=30;//缩放的距离

    public ZoomTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()&event.getActionMasked()){
            case MotionEvent.ACTION_POINTER_UP:
                isZoom = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = motionSpacing(event);//计算当两点的距离
                if(oldDist==-1)
                    break;
                isZoom = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isZoom){
                    float newDist = motionSpacing(event);
                    if(newDist==-1)
                        break;
                    /**
                     * 表示新的距离比两个手指刚触碰的距离大
                     * ( +10个像素用来延迟一下放大，不然稍微动一点像素,也放大，感觉也太快了。)
                     */
                    if (newDist + DIST_NUM > oldDist) {
                        //处理放大的逻辑
                        H5Activity.toH5Act(context);
//                        CommonUtils.showToast(context, "放大" + newDist);
                    }
                    /**
                     * 表示新的距离比两个手指刚触碰的距离小
                     */
                    if (newDist + DIST_NUM < oldDist) {
                        //处理缩小的逻辑
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 勾股定理，算两地的距离
     * @param event
     * @return
     */
    private float motionSpacing(MotionEvent event) {
        if(event.getPointerCount()<2){
            return -1;
        }
        float x = event.getX(1) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
}
