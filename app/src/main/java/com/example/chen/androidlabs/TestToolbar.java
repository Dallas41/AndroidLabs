package com.example.chen.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    private static final String Activity_Name = "TestToolbar";
    private Toolbar lab8Toolbar;
    private FloatingActionButton floatingActionButton;
    private Snackbar snackbar;
    private String message = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        lab8Toolbar = findViewById(R.id.lab8Toolbar);
        setSupportActionBar(lab8Toolbar);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar = Snackbar.make(view, "Floating Action Button clicked, Snackbar appears", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override  //to create toolbar by inflating it from xml file:
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    @Override  //respond to one of the items being selected
    public boolean onOptionsItemSelected(MenuItem mi){
        switch (mi.getItemId()){
            case R.id.action_one:
                Log.i(Activity_Name,"action_one is selected");
                snackbar = Snackbar.make(findViewById(R.id.TestToolbarLayout),message,Snackbar.LENGTH_LONG);
                snackbar.show();
                break;

            case R.id.action_two:
                Log.i(Activity_Name,"action_two is selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.lab8_item2_dialog);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_three:
                Log.i(Activity_Name,"action_three is selected");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();
                View view =inflater.inflate(R.layout.custom_alertdialog, null);
                final EditText et = view.findViewById(R.id.item3NewMessage);
                builder1.setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                message = et.getText().toString();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;

            case R.id.about:
                Log.i(Activity_Name,"option about is selected");
                Toast toast = Toast.makeText(getApplicationContext(),R.string.lab8_about_toast,Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }
}
