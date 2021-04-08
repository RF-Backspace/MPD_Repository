package gcu.xiaobin.gcu.earthquakeapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gcu.xiaobin.gcu.earthquakeapplication.R;
import gcu.xiaobin.gcu.earthquakeapplication.model.EarthquakesModel;

/**
 * @author Xiaobin_Ma
 * @student_id S1803078
 * @project EarthQuakeApplication
 * @package_name gcu.xiaobin.gcu.earthquakeapplication.adapter
 * @date 06/04/2021
 * @time 08:14
 * @year 2021
 * @month 04
 * @month_short Apr
 * @month_full April
 * @day 06
 * @day_short Tue
 * @day_full Tuesday
 * @hour 08
 * @minute 14
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<EarthquakesModel> data;
    private OnItemClickListener mOnItemClickListener;

    public HomeAdapter(List<EarthquakesModel> data) {
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick (View view, int position);
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        //holder.titleTv.setText(data.get(position).getTitle());
        //holder.descTv.setText(data.get(position).getDescription());
        String temp = data.get(position).getDescription();
        String description[] = temp.split(";");

        String mag = description[4].substring(11).replace(" ", "");
        Float val = Float.parseFloat(mag);
        Integer color = 0xFF000000;
        if(0 < val & val <= 1){
            color = 0xFF32CD32;
        } if(1 < val & val <= 3){
            color = 0xFF6495ED;
        } if(3 < val & val <= 4){
            color = 0xFFEEC900;
        } if(4 < val & val <= 5){
            color = 0xFFFF8247;
        } if(5 < val){
            color = 0xFFCD5555;
        }

        holder.titleTv.setText("Location: "+description[1].substring(10).replace(";", ""));
        holder.descTv.setText("Magnitude:" + description[4].substring(10).replace(":", ""));
        holder.descTv.setTextColor(color);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView descTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.Location);
            descTv = itemView.findViewById(R.id.Strength);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnFragmentListener{
        void onFragment(String clsNameDetail,Object object);
    }
}
