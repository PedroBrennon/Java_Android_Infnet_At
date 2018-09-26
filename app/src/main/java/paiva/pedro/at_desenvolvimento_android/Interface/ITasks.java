package paiva.pedro.at_desenvolvimento_android.Interface;

import java.util.ArrayList;

import paiva.pedro.at_desenvolvimento_android.Model.TaskModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ITasks {

    @GET("tarefas")
    Call<ArrayList<TaskModel>> getTasks();
}
