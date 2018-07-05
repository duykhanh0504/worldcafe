package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Fragment.AlbumFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPageDetailFragment;
import com.aseanfan.worldcafe.UI.Fragment.MyPostFragment;
import com.aseanfan.worldcafe.worldcafe.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyPagerAdapter  extends FragmentPagerAdapter {

    private Context mContext;
    private AlbumFragment albumFragment;

    public final static int MYPPOST_PAGE = 0;
    public final static int DETAIL_PAGE = 2;
    public final static int ALBUM_PAGE = 1;


    public FragmentMyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == MYPPOST_PAGE) {
            return new MyPostFragment();
        } else if(position ==DETAIL_PAGE){
            return new MyPageDetailFragment();
        }
        else if(position == ALBUM_PAGE){
            albumFragment =  new AlbumFragment();
            return albumFragment;
        }
        return new MyPostFragment();
    }

    public void updateFragmentAlbum(List<PostTimelineModel> data)
    {
        List<String> listImage = new ArrayList<>();
        for(PostTimelineModel item : data)
        {
            if(item.getUrlImage()!=null) {
                for (String url : item.getUrlImage()) {
                    listImage.add(url);
                }
            }
        }
        albumFragment.setAlbumData(listImage);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case MYPPOST_PAGE:
                return mContext.getString(R.string.My_Post);
            case ALBUM_PAGE:
                return mContext.getString(R.string.Album_page);
            case DETAIL_PAGE:
                return mContext.getString(R.string.Detail_page);
            default:
                return null;
        }
    }


}
