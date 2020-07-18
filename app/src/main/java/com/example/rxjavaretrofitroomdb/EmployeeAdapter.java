package com.example.rxjavaretrofitroomdb;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    static final String TAG = EmployeeAdapter.class.getSimpleName();
    List<Employee> employeeList = new ArrayList<>();

    public void updateEmployeeList(List<Employee> list){
        employeeList.clear();
        employeeList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_record_view,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            holder.id.setText(employeeList.get(position).id);
            holder.fName.setText(employeeList.get(position).firstName);
            holder.lName.setText(employeeList.get(position).lastName);
        }catch (Exception e){
            Log.e(TAG,"Exception : "+e);
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView id, fName, lName;
        public ViewHolder(View v){
            super(v);
            id = (TextView)v.findViewById(R.id.emp_id);
            fName = (TextView)v.findViewById(R.id.emp_fname);
            lName = (TextView)v.findViewById(R.id.emp_lname);
        }
    }
}
