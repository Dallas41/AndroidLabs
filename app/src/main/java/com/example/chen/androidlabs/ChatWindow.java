package com.example.chen.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.chen.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;

public class ChatWindow extends Activity {

    protected ListView listView;
    protected Button sendButton;
    protected EditText editText;
    protected ArrayList<String> stringArrayList = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView=findViewById(R.id.listView);
        sendButton=findViewById(R.id.sendButton);
        editText=findViewById((R.id.editText));
        final ChatDatabaseHelper dhHelper = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = dhHelper.getWritableDatabase();

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        Cursor c = db.rawQuery("select * from " + dhHelper.name,null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            int i = c.getColumnIndex(dhHelper.KEY_MESSAGE);
           stringArrayList.add(c.getString(c.getColumnIndex(dhHelper.KEY_MESSAGE)));
           Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(dhHelper.KEY_MESSAGE)));
           c.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + c.getColumnCount() );

        for(int i=0;i<c.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,c.getColumnName(i));
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnter = editText.getText().toString();
                ContentValues cValues = new ContentValues();
                stringArrayList.add(userEnter);
                cValues.put(KEY_MESSAGE,userEnter);
                db.insert(dhHelper.name,null,cValues);

                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                editText.setText("");
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return stringArrayList.size();
        }

        public String getItem(int position){
            return stringArrayList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
