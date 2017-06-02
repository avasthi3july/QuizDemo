package com.quizflix.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.quizflix.R;
import com.quizflix.Util.Util;
import com.quizflix.dao.BaseResponse;
import com.quizflix.delegates.Api;
import com.quizflix.delegates.ServiceCallBack;
import com.quizflix.webservice.BaseRequest;
import com.quizflix.webservice.JsonDataParser;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

public class SignupActivity extends AppCompatActivity implements ServiceCallBack {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.last_name)
    EditText _lastName;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        SpannableString content = new SpannableString("Already a member? Login");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        _loginLink.setText(content);
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

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

       // _signupButton.setEnabled(false);

        try {
            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String lastName = _lastName.getText().toString();
            String password = _passwordText.getText().toString();
            String reEnterPassword = _reEnterPasswordText.getText().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FirstName", name);
            jsonObject.put("LastName", lastName);
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
            userRegistration(jsonObject);
        } catch (Exception e) {

        }


        // TODO: Implement your own signup logic here.


        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    public void userRegistration(JSONObject jsonObject) {
        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setProgressShow(true);
        baseRequest.setRequestTag(Api.ADD_USER_REGISTRATION);
        baseRequest.setMessage("Please wait...");
        baseRequest.setServiceCallBack(this);
        Api api = (Api) baseRequest.execute(Api.class);
        try {
            TypedByteArray input = new TypedByteArray("application/json", jsonObject.toString().getBytes("UTF-8"));
            api.userRegistreation(input, baseRequest.requestCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String lastName = _lastName.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastName.setError("at least 3 characters");
            valid = false;
        } else {
            _lastName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onSuccess(int tag, String baseResponse) {
        BaseResponse baseData = JsonDataParser.getInternalParser(baseResponse, new TypeToken<BaseResponse>() {
        }.getType());
        if (baseData.getSuccess()) {
            Util.showToast(this, baseData.getMessage());
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else Util.showToast(this, baseData.getMessage());
    }

    @Override
    public void onFail(RetrofitError error) {

    }

    @Override
    public void onNoNetwork() {

    }
}