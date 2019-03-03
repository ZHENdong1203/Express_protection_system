package com.example.a85161.expressqrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import com.example.a85161.expressqrcode.util.LoginHttp;

public class LogInFragment extends AuthFragment {

  private SharedPreferences settings;
  private static final int Success = 1;
  private static final int Fail = 0;

  private TextInputEditText name;
  private TextInputEditText password;
  private CheckBox rememberPass;
  private VerticalTextView login;

  @BindViews(value = {R.id.email_input_edit, R.id.password_input_edit})
  protected List<TextInputEditText> views;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (view != null) {
      caption.setText(getString(R.string.log_in_label));
      view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_log_in));
      for (TextInputEditText editText : views) {
        if (editText.getId() == R.id.password_input_edit) {
          final TextInputLayout inputLayout = ButterKnife.findById(view, R.id.password_input);
          Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
          inputLayout.setTypeface(boldTypeface);
          editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
              inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
            }
          });
        }
        editText.setOnFocusChangeListener((temp, hasFocus) -> {
          if (!hasFocus) {
            boolean isEnabled = editText.getText().length() > 0;
            editText.setSelected(isEnabled);
          }
        });
      }
    }

    name=(TextInputEditText)view.findViewById(R.id.email_input_edit);
    password=(TextInputEditText)view.findViewById(R.id.password_input_edit);
    rememberPass=(CheckBox)view.findViewById(R.id.checkbox_1);
    login=(VerticalTextView)view.findViewById(R.id.caption);



    settings = getActivity().getPreferences(Context.MODE_PRIVATE);
    if (settings.getBoolean("remember", false)) {
      rememberPass.setChecked(true);
      name.setText(settings.getString("username", ""));
      password.setText(settings.getString("password", ""));
    }
    // remember checkbox
    rememberPass.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = settings.edit();
        if (rememberPass.isChecked()) {
          editor.putBoolean("remember", true);
          editor.putString("username", name.getText().toString());
          editor.putString("password", password.getText().toString());
        } else {
          editor.putBoolean("remember", false);
          editor.putString("username", "");
          editor.putString("password", "");
        }
        editor.commit();
      }
    });




    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(name.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")) {
          Toast.makeText(getActivity(),"账号或密码不能为空",Toast.LENGTH_LONG).show();
        }else {
          new Thread(new Runnable() {
            @Override
            public void run() {
              HashMap<String, String> parameter = new HashMap<>();
              parameter.put("name", name.getText().toString());
              parameter.put("password", password.getText().toString());
//                        parameter.put("num", "5");
              Message message = new Message();
              message.what = 1;
              //登陆
              message.obj = LoginHttp.getResult("http://10.0.2.2:8001/", parameter, "postmanlogin/");
              //message.obj = Fulltask.getResult1("http://10.0.2.2:8001/get/?num=5",parameter1);
              handler.sendMessage(message);
            }
          }).start();
        }
      }
    });
  }

  @Override
  public int authLayout() {
    return R.layout.login_fragment;
  }

  @Override
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void fold() {
    lock = false;
    Rotate transition = new Rotate();
    transition.setEndAngle(-90f);
    transition.addTarget(caption);
    TransitionSet set = new TransitionSet();
    set.setDuration(getResources().getInteger(R.integer.duration));
    ChangeBounds changeBounds = new ChangeBounds();
    set.addTransition(changeBounds);
    set.addTransition(transition);
    TextSizeTransition sizeTransition = new TextSizeTransition();
    sizeTransition.addTarget(caption);
    set.addTransition(sizeTransition);
    set.setOrdering(TransitionSet.ORDERING_TOGETHER);
    final float padding = getResources().getDimension(R.dimen.folded_label_padding) / 2;
    set.addListener(new Transition.TransitionListenerAdapter() {
      @Override
      public void onTransitionEnd(Transition transition) {
        super.onTransitionEnd(transition);
        caption.setTranslationX(-padding);
        caption.setRotation(0);
        caption.setVerticalText(true);
        caption.requestLayout();

      }
    });
    TransitionManager.beginDelayedTransition(parent, set);
    caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.getTextSize() / 2);
    caption.setTextColor(Color.WHITE);
    ConstraintLayout.LayoutParams params = getParams();
    params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
    params.verticalBias = 0.5f;
    caption.setLayoutParams(params);
    caption.setTranslationX(caption.getWidth() / 8 - padding);
  }

  @Override
  public void clearFocus() {
    for (View view : views) view.clearFocus();
  }



  private Handler handler = new Handler(){
    public void handleMessage(Message msg){
      switch (msg.what){
        case Success:
          Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_LONG).show();
          if(msg.obj.toString().equals("登陆成功")) {
            Intent loginIntent = new Intent();
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            loginIntent.setClass(getActivity(), MainActivity.class);
            User.username = name.getText().toString();
            User.password = password.getText().toString();
            startActivity(loginIntent);
          }
          break;
        case Fail:
          Toast.makeText(getActivity(),"无网络",Toast.LENGTH_LONG).show();
          break;
        case 2:
          Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_LONG).show();
          break;
        default:
          break;
      }
    }
  };



}
