package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import bean.CandidateListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class CandidateList extends AppCompatActivity {

    String token,firstName,lastName,deptName,designationName,phone,email,candidateId,status,status_message,login_id;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private List<CanListBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private CandidateListAdapter candidateListAdapter;
    List<String> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(CandidateList.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(CandidateList.this, "Internet Connection");
        }


    }

    private void findviewids() {
        recyclerView = findViewById(R.id.recyview_candidatelist);
        recyclerView.setHasFixedSize(true);
        candidateListAdapter = new CandidateListAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CandidateList.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(CandidateList.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(candidateListAdapter);
        loadJSON();

    }

    private void loadJSON() {
        showBar();
        Call<CandidateListBean> call= RetrofitClient.getInstance().getApi().getCandidateList(token);
        call.enqueue(new Callback<CandidateListBean>() {

            @Override
            public void onResponse(Call<CandidateListBean> call, Response<CandidateListBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CandidateListBean candidateListBean=response.body();
                    String status_main=candidateListBean.getStatus();

                    if(status_main.equals("true")){

                        List<CandidateListBean.CandidateListData> candidateListDataList=
                                candidateListBean.getCandidateListDataList();

                        for(int i=0;i<candidateListDataList.size();i++){
                            firstName=candidateListDataList.get(i).getFirstName();
                            lastName=candidateListDataList.get(i).getLastName();
                            deptName=candidateListDataList.get(i).getDeptName();
                            designationName=candidateListDataList.get(i).getDesignationName();
                            phone=candidateListDataList.get(i).getPhone();
                            email=candidateListDataList.get(i).getEmail();
                            candidateId=candidateListDataList.get(i).getCandidateId();
                            status=candidateListDataList.get(i).getStatus();
                            status_message=candidateListDataList.get(i).getStatus_message();

                            CanListBean canListBean=new CanListBean(firstName,lastName,deptName,designationName,
                                    phone,email,candidateId,status,status_message);
                            list.add(canListBean);
                        }

                        candidateListAdapter.notifyDataSetChanged();

                    }else{
                        status_message=candidateListBean.getStatus_message();
                        new android.app.AlertDialog.Builder(CandidateList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(CandidateList.this,MenuScreen.class);
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
                        new android.app.AlertDialog.Builder(CandidateList.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(response.errorBody().string())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(CandidateList.this,MenuScreen.class);
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
            public void onFailure(Call<CandidateListBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(CandidateList.this)
                        .setCancelable(false)
                        .setTitle("Info")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(CandidateList.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();            }

        });
    }

    public class CandidateListAdapter extends RecyclerView.Adapter<CandidateListAdapter.ViewHolder> {
        private List<CanListBean> canListBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_sno,text_name,text_department,text_position,text_phone,text_mail,text_status,
                    text_action;

            public ViewHolder(View view) {
                super(view);
                text_sno = (TextView) view.findViewById(R.id.text_sno);
                text_name = (TextView) view.findViewById(R.id.text_name);
                text_department = (TextView) view.findViewById(R.id.text_department);
                text_position = (TextView) view.findViewById(R.id.text_position);
                text_phone = (TextView) view.findViewById(R.id.text_phone);
                text_mail = (TextView) view.findViewById(R.id.text_mail);
                text_status = (TextView) view.findViewById(R.id.text_status);
                text_action = (TextView) view.findViewById(R.id.text_action);

            }
        }

        public CandidateListAdapter(List<CanListBean> mlist)
        {
            this.canListBeanList = mlist;
            this.context=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_candidatelist, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CanListBean data = canListBeanList.get(position);
            holder.text_sno.setText(String.valueOf(position+1));
            holder.text_name.setText(data.getFirstName());
            holder.text_department.setText(data.getDeptName());
            holder.text_position.setText(data.getDesignationName());
            holder.text_phone.setText(data.getPhone());
            holder.text_mail.setText(data.getEmail());
            holder.text_status.setText(data.getStatus_message());
            holder.text_action.setText("View");

            holder.text_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),CandidateView.class);
                    intent.putExtra("FROM_ACTIVITY","CandidateList");
                    intent.putExtra("candidateId",data.getCandidateId());
                    intent.putExtra("token",token);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return canListBeanList.size();
        }
    }

    public class CanListBean {
        private String firstName,lastName,deptName,designationName,phone,email,candidateId,status,status_message;

        public CanListBean(String firstName,String lastName,String deptName,String designationName,
                           String phone,String email,String candidateId,String status,String status_message) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.deptName = deptName;
            this.designationName=designationName;
            this.phone=phone;
            this.email=email;
            this.candidateId=candidateId;
            this.status=status;
            this.status_message=status_message;
        }

        public String getStatus_message() {
            return status_message;
        }

        public void setStatus_message(String status_message) {
            this.status_message = status_message;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getDesignationName() {
            return designationName;
        }

        public void setDesignationName(String designationName) {
            this.designationName = designationName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    public void showBar(){
        builder = new AlertDialog.Builder(CandidateList.this);
        progressDialog = new ProgressDialog(CandidateList.this);
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
        CandidateList.this.finish();
    }


}
