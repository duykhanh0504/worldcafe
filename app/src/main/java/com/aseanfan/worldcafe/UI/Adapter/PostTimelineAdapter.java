package com.aseanfan.worldcafe.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.PostTimelineModel;
import com.aseanfan.worldcafe.UI.Component.MySpannable;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.util.ArrayList;
import java.util.List;

public class PostTimelineAdapter extends RecyclerView.Adapter<PostTimelineAdapter.MyViewHolder> {


    private List<PostTimelineModel> postList;
    private List<Boolean> viewmore;

    private static PostTimelineAdapter.ClickListener clickListener;
    private boolean requestads = false;

    public void setOnItemClickListener(PostTimelineAdapter.ClickListener clickListener) {
        PostTimelineAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v ,int type);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,PostImageAdapter.ClickListener {
        private TextView username;
        private TextView detail;
        private TextView like;
        private TextView comment;
        private FrameLayout imagePost;
        private ImageView avatar;
        private ImageView imagelike;
        private ImageView imageComment;
        private ImageView image_menu;
        private TextView create_time;
      //  private PostImageAdapter mAdapter;



        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.namePost);
            detail = (TextView) view.findViewById(R.id.detailPost);
            like = (TextView) view.findViewById(R.id.textLike);
            comment = (TextView) view.findViewById(R.id.textComment);
            imagePost = (FrameLayout) view.findViewById(R.id.list_image);
            avatar = (ImageView) view.findViewById(R.id.imageAvatar);
            imagelike = (ImageView) view.findViewById(R.id.imageLike) ;
            image_menu = (ImageView) view.findViewById(R.id.image_menu) ;
            imageComment = (ImageView)view.findViewById(R.id.imageComment) ;
            create_time =(TextView) view.findViewById(R.id.time);

          //  makeTextViewResizable(detail, 3, "View More", true);
            view.setOnClickListener(this);
            imagelike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_LIKE);
                }
            });

            image_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_MENU);
                }
            });

            imageComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_COMMENT);
                }
            });

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_AVATAR);
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(viewmore.get(getAdapterPosition()) == true)
                    {
                        viewmore.set(getAdapterPosition(), false);
                    }
                    else
                    {
                        viewmore.set(getAdapterPosition(), true);
                    }
                    notifyDataSetChanged();
                }
            });

            imagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view , Constants.CLICK_IMAGE_PREVIEW);
                }
            });

         //   mAdapter = new PostImageAdapter(null);

           // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(),3);
           /*  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            imagePost.setLayoutManager(mLayoutManager);
            imagePost.setItemAnimator(new DefaultItemAnimator());
            imagePost.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);*/


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view ,Constants.CLICK_TIMELINE);
        }

        @Override
        public void onItemClick(int position, View v) {
            //clickListener.onItemClick(getAdapterPosition(), v ,Constants.CLICK_TIMELINE);
        }
    }


    public PostTimelineAdapter(List<PostTimelineModel> postList , boolean requestads) {
        this.requestads = requestads;
        this.postList = postList;
        if (viewmore==null)
        {
            viewmore = new ArrayList<>();
        }
        if (postList==null)
        {
            postList = new ArrayList<>();
        }
        viewmore.clear();
        for (PostTimelineModel i : postList)
        {
            viewmore.add(true);
        }
    }

    public void setPostList (List<PostTimelineModel> postList) {
        this.postList = postList;
        this.notifyDataSetChanged();
        if (viewmore==null)
        {
            viewmore = new ArrayList<>();
        }
        if (postList==null)
        {
            postList = new ArrayList<>();
        }
        viewmore.clear();
        for (PostTimelineModel i : postList)
        {
            viewmore.add(true);
        }
    }

    @NonNull
    @Override
    public PostTimelineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_row, viewGroup, false);

        return new PostTimelineAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        PostTimelineModel post = postList.get(i);

        myViewHolder.username.setText(post.getUsername());
        myViewHolder.like.setText(String.valueOf(post.getNumberLike()));
        myViewHolder.comment.setText(String.valueOf(post.getNumberComment()));
       /* if(post.getAccountid().equals(AccountController.getInstance().getAccount().getId()))
        {
            myViewHolder.image_menu.setVisibility(View.VISIBLE);
        }
        else
        {
            myViewHolder.image_menu.setVisibility(View.GONE);
        }*/
        if(post.getIslike() == 0)
        {
            myViewHolder.imagelike.setBackgroundResource(R.drawable.unlike);
        }
        else
        {
            myViewHolder.imagelike.setBackgroundResource(R.drawable.like);
        }

        myViewHolder.create_time.setText(Utils.ConvertDiffTime(post.getTimeDiff()));
        Drawable mDefaultBackground = myViewHolder.avatar.getContext().getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(myViewHolder.avatar.getContext()).load(post.getUrlAvatar()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE).error(mDefaultBackground)).into(myViewHolder.avatar);

        if(post.getDetail()!=null && !post.getDetail().isEmpty()) {
            myViewHolder.detail.setVisibility(View.VISIBLE);
            if(viewmore.get(i) == true) {
                myViewHolder.detail.setText(Html.fromHtml((makeTextViewResizable(post.getDetail(), "View more", viewmore.get(i)))));
            }
            else
            {
                myViewHolder.detail.setText(post.getDetail());
            }
            //makeTextViewResizable(post.getDetail(),myViewHolder.detail,3 , "View more" ,myViewHolder.viewMore);
        }
        else
        {
            myViewHolder.detail.setVisibility(View.GONE);
        }


            if(post.getUrlImage()!=null && post.getUrlImage().size() > 0) {
                myViewHolder.imagePost.setVisibility(View.VISIBLE);
                UpdateLayoutImage(myViewHolder.imagePost,post.getUrlImage());
               // myViewHolder.mAdapter.setData(post.getUrlImage());
               /* for (String url : post.getUrlImage()) {
                    //listImage.add(url);
                    urlimg = url;
                }
                if(urlimg!=null) {
                    Glide.with(myViewHolder.context).load(post.getUrlImage().get(0)).into(myViewHolder.imagePost);
                }*/
            }
            else
            {
                myViewHolder.imagePost.setVisibility(View.GONE);
            }


    }

    public  String makeTextViewResizable(final String s ,String expandText,  boolean viewMore) {

        String text = s;
        if (s == null && s.isEmpty()) {
            return text;
        }
        if (s.length() > 100 && viewMore == true ) {

            text = s.subSequence(0, 101  ) + "... " + "<b style='color:black;'>" + expandText + "</b>";
        }
        else
        {
            text = s;
        }
        return text;
    }

    public  void makeTextViewResizable(final String s , final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text  = s;
                tv.setText(s);
                int lineEndIndex = tv.getLayout().getLineEnd(0);
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + "<font color='red'>" + expandText + "</font>";
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + "..." + "<font color='red'>" + expandText + "</font>";
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(s,Html.fromHtml(text), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if(maxLine == - 1) {
                   // lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                   // text = tv.getText().subSequence(0, lineEndIndex) + " " + "<font color='red'>" + expandText + "</font>";
                }
                else {
                    tv.setText(s);
                }

            }
        });

    }

    private  SpannableStringBuilder addClickablePartTextViewResizable(final String s, final Spanned strSpanned, final TextView tv,
                                                                      final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                      //  makeTextViewResizable(s , tv, -1, " View Less", false);
                    } else {
                      //  makeTextViewResizable(tv, 3, "View More", true);
                    }
                    notifyDataSetChanged();

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        if (!viewMore) {
            return  new SpannableStringBuilder(s);
        } else {
            return ssb;
            //  makeTextViewResizable(tv, 3, "View More", true);
        }

      //  return ssb;

    }

    void UpdateLayoutImage(FrameLayout contain ,List<String> url)
    {
        contain.removeAllViews();
        if(url.size()==1) {
            ImageView image = new ImageView(contain.getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);
            contain.addView(image);

        }
        else if (url.size() ==2)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((Utils.getwidthScreen(contain.getContext())/2) - Utils.convertDpToPixel(1,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((Utils.getwidthScreen(contain.getContext())/2 - Utils.convertDpToPixel(1,contain.getContext())), Utils.convertDpToPixel(240,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            contentimage.addView(image);
            contentimage.addView(line);
            contentimage.addView(image1);
            contain.addView(contentimage);


        }
        else if (url.size() ==3)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(240,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            LinearLayout contentimage1 = new LinearLayout(contain.getContext());
            contentimage1.setOrientation(LinearLayout.VERTICAL);
            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            layoutParams.setMargins( 0,0,0,Utils.convertDpToPixel(5,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            ImageView image2 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(120,contain.getContext()));
            image2.setLayoutParams(layoutParams2);
            image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(2)).into(image2);

            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line1 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline1 = new LinearLayout.LayoutParams( Utils.convertDpToPixel(Utils.convertDpToPixel(120,contain.getContext()),contain.getContext()), Utils.convertDpToPixel(2,contain.getContext()));
            line1.setLayoutParams(layoutParamsline1);
            line1.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            contentimage1.addView(image1);
            contentimage1.addView(line1);
            contentimage1.addView(image2);

            contentimage.addView(image);
            contentimage.addView(line);
            contentimage.addView(contentimage1);
            contain.addView(contentimage);


        }
        else if (url.size() >=4)
        {
            LinearLayout contentimage = new LinearLayout(contain.getContext());
            contentimage.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout contentimage1 = new LinearLayout(contain.getContext());
            contentimage1.setOrientation(LinearLayout.VERTICAL);

            ImageView image = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            image.setLayoutParams(layoutParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(0)).into(image);

            ImageView image1 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(120,contain.getContext()));
            image1.setLayoutParams(layoutParams1);
            image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(1)).into(image1);

            LinearLayout contentimage2 = new LinearLayout(contain.getContext());
            contentimage2.setOrientation(LinearLayout.VERTICAL);

            ImageView image2 = new ImageView(contain.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(119,contain.getContext()));
            image2.setLayoutParams(layoutParams2);
            image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(contain.getContext()).load(url.get(2)).into(image2);

            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(Utils.getwidthScreen(contain.getContext()) / 2, Utils.convertDpToPixel(120, contain.getContext()));
            FrameLayout containplus = new FrameLayout(contain.getContext());
            ImageView image3;
            if(url.size() == 4) {
                image3 = new ImageView(contain.getContext());
                image3.setLayoutParams(layoutParams3);
                image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(contain.getContext()).load(url.get(3)).into(image3);
            }else
            {

                image3 = new ImageView(contain.getContext());
                image3.setLayoutParams(layoutParams3);
                image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(contain.getContext()).load(url.get(3)).into(image3);

                containplus.addView(image3);

                FrameLayout overlayout = new FrameLayout(contain.getContext());
                overlayout.setLayoutParams(layoutParams3);
                overlayout.setBackgroundColor(contain.getContext().getResources().getColor(R.color.blacktransparent));
                containplus.addView(overlayout);

                TextView textplus = new TextView(contain.getContext());
                textplus.setTextColor(contain.getContext().getResources().getColor(R.color.white));
                textplus.setGravity(Gravity.CENTER);
                textplus.setTextSize(Utils.convertDpToPixel(30,contain.getContext()));
                textplus.setText(String.valueOf(url.size() -4) + "+");
                containplus.addView(textplus);
            }
            FrameLayout line = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline = new LinearLayout.LayoutParams( Utils.convertDpToPixel(2,contain.getContext()), Utils.convertDpToPixel(240,contain.getContext()));
            line.setLayoutParams(layoutParamsline);
            line.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line1 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline1 = new LinearLayout.LayoutParams( Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(2,contain.getContext()));
            line1.setLayoutParams(layoutParamsline1);
            line1.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            FrameLayout line2 = new FrameLayout(contain.getContext());
            LinearLayout.LayoutParams layoutParamsline2= new LinearLayout.LayoutParams( Utils.getwidthScreen(contain.getContext())/2, Utils.convertDpToPixel(2,contain.getContext()));
            line2.setLayoutParams(layoutParamsline2);
            line2.setBackgroundColor(contain.getContext().getResources().getColor(R.color.white));

            contentimage1.addView(image);
            contentimage1.addView(line1);
            contentimage1.addView(image1);

            contentimage2.addView(image2);
            contentimage2.addView(line2);
            if(url.size() == 4) {
                contentimage2.addView(image3);
            }
            else
            {
                contentimage2.addView(containplus);
            }

            contentimage.addView(contentimage1);
            contentimage.addView(line);
            contentimage.addView(contentimage2);
            contain.addView(contentimage);


        }
    }


    @Override
    public int getItemCount() {
        if (postList == null)
        {
            return 0;
        }
        return postList.size();
    }
}