package mrboomba.check.Activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vistrav.ask.Ask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import mrboomba.check.R;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.FileUtil;

public class ImportActivity extends AppCompatActivity {

    ImageButton next;
    ImageButton browse;
    EditText edit;
    FileUtil fileutil;
    ClassModel classModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Ask.on(this)
                .forPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("For read file excel to import",
                        "For export file to you") //optional
                .go();

        classModel = null;
        fileutil = new FileUtil();
        if (FileUtil.SD) {
            fileutil.CreateFolder(fileutil.getFile("import"));
            fileutil.CreateFolder(fileutil.getFile("export"));
            fileutil.CreateFolder(fileutil.getFile("working"));

            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{new File(fileutil.getFile("")).getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Toasty.normal(ImportActivity.this,"Create folder success", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        initView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ImportActivity.this, NewItemActivity.class);
        startActivity(intent);
        return  true;
    }

    public void initView(){
        edit = (EditText) findViewById(R.id.edit);
        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImportActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        browse = (ImageButton) findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ArrayList<String> list = fileutil.getName("working", ".xls");
                    if (list.size() >= 1) {
                        new MaterialDialog.Builder(ImportActivity.this)
                                .title(R.string.choose_file)
                                .titleGravity(GravityEnum.CENTER)
                                .canceledOnTouchOutside(true)
                                .items(list)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        edit.setText(text);
                                        classModel = new ClassModel(fileutil.getFile("working/" + text));
                                    }
                                })
                                .show();

                    }
                }catch (Exception e){
                    Log.d("mrboomba",e.getMessage());
                    Toasty.error(ImportActivity.this,"Please Check Your File!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
