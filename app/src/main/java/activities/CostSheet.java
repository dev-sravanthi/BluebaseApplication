package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.activities.R;
import com.google.android.material.transition.Hold;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.CandidateListBean;
import bean.CostSheetListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class CostSheet extends AppCompatActivity {
    String token,login_id,status,callType,date,companyName,location,contactNumber,followUpDate,department,employee,resultstatus,status_message,enquiryId;
    boolean networkAvailability=false;
    RecyclerView recyview_costlist;
    CostSheetAdapter costSheetAdapter;
    List<CostSheetAdapterBean> costSheetAdapterBeanList=new ArrayList<>();
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_sheet);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        System.out.println(token+" ====== "+login_id);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(CostSheet.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(CostSheet.this, "Internet Connection");
        }

    }

    private void findviewids() {
        recyview_costlist=findViewById(R.id.recyview_costlist);
        recyview_costlist.setHasFixedSize(true);
        costSheetAdapter = new CostSheetAdapter(costSheetAdapterBeanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CostSheet.this);
        recyview_costlist.setLayoutManager(layoutManager);
        recyview_costlist.addItemDecoration(new DividerItemDecoration(CostSheet.this, 0));
        recyview_costlist.setItemAnimator(new DefaultItemAnimator());
        recyview_costlist.setAdapter(costSheetAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<CostSheetListBean> call= RetrofitClient.getInstance().getApi().getCostSheetList(token,login_id);
        call.enqueue(new Callback<CostSheetListBean>() {

            @Override
            public void onResponse(Call<CostSheetListBean> call, Response<CostSheetListBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CostSheetListBean costSheetListBean=response.body();
                    status=costSheetListBean.getStatus();

                    System.out.println("status===="+status);

                    if(status.equals("true")){
                        List<CostSheetListBean.CostSheetListResultBean> costSheetListResultBeans=
                                costSheetListBean.getCostSheetListResultBeanList();

                        for(int i=0;i<costSheetListResultBeans.size();i++){
                            enquiryId=costSheetListResultBeans.get(i).getEnquiryId();
                            callType=costSheetListResultBeans.get(i).getCallType();
                            date=costSheetListResultBeans.get(i).getDate();
                            companyName=costSheetListResultBeans.get(i).getCompanyName();
                            location=costSheetListResultBeans.get(i).getLocation();
                            contactNumber=costSheetListResultBeans.get(i).getContactNumber();
                            followUpDate=costSheetListResultBeans.get(i).getFollowUpDate();
                            department=costSheetListResultBeans.get(i).getDepartment();
                            employee=costSheetListResultBeans.get(i).getEmployee();
                            resultstatus=costSheetListResultBeans.get(i).getStatus();

                            System.out.println("resultstatus===="+resultstatus);

                            CostSheetAdapterBean costSheetAdapterBean=new CostSheetAdapterBean(enquiryId,callType,date,companyName,location,contactNumber,followUpDate,department,
                                    employee,resultstatus);
                            costSheetAdapterBeanList.add(costSheetAdapterBean);
                        }

                        costSheetAdapter.notifyDataSetChanged();

                    }else{
                        status_message=costSheetListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(CostSheet.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(CostSheet.this,MenuScreen.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        System.out.println("todayHomeWork_bean  ====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CostSheetListBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
                progressDialog.dismiss();
            }

        });
    }

    public class CostSheetAdapter extends RecyclerView.Adapter<CostSheetAdapter.ViewHolder> {
        private List<CostSheetAdapterBean> costSheetAdapterBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_sno,text_call,text_date,text_companyname,text_location,text_contactno,text_followupdate,text_department,text_employee,text_status,text_action;
            public ImageButton img_eye_view;

            public ViewHolder(View view) {
                super(view);
                text_sno = (TextView) view.findViewById(R.id.text_sno);
                text_call = (TextView) view.findViewById(R.id.text_call);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_companyname = (TextView) view.findViewById(R.id.text_companyname);
                text_location = (TextView) view.findViewById(R.id.text_location);
                text_contactno = (TextView) view.findViewById(R.id.text_contactno);
                text_followupdate = (TextView) view.findViewById(R.id.text_followupdate);
                text_department = (TextView) view.findViewById(R.id.text_department);
                text_employee = (TextView) view.findViewById(R.id.text_employee);
                text_status = (TextView) view.findViewById(R.id.text_status);
//                text_action = (TextView) view.findViewById(R.id.text_action);
                img_eye_view=(ImageButton)view.findViewById(R.id.img_eye_view);
            }
        }

        public CostSheetAdapter(List<CostSheetAdapterBean> mlist) {
            this.costSheetAdapterBeanList = mlist;
            this.context = context;
        }

        @Override
        public CostSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_cost_sheet, parent, false);

            return new CostSheetAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CostSheetAdapter.ViewHolder holder, int position) {
            CostSheetAdapterBean data = costSheetAdapterBeanList.get(position);
            holder.text_sno.setText(String.valueOf(position+1));
            holder.text_call.setText(data.getCallType());
            holder.text_date.setText(data.getDate());
            holder.text_companyname.setText(data.getCompanyName());
            holder.text_location.setText(data.getLocation());
            holder.text_contactno.setText(data.getContactNumber());
            holder.text_followupdate.setText(data.getFollowUpDate());
            holder.text_department.setText(data.getDepartment());
            holder.text_employee.setText(data.getEmployee());
            holder.text_status.setText(data.getResultstatus());
            holder.img_eye_view.setImageResource(R.drawable.eye_view_icon);
            holder.img_eye_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(CostSheet.this,QuotationGeneratedForm.class);
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
            return costSheetAdapterBeanList.size();
        }
    }

    public class CostSheetAdapterBean{
        private String enquiryId,callType,date,companyName,location,contactNumber,followUpDate,department,employee,resultstatus;

        public CostSheetAdapterBean(String enquiryId,String callType,String date,String companyName,String location,String contactNumber,String followUpDate,String department,String employee,
                                    String resultstatus){
            this.enquiryId=enquiryId;
            this.callType=callType;
            this.date=date;
            this.companyName=companyName;
            this.location=location;
            this.contactNumber=contactNumber;
            this.followUpDate=followUpDate;
            this.department=department;
            this.employee=employee;
            this.resultstatus=resultstatus;
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

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
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

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getResultstatus() {
            return resultstatus;
        }

        public void setResultstatus(String resultstatus) {
            this.resultstatus = resultstatus;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(CostSheet.this);
        progressDialog = new ProgressDialog(CostSheet.this);
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
        Intent MainActivity = new Intent(getBaseContext(), MenuScreen.class);
        MainActivity.putExtra("token",token);
        MainActivity.putExtra("login_id",login_id);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        CostSheet.this.finish();
    }

}
