package kr.co.elpusk.android.testlpu237;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.elpusk.android.liblpu237.ApiLpu237;
import kr.co.elpusk.android.liblpu237.UsbDevHandle;
import kr.co.elpusk.android.liblpu237.lpu237.Lpu237Callback;
import kr.co.elpusk.android.liblpu237.util.DukptTdes;
import kr.co.elpusk.android.liblpu237.util.ParserTagValue;
import kr.co.elpusk.android.liblpu237.util.Tools;

interface ConstTest{
    String CONST_IV = "0000000000000000";//unchangeable initial vector of CBC mode.
    String CONST_TEST_KEYBOARD_RSP = "4634303143F03036303030303030F14646464639383736353433323130453030303035F2F3F4FF344333424131463730383238373534373734344141333431373142364432443939323541443135303835383631433845394242373436303131464244393342334144344436333536303535463032354444453543334630443632393144323736424433333046393241323146463746344242464134443035374235453233444241373036373239373236453941463831343243434434444241304644363339394142FF32353342413146373038323837353437373441463542373639314439323238304131344441373031323538344439443241443833463631383343354630324545353436383230454544323132383546333033FF363833424131463730383238373534373734414635423736393144393232383041313444413730313235383444394432414438334636313833433546303245453534334346344633383337343531313546383734343035393934463042303734423235323438464345314334423232334538464444303832354539333331424236363041353338324634433746334534303736373042393536383739373539394236373844384335444439394334384336453439323341334343443142394646463944444135463642444244383735363734FE4230453137373031";
    String[] CONST_TEST_IPEK ={
            "6AC292FAA1315B4D858AB3A3D7D5933A",
            "00000000000000000000000000000000"
    };
}

public class MainActivity extends AppCompatActivity {

    String[] m_s_ipek = ConstTest.CONST_TEST_IPEK;
    private boolean m_b_canceled = false;
    private class CbMsrRead extends Lpu237Callback{
        public CbMsrRead(Lpu237Callback.TypeRx t){
            super(t);
        }
        @Override
        public void Run(Lpu237Callback.Result read_result,byte[] rx){
            //Don't use Main thread GUI Component here!
            //If use it, occur a dead-lock.!
            if(Result.RESULT_SUCCESS == read_result){
                if( !Tools.array_has_triple_e6_in_header(rx) ){
                    //PlainText here
                    int[] nIso = new int[]{rx[0],rx[1],rx[2]};

                    byte[] a1 = new byte[0];
                    byte[] a2 = new byte[0];
                    byte[] a3 = new byte[0];
                    String s1 = "", s2="", s3="";

                    int nOffset = 3;
                    if( nIso[0] >0 ) {
                        a1 = new byte[nIso[0]];
                        System.arraycopy(rx,nOffset,a1,0,a1.length);
                        for (int i = 0; i < a1.length; i++) {
                            a1[i] += 0x20;
                        }

                        s1 = new String(a1, StandardCharsets.US_ASCII);
                        nOffset += a1.length;
                    }
                    if( nIso[1] >0 ) {
                        a2 = new byte[nIso[1]];
                        System.arraycopy(rx,nOffset,a2,0,a2.length);
                        for (int i = 0; i < a2.length; i++) {
                            a2[i] += 0x30;
                        }

                        s2 = new String(a2, StandardCharsets.US_ASCII);
                        nOffset += a2.length;
                    }
                    if( nIso[2] >0 ) {
                        a3 = new byte[nIso[2]];
                        System.arraycopy(rx,nOffset,a3,0,a3.length);
                        for (int i = 0; i < a3.length; i++) {
                            a3[i] += 0x30;
                        }

                        s3 = new String(a3, StandardCharsets.US_ASCII);
                    }

                    String sInfo = "PlainText\n";
                    sInfo = sInfo + "iso1:" + String.valueOf(nIso[0]) + ":" + s1 + "\n";
                    sInfo = sInfo + "iso2:" + String.valueOf(nIso[1]) + ":" + s2 + "\n";
                    sInfo = sInfo + "iso3:" + String.valueOf(nIso[2]) + ":" + s3 + "\n";

                    _send_broadercast(sInfo);
                }
                else{//encrypted data.
                    //remove triple 0xe6
                    byte[] r = new byte[rx.length-3];
                    System.arraycopy(rx,3,r,0,r.length);
                    String sHex = Tools.get_hex_string_from_binary(r);
                    _send_broadercast_encrypted(sHex);
                }

                if(!m_b_canceled) {
                    if (!ApiLpu237.getInstance().WaitMsrOriButtonWithCallback(m_curHandle, m_cb_read)) {
                        _send_broadercast("error : re-read");
                    }
                }
            }
            else if(Result.RESULT_ERROR == read_result){
                _send_broadercast("error : read");

                if(!m_b_canceled) {
                    if (!ApiLpu237.getInstance().WaitMsrOriButtonWithCallback(m_curHandle, m_cb_read)) {
                        _send_broadercast("error : re-read");
                    }
                }
            }
            else{

            }

            if(m_b_canceled){
                m_b_canceled = false;
            }
        }
    }
    Lpu237Callback m_cb_read = new CbMsrRead(Lpu237Callback.TypeRx.RX_MSR);
    UsbDevHandle m_curHandle = null;
    UsbManager m_usbManager = null;
    final String LPU237_PERMISSION = "kr.co.elpusk.android.testlpu237.LPU237_PERMISSION";
    final String SET_TEXT_TO_RESULT = "kr.co.elpusk.android.testlpu237.SET_TEXT_TO_RESULT";
    final String SET_ENTEXT_TO_RESULT = "kr.co.elpusk.android.testlpu237.SET_ENTEXT_TO_RESULT";

    final String STR_KEY_SET_TEXT_TO_RESULT = "SET_TEXT_TO_RESULT";
    final String STR_KEY_SET_ENTEXT_TO_RESULT = "SET_ENTEXT_TO_RESULT";

    private EditText m_editTextInput, m_editTextIpek;
    private Button m_buttonRun, m_buttonTestSet, m_buttonClear, m_buttonOpen, m_buttonClose;
    private TextView m_textViewResult, m_textViewHeader;

    private ExecutorService executorServiceDe, executorServiceOpen;
    private Handler mainHandlerDe,mainHandlerOpen;
    private ParserTagValue m_tag_value = null;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Broadcast 메시지를 수신하고 처리하는 코드
            String action = intent.getAction();
            if (action != null && action.equals(LPU237_PERMISSION)) {
                // 작업 수행
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    m_textViewResult.setText("OK : Permission");
                    if(!_open_setup()){
                        m_textViewResult.setText("Fail Open setup\n");
                    }
                    else {
                        m_textViewResult.setText("OK : Open setup\n");
                    }

                }
                else{
                    m_textViewResult.setText("NOT Grant : Permission");
                }
            }
            else if(action != null && action.equals(SET_TEXT_TO_RESULT)){
                String s = intent.getExtras().getString(STR_KEY_SET_TEXT_TO_RESULT,"-_-");
                m_textViewResult.setText(s);
            }
            else if(action != null && action.equals(SET_ENTEXT_TO_RESULT)){
                String s = intent.getExtras().getString(STR_KEY_SET_ENTEXT_TO_RESULT,"-_-");
                m_editTextInput.setText(s);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_usbManager = (UsbManager) getApplication().getSystemService(getApplication().USB_SERVICE);
        // EditText, Button, TextView 초기화
        m_textViewHeader = findViewById(R.id.headerTextView);

        m_buttonClear = findViewById(R.id.clearButton);
        m_buttonTestSet = findViewById(R.id.testSetButton);
        m_buttonRun = findViewById(R.id.runButton);
        m_buttonOpen = findViewById(R.id.openButton);
        m_buttonClose = findViewById(R.id.closeButton);

        m_editTextIpek = findViewById(R.id.inputEditTextIpek);
        m_editTextInput = findViewById(R.id.inputEditText);
        m_textViewResult = findViewById(R.id.resultTextView);

        m_textViewHeader.setText("lpu237 description test");

        // IntentFilter 생성 및 등록
        IntentFilter filter = new IntentFilter(LPU237_PERMISSION);
        filter.addAction(SET_TEXT_TO_RESULT);
        filter.addAction(SET_ENTEXT_TO_RESULT);
        registerReceiver(myReceiver, filter);
        // EditText에 포커스 이벤트 리스너 추가
        m_editTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 가상 키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        });
        m_editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력 중
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 후
                int charCount = s.length(); // 입력된 문자 수 계산
                m_textViewResult.setText("CNT: " + charCount); // TextView에 표시
            }
        });

        m_buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_editTextInput.getText().clear();
                m_textViewResult.setText("");
            }
        });

        m_buttonTestSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_editTextInput.getText().clear();
                m_editTextInput.setText(ConstTest.CONST_TEST_KEYBOARD_RSP);

            }
        });
        // 버튼 클릭 리스너 설정
        m_buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 텍스트 가져오기
                String inputText = m_editTextInput.getText().toString();

                m_textViewResult.setText("Please waiting. Setup device");

                executorServiceOpen = Executors.newSingleThreadExecutor();
                mainHandlerOpen = new Handler(Looper.getMainLooper());

                startLongRunningTaskForOpen(inputText);

            }
        });


        m_buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_curHandle!= null ) {
                    m_b_canceled = true;
                    ApiLpu237.getInstance().CancelWait(m_curHandle);
                    ApiLpu237.getInstance().EnableMsr(m_curHandle,false);
                    ApiLpu237.getInstance().Close(m_curHandle);
                }
                m_textViewResult.setText("Close device");

            }
        });

        m_buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDeKey = m_editTextIpek.getText().toString();
                if( sDeKey.isEmpty()){
                    m_s_ipek = ConstTest.CONST_TEST_IPEK;
                }
                else{
                    if( !sDeKey.matches("[0-9a-fA-F]+") ){
                        //eror
                        Toast.makeText(getApplicationContext(), "invalied Decryption key", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(sDeKey.length()>32){
                        sDeKey = sDeKey.substring(0,32);
                    }
                    else if (sDeKey.length() < 32) {
                        // 길이가 32보다 작으면, 뒤에 '0'을 추가하여 길이를 32로 만듦
                        StringBuilder sb = new StringBuilder(sDeKey);
                        while (sb.length() < 32) {
                            sb.append('0');
                        }
                        sDeKey = sb.toString();
                    }
                    m_s_ipek[0] = sDeKey;
                }

                m_editTextIpek.setText(m_s_ipek[0]);

                String inputText = m_editTextInput.getText().toString();

                m_textViewResult.setText("Please waiting calculating");

                executorServiceDe = Executors.newSingleThreadExecutor();
                mainHandlerDe = new Handler(Looper.getMainLooper());

                startLongRunningTask(inputText);

            }
        });

    }

    private void _mustBeCalledBeforeExit() {
        m_b_canceled = true;

        if(m_curHandle !=null && !m_curHandle.is_empty()){
            ApiLpu237.getInstance().CancelWait(m_curHandle);
            ApiLpu237.getInstance().EnableMsr(m_curHandle,false);
            ApiLpu237.getInstance().Close(m_curHandle);
            m_curHandle = null;
        }
        ApiLpu237.getInstance().Off();
    }

    private void startLongRunningTaskForOpen(String s_input) {
        executorServiceOpen.execute(new Runnable() {
            @Override
            public void run() {
                final String result = performLongRunningCalculationForOpen(s_input);

                mainHandlerOpen.post(new Runnable() {
                    @Override
                    public void run() {
                        m_textViewResult.setText(result);
                    }
                });
            }
        });
    }
    private String performLongRunningCalculationForOpen(String s_input) {
        StringBuilder s_info = new StringBuilder();

        do {
            if (ApiLpu237.getInstance().On(getApplication())) {
                s_info.append("OK : On().");
                s_info.append("\n");
            } else {
                s_info.append("ER : On().");
                continue;
            }

            ArrayList<UsbDevHandle> al_devices = ApiLpu237.getInstance().GetList();
            if(al_devices.isEmpty()){
                s_info.append("None device.");
                continue;
            }

            for( int i =0; i<al_devices.size(); i++ ){
                s_info.append(al_devices.get(i).get_path());
                s_info.append("\n");
            }//end for


            m_curHandle = al_devices.get(0);
            if( !m_usbManager.hasPermission(m_curHandle.get_usbdevice())) {
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(
                                this
                                , 0
                                , new Intent(LPU237_PERMISSION)
                                , PendingIntent.FLAG_MUTABLE
                        );
                m_usbManager.requestPermission(m_curHandle.get_usbdevice(), pendingIntent);

                s_info.append("Waits Permission\n");
                continue;
            }

            if(!_open_setup()){
                s_info.append("Fail Open setup\n");
                continue;
            }
            s_info.append("OK : Open setup\n");
        }while(false);

        return s_info.toString();
    }

    private void startLongRunningTask(String s_input) {
        executorServiceDe.execute(new Runnable() {
            @Override
            public void run() {
                final String result = performLongRunningCalculation(s_input);

                mainHandlerDe.post(new Runnable() {
                    @Override
                    public void run() {
                        m_textViewResult.setText(result);
                    }
                });
            }
        });
    }

    private String performLongRunningCalculation(String s_input) {
        m_tag_value = new ParserTagValue(s_input);//parsing
        String s_info = _getParsedInfo();

        do{
            if(!m_tag_value.is_parsable()) {
                continue;
            }
            String[] s_ipek = m_s_ipek;

            byte[][] ipek = new byte [2][16];
            System.arraycopy(Tools.get_binary_from_hex_string(s_ipek[0]),0,ipek[0],0,ipek[0].length);
            System.arraycopy(Tools.get_binary_from_hex_string(s_ipek[1]),0,ipek[1],0,ipek[1].length);

            DukptTdes dukpt = new DukptTdes(m_tag_value.get_ksn(),ipek);
            if(!dukpt.is_setup_ok()){
                s_info = s_info + "\n dukpt error.";
                continue;
            }

            byte[] raw_data_except_mac = m_tag_value.get_raw_data_except_mac(true,true);
            byte[] mac4 = dukpt.generate_mac_4bytes(raw_data_except_mac);//calculate MAC4
            s_info = s_info + "\n calculate MAC4 = " + Tools.get_hex_string_from_binary(mac4);

            // compare the calculated MAC4 and the given MAC4
            if(!Tools.is_equal_bytes(mac4,m_tag_value.get_mac4())) {
                s_info = s_info + "\n Mismatch MAC! the given message may be sent from unauthenticated device.";
                continue;
            }

            // decryption card data.
            String s_iv = ConstTest.CONST_IV;
            byte[] iv = Tools.get_binary_from_hex_string(s_iv);
            byte[] en_key = dukpt.get_encrypt_key();

            for (int i = 0; i < 3; i++) {
                byte[] en_iso = m_tag_value.get_encrypted_track_data(i);//get plaintext length + encrypted track data.
                if (en_iso.length <= 0) {
                    s_info = s_info + "\n ISO" + String.valueOf(i + 1) + " = NONE";
                    continue;
                }

                int n_iso = en_iso[0];//get signed char type. the size of decrypted iso track data
                if (n_iso <= 0) {
                    s_info = s_info + "\n ISO" + String.valueOf(i + 1) + " = NONE";
                }

                byte[] en_iso_except_len = new byte[en_iso.length - 1];
                System.arraycopy(en_iso, 1, en_iso_except_len, 0, en_iso_except_len.length);

                byte[] iso = DukptTdes.decrypt3DESCBC(en_key, iv, en_iso_except_len);

                //remove padding data from decrypted data.
                byte[] raw_iso = new byte[n_iso];
                System.arraycopy(iso, 0, raw_iso, 0, raw_iso.length);

                String s_iso = new String(raw_iso);
                s_info = s_info + "\n ISO" + String.valueOf(i + 1) + s_iso;
            }//end for

        }while(false);
        return s_info;
    }

    private boolean _open_setup(){
        boolean bResult = false;

        do{
            if( m_curHandle == null ){
                continue;
            }
            m_curHandle = ApiLpu237.getInstance().Open(m_curHandle.get_path());
            if( m_curHandle.is_empty() ){
                continue;
            }
            if( !ApiLpu237.getInstance().SetupForRead(m_curHandle,true) ){
                continue;
            }
            if(!ApiLpu237.getInstance().EnableMsr(m_curHandle,true)){
                continue;
            }

            if(!ApiLpu237.getInstance().WaitMsrOriButtonWithCallback(m_curHandle,m_cb_read)){
                continue;
            }

            bResult = true;
        }while(false);
        return bResult;

    }

    @Override
    protected void onDestroy() {
        _mustBeCalledBeforeExit();
        super.onDestroy();
        unregisterReceiver(myReceiver);
        executorServiceDe.shutdown();
        executorServiceOpen.shutdown();
    }

    private String _getParsedInfo(){
        StringBuilder s_info = new StringBuilder();

        if(m_tag_value.is_parsable() ){
            s_info.append("KSN : ");
            s_info.append(m_tag_value.get_ksn_by_string());
            s_info.append("\n");

            s_info.append("Masked PAN : ");
            s_info.append(m_tag_value.get_mpan_by_string());
            s_info.append("\n");

            s_info.append("Card Holder Name : ");
            s_info.append(m_tag_value.get_chn_by_string());
            s_info.append("\n");

            s_info.append("Card Expiration Date : ");
            s_info.append(m_tag_value.get_ced_by_string());
            s_info.append("\n");

            s_info.append("Message Authentication Code 4bytes : ");
            s_info.append(m_tag_value.get_mac4_by_string());
            s_info.append("\n");
            for(int i=0; i<3; i++) {
                s_info.append("Encrypted ISO");
                s_info.append(i+1);
                s_info.append(" : ");
                s_info.append(m_tag_value.get_encrypted_track_data_by_string(i));
                s_info.append("\n");
            }
        }
        else {
            // TextView에 텍스트 설정하기
            s_info.append("Parssing error");
        }
        return s_info.toString();
    }

    void _send_broadercast(String s ){
        Intent intent = new Intent(SET_TEXT_TO_RESULT );
        intent.putExtra(STR_KEY_SET_TEXT_TO_RESULT, s);
        this.sendBroadcast(intent);
    }
    void _send_broadercast_encrypted(String s ){
        Intent intent = new Intent(SET_ENTEXT_TO_RESULT );
        intent.putExtra(STR_KEY_SET_ENTEXT_TO_RESULT, s);
        this.sendBroadcast(intent);
    }

}