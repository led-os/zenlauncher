
package com.cooeeui.brand.zenlauncher.scene.drawer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.PopupWindow;

import com.cooeeui.brand.zenlauncher.R;

public class ClickButtonOnClickListener implements OnClickListener {

    private Context context = null;
    private TitleBar titleBar = null;
    private AppListViewGroup applistGroup = null;
    private AppTabViewGroup tabViewGroup = null;
    private AppListUtil util = null;
    private PopMenuGroup mPopMenuGroup = null;
    private PopupWindow mPopupWindow = null;

    public PopMenuGroup getmPopMenuGroup() {
        return mPopMenuGroup;
    }

    public void setmPopMenuGroup(PopMenuGroup mPopMenuGroup) {
        this.mPopMenuGroup = mPopMenuGroup;
    }

    public AppListViewGroup getApplistGroup() {
        return applistGroup;
    }

    public void setApplistGroup(AppListViewGroup applistGroup) {
        this.applistGroup = applistGroup;
    }

    public TitleBar getNameViewGroup() {
        return titleBar;
    }

    public void setNameViewGroup(TitleBar nameViewGroup) {
        this.titleBar = nameViewGroup;
    }

    public AppTabViewGroup getTabViewGroup() {
        return tabViewGroup;
    }

    public void setTabViewGroup(AppTabViewGroup tabViewGroup) {
        this.tabViewGroup = tabViewGroup;
    }

    public ClickButtonOnClickListener(Context context, AppListUtil util) {
        this.context = context;
        this.util = util;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof String) {
            String nameTag = (String) tag;
            doneClickByValue(nameTag, v);
        }
    }

    private void doneClickByValue(String nameTag, View v) {
        if (nameTag.equals(util.optionName)) {
            doneSomethingInOption(v);
        } else if (mPopMenuGroup != null && (nameTag.contains(mPopMenuGroup.mPopTag))) {
            if (nameTag.equals(context.getResources().getString(R.string.classify)
                    + mPopMenuGroup.mPopTag)) {
                applistGroup.classifyApp();
            } else if (nameTag.equals(context.getResources().getString(R.string.unload)
                    + mPopMenuGroup.mPopTag)) {
                applistGroup.unloadApp();
            } else if (nameTag.equals(context.getResources().getString(R.string.hideicon)
                    + mPopMenuGroup.mPopTag)) {
                applistGroup.hideIcon();
            } else if (nameTag.equals(context.getResources().getString(R.string.zen_settings)
                    + mPopMenuGroup.mPopTag)) {
                applistGroup.ZenSettings();
            }
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    }

    public void changeTabByNum(int tabNum) {

        util.setTabNum(tabNum);
        if (util.getPreferences() != null) {
            util.getPreferences().edit().putInt(util.gettabNumKey(), tabNum).commit();
        }
        titleBar.setTextName(util.tabName[tabNum]);
        applistGroup.setTab(tabNum);

    }

    private void doneSomethingInOption(View view) {
        if (mPopMenuGroup == null) {
            mPopMenuGroup = new PopMenuGroup(context, this);
        }
        if (mPopupWindow == null) {
            int popWidth = context.getResources().getDimensionPixelSize(R.dimen.popmenu_width);
            int popHeight = context.getResources().getDimensionPixelSize(R.dimen.popmenu_height);
            mPopupWindow = new PopupWindow(mPopMenuGroup, popWidth, popHeight);
            mPopupWindow.setBackgroundDrawable(new
                    ColorDrawable(Color.argb(255, 0, 0, 0)));
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        int xoff = context.getResources().getDimensionPixelSize(R.dimen.popmenu_xoff);
        int yoff = context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff);
        mPopupWindow.showAsDropDown(view, xoff, yoff);

        mPopMenuGroup.setFocusableInTouchMode(true);
        mPopMenuGroup.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (event.getAction() == KeyEvent.ACTION_UP)
                        && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                return true;
            }
        });
    }

}
