package activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.activities.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bean.LoginDataBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class LoginScreen extends AppCompatActivity {
    EditText ed_username,ed_password;
    String st_ed_username,st_ed_password;
    Button btn_login;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String status,token,candidateId,empName,email,deptId,deptName,roleId,roleName,status_message,st_pwd_mdhash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(LoginScreen.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(LoginScreen.this, "Internet Connection");
        }

    }

    private void findviewids() {

        ed_username=findViewById(R.id.ed_username);
        ed_password=findViewById(R.id.ed_password);

        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_ed_username=ed_username.getText().toString().trim();
                if (st_ed_username.length()==0){
                    Utility.getAlertMsgEnter(LoginScreen.this,"UserName");
                    return;
                }

                st_ed_password=ed_password.getText().toString().trim();
                if (st_ed_password.length()==0){
                    Utility.getAlertMsgEnter(LoginScreen.this,"Password");
                    return;
                }
                st_pwd_mdhash=md5(st_ed_password);
                checkLogin();

            }
        });

    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void checkLogin() {
        showBar();

        Call<LoginDataBean> call=RetrofitClient.getInstance().getApi().checkLogin(st_ed_username,
                st_pwd_mdhash);
        call.enqueue(new Callback<LoginDataBean>() {

            @Override
            public void onResponse(Call<LoginDataBean> call, Response<LoginDataBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    LoginDataBean loginDataBean=response.body();

                    status=loginDataBean.getStatus();
                    token=loginDataBean.getToken();

                    if(status.equals("true")){
                        LoginDataBean.LoginUserDetails loginUserDetails=loginDataBean.getLoginUserDetails();

                        candidateId=loginUserDetails.getCandidateId();
                        empName=loginUserDetails.getEmpName();
                        email=loginUserDetails.getEmail();
                        deptId=loginUserDetails.getDeptId();
                        deptName=loginUserDetails.getDeptName();
                        roleId=loginUserDetails.getRoleId();
                        roleName=loginUserDetails.getRoleName();

                        Intent i =new Intent(getApplicationContext(), MenuScreen.class);
                        i.putExtra("token",token);
                        i.putExtra("login_id",candidateId);
                        startActivity(i);

                    }else{
                        status_message=loginDataBean.getStatus_message();
                        new android.app.AlertDialog.Builder(LoginScreen.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(LoginScreen.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginDataBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(LoginScreen.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    public void showBar(){
        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing Data...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void displayToast() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginScreen.this);
        builder.setTitle("Confirmation");
        builder.setIcon(R.drawable.stop_sign);
        builder.setMessage("Are you sure to quit from this APP?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        displayToast();
    }

}