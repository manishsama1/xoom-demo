package com.demo.xoom.xoomdemo.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.xoom.xoomdemo.R;
import com.demo.xoom.xoomdemo.adapter.XoomAdapter;
import com.demo.xoom.xoomdemo.network.XoomResponse;
import com.demo.xoom.xoomdemo.network.Country;
import com.demo.xoom.xoomdemo.viewmodel.XoomViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView photoListView;
    private XoomAdapter xoomAdapter;
    private LinearLayoutManager linearLayoutManager;
    private XoomViewModel viewModel;
    private int pageNumber;
    private boolean isLoading;
    private XoomViewModel.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        pageNumber = viewModel.getPageNumber() == 0 ? 1 :viewModel.getPageNumber();
        getCountries();
        pageNumber = viewModel.getPageNumber();

        xoomAdapter = new XoomAdapter(viewModel.getCountryList(), this);
        photoListView = (RecyclerView) findViewById(R.id.photo_list);
        progressBar = findViewById(R.id.main_progress_bar);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        linearLayoutManager = new LinearLayoutManager(this);
        photoListView.addItemDecoration(itemDecoration);
        photoListView.setLayoutManager(linearLayoutManager);
        photoListView.setAdapter(xoomAdapter);
        photoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && pageNumber < viewModel.getPageCount()) {
                    //make the next page call again if user has scrolled to the last item in the list.
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreItems();
                    }
                }
            }
        });

        observeViewModel(viewModel);
    }

    private void loadMoreItems() {
        pageNumber++;
        isLoading = true;
        toggleProgressBar(true);
        getCountries();
        observeViewModel(viewModel);
    }

    private void init() {
        factory = new XoomViewModel.Factory(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(XoomViewModel.class);
    }

    private void getCountries() {
        viewModel.setPageNumber(pageNumber);
        viewModel.getCountries();
    }

    private void observeViewModel(final XoomViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getObservableData().observe(this, new Observer<XoomResponse>() {
            @Override
            public void onChanged(@Nullable XoomResponse xoomResponse) {
                isLoading = false;
                toggleProgressBar(false);
                if (xoomResponse == null) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                    return;
                }
                List<Country> countryList = new ArrayList<>();
                countryList.addAll(xoomResponse.getActiveCountryList());
                viewModel.setPageCount(xoomResponse.getPageCount());
                if (!countryList.isEmpty()) {
                    xoomAdapter.updateList(countryList);
                }
            }
        });
    }

    private void toggleProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
