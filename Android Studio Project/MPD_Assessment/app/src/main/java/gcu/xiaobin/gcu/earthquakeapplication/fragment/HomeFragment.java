package gcu.xiaobin.gcu.earthquakeapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gcu.xiaobin.gcu.earthquakeapplication.DetailActivity;
import gcu.xiaobin.gcu.earthquakeapplication.R;
import gcu.xiaobin.gcu.earthquakeapplication.adapter.HomeAdapter;
import gcu.xiaobin.gcu.earthquakeapplication.model.EarthquakesModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
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
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    public void setData(List<EarthquakesModel> data){
        adapter = new HomeAdapter(data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("---", String.valueOf(position));
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("position",data.get(position));
                startActivity(intent);
            }
        });
    }
}