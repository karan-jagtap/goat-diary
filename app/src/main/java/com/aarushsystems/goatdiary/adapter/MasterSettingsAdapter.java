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
import com.aarushsystems.goatdiary.model.GenericModel;

import java.util.ArrayList;

public class MasterSettingsAdapter  extends ArrayAdapter<GenericModel>{
    private ArrayList<GenericModel> arrayList;
    private Activity activity;

    public MasterSettingsAdapter(Activity activity, ArrayList<GenericModel> arrayList) {
        super(activity, R.layout.layout_masters_add_data_display, arrayList);
        this.activity = activity;
        this.arrayList = arrayList;
        Log.i("CUSTOM", "CONSTRUCTOR");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Log.i("CUSTOM", "getView()");
        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.layout_masters_add_data_display, parent, false);
        GenericModel model = arrayList.get(position);

        TextView srNo = view.findViewById(R.id.textView_srno_MastersSettingsAdapter);
        TextView fieldValue = view.findViewById(R.id.textView_field_value_MastersSettingsAdapter);

        if (model != null) {
            srNo.setText(model.getSrno());
            fieldValue.setText(model.getField());
            if (model.getSrno().equals("0")) {
                srNo.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                fieldValue.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
            }
        }
        return view;
    }
}
