package com.example.chen.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    protected FrameLayout tabletLayOut;
    protected Boolean onTablet;
    protected Cursor c;
    private SQLiteDatabase writeableDB;
    private ChatDatabaseHelper dhHelper;
    private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView=findViewById(R.id.listView);
        sendButton=findViewById(R.id.sendButton);
        editText=findViewById((R.id.editText));

        tabletLayOut=findViewById(R.id.tableFrameLayout);
        if(tabletLayOut == null){
            onTablet=false;
            Log.i(ACTIVITY_NAME,"On Phone Layout");
        }
        else{
            onTablet=true;
            Log.i(ACTIVITY_NAME,"On Tablet Layout");
        }

        messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        dhHelper = new ChatDatabaseHelper(this);
        writeableDB = dhHelper.getWritableDatabase();
        c = writeableDB.rawQuery("select * from " + dhHelper.name,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            int i = c.getColumnIndex(dhHelper.KEY_MESSAGE);
           stringArrayList.add(c.getString(i));
           c.moveToNext();
        }

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

                writeableDB.insert(dhHelper.name,null,cValues);
                editText.setText("");
                //update cursor, this statement is added for lab7
                c = writeableDB.rawQuery("select * from " + dhHelper.name,null);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override    //the parameter "int i" means the position on the listview
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get info from the click item
                String singleMessage = messageAdapter.getItem(i);
                Long singleId = messageAdapter.getItemId(i);

                Bundle bd = new Bundle();
                bd.putString("message", singleMessage);
                bd.putLong("id",singleId);
                bd.putInt("viewPosition",i);

                if(onTablet==true){
                    MessageFragment mf = new MessageFragment(ChatWindow.this);
                    mf.setArguments(bd);
                    FragmentTransaction ft =  getFragmentManager().beginTransaction();
                    ft.replace(R.id.tableFrameLayout,mf);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else{
                    Intent phoneIntent = new Intent(ChatWindow.this, MessageDetails.class);
                    phoneIntent.putExtra("bundle",bd); //pack the info and give it a name "bundle"
                    startActivityForResult(phoneIntent,21);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == 25) {
                Bundle args =  data.getBundleExtra("forDelete");
                Long keyID = args.getLong("id");
                int viewPosition = args.getInt("viewPosition");
                removeMessage(keyID, viewPosition);
        }
    }

    protected void removeMessage(Long id, int viewPosition){
        stringArrayList.remove(viewPosition);
        writeableDB.delete(ChatDatabaseHelper.name, ChatDatabaseHelper.Key_ID + "=" + id, null);
        c = writeableDB.rawQuery("select * from " + dhHelper.name,null);
        //this will update the listview that show on the screen
        messageAdapter.notifyDataSetChanged();
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
            View result = null;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            String x;
            x = c.getString(c.getColumnIndex(ChatDatabaseHelper.Key_ID));
            return Long.parseLong(x);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
