package com.ab.yuri.aifuwu;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ScrollingView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ab.yuri.aifuwu.util.Mail;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Yuri on 2017/2/3.
 */

public class FeedbackFragment extends Fragment {
    private EditText feedbackContent;
    private EditText feedbackEmail;
    private Button feedbackSubmit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.feedback_area,container,false);
        feedbackContent= (EditText) view.findViewById(R.id.feedback_content);
        feedbackEmail= (EditText) view.findViewById(R.id.feedback_email);
        feedbackSubmit= (Button) view.findViewById(R.id.feedback_submit);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        feedbackContent.requestFocus();
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=feedbackContent.getText().toString();
                final String email=feedbackEmail.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Mail.sendMail(content,email);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

}
