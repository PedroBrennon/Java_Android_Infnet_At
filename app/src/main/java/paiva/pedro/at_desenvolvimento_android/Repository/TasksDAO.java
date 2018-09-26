package paiva.pedro.at_desenvolvimento_android.Repository;

import android.app.Activity;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;

import paiva.pedro.at_desenvolvimento_android.Interface.ITasks;
import paiva.pedro.at_desenvolvimento_android.Model.TaskModel;
import paiva.pedro.at_desenvolvimento_android.Utility.TaskAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes.URL;

public class TasksDAO {

    private Retrofit retrofit;
    private ITasks iTasks;
    private TaskAdapter taskAdapter;
    private Activity activity;
    private ArrayList<TaskModel> taskModelArrayList;

    public TasksDAO() {
        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iTasks = retrofit.create(ITasks.class);
    }

    public TasksDAO(TaskAdapter taskAdapter, ArrayList<TaskModel> taskModels) {
        this();
        this.taskAdapter = taskAdapter;
        this.taskModelArrayList = taskModels;
    }

    public void getTasks() {
        Call<ArrayList<TaskModel>> call = iTasks.getTasks();

        call.enqueue(new Callback<ArrayList<TaskModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TaskModel>> call, Response<ArrayList<TaskModel>> response) {
                taskModelArrayList.clear();
                taskModelArrayList.addAll(response.body());
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<TaskModel>> call, Throwable t) {

            }
        });
    }
}
