package mrboomba.check.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mrboomba.check.Activity.ClassCheck;
import mrboomba.check.R;
import mrboomba.check.util.ClassModel;
import mrboomba.check.util.StudentModel;

/**
 * Created by mrboomba on 1/12/2560.
 */

public class AssignAdapter extends RecyclerView.Adapter<AssignAdapter.ViewHolder> implements Filterable {

    private Activity activity;
    private AssignAdapter.SearchFilter searchFilter;

    private List<StudentModel> friendList;
    private List<StudentModel> filteredList;

    private ClassModel classModel;

    int assign;

    private String searchText;

    public AssignAdapter(Activity Activity,ClassModel classModel,int assign){
        searchText = "";
        this.assign =assign;
        this.activity = Activity;
        this.classModel = classModel;

        this.friendList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
        searchText = "";
        for(int i=0;i<classModel.getStudentAmnt();i++){
            friendList.add(classModel.getStudents()[i]);
            filteredList = friendList;
        }
        Log.d("mrboomba","filterListsize: "+filteredList.size());


        getFilter();
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    @Override
    public AssignAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        final AssignAdapter.ViewHolder holder = new AssignAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AssignAdapter.ViewHolder holder, int position) {


        holder.setIsRecyclable(false);
        StudentModel s = filteredList.get(position);
        String str = s.getFirstName()+" "+s.getLastName();
        String str2 = s.getId()+"";
        holder.idText.setText(str2);
        holder.data.setText(str);
        holder.setPosition(position);

        int indextmp = 0;

        for(int i=0;i<classModel.getStudentAmnt();i++){
            if(classModel.getStudents()[i].getId().equals(s.getId())){
                indextmp = i;
            }
        }

        final int finalIndex = indextmp;
        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classModel.getStudents()[finalIndex].checkAssign(assign,0);
                holder.setView(activity,0);
            }
        });

        holder.late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classModel.getStudents()[finalIndex].checkAssign(assign,2);
                holder.setView(activity,2);
            }
        });

        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classModel.getStudents()[finalIndex].checkAssign(assign,1);
                holder.setView(activity,1);
            }
        });

        holder.setView(activity,s.getAssignChecked(assign));


        if(str.contains(searchText.toLowerCase())){
            if(searchText.length()>0){
                //color your text here
                int index = str.indexOf(searchText.toLowerCase());
                SpannableStringBuilder sb = null;

                sb = new SpannableStringBuilder(str);
                BackgroundColorSpan bcs = new BackgroundColorSpan(Color.rgb(0, 255, 0)); //specify color here
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 255, 255)); //specify color here
                sb.setSpan(bcs, index,searchText.length()+index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(fcs, index,searchText.length()+index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.data.setText(sb);

            }
            else{
                holder.data.setText(str);
            }
        }else{
            holder.data.setText(str);
        }

        if(str2.contains(searchText.toLowerCase())){
            if(searchText.length()>0){
                //color your text here
                int index = str2.indexOf(searchText.toLowerCase());
                SpannableStringBuilder sb = null;

                sb = new SpannableStringBuilder(str2);
                BackgroundColorSpan bcs = new BackgroundColorSpan(Color.rgb(0, 255, 0)); //specify color here
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 255, 255)); //specify color here
                sb.setSpan(bcs, index,searchText.length()+index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(fcs, index,searchText.length()+index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                holder.idText.setText(sb);

            }
            else{
                holder.idText.setText(str2);
            }
        }else{
            holder.idText.setText(str2);
        }

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView idText,data;
        ImageButton no,late,yes;
        View view;
        String id;
        int position;
        public ViewHolder(View itemView) {
            super(itemView);
            data = (TextView) itemView.findViewById(R.id.data);
            idText = (TextView) itemView.findViewById(R.id.idText);
            no = (ImageButton) itemView.findViewById(R.id.no);
            late = (ImageButton) itemView.findViewById(R.id.late);
            yes = (ImageButton) itemView.findViewById(R.id.yes);

            this.id = id;


            this.view = itemView;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setId(String id){
            this.id =id;
        }


        public int getPosi() {
            return position;
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

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new AssignAdapter.SearchFilter();
        }

        return searchFilter;
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            searchText = (String) constraint;
            if (constraint != null && constraint.length() > 0) {
                ArrayList<StudentModel> tempList = new ArrayList<StudentModel>();


                // search content in friend list
                for (StudentModel s : friendList) {
                    String str = s.getId()+" "+s.getFirstName()+" "+s.getLastName();
                    if (str.contains(constraint.toString().toLowerCase())) {
                        tempList.add(s);
                    }

                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = friendList.size();
                filterResults.values = friendList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<StudentModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
