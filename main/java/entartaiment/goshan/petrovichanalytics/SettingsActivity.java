package entartaiment.goshan.petrovichanalytics;

import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
 private EditText mPercent;
    private CheckBox mCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mPercent=(EditText)findViewById(R.id.settings_edittext);
        mCheckBox=(CheckBox)findViewById(R.id.settings_checkbox);

        mPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                   ShareArray.getShareArray(SettingsActivity.this).updatePercent(s.toString());

                }
                catch(Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) ShareArray.getShareArray(SettingsActivity.this).updateNotificcation("true");
                else ShareArray.getShareArray(SettingsActivity.this).updateNotificcation("false");
            }
        });
    }
}
