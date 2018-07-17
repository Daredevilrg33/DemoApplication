package com.example.rohitgupta3.demoapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SetPinScreen extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin_screen);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText8 = (EditText) findViewById(R.id.editText8);
        editText1.addTextChangedListener(new MyTextWatcher());
        editText2.addTextChangedListener(new MyTextWatcher());
        editText3.addTextChangedListener(new MyTextWatcher());
        editText4.addTextChangedListener(new MyTextWatcher());
        editText5.addTextChangedListener(new MyTextWatcher());
        editText6.addTextChangedListener(new MyTextWatcher());
        editText7.addTextChangedListener(new MyTextWatcher());
        editText8.addTextChangedListener(new MyTextWatcher());

    }


    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText1.getText().toString().trim().length() > 0 && (editText2.getText().toString().trim().length() == 0 || editText3.getText().toString().trim().length() == 0) && (editText1.hasFocus() || editText3.hasFocus())) {
                editText2.requestFocus();
                return;
            }
            if (editText2.getText().toString().trim().length() > 0 && editText4.getText().toString().trim().length() == 0 && (editText2.hasFocus() || editText4.hasFocus())) {
                editText3.requestFocus();
                return;
            }
            if (editText3.getText().toString().trim().length() > 0 && editText5.getText().toString().trim().length() == 0 && (editText3.hasFocus() || editText5.hasFocus())) {
                editText4.requestFocus();
                return;
            }
            if (editText4.getText().toString().trim().length() > 0 && editText6.getText().toString().trim().length() == 0 && (editText4.hasFocus() || editText6.hasFocus())) {
                editText5.requestFocus();
                return;
            }
            if (editText5.getText().toString().trim().length() > 0 && editText7.getText().toString().trim().length() == 0 && (editText5.hasFocus() || editText7.hasFocus())) {
                editText6.requestFocus();
                return;
            }
            if (editText6.getText().toString().trim().length() > 0 && editText8.getText().toString().trim().length() == 0 && (editText6.hasFocus() || editText8.hasFocus())) {
                editText7.requestFocus();
                return;
            }
            if (editText7.getText().toString().trim().length() > 0 && editText7.hasFocus()) {
                editText8.requestFocus();
                return;
            }
            if (editText2.getText().toString().trim().length() == 0 && editText2.hasFocus()) {
                editText1.requestFocus();
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
