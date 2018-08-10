package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Fragment.AlbumFragment;
import com.aseanfan.worldcafe.UI.Fragment.FragmentEventPage;
import com.aseanfan.worldcafe.UI.Fragment.MyPageDetailFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPostFragment;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.List;

public class FragmentEventPageAdapter  extends FragmentPagerAdapter {

    private Context mContext;


    public final static int FRIEND_PAGE = 0;
    public final static int BUSINESS_PAGE = 1;
    public final static int LOCAL_PAGE = 2;
    public final static int LANGUAGE_PAGE = 3;

    FragmentEventPage fragmentFriendPage ;
    FragmentEventPage fragmentBusinessPage ;
    FragmentEventPage fragmentLocalPage ;
    FragmentEventPage fragmentLanguagePage ;

    public FragmentEventPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }




    public FragmentEventPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void updateFragmentEvent(List<EventModel> data,int type)
    {
        if (type == FRIEND_PAGE) {
            fragmentFriendPage.setData(data);
        }
        else if (type == BUSINESS_PAGE) {
            fragmentBusinessPage.setData(data);
        }
        else if (type == LOCAL_PAGE) {
            fragmentLocalPage.setData(data);
        }
        else if (type == LANGUAGE_PAGE) {
            fragmentLanguagePage.setData(data);
        }
    }
    @Override
    public Fragment getItem(int i) {
       if (i == BUSINESS_PAGE) {
            fragmentBusinessPage = new FragmentEventPage();
            return fragmentBusinessPage;
        }
        else if (i == LOCAL_PAGE) {
            fragmentLocalPage = new FragmentEventPage();
            return fragmentLocalPage;
        }
        else if (i == LANGUAGE_PAGE) {
            fragmentLanguagePage = new FragmentEventPage();
            return fragmentLanguagePage;
        }
        else  {
            fragmentFriendPage = new FragmentEventPage();
            return fragmentFriendPage;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case FRIEND_PAGE:
                return mContext.getString(R.string.Friend);
            case BUSINESS_PAGE:
                return mContext.getString(R.string.Business);
            case LOCAL_PAGE:
                return mContext.getString(R.string.Local);
            case LANGUAGE_PAGE:
                return mContext.getString(R.string.Language);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
