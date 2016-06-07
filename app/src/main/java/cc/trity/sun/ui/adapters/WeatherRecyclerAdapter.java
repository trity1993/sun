package cc.trity.sun.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cc.trity.sun.R;
import cc.trity.sun.listener.OnRecyclerItemClickListener;
import cc.trity.sun.model.WeatherMsg;
import cc.trity.sun.utils.UIUtils;

/**
 * Created by TryIT on 2016/2/18.
 */
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> {

    private List<WeatherMsg> weatherMsgList;
    private Activity activity;

    private static OnRecyclerItemClickListener listener;

    public WeatherRecyclerAdapter(Activity activity,List<WeatherMsg> weatherMsgList){
        this.activity=activity;
        this.weatherMsgList=weatherMsgList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.recycler_weather_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherMsg weatherMsg=weatherMsgList.get(position);

        holder.imageView.setImageResource(weatherMsg.getWeatherLittleImage());
        holder.txtTemp.setText(weatherMsg.getWeatherTemp());
        holder.txtLocation.setText(weatherMsg.getWeatherLocation());

        //重新设定子项item的宽高
        DisplayMetrics displayMetrics= UIUtils.getDisplayMetrics(activity);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(displayMetrics.widthPixels/2-16,displayMetrics.heightPixels/2-16);
        layoutParams.setMargins(8, 8, 8, 8);
        holder.recyclerRLlayout.setLayoutParams(layoutParams);
        holder.recyclerRLlayout.setBackgroundResource(weatherMsg.getWeatherBackground());

    }

    @Override
    public int getItemCount() {
        return weatherMsgList.size();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtTemp,txtLocation;
        RelativeLayout recyclerRLlayout;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_weather_flag);
            txtTemp = (TextView) itemView.findViewById(R.id.txt_weather_location);
            txtLocation = (TextView) itemView.findViewById(R.id.txt_location_temp);
            recyclerRLlayout = (RelativeLayout) itemView.findViewById(R.id.srefresh_rl_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null)
                listener.onItemClick(v,getLayoutPosition());
        }
    }
}
