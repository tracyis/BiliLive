package com.tracyis.bililive.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tracyis.bililive.R;
import com.tracyis.bililive.adapter.HlvAdapter;
import com.tracyis.bililive.adapter.LiveListGVAdapter;
import com.tracyis.bililive.adapter.RvPopAdapter;
import com.tracyis.bililive.bean.LiveBean;
import com.tracyis.bililive.view.FlowLayout;
import com.tracyis.bililive.view.HorizontalListView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Trasys on 2017/5/22.
 */

public class LiveListActivity extends BaseActivity {
    private static final String TAG = "LiveListActivity";

    @InjectView(R.id.tb_liveList)
    Toolbar mTbLiveList;
    @InjectView(R.id.hlv_livelist)
    HorizontalListView mHlv;
    @InjectView(R.id.gv_live_list)
    GridView mGv;
    @InjectView(R.id.iv_livelist_search)
    ImageView ivLivelistSearch;
    @InjectView(R.id.iv_livelist_expand)
    ImageView ivLivelistExpand;

    private LiveBean mBean;
    private String mTitle;
    private ArrayList<String> mLiveTopList = new ArrayList<>();
    private FlowLayout mFl;
    private Random random = new Random();
    private PopupWindow mPopupWindow;


    @Override
    protected void initListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LiveListActivity.this, VideoViewActivity.class);
                intent.putExtra("room_id", mBean.data.get(position).room_id);
                intent.putExtra("stream_addr", mBean.data.get(position).playurl);
                startActivity(intent);
            }
        });

        mTbLiveList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        mBean = (LiveBean) getIntent().getSerializableExtra("liveBean");
        mTitle = (String) getIntent().getSerializableExtra("tb_title");
        mLiveTopList = (ArrayList<String>) getIntent().getSerializableExtra("topList");
        mTbLiveList.setTitle(mTitle);
        mGv.setAdapter(new LiveListGVAdapter(this, mBean));
        mHlv.setAdapter(new HlvAdapter(this, mLiveTopList));
    }

    @Override
    protected int getResId() {
        return R.layout.activity_livelist;
    }

    @OnClick({R.id.iv_livelist_search, R.id.iv_livelist_expand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_livelist_search:
                Toast.makeText(this, "开始搜索", Toast.LENGTH_SHORT).show();

                break;
            case R.id.iv_livelist_expand:
                initPopView();

                break;
        }
    }

    //    @InjectView(R.id.rv_pop)
    RecyclerView mRvPop;
    //    @InjectView(R.id.tv_pop_all)
    TextView mTvAll;
    //    @InjectView(R.id.tv_pop_new)
    TextView mTvNew;
    //    @InjectView(R.id.tv_pop_hot)
    TextView mTvHot;

    private void initPopView() {
        View popview = View.inflate(this, R.layout.view_pop, null);
//        ButterKnife.inject(popview);
        ButterKnife.inject(this);
        mRvPop = (RecyclerView) popview.findViewById(R.id.rv_pop);
        mTvAll = (TextView) popview.findViewById(R.id.tv_pop_all);
        mTvNew = (TextView) popview.findViewById(R.id.tv_pop_new);
        mTvHot = (TextView) popview.findViewById(R.id.tv_pop_hot);

        //初始化popwindow点击事件
        initPopEvent();
        initRv();

        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(popview);
            mPopupWindow.setContentView(popview);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.showAsDropDown(mTbLiveList);
        }
    }

    //流式布局
    private void initRv() {
        mRvPop.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.GAP_HANDLING_LAZY));
        if (!mLiveTopList.contains("全部")) {
            mLiveTopList.add("全部");
        }
        mRvPop.setAdapter(new RvPopAdapter(this, mLiveTopList));
//        mFl.setPadding(18, 18, 18, 18);
//        给flowlayout添加textview
//        for (int i = 0; i < mLiveTopList.size(); i++) {
//            TextView view = new TextView(LiveListActivity.this);
//            view.setText(mLiveTopList.get(i));
//            view.setBackgroundColor(Color.GRAY);
//
//            view.setTextColor(Color.WHITE);  //支持颜色渐变和常见图形的填充的drawable
//            GradientDrawable gradientDrawable = new GradientDrawable();
//            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
//            gradientDrawable.setCornerRadius(8);
//            int a = 255;
//            int r = 100 + random.nextInt(155); //100-255
//            int g = 100 + random.nextInt(155); //100-255
//            int b = 100 + random.nextInt(155); //100-255
//            gradientDrawable.setColor(Color.argb(a, r, g, b));
//
//            //代码中实现选择器，实际上是2个drawable混合到一起
//            StateListDrawable stateListDrawable = new StateListDrawable();
//
//            GradientDrawable gradientDrawable2 = new GradientDrawable();
//            gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
//            gradientDrawable2.setCornerRadius(8);
//            gradientDrawable2.setColor(Color.GRAY);
//            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable2); //灰色的
//
//            stateListDrawable.addState(new int[]{}, gradientDrawable); //带颜色的
//            view.setBackgroundDrawable(stateListDrawable);
//            view.setPadding(5, 5, 5, 5);
//            view.setGravity(Gravity.CENTER);
//            view.setTextSize(14);
//            mFl.addView(view);//触发重绘
//        }
    }

    private void initPopEvent() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_pop_all:

                        break;
                    case R.id.tv_pop_new:

                        break;
                    case R.id.tv_pop_hot:

                        break;
                }
            }
        };
        mTvAll.setOnClickListener(onClickListener);
        mTvNew.setOnClickListener(onClickListener);
        mTvHot.setOnClickListener(onClickListener);
    }
}
