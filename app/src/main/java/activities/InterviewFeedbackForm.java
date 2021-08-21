package activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.activities.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.FeedbackFormColumnsBean;
import bean.SetFeedbackFormBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class InterviewFeedbackForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    boolean networkAvailability=false;
    String token,candidate_id,status,roundNameId,roundName,ed_val,login_id,st_spin_status,selected_status,status_message,set_status_message;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    LinearLayout ll_feedbackform;
    List<EditText> allEds = new ArrayList<EditText>();
    Button btn_submit_iff;
    ArrayList<String> list_roundnameid,list_roundname;
    JSONObject jsonObject_rounds;
    List<String> list_edvalues=new ArrayList<>();
    Spinner spin_status;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_feedback_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");
        candidate_id=i.getStringExtra("feedback_candidateId");

        System.out.println(token+" ====== "+candidate_id+"======= "+login_id);

        jsonObject_rounds=new JSONObject();

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(InterviewFeedbackForm.this);

        if(networkAvailability==true){
            findViewids();
        }else{
            Utility.getAlertNetNotConneccted(InterviewFeedbackForm.this, "Internet Connection");
        }

    }

    private void findViewids() {

        spin_status=findViewById(R.id.spin_status);
        spin_status.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(InterviewFeedbackForm.this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_status.setAdapter(adapter);

        spin_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_spin_status = spin_status.getSelectedItem().toString();
                System.out.println("st_spin_statu====="+st_spin_status);
                if(st_spin_status.equals("Select for Next Level")){
                    selected_status="true";
                }else if(st_spin_status.equals("Not Selected")){
                    selected_status="false";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        list_roundname=new ArrayList<>();
        list_roundnameid=new ArrayList<>();

        ll_feedbackform=findViewById(R.id.ll_feedbackform);
        getinterviewFeedbackForm();

        btn_submit_iff=findViewById(R.id.btn_submit_iff);
        btn_submit_iff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_edvalues.clear();
                for(int i=0; i < allEds.size(); i++){
                    ed_val = allEds.get(i).getText().toString();
                    String name=list_roundname.get(i);

                    if(ed_val.equals("")){
                        builder.setMessage("Enter "+name).setCancelable(true).create().show();
                        return;
                    }else{
                        list_edvalues.add(ed_val);
                        String id=list_roundnameid.get(i);

                        try {
                            jsonObject_rounds.put(id,ed_val);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if(list_edvalues.size()==3){
                    if(st_spin_status.equals("-Select-")){
                        builder.setMessage("Please Select Status").setCancelable(true).create().show();
                        return;
                    }
                    setDataInterviewFeedbackForm();
                }

            }
        });

    }

    private void setDataInterviewFeedbackForm(){
        showBar();

        Call<SetFeedbackFormBean> call= RetrofitClient.getInstance().getApi().setFeedbackForm(
                token,candidate_id,login_id,jsonObject_rounds,selected_status);
        call.enqueue(new Callback<SetFeedbackFormBean>() {
            @Override
            public void onResponse(Call<SetFeedbackFormBean> call, Response<SetFeedbackFormBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    SetFeedbackFormBean setFeedbackFormBean=response.body();

                    status=setFeedbackFormBean.getStatus();

                    if(status.equals("true")){
                        status_message=setFeedbackFormBean.getStatus_message();
                        new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                                .setCancelable(false)
                                .setIcon(R.drawable.success)
                                .setTitle("Success")
                                .setMessage("Interview Feedback Submmited")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        set_status_message=setFeedbackFormBean.getStatus_message();
                        new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    try {
                        new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<SetFeedbackFormBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                        .setCancelable(false)
                        .setTitle("Info")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();            }
        });

    }

    private void getinterviewFeedbackForm() {
        showBar();

        Call<FeedbackFormColumnsBean> call= RetrofitClient.getInstance().getApi().getFeedbackFormColumns(token,login_id);
        call.enqueue(new Callback<FeedbackFormColumnsBean>() {
            @Override
            public void onResponse(Call<FeedbackFormColumnsBean> call, Response<FeedbackFormColumnsBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    FeedbackFormColumnsBean feedbackFormColumnsBean=response.body();

                    status=feedbackFormColumnsBean.getStatus();

                    if(status.equals("true")){
                        List<FeedbackFormColumnsBean.FFC_result> ffc_resultList=feedbackFormColumnsBean.getFfc_resultList();

                        for(int i=0;i<ffc_resultList.size();i++){
                            roundNameId=ffc_resultList.get(i).getRoundNameId();
                            roundName=ffc_resultList.get(i).getRoundName();

                            list_roundname.add(roundName);
                            list_roundnameid.add(roundNameId);

                            LinearLayout myRoot = (LinearLayout) findViewById(R.id.ll_feedbackform);
                            myRoot.setOrientation(LinearLayout.VERTICAL);

                            LinearLayout ll_child = new LinearLayout(InterviewFeedbackForm.this);
                            ll_child.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams attributLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            attributLayoutParams.gravity = Gravity.CENTER;
                            attributLayoutParams.setMargins(70,60,70,0);
                            ll_child.setLayoutParams(attributLayoutParams);
                            ll_child.setGravity(Gravity.CENTER);

                            Typeface typeface =  Typeface.createFromAsset(getAssets(),"Sansation-Bold.ttf");

                            LinearLayout.LayoutParams leftLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                            leftLabelParams.gravity = Gravity.LEFT;
                            leftLabelParams.setMargins(0,0,0,30);
                            TextView textView = new TextView(InterviewFeedbackForm.this);
                            textView.setText(roundName);
                            textView.setTextSize(18);
                            textView.setTextColor(Color.parseColor("#000000"));
                            textView.setTypeface(typeface,Typeface.BOLD);
                            textView.setLayoutParams(leftLabelParams);

                            LinearLayout.LayoutParams ed_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                            EditText editText = new EditText(InterviewFeedbackForm.this);
                            ed_params.gravity=Gravity.CENTER;
                            editText.setLayoutParams(ed_params);
                            allEds.add(editText);
                            editText.setBackgroundResource(R.drawable.edittext_rounded_box);
//                        editText.setHint("enter "+roundName);

                            ll_child.addView(textView);
                            ll_child.addView(editText);

                            ll_feedbackform.addView(ll_child);

                        }

                    }else{
                        status_message=feedbackFormColumnsBean.getStatus_message();
                        new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }


                }else{
                    progressDialog.dismiss();
                    try {
                        new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<FeedbackFormColumnsBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(InterviewFeedbackForm.this)
                        .setCancelable(false)
                        .setTitle("Info")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(InterviewFeedbackForm.this,FeedbackCandidateList.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();            }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), FeedbackCandidateList.class);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.putExtra("token",token);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        InterviewFeedbackForm.this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}