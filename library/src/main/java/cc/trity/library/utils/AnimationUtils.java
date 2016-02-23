package cc.trity.library.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import cc.trity.library.animation.Rotate3dAnimation;

/**
 * 动画工具类
 * Created by TryIT on 2016/2/22.
 */
public class AnimationUtils {
    public static final int duration=600;
    public static final int depthZ=400;

    /**
     * 仿照相机显示图片的3D显示方式
     * @param defaultView 默认view
     * @param showView 旋转后显示的view
     * @param viewParent 使用动画的view
     * @param centerX 旋转中心x轴
     * @param centerY 旋转中心y轴
     * @param startRotate 开始旋转角度
     * @param endRotate 结束旋转角度
     * @return
     */
    public static Rotate3dAnimation getRotate3dAnim
            (final View defaultView, final View showView,final ViewGroup viewParent,
             final int centerX,final int centerY,
             final int startRotate, final int endRotate) {

        Rotate3dAnimation openAnimation = new Rotate3dAnimation
                (startRotate, endRotate, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                defaultView.setVisibility(View.GONE);
                showView.setVisibility(View.VISIBLE);

                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(360-endRotate, 360-startRotate, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                viewParent.startAnimation(rotateAnimation);
            }
        });

        return openAnimation;
    }
}
