package com.example.alexey.parentscoach;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.alexey.parentscoach.classes.Child;
import com.example.alexey.parentscoach.classes.Task;
import com.example.alexey.parentscoach.utils.WordUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.alexey.parentscoach.MainActivity.childes;
import static com.example.alexey.parentscoach.MainActivity.context;
import static com.example.alexey.parentscoach.MainActivity.fragment;
import static com.example.alexey.parentscoach.MainActivity.socket;
import static com.example.alexey.parentscoach.MainActivity.stackFragments;
import static com.example.alexey.parentscoach.MainActivity.user;


public class MainWindowFragment extends Fragment {


    ListView listChildes;
    static LinearLayout lisLayout;
    static RelativeLayout emptyLayout;
    static LinkedList<Map<String, Object>> dataChildes = null;
    static SimpleAdapter simpleAdapterForChildes = null;
    private static Integer positionRays = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_window, container, false);

        emptyLayout = (RelativeLayout)view.findViewById(R.id.emptyLayout);
        lisLayout = (LinearLayout)view.findViewById(R.id.listLayout);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddChildActivity.class));
            }
        });

        listChildes = (ListView)view.findViewById(R.id.listView);
        dataChildes = new LinkedList<>();
        if(childes != null){
            for(Child child: childes){
                dataChildes.add(new HashMap<String, Object>());
            }
            if(childes.size()>0){
                emptyLayout.setVisibility(View.INVISIBLE);
                lisLayout.setVisibility(View.VISIBLE);
            } else {
                lisLayout.setVisibility(View.INVISIBLE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }

        simpleAdapterForChildes = new SimpleAdapter(MainActivity.context, dataChildes, R.layout.item_list, new String[]{}, new int[]{}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View item = convertView;
                if(item == null){
                    item = getActivity().getLayoutInflater().inflate(R.layout.item_list, parent, false);
                }
                Child child = childes.get(position);

                ((ImageView)item.findViewById(R.id.itemImageBig)).setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon));

                ((TextView)item.findViewById(R.id.itemTextTitle)).setText(child.getName());
                int countInProcess = 0;
                child.getTasks();
                for (Task task : child.getTasks()){
                    if(task.getState() == 0)
                        countInProcess++;
                }
                String resultState;
                if(countInProcess != 0){
                    StringBuilder builder = new StringBuilder("Ожидает ");
                    WordUtil.wordEdit(builder, countInProcess, "поступ", "ков", "ок", "ка");
                    resultState = builder.toString();

                    ((ImageView)item.findViewById(R.id.itemImageSmall)).setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
                } else {
                    resultState = "Всё сделано";
                    ((ImageView)item.findViewById(R.id.itemImageSmall)).setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
                }

                ((TextView)item.findViewById(R.id.itemTextState)).setText(resultState);

                return item;
            }
        };
        listChildes.setAdapter(simpleAdapterForChildes);
        listChildes.setSelection(positionRays);
        listChildes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.childes != null) {
                    try {
                        stackFragments.push(fragment);
                        fragment = new TasksCurrentChildrenFragment();
                        TasksCurrentChildrenFragment.currentTasks = childes.get(position).getTasks();
                        context.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        socket.emit("getChildes", "{\"token\":\"" + user.getToken() + "\"}");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        positionRays = listChildes.getFirstVisiblePosition();
    }
}
