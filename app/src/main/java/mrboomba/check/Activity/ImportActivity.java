package mrboomba.check.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        Ask.on(ImportActivity.this)
                .forPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("For read file excel to import",
                        "For export file to you") //optional
                .go();

        final SharedPreferences sharedPref = getSharedPreferences("mrboomba", Context.MODE_PRIVATE);

        boolean check = sharedPref.getBoolean("first_come", true);
        fileutil = new FileUtil();

        if(check) {
            AlertDialog alertDialog = new AlertDialog.Builder(ImportActivity.this).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Please move your excel file to folder Checker/import/ before use");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("first_come", false);
                            editor.commit();
                            dialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(ImportActivity.this).create();
                            alertDialog.setTitle("Info");
                            alertDialog.setMessage("We provide example file for test please try to crate new class on top right");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                            alertDialog.show();

                        }
                    });
            alertDialog.show();

            AssetManager assetManager = getAssets();
            String[] files = null;
            try {
                files = assetManager.list("");
            } catch (IOException e) {
                Log.e("tag", "Failed to get asset file list.", e);
            }
            if (files != null) for (String filename : files) {
                if (filename.equals("Example.xls")) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = assetManager.open(filename);
                        File outFile = new File(fileutil.getFile("import/" + filename));
                        out = new FileOutputStream(outFile);
                        copyFile(in, out);
                    } catch (IOException e) {
                        Log.e("tag", "Failed to copy asset file: " + filename, e);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                // NOOP
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                // NOOP
                            }
                        }

                    }
                }
            }

            classModel = null;

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
                                Toasty.normal(ImportActivity.this, "Create folder success", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            initView();
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ImportActivity.this, NewItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        return  true;
    }

    public void initView(){
        edit = (EditText) findViewById(R.id.edit);
        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit.getText().toString().equals("")){
                    Toasty.error(ImportActivity.this,getResources().getString(R.string.file_error), Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent intent = new Intent(ImportActivity.this, HomeActivity.class);
                    intent.putExtra("class", classModel);
                    startActivity(intent);
                }
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
                                .titleColor(getResources().getColor(R.color.colorAccent))
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
                    else{
                        Toasty.warning(ImportActivity.this,getResources().getString(R.string.folder_empty_error),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toasty.error(ImportActivity.this,"Please Check Your File!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
