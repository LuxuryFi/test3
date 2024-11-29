package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class YogaClassAdapter extends ArrayAdapter<YogaClass> {

    private final Context context;
    private final List<YogaClass> classes;

    public YogaClassAdapter(Context context, List<YogaClass> classes) {
        super(context, R.layout.yoga_class_item, classes);
        this.context = context;
        this.classes = classes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.yoga_class_item, parent, false);
        }

        YogaClass yogaClass = classes.get(position);

        TextView classNameText = convertView.findViewById(R.id.className);
        TextView instructorText = convertView.findViewById(R.id.instructor);
        TextView scheduleText = convertView.findViewById(R.id.schedule);
        classNameText.setText(yogaClass.getDate());
        instructorText.setText(yogaClass.getComment());
        scheduleText.setText(yogaClass.getTeacher());

        return convertView;
    }
}
