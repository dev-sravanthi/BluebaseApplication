package activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bluebase.activities.R;

import java.io.IOException;
import java.util.List;

import bean.CandidateViewBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class CandidateView extends AppCompatActivity {
    private EditText ed_deparment,ed_post_applied_for,ed_firstname,ed_lastname,ed_gender,ed_fathersname,ed_dateofbirth,
            ed_address_commun,ed_permanent_address,ed_mob_no,ed_alternate_mob_no,ed_emailid,ed_aadhar_no,ed_pan_no,
            ed_voterid,ed_education_details,ed_emplstatus,ed_yearofpassout,ed_round,ed_ques_name,ed_hodname,ed_mdname,
            ed_marks_scored,ed_technicalname,ed_deptname;
    boolean networkAvailability=false;
    String token,candidate_id;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private String status_message,previousActivity,candidateStatus,designationName,deptName,firstName,lastName,gender,fatherName,dob,address,permanentAddress,phone,
            alternativePhone,email,aadharNo,panNo,voterNo,educationalDetails,employeeStatus,yearOfPass,aplitudeStatus,technicalStatus,technicalName,departmentName,
            sectionName,feedBack,hodStatus,hodName,mdStatus,mdName,totalMarks,login_id;
    CardView cv_feedback_apt,cv_technical,cv_technical_feedback,cv_hodstatus,cv_hod_feedback,cv_md_feedback,cv_mdstatus;
    TextView text_feedback_apt,text_technical,text_technical_feedback,text_hodstatus,text_hod_feedback,text_md_feedback;
    LinearLayout ll_feedback_apt,ll_technical,ll_technical_feedback,ll_hodstatus,ll_hod_feedback,ll_md_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_view);

        Intent i=getIntent();
        previousActivity= i.getStringExtra("FROM_ACTIVITY");
        token=i.getStringExtra("token");
        if(previousActivity.equals("CandidateList")){
            candidate_id=i.getStringExtra("candidateId");
            login_id=i.getStringExtra("login_id");
        }else if(previousActivity.equals("FeedbackCandidateList")){
            candidate_id=i.getStringExtra("feedback_candidateId");
            login_id=i.getStringExtra("login_id");
        }

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(CandidateView.this);

        if(networkAvailability==true){
            findViewids();
        }else{
            Utility.getAlertNetNotConneccted(CandidateView.this, "Internet Connection");
        }

    }

    private void findViewids() {
        ed_deparment=findViewById(R.id.ed_deparment);
        ed_post_applied_for=findViewById(R.id.ed_post_applied_for);
        ed_firstname=findViewById(R.id.ed_firstname);
        ed_lastname=findViewById(R.id.ed_lastname);
        ed_gender=findViewById(R.id.ed_gender);
        ed_fathersname=findViewById(R.id.ed_fathersname);
        ed_dateofbirth=findViewById(R.id.ed_dateofbirth);
        ed_address_commun=findViewById(R.id.ed_address_commun);
        ed_permanent_address=findViewById(R.id.ed_permanent_address);
        ed_mob_no=findViewById(R.id.ed_mob_no);
        ed_alternate_mob_no=findViewById(R.id.ed_alternate_mob_no);
        ed_emailid=findViewById(R.id.ed_emailid);
        ed_aadhar_no=findViewById(R.id.ed_aadhar_no);
        ed_pan_no=findViewById(R.id.ed_pan_no);
        ed_voterid=findViewById(R.id.ed_voterid);
        ed_education_details=findViewById(R.id.ed_education_details);
        ed_emplstatus=findViewById(R.id.ed_emplstatus);
        ed_yearofpassout=findViewById(R.id.ed_yearofpassout);
        ed_round=findViewById(R.id.ed_round);
        ed_ques_name=findViewById(R.id.ed_ques_name);

        cv_feedback_apt=findViewById(R.id.cv_feedback_apt);
        text_feedback_apt=findViewById(R.id.text_feedback_apt);
        ll_feedback_apt=findViewById(R.id.ll_feedback_apt);
        ed_marks_scored=findViewById(R.id.ed_marks_scored);

        cv_technical=findViewById(R.id.cv_technical);
        text_technical=findViewById(R.id.text_technical);
        ll_technical=findViewById(R.id.ll_technical);
        ed_technicalname=findViewById(R.id.ed_technicalname);
        ed_deptname=findViewById(R.id.ed_deptname);

        cv_technical_feedback=findViewById(R.id.cv_technical_feedback);
        text_technical_feedback=findViewById(R.id.text_technical_feedback);
        ll_technical_feedback=findViewById(R.id.ll_technical_feedback);

        cv_hodstatus=findViewById(R.id.cv_hodstatus);
        text_hodstatus=findViewById(R.id.text_hodstatus);
        ll_hodstatus=findViewById(R.id.ll_hodstatus);
        ed_hodname=findViewById(R.id.ed_hodname);

        cv_hod_feedback=findViewById(R.id.cv_hod_feedback);
        text_hod_feedback=findViewById(R.id.text_hod_feedback);
        ll_hod_feedback=findViewById(R.id.ll_hod_feedback);

        cv_md_feedback=findViewById(R.id.cv_md_feedback);
        cv_md_feedback=findViewById(R.id.cv_md_feedback);
        text_md_feedback=findViewById(R.id.text_md_feedback);
        ll_md_feedback=findViewById(R.id.ll_md_feedback);
        ed_mdname=findViewById(R.id.ed_mdname);
        cv_mdstatus=findViewById(R.id.cv_mdstatus);

        loadJSON_getCandidateView();
    }

    private void loadJSON_getCandidateView() {
        showBar();

        Call<CandidateViewBean> call= RetrofitClient.getInstance().getApi().
                getCandidateView(token,candidate_id);
        call.enqueue(new Callback<CandidateViewBean>() {
            @Override
            public void onResponse(Call<CandidateViewBean> call, Response<CandidateViewBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CandidateViewBean candidateViewBean=response.body();
                    String status_main=candidateViewBean.getStatus();

                    if(status_main.equals("true")){
                        CandidateViewBean.CV_candidateDetails cv_candidateDetails=candidateViewBean.getCv_candidateDetails();

                        if(cv_candidateDetails.getCandidateStatus()==null || cv_candidateDetails.getCandidateStatus()==""){
                            candidateStatus="Not Available";
                        }else{
                            candidateStatus=cv_candidateDetails.getCandidateStatus();
                        }

                        if(cv_candidateDetails.getDesignationName()==null || cv_candidateDetails.getDesignationName()==""){
                            designationName="Not Available";
                        }else{
                            designationName=cv_candidateDetails.getDesignationName();
                        }

                        if(cv_candidateDetails.getDeptName()==null || cv_candidateDetails.getDeptName()==""){
                            deptName="Not Available";
                        }else{
                            deptName=cv_candidateDetails.getDeptName();
                        }

                        if(cv_candidateDetails.getFirstName()==null || cv_candidateDetails.getFirstName()==""){
                            firstName="Not Available";
                        }else{
                            firstName=cv_candidateDetails.getFirstName();
                        }

                        if(cv_candidateDetails.getLastName()==null || cv_candidateDetails.getLastName()==""){
                            lastName="Not Available";
                        }else{
                            lastName=cv_candidateDetails.getLastName();
                        }

                        if(cv_candidateDetails.getFatherName()==null || cv_candidateDetails.getFatherName()==""){
                            fatherName="Not Available";
                        }else{
                            fatherName=cv_candidateDetails.getFatherName();
                        }

                        if(cv_candidateDetails.getGender()==null || cv_candidateDetails.getGender()==""){
                            gender="Not Available";
                        }else{
                            gender=cv_candidateDetails.getGender();
                        }

                        if(cv_candidateDetails.getDob()==null || cv_candidateDetails.getDob()==""){
                            dob="Not Available";
                        }else{
                            dob=cv_candidateDetails.getDob();
                        }

                        if(cv_candidateDetails.getAddress()==null || cv_candidateDetails.getAddress()==""){
                            address="Not Available";
                        }else{
                            address=cv_candidateDetails.getAddress();
                        }

                        if(cv_candidateDetails.getPermanentAddress()==null || cv_candidateDetails.getPermanentAddress()==""){
                            permanentAddress="Not Available";
                        }else{
                            permanentAddress=cv_candidateDetails.getPermanentAddress();
                        }

                        if(cv_candidateDetails.getPhone()==null || cv_candidateDetails.getPhone()==""){
                            phone="Not Available";
                        }else{
                            phone=cv_candidateDetails.getPhone();
                        }

                        if(cv_candidateDetails.getAlternativePhone()==null || cv_candidateDetails.getAlternativePhone()==""){
                            alternativePhone="Not Available";
                        }else{
                            alternativePhone=cv_candidateDetails.getAlternativePhone();
                        }

                        if(cv_candidateDetails.getEmail()==null || cv_candidateDetails.getEmail()==""){
                            email="Not Available";
                        }else{
                            email=cv_candidateDetails.getEmail();
                        }

                        if(cv_candidateDetails.getAadharNo()==null || cv_candidateDetails.getAadharNo()==""){
                            aadharNo="Not Available";
                        }else{
                            aadharNo=cv_candidateDetails.getAadharNo();
                        }

                        if(cv_candidateDetails.getPanNo()==null || cv_candidateDetails.getPanNo()==""){
                            panNo="Not Available";
                        }else{
                            panNo=cv_candidateDetails.getPanNo();
                        }

                        if(cv_candidateDetails.getVoterNo()==null || cv_candidateDetails.getVoterNo()==""){
                            voterNo="Not Available";
                        }else{
                            voterNo=cv_candidateDetails.getVoterNo();
                        }

                        if(cv_candidateDetails.getEducationalDetails()==null || cv_candidateDetails.getEducationalDetails()==""){
                            educationalDetails="Not Available";
                        }else{
                            educationalDetails=cv_candidateDetails.getEducationalDetails();
                        }

                        if(cv_candidateDetails.getEmployeeStatus()==null || cv_candidateDetails.getEmployeeStatus()==""){
                            employeeStatus="Not Available";
                        }else{
                            employeeStatus=cv_candidateDetails.getEmployeeStatus();
                        }

                        if(cv_candidateDetails.getYearOfPass()==null || cv_candidateDetails.getYearOfPass()==""){
                            yearOfPass="Not Available";
                        }else{
                            yearOfPass=cv_candidateDetails.getYearOfPass();
                        }

                        ed_post_applied_for.setText(designationName);
                        ed_post_applied_for.setEnabled(false);
                        ed_post_applied_for.setBackgroundResource(R.drawable.editextbg);

                        ed_deparment.setText(deptName);
                        ed_deparment.setEnabled(false);
                        ed_deparment.setBackgroundResource(R.drawable.editextbg);

                        ed_firstname.setText(firstName);
                        ed_firstname.setEnabled(false);
                        ed_firstname.setBackgroundResource(R.drawable.editextbg);

                        ed_lastname.setText(lastName);
                        ed_lastname.setEnabled(false);
                        ed_lastname.setBackgroundResource(R.drawable.editextbg);

                        ed_gender.setText(gender);
                        ed_gender.setEnabled(false);
                        ed_gender.setBackgroundResource(R.drawable.editextbg);

                        ed_fathersname.setText(fatherName);
                        ed_fathersname.setEnabled(false);
                        ed_fathersname.setBackgroundResource(R.drawable.editextbg);

                        ed_dateofbirth.setText(dob);
                        ed_dateofbirth.setEnabled(false);
                        ed_dateofbirth.setBackgroundResource(R.drawable.editextbg);

                        ed_address_commun.setText(address);
                        ed_address_commun.setEnabled(false);
                        ed_address_commun.setBackgroundResource(R.drawable.editextbg);

                        ed_permanent_address.setText(permanentAddress);
                        ed_permanent_address.setEnabled(false);
                        ed_permanent_address.setBackgroundResource(R.drawable.editextbg);

                        ed_mob_no.setText(phone);
                        ed_mob_no.setEnabled(false);
                        ed_mob_no.setBackgroundResource(R.drawable.editextbg);

                        ed_alternate_mob_no.setText(alternativePhone);
                        ed_alternate_mob_no.setEnabled(false);
                        ed_alternate_mob_no.setBackgroundResource(R.drawable.editextbg);

                        ed_emailid.setText(email);
                        ed_emailid.setEnabled(false);
                        ed_emailid.setBackgroundResource(R.drawable.editextbg);

                        ed_aadhar_no.setText(aadharNo);
                        ed_aadhar_no.setEnabled(false);
                        ed_aadhar_no.setBackgroundResource(R.drawable.editextbg);

                        ed_pan_no.setText(panNo);
                        ed_pan_no.setEnabled(false);
                        ed_pan_no.setBackgroundResource(R.drawable.editextbg);

                        ed_voterid.setText(voterNo);
                        ed_voterid.setEnabled(false);
                        ed_voterid.setBackgroundResource(R.drawable.editextbg);

                        ed_education_details.setText(educationalDetails);
                        ed_education_details.setEnabled(false);
                        ed_education_details.setBackgroundResource(R.drawable.editextbg);

                        ed_emplstatus.setText(employeeStatus);
                        ed_emplstatus.setEnabled(false);
                        ed_emplstatus.setBackgroundResource(R.drawable.editextbg);

                        ed_yearofpassout.setText(yearOfPass);
                        ed_yearofpassout.setEnabled(false);
                        ed_yearofpassout.setBackgroundResource(R.drawable.editextbg);

                        ed_round.setText("");
                        ed_round.setEnabled(false);
                        ed_round.setBackgroundResource(R.drawable.editextbg);

                        ed_ques_name.setText("");
                        ed_ques_name.setEnabled(false);
                        ed_ques_name.setBackgroundResource(R.drawable.editextbg);

                        CandidateViewBean.CV_feedbacks cv_feedbacks=candidateViewBean.getCv_feedbacks();
                        aplitudeStatus=cv_feedbacks.getAplitudeStatus();
                        technicalStatus=cv_feedbacks.getTechnicalStatus();
                        hodStatus=cv_feedbacks.getHodStatus();
                        mdStatus=cv_feedbacks.getMdStatus();

                        if(aplitudeStatus.equals("true")) {
                            CandidateViewBean.CV_feedbacks.Feedback_aplitude feedback_aplitude = cv_feedbacks.getFeedback_aplitude();
                            totalMarks = feedback_aplitude.getTotalMarks();

                            cv_feedback_apt.setVisibility(View.VISIBLE);
                            text_feedback_apt.setText("Apptitude & Logical Marks");

                            ed_marks_scored.setText(totalMarks);
                            ed_marks_scored.setEnabled(false);
                            ed_marks_scored.setBackgroundResource(R.drawable.editextbg);
                        }

                        if(technicalStatus.equals("true")){
                            CandidateViewBean.CV_feedbacks.Feedback_technicalStatus feedback_technicalStatus=cv_feedbacks.getFeedback_technicalStatus();
                            technicalName=feedback_technicalStatus.getTechnicalName();
                            departmentName=feedback_technicalStatus.getDepartmentName();

                            cv_technical.setVisibility(View.VISIBLE);
                            text_technical.setText("Technical Dept");

                            ed_technicalname.setText(technicalName);
                            ed_deptname.setText(departmentName);
                            ed_technicalname.setEnabled(false);
                            ed_technicalname.setBackgroundResource(R.drawable.editextbg);
                            ed_deptname.setEnabled(false);
                            ed_deptname.setBackgroundResource(R.drawable.editextbg);

                            List<CandidateViewBean.CV_feedbacks.Feedback_technicalStatus.technicalFeedback> technicalFeedbackList=
                                    feedback_technicalStatus.getTechnicalFeedbackList();

                            if(technicalFeedbackList.size()>0){

                                cv_technical_feedback.setVisibility(View.VISIBLE);
                                text_technical_feedback.setText("Technical Skill Feedback Details");
                                for(int i=0;i<technicalFeedbackList.size();i++){
                                    sectionName=technicalFeedbackList.get(i).getSectionName();
                                    feedBack=technicalFeedbackList.get(i).getFeedBack();

                                    LinearLayout myRoot = (LinearLayout) findViewById(R.id.ll_technical_feedback);
                                    myRoot.setOrientation(LinearLayout.VERTICAL);

                                    LinearLayout ll_child = new LinearLayout(CandidateView.this);
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
                                    TextView textView = new TextView(CandidateView.this);
                                    textView.setText(sectionName);
                                    textView.setTextSize(18);
                                    textView.setTextColor(Color.parseColor("#000000"));
                                    textView.setTypeface(typeface,Typeface.BOLD);
                                    textView.setLayoutParams(leftLabelParams);

                                    LinearLayout.LayoutParams ed_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                    EditText editText = new EditText(CandidateView.this);
                                    ed_params.gravity=Gravity.CENTER;
                                    editText.setTextSize(18);
                                    editText.setLayoutParams(ed_params);
                                    editText.setText(feedBack);
                                    editText.setBackgroundResource(R.drawable.editextbg);
                                    editText.setEnabled(false);
                                    editText.setTextColor(Color.parseColor("#000000"));

                                    ll_child.addView(textView);
                                    ll_child.addView(editText);

                                    ll_technical_feedback.addView(ll_child);

                                }

                            }

                        }

                        if(hodStatus.equals("true")){
                            CandidateViewBean.CV_feedbacks.Feedback_hod feedback_hod=cv_feedbacks.getFeedback_hod();
                            hodName=feedback_hod.getHodName();

                            cv_hodstatus.setVisibility(View.VISIBLE);
                            text_hodstatus.setText("HOD Department");

                            ed_hodname.setText(hodName);
                            ed_hodname.setEnabled(false);
                            ed_hodname.setBackgroundResource(R.drawable.editextbg);


                            if(feedback_hod.getHodFeedbackList()!=null){
                                List<CandidateViewBean.CV_feedbacks.Feedback_hod.hodFeedback> feedback_hodList=
                                        feedback_hod.getHodFeedbackList();

                                if(feedback_hodList.size()>0){

                                    cv_hod_feedback.setVisibility(View.VISIBLE);
                                    text_hod_feedback.setText("HOD Feedback Details");
                                    for(int i=0;i<feedback_hodList.size();i++){
                                        sectionName=feedback_hodList.get(i).getSectionName();
                                        feedBack=feedback_hodList.get(i).getFeedBack();

                                        LinearLayout myRoot = (LinearLayout) findViewById(R.id.ll_hod_feedback);
                                        myRoot.setOrientation(LinearLayout.VERTICAL);

                                        LinearLayout ll_child = new LinearLayout(CandidateView.this);
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
                                        TextView textView = new TextView(CandidateView.this);
                                        textView.setText(sectionName);
                                        textView.setTextSize(18);
                                        textView.setTextColor(Color.parseColor("#000000"));
                                        textView.setTypeface(typeface,Typeface.BOLD);
                                        textView.setLayoutParams(leftLabelParams);

                                        LinearLayout.LayoutParams ed_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                        EditText editText = new EditText(CandidateView.this);
                                        ed_params.gravity=Gravity.CENTER;
                                        editText.setTextSize(18);
                                        editText.setLayoutParams(ed_params);
                                        editText.setText(feedBack);
                                        editText.setBackgroundResource(R.drawable.editextbg);
                                        editText.setEnabled(false);
                                        editText.setTextColor(Color.parseColor("#000000"));

                                        ll_child.addView(textView);
                                        ll_child.addView(editText);

                                        ll_hod_feedback.addView(ll_child);

                                    }

                                }

                            }
                        }

                        if(mdStatus.equals("true")){
                            CandidateViewBean.CV_feedbacks.Feedback_md feedback_md=cv_feedbacks.getFeedback_md();
                            mdName=feedback_md.getMdName();

                            cv_mdstatus.setVisibility(View.VISIBLE);
                            ed_mdname.setText(mdName);
                            ed_mdname.setEnabled(false);
                            ed_mdname.setBackgroundResource(R.drawable.editextbg);

                            if(feedback_md.getMdFeedbackList()!=null){
                                List<CandidateViewBean.CV_feedbacks.Feedback_md.mdFeedback> mdFeedbackList=
                                        feedback_md.getMdFeedbackList();

                                if(mdFeedbackList.size()>0){

                                    cv_md_feedback.setVisibility(View.VISIBLE);
                                    text_md_feedback.setText("MD Feedback Details");
                                    for(int i=0;i<mdFeedbackList.size();i++){
                                        sectionName=mdFeedbackList.get(i).getSectionName();
                                        feedBack=mdFeedbackList.get(i).getFeedBack();

                                        LinearLayout myRoot = (LinearLayout) findViewById(R.id.ll_md_feedback);
                                        myRoot.setOrientation(LinearLayout.VERTICAL);

                                        LinearLayout ll_child = new LinearLayout(CandidateView.this);
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
                                        TextView textView = new TextView(CandidateView.this);
                                        textView.setText(sectionName);
                                        textView.setTextSize(18);
                                        textView.setTextColor(Color.parseColor("#000000"));
                                        textView.setTypeface(typeface,Typeface.BOLD);
                                        textView.setLayoutParams(leftLabelParams);

                                        LinearLayout.LayoutParams ed_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                        EditText editText = new EditText(CandidateView.this);
                                        ed_params.gravity=Gravity.CENTER;
                                        editText.setTextSize(18);
                                        editText.setLayoutParams(ed_params);
                                        editText.setText(feedBack);
                                        editText.setBackgroundResource(R.drawable.editextbg);
                                        editText.setEnabled(false);
                                        editText.setTextColor(Color.parseColor("#000000"));

                                        ll_child.addView(textView);
                                        ll_child.addView(editText);

                                        ll_md_feedback.addView(ll_child);

                                    }

                                }

                            }


                        }
                    }else{
                        status_message=candidateViewBean.getStatus_message();
                        new android.app.AlertDialog.Builder(CandidateView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(previousActivity.equals("CandidateList")){
                                            Intent i=new Intent(CandidateView.this,CandidateList.class);
                                            i.putExtra("token",token);
                                            startActivity(i);
                                            finish();
                                        }else if(previousActivity.equals("FeedbackCandidateList")){
                                            Intent i=new Intent(CandidateView.this,FeedbackCandidateList.class);
                                            i.putExtra("token",token);
                                            i.putExtra("login_id",login_id);
                                            startActivity(i);
                                            finish();
                                        }

                                    }
                                }).show();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        new android.app.AlertDialog.Builder(CandidateView.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(previousActivity.equals("CandidateList")){
                                            Intent i=new Intent(CandidateView.this,CandidateList.class);
                                            i.putExtra("token",token);
                                            startActivity(i);
                                            finish();
                                        }else if(previousActivity.equals("FeedbackCandidateList")){
                                            Intent i=new Intent(CandidateView.this,FeedbackCandidateList.class);
                                            i.putExtra("token",token);
                                            i.putExtra("login_id",login_id);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CandidateViewBean> call, Throwable t) {
                new android.app.AlertDialog.Builder(CandidateView.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(previousActivity.equals("CandidateList")){
                                    Intent i=new Intent(CandidateView.this,CandidateList.class);
                                    i.putExtra("token",token);
                                    startActivity(i);
                                    finish();
                                }else if(previousActivity.equals("FeedbackCandidateList")){
                                    Intent i=new Intent(CandidateView.this,FeedbackCandidateList.class);
                                    i.putExtra("token",token);
                                    i.putExtra("login_id",login_id);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).show();
                progressDialog.dismiss();
            }

        });
    }

    public void showBar(){
        builder = new AlertDialog.Builder(CandidateView.this);
        progressDialog = new ProgressDialog(CandidateView.this);
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
    public void onBackPressed() {
        if(previousActivity.equals("CandidateList")){
            Intent MainActivity = new Intent(getBaseContext(), CandidateList.class);
            MainActivity.putExtra("token",token);
            MainActivity.putExtra("login_id",login_id);
            MainActivity.addCategory(Intent.CATEGORY_HOME);
            MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(MainActivity);
            CandidateView.this.finish();
        }else if(previousActivity.equals("FeedbackCandidateList")){
            Intent MainActivity = new Intent(getBaseContext(), FeedbackCandidateList.class);
            MainActivity.putExtra("token",token);
            MainActivity.putExtra("login_id",login_id);
            MainActivity.addCategory(Intent.CATEGORY_HOME);
            MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(MainActivity);
            CandidateView.this.finish();
        }

    }

}
