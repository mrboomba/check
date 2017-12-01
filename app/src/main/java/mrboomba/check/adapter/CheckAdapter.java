package mrboomba.check.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mrboomba.check.R;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.StudentModel;

/**
 * Created by mrboomba on 30/11/2560.
 */

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.check_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.myTextView.setText(position+1+"");
        holder.setView(context,studentModel.getClassChecked(position));
        if(currentWeek==position){
            holder.myTextView.setTextSize(context.getResources().getDimension(R.dimen.fontsize_big));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("mrboomba",studentModel.getClassAmnt()+"");
        return studentModel.getClassAmnt();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.number);
            view = itemView;
        }

        public void setView(Activity activity, int check) {
            if(check == 1) {
                view.setBackgroundColor(ContextCompat.getColor(activity,android.R.color.holo_green_light));
            }
            else if (check == 0){
                view.setBackgroundColor(ContextCompat.getColor(activity,android.R.color.holo_red_light));
            }else if(check == -1){
                view.setBackgroundColor(ContextCompat.getColor(activity,R.color.cardview_light_background));
            }else if(check == 2){
                view.setBackgroundColor(ContextCompat.getColor(activity,R.color.yellow));
            }
        }
    }
}
