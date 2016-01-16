package com.example.ric.mydiary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.EventAdapter;

import java.util.ArrayList;

public class MainSearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private ProgressBar loader;
    EventsDataSource mydb;
    private View rootView;
    private Context context;
    FloatingActionButton searchByCategoryButton;
    private EventAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        this.context = getActivity();
        mydb = new EventsDataSource(context);

        searchByCategoryButton = (FloatingActionButton) rootView.findViewById(R.id.btn_search);
        searchByCategoryButton.setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.list_of_found_events);
        loader = (ProgressBar) rootView.findViewById(R.id.pb_loader);

        listView.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_search: {
                loaderAnimation();

                ArrayList<Event> list = mydb.getEventsByCategory(Category.Birthday);
                adapter = new EventAdapter(context, list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
                break;
            }
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event event = adapter.getItem(position);

        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra("id", event.getId());
        intent.putExtra("SENDER_CLASS_NAME", this.getClass());

        startActivity(intent);
    }

    private void loaderAnimation() {
        listView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loader.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener((new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loader.setVisibility(View.GONE);

                        listView.animate()
                                .translationY(0)
                                .alpha(1)
                                .setDuration(500);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }));
    }
}