package com.example.rxjavaretrofitroomdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    EmployeeViewModel viewModel;
    EditText empID, firstName, lastName;
    Button insertBtn, updateBtn, deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        viewModel.init(getApplication());
        empID = (EditText)findViewById(R.id.employee_id);
        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        insertBtn = (Button)findViewById(R.id.insert);
        updateBtn = (Button)findViewById(R.id.update);
        deleteBtn = (Button)findViewById(R.id.delete);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.employee_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmployeeAdapter employeeAdapter = new EmployeeAdapter();
        recyclerView.setAdapter(employeeAdapter);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = empID.getEditableText().toString();
                String fName = firstName.getEditableText().toString();
                String lName = lastName.getEditableText().toString();
                viewModel.inserEmployeeDetails(id,fName,lName);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = empID.getEditableText().toString();
                String fName = firstName.getEditableText().toString();
                String lName = lastName.getEditableText().toString();
                viewModel.updateEmployeeDetails(id,fName,lName);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = empID.getEditableText().toString();
                viewModel.deleteEmployeeDetails(id);
            }
        });

        viewModel.getAllEmployees().observeForever(employeeList -> {
            employeeAdapter.updateEmployeeList(employeeList);
            Log.d(TAG,"employeeList size: "+employeeList.size());
        });
    }
}
