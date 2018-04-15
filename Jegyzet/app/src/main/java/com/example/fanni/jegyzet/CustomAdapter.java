package com.example.fanni.jegyzet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


// https://stackoverflow.com/questions/21381129/is-there-a-way-to-add-two-textviews-to-arrayadapter
// ez az osztály jelenítti meg a ListView-ban az elemeket
class CustomAdapter extends ArrayAdapter<String>
{
    String A[],B[];
    LayoutInflater mInfalter;

    public CustomAdapter(Context context, String[] A, String B[])
    {
        super(context,R.layout.jegyzet_list_item, A);
        this.A = A;
        this.B = B;
        mInfalter = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView = mInfalter.inflate(R.layout.jegyzet_list_item,parent,false);
            holder = new ViewHolder();
            holder.tv1 = (TextView)convertView.findViewById(R.id.jegyzetNev);
            holder.tv2 = (TextView)convertView.findViewById(R.id.jegyzetDatum);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv1.setText(A[position]);
        holder.tv2.setText(B[position]);
        return convertView;
    }

    static class ViewHolder
    {
        TextView tv1,tv2;
    }

}
