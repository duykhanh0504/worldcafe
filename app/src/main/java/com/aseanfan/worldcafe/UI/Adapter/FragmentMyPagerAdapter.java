package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aseanfan.worldcafe.UI.Fragment.MyPageDetailFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPostFragment;
import com.aseanfan.worldcafe.worldcafe.R;

public class FragmentMyPagerAdapter  extends FragmentPagerAdapter {

    private Context mContext;

    public FragmentMyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MyPostFragment();
        } else{
            return new MyPageDetailFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.My_Post);
            case 1:
                return mContext.getString(R.string.Dtail_page);
            default:
                return null;
        }
    }


}
