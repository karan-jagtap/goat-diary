package com.aarushsystems.goatdiary.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aarushsystems.goatdiary.R;
import com.aarushsystems.goatdiary.helper.LocalDatabase;
import com.aarushsystems.goatdiary.model.VaccinationModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HealthCareAdapter extends ArrayAdapter<VaccinationModel> {

    private ArrayList<VaccinationModel> arrayList;
    private Activity activity;
    private LocalDatabase db;

    public HealthCareAdapter(Activity activity, ArrayList<VaccinationModel> arrayList) {
        super(activity, R.layout.layout_vaccination_link, arrayList);
        this.activity = activity;
        this.arrayList = arrayList;
        db = new LocalDatabase(activity);
        Log.i("CUSTOM","CONSTRUCTOR");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Log.i("CUSTOM","getView()");
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.layout_vaccination_link, parent, false);
        VaccinationModel model = arrayList.get(position);

        TextView srNo = view.findViewById(R.id.textView_sr_no_layout_vaccination_link);
        TextView date = view.findViewById(R.id.textView_date_layout_vaccination_link);
        TextView tagId = view.findViewById(R.id.textView_tag_id_layout_vaccination_link);
        TextView booster = view.findViewById(R.id.textView_booster_layout_vaccination_link);

        if (model != null) {
            srNo.setText(String.valueOf(model.getSrno()));
            date.setText(model.getProposedDate());
            tagId.setText(String.valueOf(model.getTagId()));
            booster.setText(db.getFieldBySrNo(model.getBooster(), LocalDatabase.TABLE_VACCINE));
        }

        Log.i("CUSTOM","Data in adapter = "+srNo.getText()+" - "+date.getText()+" - "+tagId.getText()+" - "+booster.getText());
        return view;
    }
}
