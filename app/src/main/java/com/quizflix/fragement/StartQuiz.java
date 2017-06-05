package com.quizflix.fragement;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quizflix.R;

/**
 * Created by kavasthi on 6/5/2017.
 */

public class StartQuiz extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_quiz,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }
    private void openDialog() {
        try {
            Dialog dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
            dialog.setContentView(R.layout.edit_layout);
            // set the custom dialog components - text, image and button
            openImage = (LinearLayout) dialog.findViewById(R.id.openLayout);
            edit = (LinearLayout) dialog.findViewById(R.id.editLayout);
            email = (LinearLayout) dialog.findViewById(R.id.emailLayout);
            message = (LinearLayout) dialog.findViewById(R.id.megLayout);
            delete = (LinearLayout) dialog.findViewById(R.id.delLayout);
            closeDialog = (RelativeLayout) dialog.findViewById(R.id.close_dialog);
            crossClick = (ImageView) dialog.findViewById(R.id.cross);
            imgView = (ImageView) dialog.findViewById(R.id.imgName);
            emailIcon = (ImageView) dialog.findViewById(R.id.email_icn);
            megIcon = (ImageView) dialog.findViewById(R.id.meg_icn);
            emailText = (TextView) dialog.findViewById(R.id.email_txt);
            megText = (TextView) dialog.findViewById(R.id.message);
            isPremium();
            edit.setOnClickListener(this);
            email.setOnClickListener(this);
            message.setOnClickListener(this);
            delete.setOnClickListener(this);
            crossClick.setOnClickListener(this);
            openImage.setOnClickListener(this);
            closeDialog.setOnClickListener(this);
            if (searchValue.length() > 0)

                Glide.with(getActivity()).load(filteredList.get(seletedPos).getPicPath()).into(imgView);
            else Glide.with(getActivity()).load(picList.get(seletedPos).getPicPath()).into(imgView);
            dialog.show();

        } catch (Exception e) {

        }
    }
    private void initViews(View view) {
    }
}
