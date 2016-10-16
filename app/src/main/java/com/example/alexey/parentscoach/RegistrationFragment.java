package com.example.alexey.parentscoach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alexey.parentscoach.classes.User;
import com.example.alexey.parentscoach.utils.ConnectionUtil;

import static com.example.alexey.parentscoach.MainActivity.user;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    static EditText editNameReg;
    static Button buttonReg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        editNameReg = (EditText) view.findViewById(R.id.editTextNameReg);
        final EditText passwordFirst = (EditText) view.findViewById(R.id.editTextPasswordRegFirst);
        final EditText passwordSecond = (EditText) view.findViewById(R.id.editTextPasswordRegSecond);

        buttonReg = (Button) view.findViewById(R.id.buttonReg);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordFirst.getText().toString().equals(passwordSecond.getText().toString())) {
                    buttonReg.setEnabled(false);
                    try {
                        user = new User(editNameReg.getText().toString(), editNameReg.getText().toString(), passwordFirst.getText().toString());
                        MainActivity.socket.emit("signup", ConnectionUtil.transformToJson(user));
                    } catch (Exception e) {
                        MainActivity.connectError();
                    }
                }
            }
        });
        return view;
    }

}
