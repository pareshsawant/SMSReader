package com.example.paresh.smsreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME="PUBG.db";
    public static final String TABLE_NAME="PUBG";
    public static final String COL_1="ID";
    public static final String COL_2="address";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,address TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    //to insert data


    public boolean insertData(String address ){

        SQLiteDatabase db=this.getWritableDatabase();
        // creating instant variable n storing data
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,address);


        //to put data in database table

        // result will strore the value -1 is not inserted

        long result= db.insert(TABLE_NAME,null,contentValues);

        // to check if data are inserted or not

        if (result==-1){

            return false;
        }
        else {

            return true;
        }


    }

    public Cursor displayAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res= db.rawQuery(" SELECT * FROM "+TABLE_NAME,null);
        return res;


    }


    public boolean upadteData(String ID ,String Squad,String Killed,String Dinner){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,ID);
        contentValues.put(COL_2,Squad);

        db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{ID});

        return true;


    }


    public Integer deleteData(String id){

        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{id});
    }


}
