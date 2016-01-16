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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.ric.mydiary.Database.Category;
import com.example.ric.mydiary.Database.Event;
import com.example.ric.mydiary.Database.EventsDataSource;
import com.example.ric.mydiary.HelperClasses.DateSetter;
import com.example.ric.mydiary.HelperClasses.EventAdapter;

import java.util.ArrayList;

public class MainSearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText searchedInput;
    private Spinner searchedCategory;

    private ListView listView;
    private ProgressBar loader;
    EventsDataSource mydb;
    private View rootView;
    private Context context;
    FloatingActionButton searchByCategoryButton;
    private EventAdapter adapter;
    private RadioGroup rg;
    ArrayList<Event> list;
    private String searchedItem;
    private String checked;
    DateSetter dateSetter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        this.context = getActivity();
        mydb = new EventsDataSource(context);

        searchedInput = (EditText) rootView.findViewById(R.id.editText);
        searchedCategory = (Spinner) rootView.findViewById(R.id.sp_categories_to_search);
        searchedCategory.setAdapter(new ArrayAdapter<Category>(context, android.R.layout.simple_spinner_dropdown_item, Category.values()));
        searchByCategoryButton = (FloatingActionButton) rootView.findViewById(R.id.btn_search);
        searchByCategoryButton.setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.list_of_found_events);
        loader = (ProgressBar) rootView.findViewById(R.id.pb_loader);
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(this);


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
                checked = ((RadioButton) rootView.findViewById(rg.getCheckedRadioButtonId())).getText().toString();;
                getDataFromDb(checked);
                adapter = new EventAdapter(context, this.list);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_title:
                searchedCategory.setVisibility(View.GONE);
                searchedInput.setVisibility(View.VISIBLE);
                searchedInput.setText("");
                break;

            case R.id.radio_category:
                searchedCategory.setVisibility(View.VISIBLE);
                searchedInput.setVisibility(View.GONE);
                break;

            case R.id.radio_date:
                searchedCategory.setVisibility(View.GONE);
                searchedInput.setVisibility(View.VISIBLE);
                searchedInput.setText("");
                dateSetter = new DateSetter(context, searchedInput);
                break;

            case R.id.radio_all:
                searchedCategory.setVisibility(View.GONE);
                searchedInput.setVisibility(View.VISIBLE);
                searchedInput.setText("");

                break;
        }
    }

    private void getDataFromDb(String radio) {
        switch (radio) {
            case "Title":
                searchedItem = searchedInput.getText().toString();
                list = mydb.getEventsByTitle(searchedItem);
                break;
            case "Category":
                String category = searchedCategory.getSelectedItem().toString();
                list = mydb.getEventsByCategory(category);
                break;
            case "Date":
                searchedItem = searchedInput.getText().toString();
                list = mydb.getEventsByDate(dateSetter.getChosenDate());
                break;
            case "Get all":
                searchedItem = searchedInput.getText().toString();
                list = mydb.getEvents("", new String[]{""});
                break;
        }
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