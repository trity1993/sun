package cc.trity.sun.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.trity.sun.R;
import cc.trity.sun.model.ForcecastItem;

/**
 * Created by TryIT on 2016/2/18.
 */
public class ForceCastRAdapter extends RecyclerView.Adapter<ForceCastRAdapter.ViewHolder> {

    private List<ForcecastItem> forcecastList;
    private Activity activity;

//    private static OnRecyclerItemClickListener listener;

    public ForceCastRAdapter(Activity activity, List<ForcecastItem> forcecastList){
        this.activity=activity;
        this.forcecastList=forcecastList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.recycler_forcecast_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForcecastItem forcecastItem=forcecastList.get(position);

        holder.imgWeatherFlag.setImageResource(forcecastItem.getWeatherLittleImage());
        holder.txtDate.setText(forcecastItem.getDate());
        holder.txtTemp.setText(forcecastItem.getWeatherTemp());
        holder.txtDayTemp.setText(forcecastItem.getWeatherDayTemp());
        holder.txtNightTemp.setText(forcecastItem.getWeatherNightTemp());

    }

    @Override
    public int getItemCount() {
        return forcecastList.size();
    }

   /* public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgWeatherFlag;
        TextView txtDate,txtTemp,txtDayTemp,txtNightTemp;
        LinearLayout llayoutDetail;
        boolean isShow=true;

        public ViewHolder(View itemView) {
            super(itemView);
            imgWeatherFlag = (ImageView) itemView.findViewById(R.id.img_weather_flag);
            txtDate = (TextView) itemView.findViewById(R.id.txt_forcecast_date);
            txtTemp = (TextView) itemView.findViewById(R.id.txt_forcecast_temp);
            txtDayTemp = (TextView) itemView.findViewById(R.id.txt_day_temp);
            txtNightTemp = (TextView) itemView.findViewById(R.id.txt_night_temp);

            llayoutDetail = (LinearLayout) itemView.findViewById(R.id.llayout_forcecast_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(llayoutDetail!=null){
                if(isShow){
                    llayoutDetail.setVisibility(View.GONE);
                    isShow=false;
                }else{
                    llayoutDetail.setVisibility(View.VISIBLE);
                    isShow=true;
                }
            }
            /*if(listener!=null)
                listener.onItemClick(v,getLayoutPosition());*/
        }
    }
}
