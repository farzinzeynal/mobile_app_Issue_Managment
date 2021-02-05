package com.example.user.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.issue_managment.R;
import com.example.user.models.IssueModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CostomListViewAdapter extends ArrayAdapter
{
    int resource;
    LayoutInflater inflater;
    List<IssueModel> myList;
    ArrayList<IssueModel> arrayList = new ArrayList<>();
    TextView textView_id,
             textView_descrip;


    public CostomListViewAdapter(@NonNull Context context, int resource, @NonNull List<IssueModel> objects)
    {
        super(context, resource, (List) objects);
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myList = objects;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        textView_id = convertView.findViewById(R.id.textView_id_issue);
        textView_descrip = convertView.findViewById(R.id.textView_descrip_issue);



        IssueModel qurgu = new IssueModel();

        textView_id.setText("No: "+myList.get(position).getId());
        textView_descrip.setText(myList.get(position).getDesription());


        return convertView;

    }


    public void mfilter(String newText) {
        newText = newText.toLowerCase(Locale.getDefault());
        myList.clear();
        if (newText.length() == 0) {
            myList.addAll(arrayList);
        } else {
            for (IssueModel wp : arrayList) {
                if (wp.getDesription().toLowerCase(Locale.getDefault()).contains(newText))
                {
                    myList.add(wp);
                }
            }
        }

        notifyDataSetChanged();

    }


}
