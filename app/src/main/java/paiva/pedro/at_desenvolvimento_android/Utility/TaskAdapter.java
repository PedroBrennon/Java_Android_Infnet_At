package paiva.pedro.at_desenvolvimento_android.Utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import paiva.pedro.at_desenvolvimento_android.Model.TaskModel;
import paiva.pedro.at_desenvolvimento_android.R;

public class TaskAdapter extends BaseAdapter {

    private ArrayList<TaskModel> taskModelArrayList;
    private Activity activity;

    public TaskAdapter(Activity activity, ArrayList<TaskModel> taskModels){
        super();
        this.activity = activity;
        this.taskModelArrayList = taskModels;
    }

    @Override
    public int getCount() {
        if(taskModelArrayList.size() == 0){
            return 0;
        }

        return taskModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViews textViews;
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.task_list_item, null);
            textViews = new TextViews();

            textViews.txtTitle = convertView.findViewById(R.id.txtTitle);
            textViews.txtText = convertView.findViewById(R.id.txtText);
            convertView.setTag(textViews);
        } else{
            textViews = (TextViews) convertView.getTag();
        }

        TaskModel taskModel = taskModelArrayList.get(position);
        textViews.txtTitle.setText(taskModel.getTitulo());
        textViews.txtText.setText(taskModel.getData());

        return convertView;
    }

    private class TextViews {
        TextView txtTitle, txtText;
    }
}
