
package com.cooeeui.brand.zenlauncher.scene.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cooeeui.brand.zenlauncher.Launcher;
import com.cooeeui.brand.zenlauncher.R;
import com.cooeeui.brand.zenlauncher.apps.AppInfo;
import com.cooeeui.brand.zenlauncher.category.CategoryData;
import com.cooeeui.brand.zenlauncher.category.CategoryHelper;
import com.cooeeui.brand.zenlauncher.config.GridConfig;
import com.cooeeui.brand.zenlauncher.scenes.ui.BubbleView;
import com.cooeeui.brand.zenlauncher.scenes.ui.ZenGridView;
import com.cooeeui.brand.zenlauncher.scenes.utils.DragController;
import com.cooeeui.brand.zenlauncher.scenes.utils.DragSource;
import com.viewpagerindicator.UnderlinePageIndicator;

public class AppListViewGroup extends FrameLayout {
    PageAdapter mAdapters[];
    Context mContext;
    int mTab;
    private Launcher mLauncher = null;
    private DragController mDragController = null;
    private BubbleView mBubbleView = null;
    private FrameLayout mSelectIcon = null;
    private ZenGridView mSelectGridView = null;

    public DragController getmDragController() {
        return mDragController;
    }

    public void setmDragController(DragController mDragController) {
        this.mDragController = mDragController;
    }

    public Launcher getmLauncher() {
        return mLauncher;
    }

    public void setmLauncher(Launcher mLauncher) {
        this.mLauncher = mLauncher;
    }

    public AppListViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTab = 0;
        mAdapters = new PageAdapter[CategoryHelper.COUNT];
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        FragmentActivity activity = (FragmentActivity) mContext;
        for (int i = 0; i < mAdapters.length; i++) {
            mAdapters[i] = new PageAdapter(activity.getSupportFragmentManager(), i);

            ViewPager pager = (ViewPager) findViewById(R.id.pager_0 + 2 * i);
            pager.setAdapter(mAdapters[i]);

            UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator_0
                    + 2 * i);
            indicator.setViewPager(pager);
        }

        setTab(mTab);
    }

    class PageAdapter extends FragmentPagerAdapter {
        int mCount;
        int mTab;

        public PageAdapter(FragmentManager fm, int tab) {
            super(fm);

            mCount = 1;
            mTab = tab;
        }

        @Override
        public Fragment getItem(int position) {
            return GridFragment.newInstance(mTab, position);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.v("suyu", "instantiateItem: " + position);
            GridFragment fragment = (GridFragment) super.instantiateItem(container, position);
            if (fragment != null) {
                fragment.notifyDataSetChanged();
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            Log.v("suyu", "getItemPosition: " + object);
            GridFragment fragment = (GridFragment) object;
            if (fragment != null) {
                fragment.notifyDataSetChanged();
            }
            return super.getItemPosition(object);
        }

        public void setCount(int count) {
            if (count > 0) {
                mCount = count;
                notifyDataSetChanged();
            }
        }

        public int getTab() {
            return mTab;
        }
    }

    public void notifyDataSetChanged() {
        Log.v("suyu", "AppListViewGroup notifyDataSetChanged");
        for (int i = 0; i < mAdapters.length; i++) {
            mAdapters[i].notifyDataSetChanged();
            // calculate page count
            int cpp = GridConfig.getCountPerPageOfDrawer();
            if (cpp != 0 && CategoryData.datas != null) {
                int pageCount = (CategoryData.datas.get(i).size() + cpp) / cpp;
                mAdapters[i].setCount(pageCount);
            }
        }
        invalidate();
    }

    public void setTab(int tab) {
        mTab = tab;

        for (int i = 0; i < CategoryHelper.COUNT; i++) {
            if (i != mTab) {
                getChildAt(i).setVisibility(View.INVISIBLE);
            } else {
                getChildAt(i).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 重新分类
     */
    public void classifyApp() {
        // TODO Auto-generated method stub
        Log.v("", "popmenu classifyApp");
    }

    /**
     * 卸载应用
     */
    public void unloadApp() {
        // TODO Auto-generated method stub
        Log.v("", "popmenu unloadApp");
    }

    /**
     * 隐藏图标
     */
    public void hideIcon() {
        // TODO Auto-generated method stub
        Log.v("", "popmenu hideIcon");
    }

    /**
     * 开启Zen设置
     */
    public void ZenSettings() {
        // TODO Auto-generated method stub
        Log.v("", "popmenu ZenSettings");
    }

    /**
     * 开始长按拖动的一些操作
     * 
     * @param v
     * @param parentGridView
     */
    public void startDrag(DragSource source, FrameLayout v, ZenGridView parentGridView) {
        if (v.getTag() instanceof AppInfo) {
            mSelectIcon = v;
            mSelectGridView = parentGridView;
            ImageView image = (ImageView) v.findViewById(R.id.icon_image);
            int width = image.getRight() - image.getLeft();
            AppInfo info = (AppInfo) v.getTag();
            Bitmap bitmap = info.iconBitmap;
            mSelectIcon.setVisibility(View.INVISIBLE);
            mBubbleView = new BubbleView(mContext, bitmap, width);
            mDragController.startDrag(source, mBubbleView, width);
        }
    }

    public void showIcon() {
        mSelectIcon.setVisibility(View.VISIBLE);
        mLauncher.getDragLayer().removeView(mBubbleView);
        mBubbleView = null;
    }

    public void removeIcon() {
        mLauncher.getDragLayer().removeView(mBubbleView);
        mBubbleView = null;
    }

    /**
     * 处理由上一页抽屉转移到下一页抽屉的操作
     * 
     * @param oldTabNum
     * @param tabNum
     */
    public void changeTabNum(int oldTabNum, int tabNum) {
        // TODO
        // 将oldTabNum中的mSelectGridView删除掉mSelectIcon，以及在tabNum中的GridView添加mSelectIcon
        Log.v("", "oldTabNum is " + oldTabNum + " tabNum is " + tabNum);
    }
}
