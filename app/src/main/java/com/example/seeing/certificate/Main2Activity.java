package com.example.seeing.certificate;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Main2Activity extends BaseNfcActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onNewIntent(Intent intent){
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        analysisTag(detectedTag);
    }

    /** 读uid和状态位*/
    private boolean analysisTag(Tag tag) {
        //Intent intent = mAct.getIntent();
        //Tag tag = NAFVerifyHelper.getNfcData(intent);
        boolean a = false;
        if (tag != null) {
            //mView.restUI();
            MifareUltralight mifare = MifareUltralight.get(tag);
            String result = "";
            String temp = "";
            try {
                mifare.connect();
                byte[] payload = mifare.readPages(0);
                for (int j = 0; j < 8; j++) {
                    temp = Integer.toHexString(payload[j] & 0xFF);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
                if(result != null) a = true;
                System.out.println("uid为：" + result);

//                temp = "";
//                int move = 0x80;
//                byte[] transceive = mifare.transceive(new byte[]{0x30, -128});
//                for (int i = 0; i < 5; i++) {
//                    if ((transceive[0] & move) == 0) temp += "0";
//                    else temp += "1";
//                    move = move >> 1;
//                }


            }catch (Exception e){
                e.printStackTrace();
                a = false;
            }
        }
        System.out.println(a);
        return a;
    }
}
