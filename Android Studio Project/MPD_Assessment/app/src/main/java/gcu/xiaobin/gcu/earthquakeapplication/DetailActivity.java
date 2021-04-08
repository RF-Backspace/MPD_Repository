package gcu.xiaobin.gcu.earthquakeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import gcu.xiaobin.gcu.earthquakeapplication.fragment.FilterFragment;
import gcu.xiaobin.gcu.earthquakeapplication.fragment.HomeFragment;
import gcu.xiaobin.gcu.earthquakeapplication.fragment.MapFragment;
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
public class DetailActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getIntent()
                .getSerializableExtra("position");

        EarthquakesModel data = (EarthquakesModel) getIntent().getSerializableExtra("position");
        Log.e("---", String.valueOf(data));

        //输出字符
        String temtitle = data.getTitle();
        TextView titleTextView = (TextView) findViewById(R.id.Title);
        String title = "Title: " + temtitle;
        titleTextView.setText(title);

        String tempubdate = data.getPubDate();
        TextView pubdateTextView = (TextView) findViewById(R.id.Date);
        //String text = tempubdate.substring(5,15);
        String pubdate = "Publish Date: " + tempubdate;
        pubdateTextView.setText(pubdate);
        Log.e("search",pubdate);



        String temp = data.getDescription();
        String description[] = temp.split(";");

        String temlocality = description[1].substring(10).replace(" ", "");
        TextView localityTextView = (TextView) findViewById(R.id.Locality);
        String locality = "Locality: " + temlocality;
        localityTextView.setText(locality);
        Log.e("check",temp);
        Log.e("check",temlocality);

        String temlocation = description[2].substring(11).replace(";", "");
        TextView locationTextView = (TextView) findViewById(R.id.Location);
        String location = "Location (lat, long): " + temlocation;
        locationTextView.setText(location);

        String temdepth = description[3].substring(8).replace(";", "");
        TextView depthTextView = (TextView) findViewById(R.id.Depth);
        String depth = "Depth: " + temdepth;
        depthTextView.setText(depth);

        String temmagnitude = description[4].substring(11).replace(" ", "");
        TextView magnitudeTextView = (TextView) findViewById(R.id.Magnitude);
        String magnitude = "Magnitude: " + temmagnitude;
        magnitudeTextView.setText(magnitude);

        String temlink = data.getLink();
        TextView linkTextView = (TextView) findViewById(R.id.Link);
        String link = "Source from: " + temlink;
        linkTextView.setText(link);

        Log.e("---", locality);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}