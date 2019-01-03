package com.example.seeing.certificate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** TextView选择框 */
    private TextView mSelectTv;
    private TextView mtextview;//单双证

    /** popup窗口里的ListView */
    private ListView mTypeLv;
    private ListView mNumLV;//单双证

    /** popup窗口 */
    private PopupWindow typeSelectPopup;
    private PopupWindow myPopup;

    /** 模拟的假数据 */
    private List<String> testData1;
    private List<String> testData2;

    /** 数据适配器 */
    private ArrayAdapter<String> testDataAdapter;
    private ArrayAdapter<String> testDataAdapter1;

    /** ImageView*/
    private ImageView imageView1;   //发证
    private ImageView imageView2;   //复制

    /** 保存随机数*/
    private String passnum = "11111111";

    private Intent intent;

    /** 保存品牌和单双证 */
    private String logo;
    private String mtype;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();


    }

    /**
     * 初始化UI
     */
    private void initUI() {
        mSelectTv = (TextView) findViewById(R.id.tv_select_input);
        mtextview = (TextView) findViewById(R.id.tv_select_num);
        imageView1 = (ImageView) findViewById(R.id.my_fa);
        imageView2 = (ImageView) findViewById(R.id.my_copy);
     }

    /**
     * 初始化监听
     */
    private void initListener() {
        mSelectTv.setOnClickListener(this);
        mtextview.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_input:
                // 点击控件后显示popup窗口
                initSelectPopup();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(mSelectTv, 0, 10);
                }
                break;
            case R.id.tv_select_num:
                // 点击控件后显示popup窗口
                initSelectPopup1();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (myPopup != null && !myPopup.isShowing()) {
                    myPopup.showAsDropDown(mtextview, 0, 10);
                }
                break;
            case R.id.my_fa:
                SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name", logo);
                editor.putString("Type", mtype);
                editor.apply();
                intent = new Intent(MainActivity.this, WriteTextActivity.class);
                startActivity(intent);
                break;
            case R.id.my_copy:
                intent = new Intent(MainActivity.this, CopyActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 初始化popup窗口1
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(this);
        testData1 = new ArrayList<String>();
        testData1.add("五粮液");
        testData1.add("剑南春");
        testData1.add("西凤酒");
        testData1.add("茅台");
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.popup_text_item, testData1);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = testData1.get(position);
                logo = value;
                // 把选择的数据展示对应的TextView上
                mSelectTv.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, mSelectTv.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_corner);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }

    /**
     * 初始化popwindow2
     */
    private void initSelectPopup1(){
        mNumLV = new ListView(this);
        testData2 = new ArrayList<String>();
        testData2.add("单证");
        testData2.add("双证");
        //设置适配器
        testDataAdapter1 = new ArrayAdapter<String>(this, R.layout.popup_text_item, testData2);
        mNumLV.setAdapter(testDataAdapter1);

        //设置ListView的点击事件
        mNumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 在这里获取item数据
                String value = testData2.get(position);
                // 把选择的数据展示对应的TextView上
                mtextview.setText(value);
                mtype = value;
                // 选择完后关闭popup窗口
                myPopup.dismiss();
            }
        });

        myPopup = new PopupWindow(mNumLV, mtextview.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_corner);
        myPopup.setBackgroundDrawable(drawable);
        myPopup.setFocusable(true);
        myPopup.setOutsideTouchable(true);
        myPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //关闭popup窗口
                myPopup.dismiss();
            }
        });
    }

    /**
     *  onNewIntent方法，探测到标签会调用
     */
//    @Override
//    public void onNewIntent(Intent intent){
//        if(passnum == null){
//            return;
//        }
//        //1.获取Tag对象
//        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(passnum)});
//        boolean result = writeTag(ndefMessage, detectedTag);
//        if(result){
//            Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * 创建NDEF文本数据
     * @param text
     * @return
     */
//    public static NdefRecord createTextRecord(String text) {
//        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
//        Charset utfEncoding = Charset.forName("UTF-8");
//        //将文本转换为UTF-8格式
//        byte[] textBytes = text.getBytes(utfEncoding);
//        //设置状态字节编码最高位数为0
//        int utfBit = 0;
//        //定义状态字节
//        char status = (char) (utfBit + langBytes.length);
//        byte[] data = new byte[1 + langBytes.length + textBytes.length];
//        //设置第一个状态字节，先将状态码转换成字节
//        data[0] = (byte) status;
//        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
//        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
//        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
//        //到textBytes.length的位置
//        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
//        //通过字节传入NdefRecord对象
//        //NdefRecord.RTD_TEXT：传入类型 读写
//        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
//                NdefRecord.RTD_TEXT, new byte[0], data);
//        return ndefRecord;
//    }

    /**
     * 写数据
     * @param ndefMessage 创建好的NDEF文本数据
     * @param tag 标签
     * @return
     */
//    public static boolean writeTag(NdefMessage ndefMessage, Tag tag) {
//        try {
//            Ndef ndef = Ndef.get(tag);
//            ndef.connect();
//            ndef.writeNdefMessage(ndefMessage);
//            return true;
//        } catch (Exception e) {
//        }
//        return false;
//    }

}
