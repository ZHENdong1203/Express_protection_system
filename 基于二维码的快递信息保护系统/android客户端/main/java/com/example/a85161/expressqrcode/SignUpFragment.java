package com.example.a85161.expressqrcode;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;

import com.example.a85161.expressqrcode.util.LoginHttp;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import android.support.annotation.Nullable;
import android.annotation.TargetApi;
import android.widget.Toast;

import butterknife.BindViews;
import butterknife.ButterKnife;


public class SignUpFragment extends AuthFragment {

  private static final int Success = 1;
  private static final int Fail = 0;

    private TextInputEditText registerName;
    private TextInputEditText registerPass;
    private TextInputEditText registerPhone;
    private VerticalTextView register;


  @BindViews(value = {R.id.email_input_edit,
          R.id.password_input_edit,
          R.id.confirm_password_edit})
  protected List<TextInputEditText> views;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (view != null) {
      view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_sign_up));
      caption.setText(getString(R.string.sign_up_label));
      for (TextInputEditText editText : views) {
        if (editText.getId() == R.id.password_input_edit) {
          final TextInputLayout inputLayout = ButterKnife.findById(view, R.id.password_input);
          final TextInputLayout confirmLayout = ButterKnife.findById(view, R.id.confirm_password);
          Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
          inputLayout.setTypeface(boldTypeface);
          confirmLayout.setTypeface(boldTypeface);
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
      caption.setVerticalText(true);
      foldStuff();
      caption.setTranslationX(getTextPadding());
    }

    registerName=(TextInputEditText)view.findViewById(R.id.email_input_edit) ;
    registerPass=(TextInputEditText)view.findViewById(R.id.password_input_edit) ;
    registerPhone=(TextInputEditText)view.findViewById(R.id.confirm_password_edit) ;


    register=(VerticalTextView)view.findViewById(R.id.caption);
    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(registerName.getText().toString().trim().equals("") || registerPass.getText().toString().trim().equals("")||registerPhone.getText().toString().trim().equals("")) {
          Toast.makeText(getActivity(),"信息不能为空",Toast.LENGTH_LONG).show();
        }else{
          new Thread(new Runnable() {
            @Override
            public void run() {
              Looper.prepare();
              HashMap<String, String> parameter = new HashMap<>();
              parameter.put("name", registerName.getText().toString());
              parameter.put("password", registerPass.getText().toString());
              parameter.put("phonenum",registerPhone.getText().toString());
              Toast.makeText(getContext(),registerName.getText().toString(),Toast.LENGTH_SHORT).show();
              Message message = new Message();
              message.what = 1;
              //注册
              message.obj = LoginHttp.getResult("http://10.0.2.2:8001/", parameter, "postmanregister/");
              handler.sendMessage(message);
              Looper.loop();
            }
          }).start();
        }
      }
    });
  }

  private Handler handler = new Handler(){
    public void handleMessage(Message msg){
      switch (msg.what){
        case Success:
          Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_LONG).show();
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




  @Override
  public int authLayout() {
    return R.layout.sign_up_fragment;
  }

  @Override
  public void clearFocus() {
    for (View view : views) view.clearFocus();
  }

  @Override
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
    set.addListener(new Transition.TransitionListenerAdapter() {
      @Override
      public void onTransitionEnd(Transition transition) {
        super.onTransitionEnd(transition);
        caption.setTranslationX(getTextPadding());
        caption.setRotation(0);
        caption.setVerticalText(true);
        caption.requestLayout();

      }
    });
    TransitionManager.beginDelayedTransition(parent, set);
    foldStuff();
    caption.setTranslationX(-caption.getWidth() / 8 + getTextPadding());
  }

  private void foldStuff() {
    caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.getTextSize() / 2f);
    caption.setTextColor(Color.WHITE);
    ConstraintLayout.LayoutParams params = getParams();
    params.rightToRight = ConstraintLayout.LayoutParams.UNSET;
    params.verticalBias = 0.5f;
    caption.setLayoutParams(params);
  }

  private float getTextPadding() {
    return getResources().getDimension(R.dimen.folded_label_padding) / 2.1f;
  }
}
