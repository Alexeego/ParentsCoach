package com.example.alexey.parentscoach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexey.parentscoach.classes.User;
import com.example.alexey.parentscoach.utils.ConnectionUtil;

import static com.example.alexey.parentscoach.MainActivity.connectError;
import static com.example.alexey.parentscoach.MainActivity.socket;
import static com.example.alexey.parentscoach.MainActivity.user;
import static com.example.alexey.parentscoach.utils.ConnectionUtil.transformToJson;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {

    private EditText editNameAuthorization;
    static Button buttonAuthorization;
    static Button buttonReg;
    private static String name = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        final LinearLayout layoutReg = (LinearLayout)view.findViewById(R.id.linearLayoutRegistration);
        final LinearLayout layoutAuth = (LinearLayout)view.findViewById(R.id.linearLayoutAuthorization);

        TextView authorizationText = (TextView) view.findViewById(R.id.textAuthorization);
        authorizationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutAuth.setVisibility(View.VISIBLE);
                layoutReg.setVisibility(View.INVISIBLE);
            }
        });
        TextView textRegistration = (TextView)view.findViewById(R.id.textRegestration);
        textRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutAuth.setVisibility(View.INVISIBLE);
                layoutReg.setVisibility(View.VISIBLE);
            }
        });

        editNameAuthorization = (EditText) view.findViewById(R.id.editTextNameAuthorization);
        final EditText editPasswordAuthorization = (EditText) view.findViewById(R.id.editTextPasswordAuthorization);
        editNameAuthorization.setText(name);

        buttonAuthorization = (Button) view.findViewById(R.id.buttonAuthorization);
        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAuthorization.setEnabled(false);
                try {
                    user = new User(editNameAuthorization.getText().toString(), "", editPasswordAuthorization.getText().toString());
                    socket.emit("login", transformToJson(user));
                } catch (Exception e) {
                    connectError();
                }
            }
        });

        final EditText editNameReg = (EditText) view.findViewById(R.id.editTextNameReg);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        name = editNameAuthorization.getText().toString();
    }

}
