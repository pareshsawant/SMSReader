package com.example.paresh.smsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static MainActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    DatabaseHelper db;

    public static MainActivity instance() {
        return inst;
    }
    @Override
    public void onStart() {

        // display();


        // onclick

        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHelper(this);
        smsListView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
       smsListView.setAdapter(arrayAdapter);

       // display();


        // onclick

        /*
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    String[] smsMessages = smsMessagesList.get(position).split("\n");
                    String address = smsMessages[0];
                    String smsMessage = "";
                    for (int i = 1; i < smsMessages.length; ++i) {
                        smsMessage += smsMessages[i];
                    }

                    String smsMessageStr = address + "\n";
                    smsMessageStr += smsMessage;

                    Toast.makeText(MainActivity.this, smsMessageStr, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //end of onclick

        */


        // Add SMS Read Permision At Runtime
        // If Permission Is Not GRANTED
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            //  If Permission Granted Then Show SMS
       display();
       refreshSmsInbox();

        } else {
            //  Then Set Permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }




    }

    public void display(){

        Cursor res=db.displayAll();

        if (res.getCount()==0){

            Toast.makeText(this, "ERROR:No Data To Display", Toast.LENGTH_SHORT).show();
        }
        else {

            ArrayList<String> arrayList=new ArrayList<>();

            while (res.moveToNext()){

               // arrayList.add("ID: "+res.getString(0));
                arrayList.add("Address: "+res.getString(1));

                arrayList.add("::::::::::::::::");

                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
                smsListView.setAdapter(arrayAdapter);

            }

        }

    }









    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }


    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }





}


