package com.example.alexey.parentscoach;

import android.support.design.widget.Snackbar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alexey.parentscoach.MainActivity.*;
import static com.github.nkzawa.emitter.Emitter.Listener;

/**
 * Created by Alexey on 15.10.2016.
 */
public class EventsList {

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
                        if (error.equals("0")) {
                            String token = data.getString("token");
                            user.setToken(token);
                            connectSuccess();
                            context.setEnterFragment();
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
    static Listener onSignUp = new Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String error = data.getString("error");
                        if (error.equals("0")) {
                            String token = data.getString("token");
                            user.setToken(token);
                            connectSuccess();
                            Snackbar.make(context.getCurrentFocus(), "Регистрация прошла успешно", Snackbar.LENGTH_SHORT).show();
                            context.setEnterFragment();
                        } else {
                            RegistrationFragment.buttonReg.setEnabled(true);
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        connectError();
                    }
                }
            });
        }
    };


    static Listener onAddChild = new Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String error = data.getString("error");
                        if (error.equals("0")) {
                            Snackbar.make(context.getCurrentFocus(), "Ребёнок был добавлен", Snackbar.LENGTH_SHORT).show();
                        } else {
                            RegistrationFragment.buttonReg.setEnabled(true);
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        connectError();
                    }
                }
            });
        }
    };

    static Listener onGetChildes = new Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String error = data.getString("error");
                        if (error.equals("0")) {
                            Snackbar.make(context.getCurrentFocus(), data.getString("childes"), Snackbar.LENGTH_LONG).show();
                        } else {
                            RegistrationFragment.buttonReg.setEnabled(true);
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
