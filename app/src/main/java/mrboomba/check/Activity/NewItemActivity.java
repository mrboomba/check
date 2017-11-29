package mrboomba.check.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import mrboomba.check.R;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.FileUtil;

public class NewItemActivity extends AppCompatActivity {
    FileUtil fileutil;
    EditText filename,name,classday,assign;
    Button create;
    ImageButton browse;
    ClassModel newClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        fileutil = new FileUtil();
        initView();

    }

    public void initView(){
        browse = (ImageButton) findViewById(R.id.browse);
        create = (Button) findViewById(R.id.create);
        filename = (EditText) findViewById(R.id.filename);
        name = (EditText) findViewById(R.id.name);
        classday = (EditText) findViewById(R.id.classday);
        assign = (EditText) findViewById(R.id.assign);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().equals("")) {
                    Toasty.error(NewItemActivity.this, getResources().getString(R.string.filename_error), Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().equals("")) {
                    Toasty.error(NewItemActivity.this, getResources().getString(R.string.classname_error), Toast.LENGTH_SHORT).show();
                } else if (classday.getText().toString().equals("")) {
                    Toasty.error(NewItemActivity.this, getResources().getString(R.string.class_amnt_error), Toast.LENGTH_SHORT).show();
                } else if (classday.getText().toString().equals("")) {
                    Toasty.error(NewItemActivity.this, getResources().getString(R.string.assigmn_amnt_error), Toast.LENGTH_SHORT).show();
                } else {
//                    try {
                        ClassModel newClass = new ClassModel(fileutil.getFile("import/" +filename.getText().toString()), name.getText().toString(), Integer.valueOf(classday.getText().toString()), Integer.valueOf(assign.getText().toString()));
                        Intent i = new Intent(NewItemActivity.this, HomeActivity.class);
                        i.putExtra("class", newClass);
                        startActivity(i);
//                    } catch (Exception e) {
//                        Toasty.error(NewItemActivity.this, getResources().getString(R.string.file_error), Toast.LENGTH_SHORT).show();
//                        Log.d("mrboomba",e.getMessage());
//                    }
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ArrayList<String> list = fileutil.getName("import", ".xls");
                    if (list.size() >= 1) {
                        new MaterialDialog.Builder(NewItemActivity.this)
                                .title(R.string.choose_file)
                                .titleGravity(GravityEnum.CENTER)
                                .canceledOnTouchOutside(true)
                                .items(list)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        filename.setText(text);
                                    }
                                })
                                .show();

                    }
                    else Toasty.warning(NewItemActivity.this,getResources().getString(R.string.folder_empty_error),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.d("mrboomba",e.getMessage());
                    Toasty.error(NewItemActivity.this,"Please Check Your File!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
