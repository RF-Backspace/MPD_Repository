package gcu.xiaobin.gcu.earthquakeapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import gcu.xiaobin.gcu.earthquakeapplication.R;
import gcu.xiaobin.gcu.earthquakeapplication.model.EarthquakesModel;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
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
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private boolean dataIsLoad = false;
    private boolean mapIsLoad = false;
    private List<EarthquakesModel> data = new ArrayList<>();
    private GoogleMap map;


    public void testList(List<EarthquakesModel> data){
        for (EarthquakesModel item : data) {
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    public void setData(List<EarthquakesModel> descList) {
        data = descList;
        dataIsLoad = true;
        lodData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapIsLoad = true;
        map = googleMap;
        lodData();
    }

    private void lodData() {
        if (mapIsLoad && dataIsLoad) {
            LatLng london = new LatLng(51.5074, 0.1278);
            UiSettings uiSettings = map.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            //循环添加mark
            for (EarthquakesModel item : data) {
                double geoLat = Double.parseDouble(item.getGeoLat());
                double getLong = Double.parseDouble(item.getGetLong());

                String position = String.format(item.getDescription());
                String description[] = position.split(";");
                String mag = description[4].substring(11).replace(" ", "");
                Float val = Float.parseFloat(mag);
                Float color = HUE_RED;
                if(0 < val & val <= 1){
                    color = HUE_GREEN;
                } if(1 < val & val <= 3){
                    color = HUE_CYAN;
                } if(3 < val & val <= 4){
                    color = HUE_YELLOW;
                } if(4 < val & val <= 5){
                    color = HUE_ORANGE;
                } if(5 < val){
                    color = HUE_RED;
                }

                //创建坐标点，添加mark，设置title，添加到地图中
                london = new LatLng(geoLat, getLong);
                MarkerOptions mark = new MarkerOptions();
                mark.position(london);
                mark.title("Location:"+item.getGeoLat()+","+item.getGetLong());
                mark.icon(BitmapDescriptorFactory.defaultMarker(color));
                map.addMarker(mark);
            }
            //移动到最后一个mark的坐标
            map.moveCamera(CameraUpdateFactory.newLatLng(london));
        }
    }
}