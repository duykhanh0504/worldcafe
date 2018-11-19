package com.aseanfan.worldcafe.UI.Component;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aseanfan.worldcafe.worldcafe.R;



public class DialogReview {

     int starvote = 0;
     Dialog dialog;


    public interface DialogListenner {
        public void OnClickSend(int starvote ,Long eventid, String comment );
    }

    public void cancel()
    {
        dialog.dismiss();
    }

    public void showDialog(final Activity activity,final Long event_id, final DialogListenner listenner){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.review_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        // Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        //  dialogBtn_cancel.setVisibility(View.GONE);
        final ImageView star1 = (ImageView) dialog.findViewById(R.id.star1);
        final ImageView star2 = (ImageView) dialog.findViewById(R.id.star2);
        final ImageView star3 = (ImageView) dialog.findViewById(R.id.star3);
        final ImageView star4 = (ImageView) dialog.findViewById(R.id.star4);
        final ImageView star5 = (ImageView) dialog.findViewById(R.id.star5);
        final EditText  comment = (EditText) dialog.findViewById(R.id.edtcomment);

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.starfill);
                star2.setBackgroundResource(R.drawable.starnull);
                star3.setBackgroundResource(R.drawable.starnull);
                star4.setBackgroundResource(R.drawable.starnull);
                star5.setBackgroundResource(R.drawable.starnull);
                starvote = 1;
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.starfill);
                star2.setBackgroundResource(R.drawable.starfill);
                star3.setBackgroundResource(R.drawable.starnull);
                star4.setBackgroundResource(R.drawable.starnull);
                star5.setBackgroundResource(R.drawable.starnull);
                starvote = 2;
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.starfill);
                star2.setBackgroundResource(R.drawable.starfill);
                star3.setBackgroundResource(R.drawable.starfill);
                star4.setBackgroundResource(R.drawable.starnull);
                star5.setBackgroundResource(R.drawable.starnull);
                starvote = 3;

            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.starfill);
                star2.setBackgroundResource(R.drawable.starfill);
                star3.setBackgroundResource(R.drawable.starfill);
                star4.setBackgroundResource(R.drawable.starfill);
                star5.setBackgroundResource(R.drawable.starnull);
                starvote = 4;
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.starfill);
                star2.setBackgroundResource(R.drawable.starfill);
                star3.setBackgroundResource(R.drawable.starfill);
                star4.setBackgroundResource(R.drawable.starfill);
                star5.setBackgroundResource(R.drawable.starfill);
                starvote = 5;
            }
        });


        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_send);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.OnClickSend(starvote,event_id,comment.getText().toString());
            }
        });

        dialog.show();
    }

}
