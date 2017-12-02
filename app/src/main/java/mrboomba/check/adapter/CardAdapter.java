package mrboomba.check.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;
import com.andtinder.view.SimpleCardStackAdapter;

import mrboomba.check.Activity.ClassCheck;
import mrboomba.check.R;
import mrboomba.check.util.ClassModel;

/**
 * Created by mrboomba on 2/12/2560.
 */

public class CardAdapter extends CardStackAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private ClassModel classModel;


    public CardAdapter(Activity mContext, ClassModel classModel) {
        super(mContext);
        this.mInflater = LayoutInflater.from(mContext);
        activity = mContext;
        this.classModel = classModel;
    }

    @Override
    protected View getCardView(int i, CardModel cardModel, View view, ViewGroup viewGroup) {
        View view1 = mInflater.inflate(R.layout.card_item, viewGroup, false);
        TextView textView = (TextView) view1.findViewById(R.id.name);
        textView.setText(cardModel.getTitle());
        GridView recyclerView = (GridView) view1.findViewById(R.id.check_recycler);
        if(i<classModel.getStudentAmnt())
        {
            CheckAdapter adapter = new CheckAdapter(activity, classModel.getStudents()[i], classModel.getCurrentWeek());
            recyclerView.setAdapter(adapter);
        }
        return  view1;
    }


}
