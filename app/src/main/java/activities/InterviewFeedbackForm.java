package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.activities.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.FeedbackFormColumnsBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class InterviewFeedbackForm extends AppCompatActivity {
    boolean networkAvailability=false;
    String token,candidate_id,status,roundNameId,roundName,ed_val;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    LinearLayout ll_feedbackform;
    List<EditText> allEds = new ArrayList<EditText>();
    Button btn_submit_iff;
    ArrayList<String> list_roundnameid,list_roundname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_feedback_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        candidate_id=i.getStringExtra("login_id");

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

                for(int i=0; i < allEds.size(); i++){
                    ed_val = allEds.get(i).getText().toString();
                    String name=list_roundname.get(i);
                    String id=list_roundnameid.get(i);

                    if(ed_val.equals("")){

                    }
                }
            }
        });

    }

    private void getinterviewFeedbackForm() {
        showBar();

        Call<FeedbackFormColumnsBean> call= RetrofitClient.getInstance().getApi().
                getFeedbackFormColumns("133d918cf2de6c6cf81fb6d3a08656a7",
                "3");
        call.enqueue(new Callback<FeedbackFormColumnsBean>() {

            @Override
            public void onResponse(Call<FeedbackFormColumnsBean> call, Response<FeedbackFormColumnsBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    FeedbackFormColumnsBean feedbackFormColumnsBean=response.body();

                    status=feedbackFormColumnsBean.getStatus();

                    System.out.println("status===="+status);

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
//                        textView.setGravity(View.FOCUS_LEFT);
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
}