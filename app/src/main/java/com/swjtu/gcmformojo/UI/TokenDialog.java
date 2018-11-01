package com.swjtu.gcmformojo.UI;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.swjtu.gcmformojo.R;

import static com.swjtu.gcmformojo.MyApplication.deviceGcmToken;
import static com.swjtu.gcmformojo.MyApplication.mySettings;


public class TokenDialog extends DialogFragment {


    private TextView myTokenSender;
    private TextView myToken;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_token, null);
        myTokenSender = (TextView) view.findViewById(R.id.textView_sender);
        myToken = (TextView) view.findViewById(R.id.myToken);
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onResume() {

        super.onResume();
        String tokenNo = getString(R.string.text_token_no);

        //SharedPreferences Settings =        getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String pushType = mySettings.getString("push_type","GCM");
        myTokenSender.setText(pushType);

        switch (pushType) {
            case "GCM":
                deviceGcmToken = FirebaseInstanceId.getInstance().getToken();
                if(deviceGcmToken !=null)
                    myToken.setText(deviceGcmToken);
                else {
                    myToken.setText(tokenNo);
                }
                break;
            /*case "MiPush":
                //SharedPreferences miSettings =        getSharedPreferences("mipush", Context.MODE_PRIVATE);
                deviceMiToken = miSettings.getString("regId",deviceMiToken);
                if(deviceMiToken!=null)
                    myToken.setText(deviceMiToken);
                else
                    myToken.setText(tokenNo);
                break;*/
            /*case "HwPush":
                HMSAgent.connect(this, new ConnectHandler() {
                    @Override
                    public void onConnect(int rst) {
                        //Log.e("HMS connect end:" + rst);
                    }
                });
                HMSAgent.Push.getToken(new GetTokenHandler() {
                    public void onResult(int rtnCode, TokenResult tokenResult) {
                        //Log.e("get token: end" + rtnCode);
                    }
                });
                if(deviceHwToken!=null)
                    myToken.setText(deviceHwToken);
                else {
                    myToken.setText(tokenNo);
                }
                break;*/
                /*
            case "FmPush":
                com.meizu.cloud.pushsdk.PushManager.register(this, fm_APP_ID, fm_APP_KEY);
                if(deviceFmToken!=null)
                    myToken.setText(deviceFmToken);
                else {
                    myToken.setText(tokenNo);
                }
                break;
                */
        }


    }
}
