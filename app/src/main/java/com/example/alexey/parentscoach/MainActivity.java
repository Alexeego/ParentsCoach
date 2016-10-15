package com.example.alexey.parentscoach;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexey.parentscoach.user.User;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.util.Date;
import java.util.Stack;

import static com.example.alexey.parentscoach.EventsList.onDisconnect;
import static com.example.alexey.parentscoach.EventsList.hello;
import static com.example.alexey.parentscoach.EventsList.onLogin;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static MainActivity context;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //////////////////////////////////

        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        initDialog();
        setEnterFragment();
        tryConnectOrCreateDialog();

    }

    EditText editIp;
    EditText editPort;
    AlertDialog alertDialog;

    private void initDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Не удалось подключиться к серверу по данным параметрам");
        View view = getLayoutInflater().inflate(R.layout.connection_dialog, null);
        editIp = (EditText) view.findViewById(R.id.editIP);
        editPort = (EditText) view.findViewById(R.id.editPort);
        editIp.setText(preferences.getString("ip", ""));
        editPort.setText(preferences.getString("port", ""));
        view.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                new AsyncConnect().execute(editIp.getText().toString(), editPort.getText().toString()); // Asynchronous connection
            }
        });
        view.findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setView(view);
        dialog.setCancelable(false);
        alertDialog = dialog.create();
    }

    void tryConnectOrCreateDialog() {
        if (preferences.getBoolean("autoConnect", false)) {
            new AsyncConnect().execute(preferences.getString("ip", ""), preferences.getString("port", ""));
        } else {
            alertDialog.show();
        }
    }


    class AsyncConnect extends AsyncTask<String, String, Socket> {

        private ProgressDialog progressDialog;
        private volatile boolean work = true;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Подключение к серверу");
            progressDialog.setMessage("Пожалуйста подождите");
            progressDialog.setCancelable(false);
            progressDialog.setButton(Dialog.BUTTON_POSITIVE, "Прервать", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    work = false;
                    Toast.makeText(context, "qwe", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.dismiss();
            progressDialog.show();
        }

        @Override
        protected Socket doInBackground(String... params) {
            if (params.length != 2) return null;
            try {
                Socket socket = null;
                outer:
                do {
                    if (socket != null)
                        deleteListenersServer(socket);
                    socket = IO.socket("http://" + params[0] + ":" + params[1]);
                    addListenersServer(socket);
                    socket.connect();
                    Date first = new Date();
                    while (work && !Thread.currentThread().isInterrupted() && !socket.connected()) {
                        if (first.getTime() + 3000 < new Date().getTime())
                            continue outer;
                        Thread.yield();
                    }
                    if (!work)
                        throw new Exception();
                    else break;
                } while (true);
                return socket;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Socket socket) {
            progressDialog.dismiss();
            try {
                if (socket != null) {
                    Toast.makeText(MainActivity.context, "Соединение установленно", Toast.LENGTH_SHORT).show();
                    MainActivity.socket = socket;
                    connectAuthorization();
                    return;
                }
            } catch (Exception ignored) {}
            Toast.makeText(MainActivity.context, "Соединение не удалось установить", Toast.LENGTH_SHORT).show();
            alertDialog.show();
        }

        private void addListenersServer(Socket socket) throws Exception {
            socket.on("hello", hello);
            socket.on("login", onLogin);
            socket.on("disconnect", onDisconnect);
        }
    }

    private static void deleteListenersServer(Socket socket) {
        socket.disconnect();
        socket.off("hello", hello);
        socket.off("onLogin", onLogin);
        socket.off("onDisconnect", onDisconnect);
    }


    enum ConnectionState {
        TRY_CONNECTION,
        AUTHORIZATION,
        REGISTRATION,
        CONNECT
    }

    static ConnectionState nowConnectionState = ConnectionState.TRY_CONNECTION;
    static Stack<Fragment> stackFragments = new Stack<>();
    static Fragment fragment = null;
    static User user = null;
    static Socket socket = null;

    static synchronized void connectError() {
        if (nowConnectionState != ConnectionState.TRY_CONNECTION) {
            nowConnectionState = ConnectionState.TRY_CONNECTION;
            if (socket != null) {
                deleteListenersServer(socket);
            }
            socket = null;
            stackFragments.clear();
            context.setEnterFragment();
            context.tryConnectOrCreateDialog();
            Toast.makeText(MainActivity.context, "Соединение разорвано", Toast.LENGTH_SHORT).show();
        }
    }
    static void connectRegistration() {
        nowConnectionState = ConnectionState.REGISTRATION;
    }
    static void connectAuthorization() {
        nowConnectionState = ConnectionState.AUTHORIZATION;
    }
    static void connectSuccess() {
        nowConnectionState = ConnectionState.CONNECT;
    }

    void setEnterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (nowConnectionState) {
            case TRY_CONNECTION: {
            }
            case AUTHORIZATION: {
                fragment = new ConnectionFragment();
                break;
            }
            case REGISTRATION: {
                fragment = new RegistrationFragment();
                break;
            }
            case CONNECT: {
                fragment = new MainWindowFragment();
                break;
            }
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (nowConnectionState == ConnectionState.CONNECT && fragment instanceof MainWindowFragment) {
                connectError();
            } else if (nowConnectionState == ConnectionState.REGISTRATION && fragment instanceof RegistrationFragment) {
                connectAuthorization();
                setEnterFragment();
            } else if (!stackFragments.isEmpty()) {
                fragment = stackFragments.pop();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            } else super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nowConnectionState = ConnectionState.TRY_CONNECTION;
        if (socket != null) {
            deleteListenersServer(socket);
            socket = null;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all_tasks:
                stackFragments.clear();
                setEnterFragment();
                break;
            case R.id.nav_my_tasks:
                if (nowConnectionState == ConnectionState.CONNECT) {
                    //TODO
                } else
                    Toast.makeText(context, "Необходимо аворизоваться", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage:
                startActivity(new Intent(this, PrefActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
