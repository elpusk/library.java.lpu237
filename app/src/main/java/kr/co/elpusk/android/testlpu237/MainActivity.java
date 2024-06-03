package kr.co.elpusk.android.testlpu237;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.elpusk.android.liblpu237.DeviceLpu237;

interface ConstTest{
    String CONST_IV = "0000000000000000";//unchangeable initial vector of CBC mode.
    String CONST_TEST_KEYBOARD_RSP = "this is input test";
    String[] CONST_TEST_IPEK ={
            "6AC292FAA1315B4D858AB3A3D7D5933A",
            "00000000000000000000000000000000"
    };
}

public class MainActivity extends AppCompatActivity {

    private EditText m_editTextInput;
    private Button m_buttonRun, m_buttonTestSet, m_buttonClear;
    private TextView m_textViewResult, m_textViewHeader;

    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText, Button, TextView 초기화
        m_textViewHeader = findViewById(R.id.headerTextView);

        m_buttonClear = findViewById(R.id.clearButton);
        m_buttonTestSet = findViewById(R.id.testSetButton);
        m_buttonRun = findViewById(R.id.runButton);

        m_editTextInput = findViewById(R.id.inputEditText);
        m_textViewResult = findViewById(R.id.resultTextView);

        m_textViewHeader.setText("lpu237 description test");

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
        m_buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 텍스트 가져오기
                String inputText = m_editTextInput.getText().toString();

                m_textViewResult.setText("Please waiting calculating");

                executorService = Executors.newSingleThreadExecutor();
                mainHandler = new Handler(Looper.getMainLooper());

                startLongRunningTask(inputText);

            }
        });
    }

    private void startLongRunningTask(String s_input) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final String result = performLongRunningCalculation(s_input);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_textViewResult.setText(result);
                    }
                });
            }
        });
    }

    private String performLongRunningCalculation(String s_input) {
        String s_info = _getParsedInfo();

        do{
        }while(false);
        return s_info;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
    private String _getParsedInfo(){
        StringBuilder s_info = new StringBuilder();
        s_info.append(DeviceLpu237.getFirstTest());
        s_info.append("\n");
        s_info.append("test parsing : OK.");

        return s_info.toString();
    }
}