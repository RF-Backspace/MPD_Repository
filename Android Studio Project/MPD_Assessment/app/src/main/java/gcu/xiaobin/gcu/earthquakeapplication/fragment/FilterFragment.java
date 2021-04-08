package gcu.xiaobin.gcu.earthquakeapplication.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import gcu.xiaobin.gcu.earthquakeapplication.R;
import gcu.xiaobin.gcu.earthquakeapplication.model.EarthquakesModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment} factory method to
 * create an instance of this fragment.
 * /
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
public class FilterFragment extends Fragment {

    private List<EarthquakesModel> data = new ArrayList<>();

    private TextView tv_start_date, tv_end_date, most_north, most_south, most_east,most_west,largest_magnitude,deapest,shallowest;
    private Button btn_start_date, btn_end_date;
    private int startYear;
    private int startMonth;
    private int startDay;

    private int endYear;
    private int endMonth;
    private int endDay;
    private long endTime = -1;
    private long startTime = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        btn_start_date = view.findViewById(R.id.start_date);
        tv_start_date = view.findViewById(R.id.date1);

        btn_end_date = view.findViewById(R.id.end_date);
        tv_end_date = view.findViewById(R.id.date2);
        most_north = view.findViewById(R.id.most_north);
        most_south = view.findViewById(R.id.most_south);
        most_east = view.findViewById(R.id.most_east);
        most_west = view.findViewById(R.id.most_west);
        largest_magnitude = view.findViewById(R.id.largest_magnitude);
        deapest = view.findViewById(R.id.deepest);
        shallowest = view.findViewById(R.id.shallowest);
        Calendar kal = Calendar.getInstance();
        startYear = kal.get(Calendar.YEAR);
        startMonth = kal.get(Calendar.MONTH) + 1;
        startDay = kal.get(Calendar.DAY_OF_MONTH);
        endYear = kal.get(Calendar.YEAR);
        endMonth = kal.get(Calendar.MONTH) + 1;
        endDay = kal.get(Calendar.DAY_OF_MONTH);

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog dialog = new DatePickerDialog(getContext()
                        , null, startYear, startMonth - 1, startDay);

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //获取年
                        startYear = dialog.getDatePicker().getYear();
                        //此处月加1因为月是从0月开始的
                        startMonth = (dialog.getDatePicker().getMonth() + 1);
                        //获取日
                        startDay = dialog.getDatePicker().getDayOfMonth();
                        String date = startDay + "/" + startMonth + "/" + startYear;
                        tv_start_date.setText("Start with: " + date);
                        //把时间换成时间戳
                        startTime = timeToTimestamp(date + " 00:00:00", "dd/MM/yyyy hh:mm:ss");
                        //此处的判断是为了判断是否选择了 endTime
                        if (endTime != -1) {
                            //初始化数据
                            initData();
                        }
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否选择时间
                if (startTime == -1) {
                    //说明还未选择开始时间 需要先选择开始时间 在选择结束时间,弹出toast
                    Toast.makeText(getContext(), "Please select the start time first, then select the end time!", Toast.LENGTH_LONG).show();
                    return;
                }

                DatePickerDialog dialog = new DatePickerDialog(getContext()
                        , null, endYear, endMonth - 1, endDay);
                //设置最小选择的日期
                dialog.getDatePicker().setMinDate(startTime);

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        endYear = dialog.getDatePicker().getYear();
                        //注意此处月加1这个月是从0月开始的
                        endMonth = (dialog.getDatePicker().getMonth() + 1);
                        endDay = dialog.getDatePicker().getDayOfMonth();
                        String date = endDay + "/" + endMonth + "/" + endYear;
                        tv_end_date.setText("Finish at: " + date);
                        //把时间换成时间戳
                        endTime = timeToTimestamp(date + " 23:59:59", "dd/MM/yyyy hh:mm:ss");

                        //此处的判断是为了判断是否选择了 startTime
                        if (startTime != -1) {
                            //初始化数据
                            initData();
                        }


                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    /**
     * 从上一个页面传入数据
     *
     * @param descList
     */
    public void setData(List<EarthquakesModel> descList) {
        data = descList;
    }


    /**
     * 设置数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        //最北
        List<EarthquakesModel> mostNortherlySelect = new ArrayList<>();
        //最南
        List<EarthquakesModel> mostSouthSelect = new ArrayList<>();
        //最东
        List<EarthquakesModel> mostEastSelect = new ArrayList<>();
        //最西
        List<EarthquakesModel> mostWestSelect = new ArrayList<>();
        //最大
        List<EarthquakesModel> LargestMagnitude = new ArrayList<>();
        //最浅
        List<EarthquakesModel> Shallowest = new ArrayList<>();
        //最深
        List<EarthquakesModel> Deepest = new ArrayList<>();

        for (EarthquakesModel datum : data) {
            String pubDate = datum.getPubDate();
            //把时间换成时间戳
            long pubTime = timeToTimestamp(pubDate, "EEE, dd MMM yyyy hh:mm:ss");
            //判断数据中大于开始时间,小于结束时间,然后把他放进数组中
            if (pubTime > startTime && pubTime < endTime) {
                mostNortherlySelect.add(datum);
                mostSouthSelect.add((datum));
                mostEastSelect.add((datum));
                mostWestSelect.add((datum));
                LargestMagnitude.add((datum));
                Shallowest.add((datum));
                Deepest.add((datum));
            }
        }

        //最北
        //正序排序 从小到大, 所以数组最后一个就是最大的,也就是最北的
        // Double.valueOf(earthquakesModel.getGeoLat())这句话是把String 转成 double 以便于排序
        mostNortherlySelect = mostNortherlySelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGeoLat()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostNortherlySelect) {
            Log.e("TAG", "N: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (mostNortherlySelect.size() > 0) {
            //获取最后一个
            String description = mostNortherlySelect.get(mostNortherlySelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            most_north.setText("Most Northerly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            most_north.setText("No earthquakes in this date range!");
        }

        //最南
        //正序排序 从小到大, 所以数组第一个就是最小的,也就是最南的
        mostSouthSelect = mostSouthSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGeoLat()))).collect(Collectors.toList());
        //判断时间段内的是否有地震
        if (mostSouthSelect.size() > 0) {
            //获取第一个
            String description = mostSouthSelect.get(0).getDescription();
            String[] descriptions = description.split(";");
            most_south.setText("Most South earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            most_south.setText("No earthquakes in this date range!");
        }

        //最东
        mostEastSelect = mostEastSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGetLong()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostEastSelect) {
            Log.e("TAG", "E: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (mostEastSelect.size() > 0) {
            //获取最后一个
            String description = mostEastSelect.get(mostEastSelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            most_east.setText("Most Eastly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            most_east.setText("No earthquakes in this date range!");
        }


        //最西
        mostWestSelect = mostWestSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGetLong()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostWestSelect) {
            Log.e("TAG", "W: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (mostWestSelect.size() > 0) {
            //获取最后一个
            String description = mostWestSelect.get(0).getDescription();
            String[] descriptions = description.split(";");
            most_west.setText("Most Eastly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            most_west.setText("No earthquakes in this date range!");
        }

        //最大 data.getDescription().split(";")[3].substring(8).replace(";", "");
        LargestMagnitude = LargestMagnitude.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[4].substring(11).replace(" ", "")))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : LargestMagnitude) {
            Log.e("TAG", "L: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (mostWestSelect.size() > 0) {
            //获取最后一个
            String description = LargestMagnitude.get(mostWestSelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            largest_magnitude.setText("Most max earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            largest_magnitude.setText("No earthquakes in this date range!");
        }

        //最深
        Deepest = Deepest.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[3].substring(8).replace(" ", "").split("k")[0]))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : Deepest) {
            Log.e("TAG", "D: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (Deepest.size() > 0) {
            //获取最后一个
            String description = Deepest.get(Deepest.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            deapest.setText("Most deepest earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            deapest.setText("No earthquakes in this date range!");
        }


        //最浅
        Shallowest = Shallowest.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[3].substring(8).replace(" ", "").split("k")[0]))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : Shallowest) {
            Log.e("TAG", "Sha   : " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //判断时间段内的是否有地震
        if (Shallowest.size() > 0) {
            //获取最后一个
            String description = Shallowest.get(0).getDescription();
            String[] descriptions = description.split(";");
            shallowest.setText("Most shallowest earthquake location"+ "\n" + descriptions[1].substring(10).replace(";", ""));
        } else {
            //此时间段内没有地震
            shallowest.setText("No earthquakes in this date range!");
        }
    }

    /**
     * 把时间格式化成时间戳
     *
     * @param time
     * @param format
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private long timeToTimestamp(String time, String format) {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.US);
        try {
            d = sf.parse(time);
        } catch (ParseException e) {
            Log.e("TAG", "timeToTimestamp: " + e);
            e.printStackTrace();
        }
        return d != null ? d.getTime() : 0;
    }
}