package com.example.paresh.smsreader;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.paresh.smsreader.DatabaseHelper.TABLE_NAME;

// BroadcastReceiver Class To Receive And Read SMS

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    DatabaseHelper db;

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";

             putSmsToDatabase(smsMessage,context);


        }




        Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

        //this will update the UI with message
        MainActivity inst = MainActivity.instance();
        inst.updateList(smsMessageStr);
    }

}



















    private void putSmsToDatabase(SmsMessage sms, Context context) {
        DatabaseHelper dataBaseHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        // Create SMS row
        ContentValues values = new ContentValues();

        values.put("address", sms.getOriginatingAddress().toString());
        values.put("date", mydate);
        values.put("body", sms.getMessageBody().toString());
        // values.put( READ, MESSAGE_IS_NOT_READ );
        // values.put( STATUS, sms.getStatus() );
        // values.put( TYPE, MESSAGE_TYPE_INBOX );
        // values.put( SEEN, MESSAGE_IS_NOT_SEEN );

        db.insert("PUBG", null, values);

        db.close();
    }

}