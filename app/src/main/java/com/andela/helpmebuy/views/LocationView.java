package com.andela.helpmebuy.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.LocationAdapter;
import com.andela.helpmebuy.models.AbsLocation;
import com.andela.helpmebuy.utilities.LocationFilter;

import java.util.List;

public class LocationView<T extends AbsLocation> extends FrameLayout {

    private EditText editText;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LocationAdapter<T> adapter;

    private LocationFilter<T> filter;

    public LocationView(Context context) {
        this(context, null);
    }

    public LocationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.location_view,this,false);
        addView(view);

        editText = (EditText) findViewById(R.id.search_view);
        editText.addTextChangedListener(locationFilter());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LocationAdapter<>(getContext());

        recyclerView.setAdapter(adapter);

        filter = new LocationFilter<>(adapter);
    }

    public void setLocations(List<T> locations) {
        adapter.setInitialLocations(locations);

        adapter.notifyDataSetChanged();
    }

    public TextWatcher locationFilter() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        return textWatcher;
    }

    public interface OnLocationClickedListener<T extends AbsLocation> {
        void onLocationClicked(T location);
    }

    public void setOnLocationClickedListener(OnLocationClickedListener<T> listener) {
        this.adapter.setOnLocationClickedListener(listener);
    }
}
