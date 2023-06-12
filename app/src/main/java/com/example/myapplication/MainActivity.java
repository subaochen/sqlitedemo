package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button addBtn;
    Button queryAllBtn;
    Button queryOneBtn;
    EditText idField;
    EditText nameField;
    EditText ageField;
    EditText heightField;
    TextView resultField;


    DbAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DbAdapter(this);
        dbAdapter.open();

        addBtn = findViewById(R.id.addBtn);
        nameField = findViewById(R.id.nameField);
        ageField = findViewById(R.id.ageField);
        heightField = findViewById(R.id.heightField);
        resultField = findViewById(R.id.resultField);
        queryAllBtn = findViewById(R.id.queryAllBtn);
        queryOneBtn = findViewById(R.id.queryOneBtn);
        idField = findViewById(R.id.idField);

        queryOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(idField.getText().toString());
                Person p = dbAdapter.queryById(id);
                if(p != null){
                    resultField.setText(p.name + "," + p.age + "," + p.height);
                }
            }
        });

        queryAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                Person[] ps = dbAdapter.getAllData();
                for(Person p: ps){
                    result += p.name + "," + p.age + "," + p.height + "\n";
                }
                resultField.setText(result);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = new Person();
                p.name = nameField.getText().toString();
                p.age = Integer.parseInt(ageField.getText().toString());
                p.height = Float.parseFloat(heightField.getText().toString());
                dbAdapter.insert(p);
            }
        });

    }
}