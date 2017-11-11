package com.mobilizedconstruction;

import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String listItem []={"Login","Create User Account","Department  Details"};
        ListView lvMain=(ListView)findViewById(R.id.lv_main);
        lvMain.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItem));

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if(arg2==0)
                {
                    Intent i=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else if(arg2==1)
                {
                    Intent i=new Intent(MainActivity.this,CreateUserActivity.class);
                    startActivity(i);
                }
                else if(arg2==2)
                {
                    Intent i=new Intent(MainActivity.this,DeptActivity.class);
                    startActivity(i);
                }
            }
        });
    }
*/

    private EditText usernameField,passwordField;
    private TextView status,role,method;
    private Button get, post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);

        status = (TextView)findViewById(R.id.textView6);
        method = (TextView)findViewById(R.id.textView9);
        get = (Button)findViewById(R.id.button1);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                login();
            }
        });
        post = (Button)findViewById(R.id.button2);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                singup();
            }
        });
    }



    public void login(){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        method.setText("Get Method");
        new SigninActivity(this,status,0).execute(username,password);

    }

    public void singup(){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        method.setText("Post Method");
        new SigninActivity(this,status,1).execute(username,password);
    }
}
