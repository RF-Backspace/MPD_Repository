package gcu.xiaobin.gcu.earthquakeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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
public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private FilterFragment filterFragment;
    private MapFragment mapFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private int lastFragment = 0;//用于记录上个选择的Fragment
    private String result = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    //private String urlSource = "http://earthquakes.bgs.ac.uk/feeds/WorldSeismology.xml";
    private List<EarthquakesModel> descList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        homeFragment = new HomeFragment();
        filterFragment = new FilterFragment();
        mapFragment = new MapFragment();
        fragments.add(homeFragment);
        fragments.add(filterFragment);
        fragments.add(mapFragment);
        //获取xml
        new Thread(new Task(urlSource)).start();

    }

    private void initView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).show(homeFragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        //避免重复显示
                        if (lastFragment != 0) {
                            switchFragment(lastFragment, 0);
                        }
                        lastFragment = 0;

                        return true;
                    case R.id.search:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);

                        }
                        lastFragment = 1;

                        return true;
                    case R.id.map:
                        if (lastFragment != 2) {
                            switchFragment(lastFragment, 2);
                        }
                        lastFragment = 2;
                        return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments.get(lastFragment));//隐藏上个Fragment
        //判断是否之前add过
        if (!(fragments.get(index)).isAdded()) {
            transaction.add(R.id.frameLayout, fragments.get(index));
        }
        transaction.show(fragments.get(index)).commitAllowingStateLoss();
    }


    /**
     * 获取数据
     */
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run" + ae.getMessage());
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    parserXml();
                }
            });
        }
    }

    /**
     * 解析xml
     */
    private void parserXml() {
        try {
            //创建
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            //从文件中获取
//            InputStream xml = getAssets().open("test.xml");
//            parser.setInput(xml, "UTF-8");
//            //如果数据从网上拿下来的话 把xml String 放到这里面
            Log.e("TAG", "result:" + result);

            parser.setInput(new StringReader(result));
            //获取 event
            int eventType = parser.getEventType();
            EarthquakesModel desc = new EarthquakesModel();

            boolean isItem = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.e("TAG", "解析开始");
                        break;
                    case XmlPullParser.START_TAG:

                        if ("item".equals(parser.getName())) {
                            // 取出属性值
                            isItem = true;

                            Log.e("TAG", "走到了Item");
                            desc = new EarthquakesModel();
                        }

                        //解析item 中的数据
                        if ("title".equals(parser.getName()) && isItem) {
                            desc.setTitle(parser.nextText());
                        }
                        if ("description".equals(parser.getName()) && isItem) {
                            desc.setDescription(parser.nextText());
                        }
                        if ("link".equals(parser.getName()) && isItem) {
                            desc.setLink(parser.nextText());
                        }
                        if ("pubDate".equals(parser.getName()) && isItem) {
                            desc.setPubDate(parser.nextText());
                        }
                        if ("category".equals(parser.getName()) && isItem) {
                            desc.setCategory(parser.nextText());
                        }
                        if ("geo:lat".equals(parser.getName()) && isItem) {
                            desc.setGeoLat(parser.nextText());
                        }
                        if ("geo:long".equals(parser.getName()) && isItem) {
                            desc.setGetLong(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(parser.getName())) {
                            Log.e("TAG", "item解析结束");
                            descList.add(desc);
                            desc = null;
                            isItem = false;
                        }
                        break;
                }
                eventType = parser.next();
            }

            Log.e("====", "item列表数据: ");

            mapFragment.setData(descList);
            homeFragment.setData(descList);
            filterFragment.setData(descList);

            //打印log
            for (EarthquakesModel item : descList) {
                Log.e("====", "\nitemTitle: " + item.getTitle()
                        + "\nitemDescription: " + item.getDescription()
                        + "\nitemLink: " + item.getLink()
                        + "\nitemPubDate: " + item.getPubDate()
                        + "\nitemCategory: " + item.getCategory()
                        + "\nitemGeo:lat: " + item.getGeoLat()
                        + "\nitemGeo:long: " + item.getGetLong() + "\n");
            }

            for (EarthquakesModel item : descList) {
                String str = new String(item.getDescription());
                String a[] = str.split(";");
                Log.e("***", a[0] + "\n" + a[1] + "\n" + a[2] + "\n" + a[3] + "\n" + a[4]);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("失败", "Throwable:" + e.getMessage());
        }
    }
}