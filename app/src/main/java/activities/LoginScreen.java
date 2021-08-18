package activities;

import android.app.ProgressDialog;
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
    String status,token,candidateId,empName,email,deptId,deptName,roleId,roleName;

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

        ed_username.setText("Rajeshwari");
        ed_password.setText("cd84d683cc5612c69efe115c80d0b7dc");

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

                checkLogin();

            }
        });

    }

    private void checkLogin() {
        showBar();

        Call<LoginDataBean> call=RetrofitClient.getInstance().getApi().checkLogin(st_ed_username,
                st_ed_password);
        call.enqueue(new Callback<LoginDataBean>() {

            @Override
            public void onResponse(Call<LoginDataBean> call, Response<LoginDataBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    LoginDataBean loginDataBean=response.body();

                    status=loginDataBean.getStatus();
                    token=loginDataBean.getToken();

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
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        Utility.showAlertDialog(LoginScreen.this,"Error",error,false);

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
                Utility.showAlertDialog(LoginScreen.this,"Error",t.getMessage(),false);
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
}