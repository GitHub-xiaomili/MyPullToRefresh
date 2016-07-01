package com.xiaomili.mypulltorefresh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    /**
     * 下拉刷新控件
     */
    private PullToRefreshListView mPtrListView;
    private LinkedList<String> mList;
    private int mListCount = 2;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPtrListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh);
        mPtrListView.setMode(PullToRefreshBase.Mode.BOTH);
        initData();
        initIndacator();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mPtrListView.setAdapter(mAdapter);
//        mPtrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        //获取格式化后的标签
//                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//                        | DateUtils.FORMAT_SHOW_DATE
//                        | DateUtils.FORMAT_ABBREV_ALL);
        //显示最后跟新的时间
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                new GetDataTask().execute();
//            }
//        });
        mPtrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //获取格式化后的标签
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
                //显示最后跟新的时间
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new GetDataTask().execute();
            }
        });
    }

    //初始化List的数据源
    private void initData() {
        mList = new LinkedList<>();
        for (int i = 0; i < mListCount; i++) {
            mList.add("测试数据" + i);
        }
    }

    //自定义上拉和下拉显示的文字和图片
    private void initIndacator() {
        //获取格式化后的标签
        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                | DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_ABBREV_ALL);

        //自定义下拉显示
        ILoadingLayout startLabels = mPtrListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("拉我啊,你个逗比");
        startLabels.setRefreshingLabel("大哥,我错了,我马上帮你刷新哈");
        startLabels.setReleaseLabel("矮油,你牛逼,松开我试试");
        //显示最后跟新的时间
        startLabels.setLastUpdatedLabel(label);
        startLabels.setLoadingDrawable(getResources().getDrawable(R.drawable.default_ptr_flip));
        //自定义上拉显示
        ILoadingLayout endLabels = mPtrListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("你再拉我啊,逗比");
        endLabels.setRefreshingLabel("大哥,我这次真的错了,再也不敢了,我马上帮你刷新哈");
        endLabels.setReleaseLabel("我操,你还真敢拉,有种你放开我单挑");
        //显示最后跟新的时间
        endLabels.setLastUpdatedLabel(label);
        endLabels.setLoadingDrawable(getResources().getDrawable(R.drawable.indicator_arrow));

    }

    private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "新增测试数据" + (mListCount++);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                mList.add(s);
                mAdapter.notifyDataSetChanged();
                //当list发生改变的时候调用该方法(作用是加载完数据后隐藏进度动画)
                mPtrListView.onRefreshComplete();
            }
        }
    }

}
