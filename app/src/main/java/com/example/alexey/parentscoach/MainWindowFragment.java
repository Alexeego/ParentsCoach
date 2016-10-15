package com.example.alexey.parentscoach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.LinkedList;
import java.util.Map;


public class MainWindowFragment extends Fragment {


    ListView listRays;
    static LinkedList<Map<String, Object>> data = null;
    static SimpleAdapter simpleAdapterForRays = null;
    private static Integer positionRays = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_window, container, false);

        listRays = (ListView)view.findViewById(R.id.listView);
        data = new LinkedList<>();

//        simpleAdapterForRays = new SimpleAdapter(MainActivity.context, data, R.layout.item_list_rays, new String[]{}, new int[]{}) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View item = convertView;
//                if(item == null){
//                    item = getActivity().getLayoutInflater().inflate(R.layout.item_list_rays, parent, false);
//                }
//                Ray ray = MainActivity.rays.get(position);
//                ((TextView) item.findViewById(R.id.itemId))
//                        .setText(String.valueOf((int)ray.id));
//                ((TextView) item.findViewById(R.id.itemCoordinates))
//                        .setText(ray.coordinates.country + ", " + ray.coordinates.city);
//                ((TextView) item.findViewById(R.id.itemDateSending))
//                        .setText("Дата отправления:\n" + new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(new Date(ray.timeSending.getTime() - 3600000)));
//                ((TextView) item.findViewById(R.id.itemTimeInWay))
//                        .setText("Время в пути:\n" + new SimpleDateFormat("HH:mm:ss").format(new Date(ray.timeInWay - 10800000)));
//                TextView status = (TextView) item.findViewById(R.id.itemStatus);
//                status.setText(ray.stateRay.toString());
//                try{
//                    status.setTextColor(getResources().getColor(colors[ray.stateRay.ordinal()]));
//                } catch (Exception ignore){}
//                return item;
//            }
//        };
//        listRays.setAdapter(simpleAdapterForRays);
//        listRays.setSelection(positionRays);
//        listRays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (MainActivity.rays != null) {
//                    try {
//                        selectedRay = null;
//                        for (Ray ray : MainActivity.rays) {
//                            if (ray.id == Integer.parseInt(((TextView) view.findViewById(R.id.itemId)).getText().toString())) {
//                                selectedRay = ray;
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                MainActivity.stackFragments.push(MainActivity.fragment);
//                                MainActivity.fragment = new ItemListRaysFragment();
//                                fragmentManager.beginTransaction().
//                                        replace(R.id.content_frame, MainActivity.fragment).commit();
//                                break;
//                            }
//                        }
//                    } catch (Exception ignored) {}
//                }
//            }
//        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        positionRays = listRays.getFirstVisiblePosition();
    }
}
