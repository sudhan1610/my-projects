package com.example.rxjavaretrofitroomdb;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeViewModel extends ViewModel {

    private static final String TAG = EmployeeViewModel.class.getSimpleName();
    private Context context;
    private EmployeeDao employeeDao;
    private LiveData<List<Employee>> employeeList;

    public void init(Application application){
        this.context = application;
        employeeDao = EmployeeDatabase.getInstance(application).getEmployeeDao();
    }

    public void inserEmployeeDetails(String id, String fName, String lName){

        if ((id!=null && !id.isEmpty()) && (fName!=null && !fName.isEmpty()) && (lName!=null && !lName.isEmpty())){
            Employee employee = new Employee(id,fName,lName);
            new InsertTask(employee).execute();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                }
            });

        }else{
            Toast.makeText(context,"Enter all the details",Toast.LENGTH_SHORT).show();
        }

    }

    public void updateEmployeeDetails(String id, String fName, String lName){
        if ((id!=null && !id.isEmpty()) && (fName!=null && !fName.isEmpty()) && (lName!=null && !lName.isEmpty())){
            Employee employee = new Employee(id,fName,lName);
            new UpdateTask(employee).execute();

        }else{
            Toast.makeText(context,"Enter all the details",Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteEmployeeDetails(String id){
        if (id!=null && !id.isEmpty()){
            Employee employee = new Employee(id,null,null);
            new DeleteTask(employee).execute();

        }else{
            Toast.makeText(context,"Enter employee ID",Toast.LENGTH_SHORT).show();
        }

    }

    public LiveData<List<Employee>> getAllEmployees(){
        Log.d(TAG,"getAllEmployees");
        //new GetDataTask().execute();
        //return employeeList;
        return employeeDao.getAllEmployeeData();
    }

    private class InsertTask extends AsyncTask<Void,Void,Void>{

        Employee employee;
        public InsertTask(Employee employee){
            this.employee = employee;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            employeeDao.insertEmployee(employee);
            //Log.d(TAG,"employee size: "+employeeDao.getAllEmployeeData().getValue().size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"InserTask data inserted");
        }
    }

    private class UpdateTask extends AsyncTask<Void,Void,Void>{

        Employee employee;
        public UpdateTask(Employee employee){
            this.employee = employee;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            employeeDao.updateEmployee(employee);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"UpdateTask data updated");
        }
    }

    private class DeleteTask extends AsyncTask<Void,Void,Void>{

        Employee employee;
        public DeleteTask(Employee employee){
            this.employee = employee;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            employeeDao.deleteEmployee(employee);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"DeleteTask data deleted");
        }
    }

    private class GetDataTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG,"GetDataTask");
            employeeList = employeeDao.getAllEmployeeData();
            Log.d(TAG,"GetDataTask employeeList size: "+employeeList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"GetDataTask completed");
        }
    }
}
