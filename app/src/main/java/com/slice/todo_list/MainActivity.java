package com.slice.todo_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> madapter;
    ListView lsttask;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper =new DbHelper(this);
        lsttask=(ListView)findViewById(R.id.lsttask);
        loadtasklist();
    }

    private void loadtasklist() {
        ArrayList<String> tasklist=dbHelper.getTaskList();
        if(madapter==null)
        {
            madapter=new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,tasklist);
            lsttask.setAdapter(madapter);
        }
        else
        {
            madapter.clear();
            madapter.addAll(tasklist);
            madapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add_task:
            final EditText taskedittext=new EditText(this);
                AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("add new task")
                        .setMessage("what do you want to do next")
                        .setView(taskedittext)
                        .setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task=String.valueOf(taskedittext.getText());
                                dbHelper.insertNewTask(task);
                                loadtasklist();
                            }
                        })
                        .setNegativeButton("cancel",null)
                        .create();
                dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        Drawable icon=menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

   public void deleteTask(View view)
   {
       View parent=(View)view.getParent();
       TextView taskTextView=(TextView)findViewById(R.id.task_title);
       String task=String.valueOf(taskTextView.getText());
       dbHelper.deleteTask(task);
       loadtasklist();
   }
}
