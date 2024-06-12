package kr.co.elpusk.android.test_liblpu237;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText consoleInput;
    private static final String PROMPT = "cmd> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consoleInput = findViewById(R.id.id_main);

        consoleInput.setText(PROMPT);
        consoleInput.setSelection(consoleInput.getText().length());

        consoleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start < PROMPT.length()) {
                    consoleInput.setText(PROMPT);
                    consoleInput.setSelection(consoleInput.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });

        consoleInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String input = consoleInput.getText().toString().substring(PROMPT.length()).trim();
                if (!input.isEmpty()) {
                    showDialog(input);
                    consoleInput.setText(PROMPT);
                    consoleInput.setSelection(consoleInput.getText().length());
                }
                return true;
            }
            return false;
        });
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Input Received")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
