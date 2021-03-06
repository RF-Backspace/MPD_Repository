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
                        //?????????
                        startYear = dialog.getDatePicker().getYear();
                        //????????????1???????????????0????????????
                        startMonth = (dialog.getDatePicker().getMonth() + 1);
                        //?????????
                        startDay = dialog.getDatePicker().getDayOfMonth();
                        String date = startDay + "/" + startMonth + "/" + startYear;
                        tv_start_date.setText("Start with: " + date);
                        //????????????????????????
                        startTime = timeToTimestamp(date + " 00:00:00", "dd/MM/yyyy hh:mm:ss");
                        //????????????????????????????????????????????? endTime
                        if (endTime != -1) {
                            //???????????????
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
                //????????????????????????
                if (startTime == -1) {
                    //?????????????????????????????? ??????????????????????????? ?????????????????????,??????toast
                    Toast.makeText(getContext(), "Please select the start time first, then select the end time!", Toast.LENGTH_LONG).show();
                    return;
                }

                DatePickerDialog dialog = new DatePickerDialog(getContext()
                        , null, endYear, endMonth - 1, endDay);
                //???????????????????????????
                dialog.getDatePicker().setMinDate(startTime);

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        endYear = dialog.getDatePicker().getYear();
                        //??????????????????1???????????????0????????????
                        endMonth = (dialog.getDatePicker().getMonth() + 1);
                        endDay = dialog.getDatePicker().getDayOfMonth();
                        String date = endDay + "/" + endMonth + "/" + endYear;
                        tv_end_date.setText("Finish at: " + date);
                        //????????????????????????
                        endTime = timeToTimestamp(date + " 23:59:59", "dd/MM/yyyy hh:mm:ss");

                        //????????????????????????????????????????????? startTime
                        if (startTime != -1) {
                            //???????????????
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
     * ??????????????????????????????
     *
     * @param descList
     */
    public void setData(List<EarthquakesModel> descList) {
        data = descList;
    }


    /**
     * ????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        //??????
        List<EarthquakesModel> mostNortherlySelect = new ArrayList<>();
        //??????
        List<EarthquakesModel> mostSouthSelect = new ArrayList<>();
        //??????
        List<EarthquakesModel> mostEastSelect = new ArrayList<>();
        //??????
        List<EarthquakesModel> mostWestSelect = new ArrayList<>();
        //??????
        List<EarthquakesModel> LargestMagnitude = new ArrayList<>();
        //??????
        List<EarthquakesModel> Shallowest = new ArrayList<>();
        //??????
        List<EarthquakesModel> Deepest = new ArrayList<>();

        for (EarthquakesModel datum : data) {
            String pubDate = datum.getPubDate();
            //????????????????????????
            long pubTime = timeToTimestamp(pubDate, "EEE, dd MMM yyyy hh:mm:ss");
            //?????????????????????????????????,??????????????????,???????????????????????????
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

        //??????
        //???????????? ????????????, ???????????????????????????????????????,??????????????????
        // Double.valueOf(earthquakesModel.getGeoLat())???????????????String ?????? double ???????????????
        mostNortherlySelect = mostNortherlySelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGeoLat()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostNortherlySelect) {
            Log.e("TAG", "N: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (mostNortherlySelect.size() > 0) {
            //??????????????????
            String description = mostNortherlySelect.get(mostNortherlySelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            most_north.setText("Most Northerly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            most_north.setText("No earthquakes in this date range!");
        }

        //??????
        //???????????? ????????????, ????????????????????????????????????,??????????????????
        mostSouthSelect = mostSouthSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGeoLat()))).collect(Collectors.toList());
        //????????????????????????????????????
        if (mostSouthSelect.size() > 0) {
            //???????????????
            String description = mostSouthSelect.get(0).getDescription();
            String[] descriptions = description.split(";");
            most_south.setText("Most South earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            most_south.setText("No earthquakes in this date range!");
        }

        //??????
        mostEastSelect = mostEastSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGetLong()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostEastSelect) {
            Log.e("TAG", "E: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (mostEastSelect.size() > 0) {
            //??????????????????
            String description = mostEastSelect.get(mostEastSelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            most_east.setText("Most Eastly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            most_east.setText("No earthquakes in this date range!");
        }


        //??????
        mostWestSelect = mostWestSelect.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getGetLong()))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : mostWestSelect) {
            Log.e("TAG", "W: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (mostWestSelect.size() > 0) {
            //??????????????????
            String description = mostWestSelect.get(0).getDescription();
            String[] descriptions = description.split(";");
            most_west.setText("Most Eastly earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            most_west.setText("No earthquakes in this date range!");
        }

        //?????? data.getDescription().split(";")[3].substring(8).replace(";", "");
        LargestMagnitude = LargestMagnitude.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[4].substring(11).replace(" ", "")))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : LargestMagnitude) {
            Log.e("TAG", "L: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (mostWestSelect.size() > 0) {
            //??????????????????
            String description = LargestMagnitude.get(mostWestSelect.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            largest_magnitude.setText("Most max earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            largest_magnitude.setText("No earthquakes in this date range!");
        }

        //??????
        Deepest = Deepest.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[3].substring(8).replace(" ", "").split("k")[0]))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : Deepest) {
            Log.e("TAG", "D: " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (Deepest.size() > 0) {
            //??????????????????
            String description = Deepest.get(Deepest.size() - 1).getDescription();
            String[] descriptions = description.split(";");
            deapest.setText("Most deepest earthquake location"+ "\n"  + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            deapest.setText("No earthquakes in this date range!");
        }


        //??????
        Shallowest = Shallowest.stream().sorted(Comparator.comparing(earthquakesModel -> Double.valueOf(earthquakesModel.getDescription().split(";")[3].substring(8).replace(" ", "").split("k")[0]))).collect(Collectors.toList());
        for (EarthquakesModel earthquakesModel : Shallowest) {
            Log.e("TAG", "Sha   : " + earthquakesModel.getGeoLat() + "====" + earthquakesModel.getDescription());
        }
        //????????????????????????????????????
        if (Shallowest.size() > 0) {
            //??????????????????
            String description = Shallowest.get(0).getDescription();
            String[] descriptions = description.split(";");
            shallowest.setText("Most shallowest earthquake location"+ "\n" + descriptions[1].substring(10).replace(";", ""));
        } else {
            //???????????????????????????
            shallowest.setText("No earthquakes in this date range!");
        }
    }

    /**
     * ??????????????????????????????
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