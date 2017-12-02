package mrboomba.check.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mrboomba.check.R;
import mrboomba.check.util.ClassModel;

import static android.media.AudioRecord.SUCCESS;

public class HomeActivity extends AppCompatActivity {
    private final int CLASS_CODE = 100;
    private final int ASSIGN_CODE = 101;
    ClassModel classModel;
    ImageButton classButton,assignButton;
    TextView classText,assignText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        classModel = intent.getParcelableExtra("class");
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_class){
            classModel.setClassAmnt(classModel.getClassAmnt()+1);
            initView();
        }else if(item.getItemId()==R.id.add_assign){
            classModel.setAssignAmnt(classModel.getAssignAmnt()+1);
            initView();
        }
        else if(item.getItemId()==R.id.export){
            classModel.export();
            finish();
        }
        return  true;
    }

    public void initView(){
        classButton = (ImageButton) findViewById(R.id.class_button);
        assignButton = (ImageButton) findViewById(R.id.assign_button);
        classText = (TextView) findViewById(R.id.class_text);
        assignText = (TextView)findViewById(R.id.assign_text);
        if(classModel.getCurrentWeek()>=classModel.getClassAmnt()) {
            classText.setText("Success");
            classButton.setEnabled(false);
        }
        else{
            classText.setText(classModel.getCurrentWeek()+1+"/"+classModel.getClassAmnt());
            classButton.setEnabled(true);
        }
        assignText.setText(classModel.getAssignAmnt()+"");
        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,ClassCheck.class);
                i.putExtra("class",classModel);
                startActivityForResult(i,CLASS_CODE);
            }
        });

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,AssignCheck.class);
                i.putExtra("class",classModel);
                startActivityForResult(i,ASSIGN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CLASS_CODE){
            if(resultCode == 200) {
                classModel = data.getParcelableExtra("class");
                initView();
            }
        }
        if(requestCode == ASSIGN_CODE){
            if(resultCode == 200) {
                classModel = data.getParcelableExtra("class");
                initView();
            }
        }
    }
}
