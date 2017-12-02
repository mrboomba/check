package mrboomba.check.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mrboomba.check.Activity.AssignCheck;
import mrboomba.check.R;
import mrboomba.check.adapter.AssignAdapter;
import mrboomba.check.util.ClassModel;


public class RecyclerFragment extends Fragment implements  SearchView.OnQueryTextListener {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    AssignAdapter mAdapter;
    ClassModel classModel;
    int position;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);


        if (getArguments() != null) {
            classModel = getArguments().getParcelable("class");
            this.position = getArguments().getInt("position");
            getArguments().getParcelable("activity");
        }

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        AssignCheck assignCheck = (AssignCheck)this.getActivity();
        mAdapter = new AssignAdapter(this.getActivity(),classModel,position);

        mRecyclerView.setAdapter(mAdapter);

        SearchView searchView = (SearchView) view.findViewById(R.id.sv);
        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);



        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }
}
