package com.example.alexey.parentscoach;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.alexey.parentscoach.classes.Child;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.alexey.parentscoach.MainActivity.childes;
import static com.example.alexey.parentscoach.MainActivity.context;


public class MainWindowFragment extends Fragment {


    ListView listChildes;
    static LinkedList<Map<String, Object>> dataChildes = null;
    static SimpleAdapter simpleAdapterForChildes = null;
    private static Integer positionRays = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_window, container, false);

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
        }

        simpleAdapterForChildes = new SimpleAdapter(MainActivity.context, dataChildes, R.layout.item_list_tasks, new String[]{}, new int[]{}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View item = convertView;
                if(item == null){
                    item = getActivity().getLayoutInflater().inflate(R.layout.item_list_tasks, parent, false);
                }

                //TODO
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
                        //TODO
                    } catch (Exception ignored) {
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        positionRays = listChildes.getFirstVisiblePosition();
    }
}
