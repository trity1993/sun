package cc.trity.sun.ui.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import cc.trity.sun.R;

/**
 * 顶部悬浮窗的显示
 * 不需要盛情权限，放UC的快速搜索的实现
 * 来源github:https://github.com/liaohuqiu/android-UCToast
 * Created by TryIT on 2016/2/26.
 */
public class TipViewController implements View.OnClickListener, View.OnTouchListener{
    private WindowManager windowManager;
    private Context context;
    private View contentView;
    private View wholeView;
    public TipViewController(Context context) {
        this.context=context;
        windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void showView(){
        wholeView=View.inflate(context, R.layout.pop_view, null);

        contentView=wholeView.findViewById(R.id.content_pop_layout);

        contentView.setOnClickListener(this);
        wholeView.setOnTouchListener(this);

        //进行布局（关键）
        int w=WindowManager.LayoutParams.MATCH_PARENT;
        int h=WindowManager.LayoutParams.MATCH_PARENT;

        int flags=0;
        int type=0;
        //兼容不同版本
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            type=WindowManager.LayoutParams.TYPE_TOAST;
        }else{
            type=WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;

        windowManager.addView(wholeView,layoutParams);
    }

    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
    }

    /**
     * 清除当前的view
     */
    public void removePoppedViewAndClear() {
        if(windowManager!=null&&wholeView!=null){
            windowManager.removeView(wholeView);
            // remove listeners
            contentView.setOnClickListener(null);
            wholeView.setOnTouchListener(null);

            wholeView=null;
        }

    }

    /**
     * touch the outside of the content view, remove the popped view
     * 点击空白处的时候消失
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x=(int)event.getX();
        int y=(int)event.getY();
        Rect rect=new Rect();
        contentView.getGlobalVisibleRect(rect);
        if(!rect.contains(x,y)){
            removePoppedViewAndClear();
        }
        return false;
    }
}
