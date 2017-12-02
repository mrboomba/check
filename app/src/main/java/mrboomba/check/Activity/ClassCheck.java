package mrboomba.check.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import mrboomba.check.R;
import mrboomba.check.adapter.CardAdapter;
import mrboomba.check.adapter.CheckAdapter;
import mrboomba.check.adapter.SearchAdapter;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.StudentModel;

public class ClassCheck extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView,recyclerView2;
    private ImageButton no,yes;
    private TextView textView;
    private CheckAdapter adapter;

    private SearchAdapter searchAdapter;
    private CardContainer mCardContainer;


    private int current;
    private ClassModel classModel;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        Intent intent = getIntent();
        classModel = intent.getParcelableExtra("class");
        current = 0;
        final SharedPreferences sharedPref = getSharedPreferences("mrboomba", Context.MODE_PRIVATE);

        boolean check = sharedPref.getBoolean("first_use", true);

        if(check) {
            AlertDialog alertDialog = new AlertDialog.Builder(ClassCheck.this).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Swipe right to CHECK...Swipe left to NOT CHECK");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("first_use", false);
                            editor.commit();

                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        mCardContainer = (CardContainer) findViewById(R.id.layoutview);

        Resources r = getResources();

        CardAdapter adapter = new CardAdapter(ClassCheck.this,classModel);

        for(int i=classModel.getStudentAmnt()-1;i>=0;i--) {
            StudentModel s = classModel.getStudents()[i];
            CardModel cardModel = new CardModel(s.getFirstName()+" "+s.getLastName(),"", (Drawable) null);
            cardModel.setOnClickListener(new CardModel.OnClickListener() {
                @Override
                public void OnClickListener() {
                    Log.i("Swipeable Cards","I am pressing the card");
                }
            });

            cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                @Override
                public void onLike() {

                    current++;
                    if(current == classModel.getStudentAmnt()){
                        setUpSearch();
                    }

                }

                @Override
                public void onDislike() {
                    classModel.getStudents()[current].checkClass(classModel.getCurrentWeek(),1);
                    current++;
                    if(current == classModel.getStudentAmnt()){
                        setUpSearch();
                    }
                }
            });
            adapter.add(cardModel);
        }

        mCardContainer.setAdapter(adapter);


       // initView();

    }


    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ClassCheck.this);
        builder.setMessage("This is not keep change. You want to quit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void setUpSearch(){

        setContentView(R.layout.checklate_layout);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler);
        searchAdapter = new SearchAdapter(ClassCheck.this,classModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(searchAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i =0;i<classModel.getStudentAmnt();i++){
                    if(classModel.getStudents()[i].getClassChecked(classModel.getCurrentWeek())==-1){
                        classModel.getStudents()[i].checkClass(classModel.getCurrentWeek(),0);
                    }
                }
                classModel.setCurrentWeek(classModel.getCurrentWeek()+1);
                classModel.writeCheckedValue();
                Intent i = new Intent(ClassCheck.this,HomeActivity.class);
                i.putExtra("class",classModel);
                setResult(200,i);

                finish();

            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.sv);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchAdapter.getFilter().filter(newText);



        return true;
    }
}
