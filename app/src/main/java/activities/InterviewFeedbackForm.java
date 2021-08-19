package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class InterviewFeedbackForm extends AppCompatActivity {
    boolean networkAvailability=false;
    String token,candidate_id,status,roundNameId,roundName,ed_val,login_id;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    LinearLayout ll_feedbackform;
    List<EditText> allEds = new ArrayList<EditText>();
    Button btn_submit_iff;
    ArrayList<String> list_roundnameid,list_roundname;
    JSONObject jsonObject_rounds;
    List<String> list_edvalues=new ArrayList<>();

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
                        Toast.makeText(InterviewFeedbackForm.this, "Enter "+name, Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        list_edvalues.add(ed_val);
                        String id=list_roundnameid.get(i);

                        try {
                            jsonObject_rounds.put(id,ed_val);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//
//                    if(ed_val.equals("")){
//
//                        break;
//                    }else{
//
//                        String id=list_roundnameid.get(i);
//
//                        try {
//                            jsonObject_rounds.put(id,ed_val);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }

                }

                if(list_edvalues.size()==3){
                    Toast.makeText(InterviewFeedbackForm.this, "success", Toast.LENGTH_SHORT).show();
                }

                System.out.println("list_edvalues===="+list_edvalues);

                //setDataInterviewFeedbackForm();
            }
        });

    }

    private void setDataInterviewFeedbackForm(){
        showBar();

        Call<SetFeedbackFormBean> call= RetrofitClient.getInstance().getApi().setFeedbackForm(
                token,candidate_id,login_id,jsonObject_rounds,"true");
        call.enqueue(new Callback<SetFeedbackFormBean>() {
            @Override
            public void onResponse(Call<SetFeedbackFormBean> call, Response<SetFeedbackFormBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    SetFeedbackFormBean feedbackFormColumnsBean=response.body();

                    status=feedbackFormColumnsBean.getStatus();

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        Utility.showAlertDialog(InterviewFeedbackForm.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<SetFeedbackFormBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(InterviewFeedbackForm.this,"Error",t.getMessage(),false);
            }
        });

    }

    private void getinterviewFeedbackForm() {
        showBar();

        Call<FeedbackFormColumnsBean> call= RetrofitClient.getInstance().getApi().getFeedbackFormColumns("1b0df4c9f482893eaba3e05f474d0903","1");
        call.enqueue(new Callback<FeedbackFormColumnsBean>() {
            @Override
            public void onResponse(Call<FeedbackFormColumnsBean> call, Response<FeedbackFormColumnsBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    FeedbackFormColumnsBean feedbackFormColumnsBean=response.body();

                    status=feedbackFormColumnsBean.getStatus();

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
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        Utility.showAlertDialog(InterviewFeedbackForm.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<FeedbackFormColumnsBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(InterviewFeedbackForm.this,"Error",t.getMessage(),false);
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

}