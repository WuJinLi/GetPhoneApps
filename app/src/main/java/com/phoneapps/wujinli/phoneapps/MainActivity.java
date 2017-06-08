package com.phoneapps.wujinli.phoneapps;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.phoneapps.wujinli.phoneapps.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout
        .OnRefreshListener {
    private SwipeRefreshLayout sr_refresh;
    private RecyclerView rv_list;
    private MyAdapter adapter;
    private List<AppInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        sr_refresh = (SwipeRefreshLayout) findViewById(R.id.sr_refresh);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        rv_list.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(this, list);


        rv_list.setAdapter(adapter);

        sr_refresh.setOnRefreshListener(this);

        sr_refresh.post(new Runnable() {
            @Override
            public void run() {
                sr_refresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (list != null) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
        loadApp();
    }

    private void loadApp() {
        final PackageManager pm = getPackageManager();

        Observable
                //创建Observable对象
                .create(new Observable.OnSubscribe<ApplicationInfo>() {

                    @Override
                    public void call(Subscriber<? super ApplicationInfo> subscriber) {
                        List<ApplicationInfo> appInfos = getApplicationInfoList(pm);

                        for (ApplicationInfo appInfo : appInfos) {
                            subscriber.onNext(appInfo);
                        }
                        subscriber.onCompleted();
                    }
                })
                //创建过滤器，用于筛选三方的app
                .filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo applicationInfo) {
                        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0;
                    }
                })
                //将系统的app数据源转化成业务数据
                .map(new Func1<ApplicationInfo, AppInfo>() {
                    @Override
                    public AppInfo call(ApplicationInfo applicationInfo) {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAppIcon(applicationInfo.loadIcon(pm));
                        appInfo.setAppName(applicationInfo.loadLabel(pm).toString());
                        return appInfo;
                    }
                })
                //配置线程调度（数据的获取和数据的转化在io线程去执行，数据的展示在ui线程）
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //订阅
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        sr_refresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        sr_refresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        list.add(appInfo);
                    }
                });
    }

    /**
     * 获取系统安装app
     *
     * @param pm
     * @return
     */
    private List<ApplicationInfo> getApplicationInfoList(PackageManager pm) {
        return pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
    }
}
