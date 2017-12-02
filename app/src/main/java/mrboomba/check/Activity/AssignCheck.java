package mrboomba.check.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import mrboomba.check.R;
import mrboomba.check.adapter.AssignAdapter;
import mrboomba.check.adapter.SearchAdapter;
import mrboomba.check.fragment.RecyclerFragment;
import mrboomba.check.util.ClassModel;

public class AssignCheck extends AppCompatActivity implements MaterialTabListener {

    ViewPager pager;
    ClassModel classModel;
    MaterialTabHost tabHost;
    ViewPagerAdapter adapter;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_check);
        Intent intent = getIntent();
        classModel = intent.getParcelableExtra("class");
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classModel.writeCheckedValue();
                Intent i = new Intent(AssignCheck.this, HomeActivity.class);
                i.putExtra("class", classModel);
                setResult(200, i);

                finish();

            }
        });

        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }





        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

    }




    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("class", classModel);
            bundle.putInt("position", num);
            // set MyFragment Arguments
            RecyclerFragment myObj = new RecyclerFragment();
            myObj.setArguments(bundle);
            return myObj;
        }

        @Override
        public int getCount() {
            return classModel.getAssignAmnt();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Assign " + (position + 1);
        }

    }


}
