package nl.mjvrijn.matthewvanrijn_pset4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<ToDoTask> {

    public ListViewAdapter(Context context, int textViewID, ArrayList<ToDoTask> data) {
        super(context, textViewID, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.content_list_entry, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.list_entry_textview);
        ImageView checkMark = (ImageView) convertView.findViewById(R.id.list_entry_check);

        textView.setText(getItem(position).getTask());

        if(getItem(position).isDone()) {
            textView.setTextColor(getContext().getResources().getColor(R.color.textDone));
            checkMark.setVisibility(View.VISIBLE);
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.textNotDone));
            checkMark.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
