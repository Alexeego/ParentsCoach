package com.example.alexey.parentscoach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alexey.parentscoach.classes.User;

import static com.example.alexey.parentscoach.MainActivity.connectError;
import static com.example.alexey.parentscoach.MainActivity.socket;
import static com.example.alexey.parentscoach.MainActivity.user;
import static com.example.alexey.parentscoach.utils.ConnectionUtil.transformToJson;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {

    TextView authorizationText;
    TextView textRegistration;
    static EditText editNameAuthorization;
    static EditText editPasswordAuthorization;
    static Button buttonAuthorization;
    private static String name = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        authorizationText = (TextView) view.findViewById(R.id.authorizationText);
        textRegistration = (TextView)view.findViewById(R.id.textRegestration);
        textRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.connectRegistration();
                MainActivity.context.setEnterFragment();
            }
        });

        editNameAuthorization = (EditText) view.findViewById(R.id.editTextNameAuthorization);
        editPasswordAuthorization = (EditText) view.findViewById(R.id.editTextPasswordAuthorization);
        editNameAuthorization.setText(name);

        buttonAuthorization = (Button) view.findViewById(R.id.buttonAuthorization);
        buttonAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNameAuthorization.setEnabled(false);
                editPasswordAuthorization.setEnabled(false);
                buttonAuthorization.setEnabled(false);
                try {
                    user = new User(editNameAuthorization.getText().toString(), "", editPasswordAuthorization.getText().toString());
                    socket.emit("login", transformToJson(user));
                } catch (Exception e) {
                    connectError();
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
