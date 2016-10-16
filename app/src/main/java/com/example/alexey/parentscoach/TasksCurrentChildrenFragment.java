package com.example.alexey.parentscoach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.alexey.parentscoach.classes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksCurrentChildrenFragment extends Fragment {

    static ArrayList<Task> currentTasks;
    static LinkedList<Map<String, Object>> dataTasks = null;
    static SimpleAdapter simpleAdapterForTasks = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_current_children, container, false);

        ListView listViewTasks = (ListView)view.findViewById(R.id.listViewTasks);
        dataTasks = new LinkedList<>();
        dataTasks.add(new HashMap<String, Object>());
        if(currentTasks != null){
            for(Task task: currentTasks){
                dataTasks.add(new HashMap<String, Object>());
            }
        }

        simpleAdapterForTasks = new SimpleAdapter(MainActivity.context, dataTasks, R.layout.item_list, new String[]{}, new int[]{}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View item;
                if(position == 0){
                    item = getActivity().getLayoutInflater().inflate(R.layout.after_item, parent, false);
                } else {
                    item = getActivity().getLayoutInflater().inflate(R.layout.item_list, parent, false);
                    Task task = currentTasks.get(position - 1);
                    ((ImageView) item.findViewById(R.id.itemImageBig)).setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon));

                    ((TextView) item.findViewById(R.id.itemTextTitle)).setText(task.getTitle());

                    if (task.getState() == 0) {
                        ((ImageView) item.findViewById(R.id.itemImageSmall)).setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
                    } else if (task.getState() == -1) {
                        ((ImageView) item.findViewById(R.id.itemImageSmall)).setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
                    } else if (task.getState() == 1) {
                        ((ImageView) item.findViewById(R.id.itemImageSmall)).setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    }

                    ((TextView) item.findViewById(R.id.itemTextState)).setText("1 балл");
                }
                return item;
            }
        };
        listViewTasks.setAdapter(simpleAdapterForTasks);

        return view;
    }

}
