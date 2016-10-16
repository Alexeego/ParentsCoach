package com.example.alexey.parentscoach;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.alexey.parentscoach.classes.Child;

import static com.example.alexey.parentscoach.MainActivity.connectError;
import static com.example.alexey.parentscoach.MainActivity.socket;
import static com.example.alexey.parentscoach.MainActivity.user;
import static com.example.alexey.parentscoach.utils.ConnectionUtil.transformToJson;

public class AddChildActivity extends AppCompatActivity {

    static Button buttonAddNewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Мальчик", "Девочка"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerSexAddChild);
        spinner.setPrompt("Кто это");
        spinner.setAdapter(adapter);

        buttonAddNewChild = (Button) findViewById(R.id.buttonAddNewChild);
        buttonAddNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddNewChild.setEnabled(false);
                String name = ((EditText) findViewById(R.id.editNameAddChild)).getText().toString();

                String age = ((EditText) findViewById(R.id.editAgeAddChild)).getText().toString();
                if (!name.trim().equals("") && age.matches("^\\d{1,2}$"))
                    try {
                        socket.emit("addChild", "{\"token\" : \"" + user.getToken() + "\", \"child\" : " + transformToJson(new Child(name, spinner.getSelectedItemPosition() == 0, Integer.parseInt(age))) + "}");
                        finish();
                    } catch (Exception e) {
                        connectError();
                    }
                else {
                    buttonAddNewChild.setEnabled(true);
                    Snackbar.make(AddChildActivity.this.getCurrentFocus(), "Заполните графу с именем и возрастом", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

}
