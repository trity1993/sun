package cc.trity.sun.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * https://github.com/Udinic/ActivitySplitAnimation/blob/master/src/com/udinic/ActivitySplitAnimation/utils/ActivitySplitAnimationUtil.java
 *
 * 对应的译文：http://www.jianshu.com/p/0732653e2a8d#
 *
 * Utility class to create a split activity animation
 *
 * @author Udi Cohen (@udinic)
 */
public class ActivitySplitAnimationUtil {

    public static Bitmap mBitmap = null;
    private static int[] mLoc1;
    private static int[] mLoc2;
    private static ImageView mTopImage;
    private static ImageView mBottomImage;
    private static AnimatorSet mSetAnim;


    /**
     * Start a new Activity with a Split animation
     *
     * @param currActivity The current Activity
     * @param intent       The Intent needed tot start the new Activity
     * @param splitYCoord  The Y coordinate where we want to split the Activity on the animation. -1 will split the Activity equally
     */
    public static void startActivity(Activity currActivity, Intent intent, int splitYCoord) {

        // Preparing the bitmaps that we need to show
        prepare(currActivity, splitYCoord);

        currActivity.startActivity(intent);
        currActivity.overridePendingTransition(0, 0);//禁止默认的跳转动画
    }

    /**
     * Start a new Activity with a Split animation right in the middle of the Activity
     *
     * @param currActivity The current Activity
     * @param intent       The Intent needed tot start the new Activity
     */
    public static void startActivity(Activity currActivity, Intent intent) {
        startActivity(currActivity, intent, -1);
    }

    /**
     * Preparing the graphics on the destination Activity.
     * Should be called on the destination activity on Activity#onCreate() BEFORE setContentView()
     *
     * @param destActivity the destination Activity
     */
    public static void prepareAnimation(final Activity destActivity) {
        mTopImage = createImageView(destActivity, mBitmap, mLoc1);
        mBottomImage = createImageView(destActivity, mBitmap, mLoc2);
    }

    /**
     * Start the animation the reveals the destination Activity
     * Should be called on the destination activity on Activity#onCreate() AFTER setContentView()
     *
     * @param destActivity the destination Activity
     * @param duration The duration of the animation
     * @param interpolator The interpulator to use for the animation. null for no interpulation.
     */
    public static void animate(final Activity destActivity, final int duration, final TimeInterpolator interpolator, final boolean isCleanBmp) {

        // Post this on the UI thread's message queue. It's needed for the items to be already measured
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                mSetAnim = new AnimatorSet();
                mTopImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mBottomImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mSetAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clean(destActivity,isCleanBmp);
                        if(isCleanBmp){
                            destActivity.finish();
                            destActivity.overridePendingTransition(0,0);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        clean(destActivity,isCleanBmp);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                // Animating the 2 parts away from each other
                Animator anim1 ,anim2,animAlpha;
                if(isCleanBmp){
                    anim1= ObjectAnimator.ofFloat(mTopImage, "translationY", mTopImage.getHeight() * -1,0);
                    anim2 = ObjectAnimator.ofFloat(mBottomImage, "translationY", mBottomImage.getHeight(),0);
                    animAlpha = ObjectAnimator.ofFloat(getSceneRoot(destActivity), "alpha", 1,0);
                }else{
                    anim1= ObjectAnimator.ofFloat(mTopImage, "translationY", mTopImage.getHeight() * -1);
                    anim2 = ObjectAnimator.ofFloat(mBottomImage, "translationY", mBottomImage.getHeight());
                    animAlpha = ObjectAnimator.ofFloat(getSceneRoot(destActivity), "alpha", 0,1);
                }

                if (interpolator != null) {
                    anim1.setInterpolator(interpolator);
                    anim2.setInterpolator(interpolator);
                    animAlpha.setInterpolator(interpolator);
                }

                mSetAnim.setDuration(duration);
                mSetAnim.playTogether(animAlpha,anim1, anim2);
                mSetAnim.start();
            }
        });
    }

    /**
     * 得到activity的根view
     * @param destActivity
     * @return
     */
    public static View getSceneRoot(Activity destActivity) {
            return ((ViewGroup) destActivity.getWindow().getDecorView()).getChildAt(0);
    }

    /**
     * Start the animation that reveals the destination Activity
     * Should be called on the destination activity on Activity#onCreate() AFTER setContentView()
     *
     * @param destActivity the destination Activity
     * @param duration The duration of the animation
     */
    public static void animate(final Activity destActivity, final int duration) {
        animate(destActivity, duration, new DecelerateInterpolator(),false);
    }
    public static void animateBack(final Activity destActivity, final int duration) {

        animate(destActivity, duration, new DecelerateInterpolator(),true);
    }

    /**
     * Cancel an in progress animation
     */
    public static void cancel() {
        if (mSetAnim != null)
            mSetAnim.cancel();
    }

    /**
     * Clean stuff
     *
     * @param activity The Activity where the animation is occurring
     */
    public static void clean(Activity activity, boolean isCleanBmp) {
        if (mTopImage != null) {
            mTopImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try {
                // If we use the regular removeView() we'll get a small UI glitch
                activity.getWindowManager().removeViewImmediate(mBottomImage);
            } catch (Exception ignored) {
            }
        }
        if (mBottomImage != null) {
            mBottomImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try {
                activity.getWindowManager().removeViewImmediate(mTopImage);
            } catch (Exception ignored) {
            }
        }
        if(isCleanBmp){
            mBitmap = null;
        }
    }

    /**
     * Preparing the graphics for the animation
     *
     * @param currActivity the current Activity from where we start the new one
     * @param splitYCoord  The Y coordinate where we want to split the activity. -1 will split the activity equally
     */
    private static void prepare(Activity currActivity, int splitYCoord) {

        // Get the content of the activity and put in a bitmap
//        View root = currActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        View root = getSceneRoot(currActivity);
        root.setDrawingCacheEnabled(true);
        mBitmap = root.getDrawingCache();

        // If the split Y coordinate is -1 - We'll split the activity equally 将要展示的位置，默认从屏幕的中间开始
        splitYCoord = (splitYCoord != -1 ? splitYCoord : mBitmap.getHeight() / 2);

        if (splitYCoord > mBitmap.getHeight())
            throw new IllegalArgumentException("Split Y coordinate [" + splitYCoord + "] exceeds the activity's height [" + mBitmap.getHeight() + "]");

        // Set the location to put the 2 bitmaps on the destination activity  为了设置bitmap的大小宽高
        mLoc1 = new int[]{0, splitYCoord, root.getTop()};
        mLoc2 = new int[]{splitYCoord, mBitmap.getHeight(), root.getTop()};
    }

    /**
     * Creating the an image, containing one part of the animation on the destination activity
     *
     * @param destActivity The destination activity
     * @param bmp          The Bitmap of the part we want to add to the destination activity
     * @param loc          The location this part should be on the screen
     * @return
     */
    private static ImageView createImageView(Activity destActivity, Bitmap bmp, int loc[]) {
        if(bmp==null)
            return null;
        MyImageView imageView = new MyImageView(destActivity);
        imageView.setImageBitmap(bmp);
        imageView.setImageOffsets(bmp.getWidth(), loc[0], loc[1]);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = loc[2] + loc[0];
        windowParams.height = loc[1] - loc[0];
        windowParams.width = bmp.getWidth();
        windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        destActivity.getWindowManager().addView(imageView, windowParams);

        return imageView;
    }

    /**
     * MyImageView
     * Extended ImageView that draws just part of an image, base on start/end position
     */
    private static class MyImageView extends ImageView
    {
        private Rect mSrcRect;
        private Rect mDstRect;
        private Paint mPaint;

        public MyImageView(Context context)
        {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        /**
         * Setting the bitmap offests to control the visible area
         *
         * @param width		   The bitmap image
         * @param startY          The start Y position
         * @param endY          The end Y position
         * @return
         */
        public void setImageOffsets(int width, int startY, int endY)
        {
            mSrcRect = new Rect(0, startY, width, endY);
            mDstRect = new Rect(0, 0, width, endY - startY);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Bitmap bm = null;
            Drawable drawable = getDrawable();
            if (null != drawable && drawable instanceof BitmapDrawable)
            {
                bm = ((BitmapDrawable)drawable).getBitmap();
            }

            if (null == bm)
            {
                super.onDraw(canvas);
            }
            else
            {
                canvas.drawBitmap(bm, mSrcRect, mDstRect, mPaint);
            }
        }
    }
}
