package mrboomba.check.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mrboomba.check.R;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.StudentModel;

/**
 * Created by mrboomba on 30/11/2560.
 */

public class CheckAdapter extends BaseAdapter {

    private StudentModel studentModel;
    private int currentWeek;
    private LayoutInflater mInflater;
    private Activity context;

    public CheckAdapter(Activity context, StudentModel student, int currentWeek){
        this.currentWeek = currentWeek;
        studentModel = student;

        this.mInflater = LayoutInflater.from(context);
        this.context = context;


    }


    @Override
    public int getCount() {
        return studentModel.getClassAmnt();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.check_item, viewGroup, false);
        TextView myTextView = (TextView) view.findViewById(R.id.number);
        myTextView.setText(i+1+"");
        if(currentWeek==i){
            myTextView.setTextSize(context.getResources().getDimension(R.dimen.fontsize_big));
        }
        int check = studentModel.getClassChecked(i);
        if(check == 1) {
            view.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_green_light));
        }
        else if (check == 0){
            view.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_red_light));
        }else if(check == -1){
            view.setBackgroundColor(ContextCompat.getColor(context,R.color.cardview_light_background));
        }else if(check == 2){
            view.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow));
        }
        return view;
    }

}
