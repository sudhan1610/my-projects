package com.example.rxjavaretrofitroomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class}, version = 1)
public abstract class EmployeeDatabase extends RoomDatabase {

    public static EmployeeDatabase employeeDB = null;
    public static EmployeeDatabase getInstance(Context context){
        if (employeeDB == null){
            employeeDB = Room.databaseBuilder(context.getApplicationContext(),EmployeeDatabase.class,"Employee_Database").build();
        }
        return employeeDB;
    }
    public abstract EmployeeDao getEmployeeDao();

}
