package ca.uwaterloo.newsapp.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import ca.uwaterloo.newsapp.ui.login.LoginActivity;
import ca.uwaterloo.newsapp.ui.news.NewsActivity;
import ca.uwaterloo.newsapp.utils.ACache;
import ca.uwaterloo.newsapp.utils.HttpCode;
import ca.uwaterloo.newsapp.utils.HttpUtils;
import ca.uwaterloo.newsapp.utils.JsonUtil;
import ca.uwaterloo.newsapp.utils.ResponseBody;

import static ca.uwaterloo.newsapp.utils.ACache.TIME_DAY;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_username) EditText _usernameText;
    @BindView(R.id.input_gender) EditText _genderText;
    @BindView(R.id.input_faculty) EditText _facultyText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_department) EditText _departmentText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() throws IOException, JSONException {
        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
        String gender = _genderText.getText().toString();
        String faculty = _facultyText.getText().toString();
        String password = _passwordText.getText().toString();
        String department = _departmentText.getText().toString();

        User user = new User(username,password);
        user.setDepartment(department);
        user.setFaculty(faculty);
        user.setName(name);
        if(gender.equals("Female") | gender.equals("female") ){
            user.setGender(1);
        }else if(gender.equals("Male") | gender.equals("male")){
            user.setGender(0);
        }

        ResponseBody response = new HttpUtils().post("/api/v1/users", JsonUtil.toJson(user));
        System.out.println("$$$$$$$$$$##################"+response.getCode());
        if (response.getCode() <= 299 && response.getCode() >= 200) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            onSignupSuccess(jsonObject.getString("token"), jsonObject.getInt("id"));
        }else {
            onSignupFailed();
        }
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
        startActivity(intent);
        finish();
    }


    public void onSignupSuccess(String token, int id) {
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        ACache.get(this).put("token",token,TIME_DAY*7);
        ACache.get(this).put("id",id,TIME_DAY*7);
        ACache.get(this).put("password",_passwordText.getText().toString(),TIME_DAY*7);

        _signupButton.setEnabled(true);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up Failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }




}