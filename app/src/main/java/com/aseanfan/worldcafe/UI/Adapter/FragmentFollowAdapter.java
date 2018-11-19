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
  //  Long accountid;

    public FragmentFollowAdapter(Context context, FragmentManager fm ,Long accountid) {
        super(fm);

        mContext = context;
        follow_page = new Follow_Page();
        follow_page.setid(accountid);
        follow_page.setFollowType(0);
        folower_page = new Follow_Page();
        folower_page.setid(accountid);
        folower_page.setFollowType(1);
       // this.accountid =accountid;

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
                return mContext.getResources().getString(R.string.Follow);
            case 1:
                return mContext.getResources().getString(R.string.Followers);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
