package com.example.alexey.parentscoach;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alexey.parentscoach.MainActivity.connectError;
import static com.example.alexey.parentscoach.MainActivity.context;
import static com.example.alexey.parentscoach.MainActivity.user;
import static com.github.nkzawa.emitter.Emitter.Listener;

/**
 * Created by Alexey on 15.10.2016.
 */
public class EventsList {
    static Listener hello = new Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "hello " + args[0], Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    static Listener onDisconnect = new Listener() {
        @Override
        public void call(Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectError();
                }
            });
        }
    };
    static Listener onLogin = new Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String error = data.getString("error");
                        if(error.equals("0")){
                            String token = data.getString("token");
                            user.setToken(token);
                            MainActivity.connectSuccess();
                            MainActivity.context.setEnterFragment();
                        } else {
                            ConnectionFragment.editNameAuthorization.setEnabled(true);
                            ConnectionFragment.editPasswordAuthorization.setEnabled(true);
                            ConnectionFragment.buttonAuthorization.setEnabled(true);
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        connectError();
                    }
                }
            });
        }
    };


}
