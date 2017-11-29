package mrboomba.check.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import mrboomba.check.R;
import mrboomba.check.util.ClassModel;

public class HomeActivity extends AppCompatActivity {
    ClassModel classModel;
    Button classButton;
    ImageButton assignButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        classModel = intent.getParcelableExtra("class");
        initView();
    }

    public void initView(){
        classButton = (Button) findViewById(R.id.class_button);
        classButton.setText(classModel.getCurrentWeek()+1+"/"+classModel.getClassAmnt());
    }
}
