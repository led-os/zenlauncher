
package com.cooeeui.brand.zenlauncher.unittest.category;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * This class holds the per-item data in our Loader.
 */
public class AppEntry {

    /**
     * Perform alphabetical comparison of application entry objects.
     */
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

    public AppEntry(AppListLoader loader, ResolveInfo info) {
        mLoader = loader;
        mInfo = info;
        mApkFile = new File(info.activityInfo.applicationInfo.sourceDir);
    }

    public ResolveInfo getResolveInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            if (mApkFile.exists()) {
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }
        } else {
            return mIcon;
        }

        return mLoader.getContext().getResources().getDrawable(
                android.R.drawable.sym_def_app_icon);
    }

    @Override
    public String toString() {
        return mLabel;
    }

    void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.resolvePackageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.resolvePackageName;
            }
        }
    }

    private final AppListLoader mLoader;
    private final ResolveInfo mInfo;
    private final File mApkFile;
    private String mLabel;
    private Drawable mIcon;
    private boolean mMounted;
}
