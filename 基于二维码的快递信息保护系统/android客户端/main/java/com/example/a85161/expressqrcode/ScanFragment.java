package com.example.a85161.expressqrcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a85161.expressqrcode.util.LoginHttp;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment implements View.OnClickListener {
    private ImageButton ScanButton;
    private TextView result;

    private int REQUEST_CODE_SCAN = 111;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_tab, container, false);

        ScanButton = (ImageButton) view.findViewById(R.id.scanButton);
        ScanButton.setOnClickListener(this);
        result=(TextView)view.findViewById(R.id.scanResult);

        return view;
    }

    @Override
    public void onClick(View v) {
        //Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanButton:
                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                /*ZxingConfig是配置类
                                 *可以设置是否显示底部布局，闪光灯，相册，
                                 * 是否播放提示音  震动
                                 * 设置扫描框颜色等
                                 * 也可以不传这个参数
                                 * */
                                ZxingConfig config = new ZxingConfig();
                                config.setPlayBeep(true);//是否播放扫描声音 默认为true
                                config.setShake(true);//是否震动  默认为true
                                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                                config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为淡蓝色
                                config.setFrameLineColor(R.color.white);//设置扫描框边框颜色 默认无色
                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                                Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(getContext(),content,Toast.LENGTH_SHORT).show();
                result.setText("扫描结果为：" + content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        HashMap<String, String> parameter = new HashMap<>();
                        parameter.put("name", User.username);
                        parameter.put("uuid", content + ".png");
//                        parameter.put("num", "5");
                        Message message = new Message();
                        message.what = 1;
                        //登陆
                        message.obj = LoginHttp.getResult("http://10.0.2.2:8001/", parameter, "querycargo/");
                        //message.obj = Fulltask.getResult1("http://10.0.2.2:8001/get/?num=5",parameter1);
                        handler.sendMessage(message);
                        Toast.makeText(getContext(),message.obj.toString(),Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();

            }
        }
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    //form(msg.obj.toString());
                    result.setText("扫描结果为：" + form(msg.obj.toString()));
                    break;
                default:
                    break;
            }
        }
    };

    public String form(String s){
        String mm = "";
        try {
            JSONObject jsonObject2 = new JSONObject(s);
            JSONArray js2 = jsonObject2.getJSONArray("body");
            for (int i = js2.length(); i != 0; i--) {
                JSONObject object = js2.getJSONObject(i - 1);
                mm += "sender:" + object.getString("sender");
                mm += "receiver:" + object.getString("receiver");
                mm += "phone:" + object.getString("phone");
                mm += "address:" + object.getString("address");
            }
            //Toast.makeText(getApplicationContext(),commits.toString(),Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            //Log.e(TAG, e.toString());
        }
        return mm;
    }
}
