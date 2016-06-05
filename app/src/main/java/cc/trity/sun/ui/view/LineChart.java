package cc.trity.sun.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import cc.trity.sun.model.ChartItem;

/**
 * Created by TryIT on 2016/3/2.
 */
public class LineChart extends View {

    public static final int DEFAULT_LOGITUDE_NUM = 16;
    public static final int DEFAULT_LATITUDE_NUM = 15;
    public static final int DEFAULT_SEPARATOR = 10;

    public static final int DEFAULT_RADIUS = 10; //画圆点的半径：radius

    private float longitudeSpacing;
    private float latitudeSpacing;

    private Paint paint;

    private List<ChartItem> chartItemList;//填充图表的数据

    private int chartItemSize;
    private int maxHight, minHight;

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#77FFFFFF"));
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewheigh = getHeight() / 3 * 2;
        int viewWidth = getWidth();
        longitudeSpacing = viewWidth / (float) DEFAULT_LOGITUDE_NUM;
        latitudeSpacing = viewheigh / (float) DEFAULT_LATITUDE_NUM;

        drawLongLatitude(canvas, viewheigh, viewWidth);
        drawXYText(canvas, viewheigh, viewWidth);
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawLongLatitude(Canvas canvas, int viewHeight, int viewWidth) {
        //画经线
        for (int i = 1; i <= DEFAULT_LOGITUDE_NUM; i++) {
            canvas.drawLine(longitudeSpacing * i,
                    0, longitudeSpacing * i,
                    viewHeight, paint);
        }
        //画维度
        for (int i = 1; i <= DEFAULT_LATITUDE_NUM; i++) {
            canvas.drawLine(0, latitudeSpacing * i,
                    viewWidth,
                    latitudeSpacing * i, paint);
        }
    }

    private void drawXYText(Canvas canvas, int viewHeight, int viewWidth) {

        if (chartItemList == null || chartItemList.size() == 0) {
            return;
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);

        float dataHeigh=viewHeight-(latitudeSpacing*2);//得到实际数据的显示区域

        float dataRate = dataHeigh / (maxHight - minHight);

        int xIndex = 2;
        //画x,y轴
        for (int i = 0; i < 5; i++) {
            //y轴的赋值
            canvas.drawText((int)(latitudeSpacing * xIndex / dataRate) + "", viewWidth-longitudeSpacing, viewHeight - latitudeSpacing * xIndex, paint);
            xIndex += 3;

        }

        xIndex=2;

        for (int i = 0; i < 5; i++) {
            //x轴的赋值
            canvas.drawText(String.valueOf(i+1), longitudeSpacing * xIndex, latitudeSpacing * (DEFAULT_LATITUDE_NUM-1), paint);
            xIndex += 3;

        }

        xIndex=2;

        ChartItem chartItem=null;

        for (int i = 0; i < chartItemSize; i++) {

            chartItem = chartItemList.get(i);

            //画数据的对应点
            canvas.drawCircle(longitudeSpacing * (xIndex - 1), dataHeigh - (chartItem.getTempDay() - minHight) * dataRate,DEFAULT_RADIUS,paint);
            canvas.drawCircle( longitudeSpacing * (xIndex + 1), dataHeigh - (chartItem.getTempNight() - minHight) * dataRate, DEFAULT_RADIUS, paint);

            //两点的链接线
            canvas.drawLine
                    (longitudeSpacing * (xIndex - 1), dataHeigh-(chartItem.getTempDay() -minHight)* dataRate, longitudeSpacing * (xIndex + 1), dataHeigh-(chartItem.getTempNight()-minHight) * dataRate, paint);

            xIndex += 3;
        }

        xIndex=2;

        //划线
        for (int i = 0;i<chartItemSize ; ) {
            chartItem = chartItemList.get(i);
            float x = longitudeSpacing * (xIndex + 1);
            float y = dataHeigh-(chartItem.getTempNight() -minHight)* dataRate;

            xIndex += 3;
            i++;
            if(i < chartItemSize){
                chartItem = chartItemList.get(i);

                float endX=longitudeSpacing * (xIndex -1);
                float endY=dataHeigh-(chartItem.getTempDay() -minHight)* dataRate;

                canvas.drawLine(x, y, endX, endY, paint);
            }

        }

    }

    public void paddingData(List<ChartItem> chartItemList) {

        if (chartItemList == null || chartItemList.size() == 0) {
            return;
        }
        this.chartItemList = chartItemList;

        chartItemSize = chartItemList.size();
        ChartItem chartItem = chartItemList.get(0);
        maxHight = chartItem.getTempDay();
        minHight = chartItem.getTempNight();
        for (int i = 1; i < chartItemSize; i++) {
            ChartItem itemTmp = chartItemList.get(i);
            maxHight = (maxHight > itemTmp.getTempDay() ? maxHight : itemTmp.getTempDay());
            minHight = (minHight < itemTmp.getTempNight() ? minHight : itemTmp.getTempNight());
        }
        postInvalidate();
    }
}
