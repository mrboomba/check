package mrboomba.check.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import mrboomba.check.R;
import mrboomba.check.util.ClassModel;

import static android.media.AudioRecord.SUCCESS;

public class HomeActivity extends AppCompatActivity {
    private final int CLASS_CODE = 100;
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
        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,ClassCheck.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("class",classModel);
                startActivityForResult(i,CLASS_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CLASS_CODE){
            if(resultCode == 200)
            classModel = data.getParcelableExtra("class");
        }
    }
}
