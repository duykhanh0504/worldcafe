package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aseanfan.worldcafe.UI.Fragment.Follow_Page;
import com.aseanfan.worldcafe.UI.Fragment.FragmentEventPage;
import com.aseanfan.worldcafe.worldcafe.R;

public class FragmentFollowAdapter  extends FragmentPagerAdapter {

    private Context mContext;

    Follow_Page follow_page ;
    Follow_Page folower_page ;

    public FragmentFollowAdapter(Context context, FragmentManager fm) {
        super(fm);

        mContext = context;
        follow_page = new Follow_Page();
        follow_page.setFollowType(0);
        folower_page = new Follow_Page();
        folower_page.setFollowType(1);

    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            //    fragmentBusinessPage = new FragmentEventPage();
            return follow_page;
        }
        else if (i == 1) {
            //    fragmentLocalPage = new FragmentEventPage();
            return folower_page;
        }
        return follow_page;

    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Follow";
            case 1:
                return "Follower";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
