package com.example.lab3_and102_fa24;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3_and102_fa24.adapter.TaskInfoAdapter;
import com.example.lab3_and102_fa24.dao.TaskInfoDAO;
import com.example.lab3_and102_fa24.models.TaskInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG = "//=====";
        TaskInfoDAO dao;
        ArrayList<TaskInfo> listTask;
        RecyclerView rcvTask;
        EditText edtID, edtTitle, edtContent, edtDate, edtType;
        Button btnAdd;
        TaskInfoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        dao = new TaskInfoDAO(this);
        listTask = dao.getListInfo();
        Log.d(TAG, "onCreate: " + listTask.toString());
        adapter = new TaskInfoAdapter(MainActivity.this, listTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                String date = edtDate.getText().toString().trim();
                String type = edtType.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please iput dat", Toast.LENGTH_SHORT).show();
                        if (title.isEmpty()){
                            edtTitle.setError("Please enter title");
                        }

                    if (content.isEmpty()){
                        edtContent.setError("Please enter content");
                    }

                    if (date.isEmpty()){
                        edtDate.setError("Please enter date");
                    }

                    if (type.isEmpty()) {
                        edtType.setError("Please enter type");
                    }
                }
                else {
                    TaskInfo inFo = new TaskInfo(1, title, content, date, type, 0);
                    long check = dao.addInfo(inFo);
                    if (check < 0){
                        Toast.makeText(MainActivity.this, "ERROR: NOT INSERT DATE", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "INSERT SUCCESFULL", Toast.LENGTH_SHORT).show();
                    }
                    listTask = dao.getListInfo();
                    adapter = new TaskInfoAdapter(MainActivity.this, listTask);
                    rcvTask.setLayoutManager(linearLayoutManager);
                    rcvTask.setAdapter(adapter);
                    reset();
                }
            }
        });
        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrType = {"De", "Trung binh", "Kho"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please choose level");
                builder.setIcon(R.drawable.icon_update);
                builder.setItems(arrType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edtType.setText(arrType[i]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void init(){
        rcvTask = findViewById(R.id.rcvTask);
        edtID = findViewById(R.id.edtID);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);
    }

    public void reset(){
        edtID.setText("");
        edtTitle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
    }
}