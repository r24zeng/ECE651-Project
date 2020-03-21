package ca.uwaterloo.newsapp.ui.news;


import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.ui.login.LoginActivity;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpCode;
import ca.uwaterloo.newsapp.utils.HttpUtils;
import ca.uwaterloo.newsapp.utils.JsonUtil;
import ca.uwaterloo.newsapp.utils.ResponseBody;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static ca.uwaterloo.newsapp.utils.ACache.TIME_DAY;


public class edit_profile extends AppCompatActivity {
    private static final String TAG = "EditProfile";
    JSONObject object;
    EditText _edit_name;
    String token;
    EditText _edit_username;
    EditText _edit_gender;
    EditText _edit_faculty;
    EditText _edit_department;
    EditText _check_oldpassword;
    EditText _edit_newpassword;
    Button _btn_saveEdit;
    String data_password;
    int id;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        _edit_name = (EditText)findViewById(R.id.edit_name);
        _edit_username = (EditText)findViewById(R.id.edit_username);
        _edit_gender = (EditText)findViewById(R.id.edit_gender);
        _edit_faculty = (EditText)findViewById(R.id.edit_faculty);
        _edit_department = (EditText)findViewById(R.id.edit_department);
        _check_oldpassword = (EditText)findViewById(R.id.check_oldpassword);
        _btn_saveEdit = (Button) findViewById(R.id.btn_saveEdit);
        _edit_newpassword = (EditText)findViewById(R.id.edit_newpassword);

        try {
            object = new JSONObject(getIntent().getStringExtra("userdata"));
            init_userdata(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _btn_saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = _check_oldpassword.getText().toString();
                Log.d(TAG,"********************* try to get old password");

                try {
                    object = new JSONObject(getIntent().getStringExtra("userdata"));
                    data_password = object.getString("password");
                    id = object.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,oldPassword+"$$$$$$$$$$$$");
                Log.d(TAG,data_password+"$$$$$$$$$$$$$$");
                if(oldPassword.equals(data_password)){
                    try {
                        save(object, id);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getBaseContext(), "password is not correct", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void init_userdata(JSONObject object) throws JSONException {
        _edit_name.setText(object.getString("name"));
        _edit_name.setSelection(object.getString("name").length());

        Log.d(TAG,_edit_name.getText().toString());
        Log.d(TAG,String.valueOf(_edit_name.isEnabled()));

        _edit_username.setText(object.getString("username"));
        _edit_username.setSelection(object.getString("username").length());
        if(object.getInt("gender") == 0){
            _edit_gender.setText(this.getString(R.string.female));
            _edit_gender.setSelection(6);
        }else {
            _edit_gender.setText(this.getString(R.string.male));
            _edit_gender.setSelection(4);
        }

        _edit_faculty.setText(object.getString("faculty"));
        _edit_faculty.setSelection(object.getString("faculty").length());
        _edit_department.setText(object.getString("department"));
        _edit_department.setSelection(object.getString("department").length());
    }

    public void save(JSONObject object, int id) throws IOException, JSONException {
        Log.d(TAG,"start patch new account information");
        _btn_saveEdit.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(edit_profile.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Change Account Information...");
        progressDialog.show();

        User newU = new User();

        if(_edit_name.getText().toString() != ""){
            newU.setName(_edit_name.getText().toString());
        }else {
            newU.setName(object.getString("name"));
        }

        if(_edit_username.getText().toString() != ""){
            newU.setUsername(_edit_username.getText().toString());
        }else {
            newU.setUsername(object.getString("username"));
        }

        if(_edit_gender.getText().toString() != ""){
            if(_edit_gender.getText().toString() == this.getString(R.string.female)){
                newU.setGender(1);
            }else {
                newU.setGender(0);
            }
        }else {
            newU.setGender(object.getInt("gender"));
        }

        if(_edit_faculty.getText().toString() != ""){
            newU.setFaculty(_edit_faculty.getText().toString());
        }else {
            newU.setFaculty(object.getString("faculty"));
        }

        if(_edit_department.getText().toString() != ""){
            newU.setDepartment(_edit_department.getText().toString());
        }else{
            newU.setDepartment(object.getString("department"));
        }

        token = ACache.get(this).getAsString("token");

        if(_edit_newpassword.getText().toString() != ""){
            newU.setPassword(_edit_newpassword.getText().toString());
        }else{
            newU.setPassword(data_password);
        }

        ResponseBody response = new HttpUtils().patch("/api/v1/users/"+id, JsonUtil.toJson(newU),token);
        if (response.getCode() == HttpCode.CONFLICT) {
            onSaveFailed();
        }
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSaveFailed() {
        Toast.makeText(getBaseContext(), "fail to submit", Toast.LENGTH_LONG).show();
        _btn_saveEdit.setEnabled(true);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        onCreate(null);
//    }











}
