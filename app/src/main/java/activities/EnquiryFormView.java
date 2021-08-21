package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.activities.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.CostSheetViewBean;
import bean.CrmEnquiryFormViewBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class EnquiryFormView extends AppCompatActivity {
    EditText ed_calltype,ed_date,ed_clienttype,ed_companyname,ed_location,ed_address,ed_clientname,ed_contactno,ed_designation,ed_emailid,ed_service,ed_feedback,
            ed_followupdate,ed_department,ed_employeename;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    boolean networkAvailability=false;
    String token,enquiryId,login_id,status,status_message,callType,date,clientType,companyName,location,address,client,mobile,designation,mail,product,
            feedback,followUp,department,employee,fe_feedback,fe_feedbackDate;
    List<EnqFormViewAdapterBean> list=new ArrayList<>();
    EnqFormViewAdapter enqFormViewAdapter;
    RecyclerView rv_feedbackentrydetails;
    CardView cv_feedackentrydetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_form_view);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        enquiryId=i.getStringExtra("enquiryId");
        login_id=i.getStringExtra("login_id");

        System.out.println("token===="+token+" ===== "+enquiryId);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(EnquiryFormView.this);

        if(networkAvailability==true){
            findviewbyids();
        }else {
            Utility.getAlertNetNotConneccted(EnquiryFormView.this, "Internet Connection");
        }
    }

    private void findviewbyids(){
        cv_feedackentrydetails=findViewById(R.id.cv_feedackentrydetails);
        ed_calltype=findViewById(R.id.ed_calltype);
        ed_date=findViewById(R.id.ed_date);
        ed_clienttype=findViewById(R.id.ed_clienttype);
        ed_companyname=findViewById(R.id.ed_companyname);
        ed_location=findViewById(R.id.ed_location);
        ed_address=findViewById(R.id.ed_address);
        ed_clientname=findViewById(R.id.ed_clientname);
        ed_contactno=findViewById(R.id.ed_contactno);
        ed_designation=findViewById(R.id.ed_designation);
        ed_emailid=findViewById(R.id.ed_emailid);
        ed_service=findViewById(R.id.ed_service);
        ed_feedback=findViewById(R.id.ed_feedback);
        ed_followupdate=findViewById(R.id.ed_followupdate);
        ed_department=findViewById(R.id.ed_department);
        ed_employeename=findViewById(R.id.ed_employeename);

        rv_feedbackentrydetails=findViewById(R.id.rv_feedbackentrydetails);
        rv_feedbackentrydetails.setHasFixedSize(true);
        enqFormViewAdapter = new EnqFormViewAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EnquiryFormView.this);
        rv_feedbackentrydetails.setLayoutManager(layoutManager);
        rv_feedbackentrydetails.addItemDecoration(new DividerItemDecoration(EnquiryFormView.this, LinearLayoutManager.VERTICAL));
        rv_feedbackentrydetails.setItemAnimator(new DefaultItemAnimator());
        rv_feedbackentrydetails.setAdapter(enqFormViewAdapter);

        loadJSON();
    }

    private void loadJSON() {

        showBar();
        Call<CrmEnquiryFormViewBean> call= RetrofitClient.getInstance().getApi().getCrmEnquiryFormView("c33f17bfd02f8496665c1e0a0c2248df","26");
        call.enqueue(new Callback<CrmEnquiryFormViewBean>() {

            @Override
            public void onResponse(Call<CrmEnquiryFormViewBean> call, Response<CrmEnquiryFormViewBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmEnquiryFormViewBean crmEnquiryFormViewBean=response.body();
                    status=crmEnquiryFormViewBean.getStatus();

                    if(status.equals("true")){
                        CrmEnquiryFormViewBean.CEnqFormEnqyDetails cEnqFormEnqyDetails=crmEnquiryFormViewBean.getcEnqFormEnqyDetails();

                        callType=cEnqFormEnqyDetails.getCallType();
                        date=cEnqFormEnqyDetails.getDate();
                        clientType=cEnqFormEnqyDetails.getClientType();
                        companyName=cEnqFormEnqyDetails.getCompanyName();
                        location=cEnqFormEnqyDetails.getLocation();
                        address=cEnqFormEnqyDetails.getAddress();
                        client=cEnqFormEnqyDetails.getClient();
                        mobile=cEnqFormEnqyDetails.getMobile();
                        designation=cEnqFormEnqyDetails.getDesignation();
                        mail=cEnqFormEnqyDetails.getMail();
                        product=cEnqFormEnqyDetails.getProduct();
                        feedback=cEnqFormEnqyDetails.getFeedback();
                        followUp=cEnqFormEnqyDetails.getFollowUp();
                        department=cEnqFormEnqyDetails.getDepartment();
                        employee=cEnqFormEnqyDetails.getEmployee();

                        ed_calltype.setText(callType);
                        ed_date.setText(date);
                        ed_clienttype.setText(clientType);
                        ed_companyname.setText(companyName);
                        ed_location.setText(location);
                        ed_address.setText(address);
                        ed_clientname.setText(client);
                        ed_contactno.setText(mobile);
                        ed_designation.setText(designation);
                        ed_emailid.setText(mail);
                        ed_service.setText(product);
                        ed_feedback.setText(feedback);
                        ed_followupdate.setText(followUp);
                        ed_department.setText(department);
                        ed_employeename.setText(employee);

                        if (crmEnquiryFormViewBean.getcEnqFormfeedbackEntryDetailsList()!=null){
                            cv_feedackentrydetails.setVisibility(View.VISIBLE);
                            List<CrmEnquiryFormViewBean.CEnqFormfeedbackEntryDetails> cEnqFormfeedbackEntryDetailsList=crmEnquiryFormViewBean.getcEnqFormfeedbackEntryDetailsList();
                            for(int i=0;i<cEnqFormfeedbackEntryDetailsList.size();i++){
                                fe_feedback=cEnqFormfeedbackEntryDetailsList.get(i).getFeedBack();
                                fe_feedbackDate=cEnqFormfeedbackEntryDetailsList.get(i).getFeedbackDate();

                                EnqFormViewAdapterBean enqFormViewAdapterBean=new EnqFormViewAdapterBean(fe_feedback,fe_feedbackDate);
                                list.add(enqFormViewAdapterBean);
                            }
                            enqFormViewAdapter.notifyDataSetChanged();
                        }else{
                            cv_feedackentrydetails.setVisibility(View.GONE);
                        }

                    }else{
                        status_message=crmEnquiryFormViewBean.getStatus_message();
                        Utility.showMessageDialogue(EnquiryFormView.this,status_message,"Info");
                        new android.app.AlertDialog.Builder(EnquiryFormView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryFormView.this,CostSheet.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        new android.app.AlertDialog.Builder(EnquiryFormView.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryFormView.this,CostSheet.class);
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
            public void onFailure(Call<CrmEnquiryFormViewBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(EnquiryFormView.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(EnquiryFormView.this,CostSheet.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();
            }

        });

    }

    public class EnqFormViewAdapter extends RecyclerView.Adapter<EnqFormViewAdapter.ViewHolder> {
        private List<EnqFormViewAdapterBean> enqFormViewAdapterBeanslist;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_feedback,text_feedbackdate;

            public ViewHolder(View view) {
                super(view);
                text_feedback = (TextView) view.findViewById(R.id.text_feedback);
                text_feedbackdate = (TextView) view.findViewById(R.id.text_feedbackdate);
            }
        }

        public EnqFormViewAdapter(List<EnqFormViewAdapterBean> mlist)
        {
            this.enqFormViewAdapterBeanslist = mlist;
            this.context=context;
        }

        @Override
        public EnqFormViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_enquiryformview, parent, false);

            return new EnqFormViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EnqFormViewAdapter.ViewHolder holder, int position) {
            EnqFormViewAdapterBean data = enqFormViewAdapterBeanslist.get(position);

            holder.text_feedback.setText(data.getFeedback());
            holder.text_feedbackdate.setText(data.getFeedback_date());

        }

        @Override
        public int getItemCount() {
            return enqFormViewAdapterBeanslist.size();
        }
    }

    public class EnqFormViewAdapterBean {
        private String feedback,feedback_date;

        public EnqFormViewAdapterBean(String feedback,String feedback_date) {
            this.feedback = feedback;
            this.feedback_date = feedback_date;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFeedback_date() {
            return feedback_date;
        }

        public void setFeedback_date(String feedback_date) {
            this.feedback_date = feedback_date;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(EnquiryFormView.this);
        progressDialog = new ProgressDialog(EnquiryFormView.this);
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
        Intent MainActivity = new Intent(getBaseContext(), CostSheet.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        EnquiryFormView.this.finish();
    }


}
