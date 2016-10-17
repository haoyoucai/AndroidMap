package com.highersun.androidmap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class MainActivity extends Activity {

    Button btnGaode, btnBaidu, btnTuba, btnGoogle;
    Context mContext;
    PopupWindow mPopupWind;
    List<ResolveInfo> infos;
    PackageManager pm;
    Intent mIntent;
//    private List<DisplayResolveInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGaode = (Button) findViewById(R.id.btn_gaode);
        btnBaidu = (Button) findViewById(R.id.btn_baidu);
        btnGoogle = (Button) findViewById(R.id.btn_google);
        btnTuba = (Button) findViewById(R.id.btn_tuba);


        mContext = this;

        //百度地图
        btnGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=快方配送&lat=" + "1212" + "&lon=" + "12123"));
                if (isInstallByread("com.autonavi.minimap")) {
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "未安装高德地图", Toast.LENGTH_SHORT).show();
                }
                //参考：http://blog.csdn.net/qq_19520407/article/details/43730671
                //参考：http://developer.amap.com/api/uri-api/android-uri-explain/

            }
        });

        //高德地图
        btnBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //参考：http://my.oschina.net/u/1270405/blog/309774
                //调起百度地图客户端
                try {
                    Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    if (isInstallByread("com.baidu.BaiduMap")) {
                        startActivity(intent); //启动调用
                        Toast.makeText(MainActivity.this, "百度地图客户端已经安装", Toast.LENGTH_SHORT).show();
//                        Log.e("GasStation", "百度地图客户端已经安装") ;
                    } else {
                        Toast.makeText(MainActivity.this, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
//                        Log.e("GasStation", "没有安装百度地图客户端") ;
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });


        //谷歌地图

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInstallByread("com.google.android.apps.maps")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr= 30.6739968716,103.9602246880 &daddr=30.6798861599,103.9739656448&hl=zh"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有安装谷歌地图客户端", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //图吧地图
        btnTuba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isInstallByread("com.mapbar.android.mapbarmap")) {
//
//                    try {
////直接调用图吧地图
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        Uri uri = Uri.parse("geo:39.922840,116.3543240,北京市西城区阜外大街2号万通大厦");
//                        intent.setData(uri);
//                        intent.setClassName("com.mapbar.android.mapbarmap", "com.mapbar.android.mapbarmap.FilterServiceActivity");
//                        startActivity(intent);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "没有安装图吧地图客户端", Toast.LENGTH_SHORT).show();
//                    //http://www.cnblogs.com/luxiaofeng54/archive/2012/04/10/2439844.html
//
//                }
//

                Uri mUri = Uri.parse("geo:39.940409,116.355257?q=西直门");
               mIntent = new Intent(Intent.ACTION_VIEW, mUri);
//                for(String str:mIntent.getCategories()){
//                    Log.e("mIntent",str);
//                }


                pm = mContext.getPackageManager();


                infos = pm.queryIntentActivities(mIntent, PackageManager.MATCH_ALL);


                ResolveInfo info = pm.resolveActivity(mIntent, PackageManager.MATCH_ALL); // PackageManager.MATCH_DEFAULT_ONLY
                info.toString();


                Log.e("info", info.toString());
                Log.e("infos", infos.toString());
                initPopWindow();
            }
        });


    }


    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

//    public Intent intentForPosition(int position) {
//        if (mList == null) {
//            return null;
//        }
//        DisplayResolveInfo dri = mList.get(position);
//        Intent intent = new Intent(dri.origIntent != null
//                ? dri.origIntent : mIntent);
//        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
//                | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//        ActivityInfo ai = dri.ri.activityInfo;
//        intent.setComponent(new ComponentName(
//                ai.applicationInfo.packageName, ai.name));
//        return intent;
//    }
//
//    private final class DisplayResolveInfo {
//        Drawable displayIcon;
//        CharSequence displayLabel;
//        CharSequence extendedInfo;
//        Intent origIntent;
//        ResolveInfo ri;
//
//        DisplayResolveInfo(ResolveInfo pri, CharSequence pLabel,
//                           CharSequence pInfo, Intent pOrigIntent) {
//            ri = pri;
//            displayLabel = pLabel;
//            extendedInfo = pInfo;
//            origIntent = pOrigIntent;
//        }
//    }


    private void initPopWindow() {

        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_listview, null);
        contentView.setBackgroundColor(Color.BLUE);

        PopupWindow popupWindow = new PopupWindow(findViewById(R.id.activity_main), 800, 1000);
        popupWindow.setContentView(contentView);

//        TextView textView = (TextView) contentView.findViewById(R.id.text);
//        textView.setText("测试");
//        openDir();
        ListView listView = (ListView) contentView.findViewById(R.id.lv_mapinfo);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);

        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(btnTuba);

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.isEmpty() ? 0 : infos.size();
        }

        @Override
        public Object getItem(int i) {
            return infos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;

            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_map, viewGroup, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.iv_map_icon);
                holder.textView = (TextView) view.findViewById(R.id.tv_map_dis);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.imageView.setImageDrawable(infos.get(i).loadIcon(pm));
            holder.textView.setText(infos.get(i).loadLabel(pm).toString());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mIntent);
                    ActivityInfo activityInfo = infos.get(i).activityInfo;
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
                            | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    intent.setComponent(new ComponentName(
                            activityInfo.applicationInfo.packageName, activityInfo.name));
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;

    }

}


//参考：http://blog.csdn.net/feng88724/article/details/6198446
