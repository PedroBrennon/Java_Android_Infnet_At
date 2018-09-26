package paiva.pedro.at_desenvolvimento_android.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import paiva.pedro.at_desenvolvimento_android.Model.TaskModel;
import paiva.pedro.at_desenvolvimento_android.R;
import paiva.pedro.at_desenvolvimento_android.Repository.TasksDAO;
import paiva.pedro.at_desenvolvimento_android.Utility.DefaultAttributes;
import paiva.pedro.at_desenvolvimento_android.Utility.TaskAdapter;

import static paiva.pedro.at_desenvolvimento_android.Repository.ConnectionFirebase.logout;

public class MainActivity extends AppCompatActivity {

    private ListView tasksListView;
    private ArrayList<TaskModel> tasksList;
    private TasksDAO tasksDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findIds();
        getList();
    }

    private void getList() {
        tasksList = new ArrayList<>();
        TaskAdapter taskAdapter = new TaskAdapter(this, tasksList);
        tasksDAO = new TasksDAO(taskAdapter, tasksList);
        tasksDAO.getTasks();
        tasksListView.setAdapter(taskAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_item:
                logout();
                DefaultAttributes.toastMessage(getApplicationContext(), "Deslogado com sucesso");
                startActivity(new Intent(getApplicationContext(), InitialActivity.class));
                break;
            default:
                break;
        }

        return true;
    }

    private void findIds() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        tasksListView = findViewById(R.id.taskListView);
    }
}
