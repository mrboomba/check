package mrboomba.check.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import mrboomba.check.R;
import mrboomba.check.adapter.CheckAdapter;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.StudentModel;

public class ClassCheck extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton no,yes;
    private TextView textView;
    private CheckAdapter adapter;

    private int current;
    private ClassModel classModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_check);
        Intent intent = getIntent();
        classModel = intent.getParcelableExtra("class");
        current = 0;
        recyclerView = (RecyclerView) findViewById(R.id.check_recycler);

        initView();

    }

    public void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.check_recycler);
        no = (ImageButton) findViewById(R.id.no);
        yes = (ImageButton) findViewById(R.id.yes);
        textView = (TextView) findViewById(R.id.name);

        textView.setText(classModel.getStudents()[current].getFirstName()+" "+classModel.getStudents()[current].getLastName());


        recyclerView.setLayoutManager(new LinearLayoutManager(ClassCheck.this, LinearLayout.HORIZONTAL,false));
        adapter = new CheckAdapter(ClassCheck.this,classModel.getStudents()[current],classModel.getCurrentWeek());
        recyclerView.setAdapter(adapter);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classModel.getStudents()[current].checkClass(classModel.getCurrentWeek(),1);
                current++;
                if(current == classModel.getStudentAmnt()){
                    classModel.setCurrentWeek(classModel.getCurrentWeek()+1);
                    Intent i = new Intent(ClassCheck.this,HomeActivity.class);
                    i.putExtra("class",classModel);
                    setResult(200,i);

                    finish();
                }else{
                    textView.setText(classModel.getStudents()[current].getFirstName()+" "+classModel.getStudents()[current].getLastName());
                    adapter = new CheckAdapter(ClassCheck.this,classModel.getStudents()[current],classModel.getCurrentWeek());
                    recyclerView.setAdapter(adapter);
                }


            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current++;
                if(current == classModel.getStudentAmnt()){
                    classModel.setCurrentWeek(classModel.getCurrentWeek()+1);
                    Intent i = new Intent(ClassCheck.this,HomeActivity.class);
                    i.putExtra("class",classModel);
                    setResult(200,i);

                    finish();
                }
                else{
                    textView.setText(classModel.getStudents()[current].getFirstName()+" "+classModel.getStudents()[current].getLastName());
                    adapter = new CheckAdapter(ClassCheck.this,classModel.getStudents()[current],classModel.getCurrentWeek());
                    recyclerView.setAdapter(adapter);
                }

            }
        });

    }
}
