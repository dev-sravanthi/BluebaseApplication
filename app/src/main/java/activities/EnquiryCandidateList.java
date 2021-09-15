package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.activities.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.CostSheetListBean;
import bean.CrmEnquiryListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class EnquiryCandidateList extends AppCompatActivity {
    String token,login_id,status,enquiryId,callType,date,client,location,contactNumber,followUpDate,employee,resstatus,status_message;
    boolean networkAvailability=false;
    RecyclerView recyview_costlist;
    EnquiryCanListAdapter enquiryCanListAdapter;
    List<EnquiryCanListAdapterBean> enquiryCanListAdapterBeanList=new ArrayList<>();
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Button btn_newenquiryform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_candidate_list);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(EnquiryCandidateList.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(EnquiryCandidateList.this, "Internet Connection");
        }


    }

    private void findviewids() {

        btn_newenquiryform=findViewById(R.id.btn_newenquiryform);
        btn_newenquiryform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EnquiryCandidateList.this,NewEnquiryForm.class);
                i.putExtra("token",token);
                i.putExtra("login_id",login_id);
                startActivity(i);
                finish();
            }
        });

        recyview_costlist=findViewById(R.id.recyview_costlist);
        recyview_costlist.setHasFixedSize(true);
        enquiryCanListAdapter = new EnquiryCanListAdapter(enquiryCanListAdapterBeanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EnquiryCandidateList.this);
        recyview_costlist.setLayoutManager(layoutManager);
        recyview_costlist.addItemDecoration(new DividerItemDecoration(EnquiryCandidateList.this, 0));
        recyview_costlist.setItemAnimator(new DefaultItemAnimator());
        recyview_costlist.setAdapter(enquiryCanListAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<CrmEnquiryListBean> call= RetrofitClient.getInstance().getApi().getCrmEnquiryList(token);
        call.enqueue(new Callback<CrmEnquiryListBean>() {

            @Override
            public void onResponse(Call<CrmEnquiryListBean> call, Response<CrmEnquiryListBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmEnquiryListBean crmEnquiryListBean=response.body();
                    status=crmEnquiryListBean.getStatus();

                    if(status.equals("true")){
                        List<CrmEnquiryListBean.CrmEnqResultList> crmEnqResultLists=
                                crmEnquiryListBean.getCrmEnqResultListList();

                        for(int i=0;i<crmEnqResultLists.size();i++){
                            if(crmEnqResultLists.get(i).getEnquiryId()==null || crmEnqResultLists.get(i).getEnquiryId()==""){
                                enquiryId="Not Available";
                            }else{
                                enquiryId=crmEnqResultLists.get(i).getEnquiryId();
                            }

                            if(crmEnqResultLists.get(i).getCallType()==null || crmEnqResultLists.get(i).getCallType()==""){
                                callType="Not Available";
                            }else{
                                callType=crmEnqResultLists.get(i).getCallType();
                            }

                            if(crmEnqResultLists.get(i).getDate()==null || crmEnqResultLists.get(i).getDate()==""){
                                date="Not Available";
                            }else{
                                date=crmEnqResultLists.get(i).getDate();
                            }

                            if(crmEnqResultLists.get(i).getClient()==null || crmEnqResultLists.get(i).getClient()==""){
                                client="Not Available";
                            }else{
                                client=crmEnqResultLists.get(i).getClient();
                            }

                            if(crmEnqResultLists.get(i).getLocation()==null || crmEnqResultLists.get(i).getLocation()==""){
                                location="Not Available";
                            }else{
                                location=crmEnqResultLists.get(i).getLocation();
                            }

                            if(crmEnqResultLists.get(i).getContactNumber()==null || crmEnqResultLists.get(i).getContactNumber()==""){
                                contactNumber="Not Available";
                            }else{
                                contactNumber=crmEnqResultLists.get(i).getContactNumber();
                            }

                            if(crmEnqResultLists.get(i).getFollowUpDate()==null || crmEnqResultLists.get(i).getFollowUpDate()==""){
                                followUpDate="Not Available";
                            }else{
                                followUpDate=crmEnqResultLists.get(i).getFollowUpDate();
                            }

                            if(crmEnqResultLists.get(i).getEmployee()==null || crmEnqResultLists.get(i).getEmployee()==""){
                                employee="Not Available";
                            }else{
                                employee=crmEnqResultLists.get(i).getEmployee();
                            }

                            if(crmEnqResultLists.get(i).getStatus()==null || crmEnqResultLists.get(i).getStatus()==""){
                                resstatus="Not Available";
                            }else{
                                resstatus=crmEnqResultLists.get(i).getStatus();
                            }

                            EnquiryCanListAdapterBean enquiryCanListAdapterBean=new EnquiryCanListAdapterBean(enquiryId,callType,date,client,location,contactNumber,followUpDate,
                                    employee,resstatus);
                            enquiryCanListAdapterBeanList.add(enquiryCanListAdapterBean);
                        }

                        enquiryCanListAdapter.notifyDataSetChanged();

                    }else{
                        status_message=crmEnquiryListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(EnquiryCandidateList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryCandidateList.this,MenuScreen.class);
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
                        new android.app.AlertDialog.Builder(EnquiryCandidateList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(EnquiryCandidateList.this,MenuScreen.class);
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
            public void onFailure(Call<CrmEnquiryListBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(EnquiryCandidateList.this)
                        .setCancelable(false)
                        .setTitle("Info")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(EnquiryCandidateList.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();
            }

        });
    }

    public class EnquiryCanListAdapter extends RecyclerView.Adapter<EnquiryCanListAdapter.ViewHolder> {
        private List<EnquiryCanListAdapterBean> enquiryCanListAdapterBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_sno,text_calltype,text_date,text_client,text_location,text_contactno,text_followupdate,text_employee,text_status;
            public ImageButton img_eye_view;

            public ViewHolder(View view) {
                super(view);
                text_sno = (TextView) view.findViewById(R.id.text_sno);
                text_calltype = (TextView) view.findViewById(R.id.text_calltype);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_client = (TextView) view.findViewById(R.id.text_client);
                text_location = (TextView) view.findViewById(R.id.text_location);
                text_contactno = (TextView) view.findViewById(R.id.text_contactno);
                text_followupdate = (TextView) view.findViewById(R.id.text_followupdate);
                text_employee = (TextView) view.findViewById(R.id.text_employee);
                text_status = (TextView) view.findViewById(R.id.text_status);
                img_eye_view=(ImageButton)view.findViewById(R.id.img_eye_view);
            }
        }

        public EnquiryCanListAdapter(List<EnquiryCanListAdapterBean> mlist) {
            this.enquiryCanListAdapterBeanList = mlist;
            this.context = context;
        }

        @Override
        public EnquiryCanListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_enquiry_candidate_list, parent, false);

            return new EnquiryCanListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EnquiryCanListAdapter.ViewHolder holder, int position) {
            EnquiryCanListAdapterBean data = enquiryCanListAdapterBeanList.get(position);
            holder.text_sno.setText(String.valueOf(position+1));
            holder.text_calltype.setText(data.getCallType());
            holder.text_date.setText(data.getDate());
            holder.text_client.setText(data.getClient());
            holder.text_location.setText(data.getLocation());
            holder.text_contactno.setText(data.getContactNumber());
            holder.text_followupdate.setText(data.getFollowUpDate());
            holder.text_employee.setText(data.getEmployee());
            holder.text_status.setText(data.getStatus());
            holder.img_eye_view.setImageResource(R.drawable.view);
            holder.img_eye_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(EnquiryCandidateList.this,EnquiryFormView.class);
                    i.putExtra("token",token);
                    i.putExtra("enquiryId",enquiryId);
                    i.putExtra("login_id",login_id);
                    startActivity(i);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return enquiryCanListAdapterBeanList.size();
        }
    }

    public class EnquiryCanListAdapterBean{
        private String enquiryId,callType,date,client,location,contactNumber,followUpDate,employee,status;

        public EnquiryCanListAdapterBean(String enquiryId,String callType,String date,String client,String location,String contactNumber,String followUpDate,String employee,
                                    String status){
            this.enquiryId=enquiryId;
            this.callType=callType;
            this.date=date;
            this.client=client;
            this.location=location;
            this.contactNumber=contactNumber;
            this.followUpDate=followUpDate;
            this.employee=employee;
            this.status=status;
        }

        public String getEnquiryId() {
            return enquiryId;
        }

        public void setEnquiryId(String enquiryId) {
            this.enquiryId = enquiryId;
        }

        public String getCallType() {
            return callType;
        }

        public void setCallType(String callType) {
            this.callType = callType;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getFollowUpDate() {
            return followUpDate;
        }

        public void setFollowUpDate(String followUpDate) {
            this.followUpDate = followUpDate;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(EnquiryCandidateList.this);
        progressDialog = new ProgressDialog(EnquiryCandidateList.this);
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
        Intent MainActivity = new Intent(getBaseContext(), CRMMenu.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        EnquiryCandidateList.this.finish();
    }

}