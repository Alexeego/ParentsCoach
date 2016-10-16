package com.example.alexey.parentscoach;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.alexey.parentscoach.classes.Child;
import com.example.alexey.parentscoach.utils.ConnectionUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.alexey.parentscoach.MainWindowFragment.*;
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
                    } catch (Exception e) {
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
                    } catch (Exception e) {
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
                            socket.emit("getChildes", "{\"token\":\"" + user.getToken() + "\"}");

                        } else {
                            AddChildActivity.buttonAddNewChild.setEnabled(true);
                            Snackbar.make(context.getCurrentFocus(), "Ребёнок не был добавлен", Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
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
                            childes = ConnectionUtil.transformFromJson(new TypeReference<ArrayList<Child>>(){}, data.getString("childes"));
                            Snackbar.make(context.getCurrentFocus(), "У вас добавленно " + childes.size() + " детей", Snackbar.LENGTH_LONG).show();
                            dataChildes.clear();
                            for(Child child: childes){
                                dataChildes.add(new HashMap<String, Object>());
                            }
                            simpleAdapterForChildes.notifyDataSetChanged();

                            if(childes.size()>0){
                                emptyLayout.setVisibility(View.INVISIBLE);
                                listChildes.setVisibility(View.VISIBLE);
                            } else {
                                listChildes.setVisibility(View.INVISIBLE);
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            RegistrationFragment.buttonReg.setEnabled(true);
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
                        connectError();
                    }
                }
            });
        }
    };

}
