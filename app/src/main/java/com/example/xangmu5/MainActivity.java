package com.example.xangmu5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText1,editText2;
    CheckBox checkBox1;
    Button button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.editTextTextPersonName);
        editText2 = findViewById(R.id.editTextTextPassword);
        checkBox1 = findViewById(R.id.checkBox);
        button1 =findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.setText("");
                editText2.setText("");
            }
        });
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=getSharedPreferences("myfile",0).edit();
                editor.putString("name",editText1.getText().toString());
                editor.putString("pwd",editText2.getText().toString());
                editor.putBoolean("st",checkBox1.isChecked());
                editor.commit();
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });
        String myname1 = getSharedPreferences("myfile",0).getString("name","");
        String myname2 = getSharedPreferences("myfile",0).getString("pwd","");
        Boolean myname3 = getSharedPreferences("myfile",0).getBoolean("st",false);
        if (myname3==true){
            editText1.setText(myname1);
            editText2.setText(myname2);
            checkBox1.setChecked(true);
        }else {
            editText1.setText("");
            editText2.setText("");
            checkBox1.setChecked(false);
        }
    }

}