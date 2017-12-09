package com.example.chen.androidlabs;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private TextView msView;
    private TextView idView;
    private String msg;
    private Long id;
    private int viewPosition;
    private boolean devicePhone;
    private Button deleteButton;

    public MessageFragment(){
        devicePhone = true;
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(ChatWindow x){
        devicePhone = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_message_fragment, container, false);

        final Bundle info = this.getArguments();
        msg = info.getString("message");
        id = info.getLong("id");
        viewPosition = info.getInt("viewPosition");

        msView = view.findViewById(R.id.singleMessageView);
        msView.setText(msg);

        idView = view.findViewById(R.id.singleIDView);
        idView.setText(Long.toString(id));

        deleteButton = view.findViewById(R.id.deleteMessageButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (devicePhone == true) {
                    Intent intent = new Intent();
                    //send the deletion request to chatwindow class, and name it "forDelete"
                    intent.putExtra("forDelete",info);
                    getActivity().setResult(25, intent);
                    getActivity().finish();
                } else{
                    ((ChatWindow)getActivity()).removeMessage(id,viewPosition );
                }
            }
        });
        return view;
    }
}
