package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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

import bean.FeedbackCandidateListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class FeedbackCandidateList extends AppCompatActivity {
    String token,candidateId,firstName,lastName,position,headStatus,status,isAddFeedback,login_id;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private List<FeedbackListBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedbackListAdapter feedbackListAdapter;
    List<String> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_candidate_list);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        System.out.println(token+" ====== "+login_id);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(FeedbackCandidateList.this);

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(FeedbackCandidateList.this, "Internet Connection");
        }

    }

    private void findviewids() {
        recyclerView = findViewById(R.id.recyview_candidatelist);
        recyclerView.setHasFixedSize(true);
        feedbackListAdapter = new FeedbackListAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FeedbackCandidateList.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(FeedbackCandidateList.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feedbackListAdapter);
        loadJSON();

    }

    private void loadJSON() {
        showBar();
        Call<FeedbackCandidateListBean> call= RetrofitClient.getInstance().getApi().
                getFeedbackList(token,login_id);
        call.enqueue(new Callback<FeedbackCandidateListBean>() {

            @Override
            public void onResponse(Call<FeedbackCandidateListBean> call, Response<FeedbackCandidateListBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    FeedbackCandidateListBean feedbackCandidateListBean=response.body();
                    String status_main=feedbackCandidateListBean.getStatus();

                    List<FeedbackCandidateListBean.FeedbackCandList> feedbackCandLists=
                            feedbackCandidateListBean.getFeedbackCandLists();

                    for(int i=0;i<feedbackCandLists.size();i++){
                        candidateId=feedbackCandLists.get(i).getCandidateId();
                        firstName=feedbackCandLists.get(i).getFirstName();
                        lastName=feedbackCandLists.get(i).getLastName();
                        position=feedbackCandLists.get(i).getPosition();
                        headStatus=feedbackCandLists.get(i).getHeadStatus();
                        status=feedbackCandLists.get(i).getStatus();
                        isAddFeedback=feedbackCandLists.get(i).getIsAddFeedback();

                        FeedbackListBean feedbackListBean=new FeedbackListBean(candidateId,firstName,lastName,
                                position,headStatus,status,isAddFeedback);
                        list.add(feedbackListBean);
                    }

                    feedbackListAdapter.notifyDataSetChanged();

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
            public void onFailure(Call<FeedbackCandidateListBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
                progressDialog.dismiss();
            }

        });
    }

    public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {
        private List<FeedbackListBean> canListBeanList;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_sno,text_name,text_position,text_head_status,text_status,
                    text_action;

            public ViewHolder(View view) {
                super(view);
                text_sno = (TextView) view.findViewById(R.id.text_sno);
                text_name = (TextView) view.findViewById(R.id.text_name);
                text_position = (TextView) view.findViewById(R.id.text_position);
                text_head_status = (TextView) view.findViewById(R.id.text_head_status);
                text_status = (TextView) view.findViewById(R.id.text_status);
                text_action = (TextView) view.findViewById(R.id.text_action);

            }
        }

        public FeedbackListAdapter(List<FeedbackListBean> mlist)
        {
            this.canListBeanList = mlist;
            this.context=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_feedback_cand_list, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FeedbackListBean data = canListBeanList.get(position);
            holder.text_sno.setText(String.valueOf(position+1));
            holder.text_name.setText(data.getFirstName() +" "+data.getLastName());
            holder.text_position.setText(data.getPosition());
            holder.text_head_status.setText(data.getHeadStatus());
            holder.text_status.setText(data.getStatus());
            if(data.getIsAddFeedback().equals("true")){
                holder.text_action.setText("Add Feedback");
                holder.text_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),InterviewFeedbackForm.class);
                        intent.putExtra("feedback_candidateId",data.getCandidateId());
                        intent.putExtra("token",token);
                        intent.putExtra("login_id",login_id);
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                holder.text_action.setText("View");
                holder.text_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),CandidateView.class);
                        intent.putExtra("FROM_ACTIVITY","FeedbackCandidateList");
                        intent.putExtra("feedback_candidateId",data.getCandidateId());
                        intent.putExtra("token",token);
                        intent.putExtra("login_id",login_id);
                        startActivity(intent);
                        finish();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return canListBeanList.size();
        }
    }

    public class FeedbackListBean {
        private String candidateId,firstName,lastName,position,headStatus,status,isAddFeedback;

        public FeedbackListBean(String candidateId,String firstName,String lastName,String position,
                           String headStatus,String status,String isAddFeedback) {
            this.candidateId = candidateId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.position=position;
            this.headStatus=headStatus;
            this.status=status;
            this.isAddFeedback=isAddFeedback;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
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

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getHeadStatus() {
            return headStatus;
        }

        public void setHeadStatus(String headStatus) {
            this.headStatus = headStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsAddFeedback() {
            return isAddFeedback;
        }

        public void setIsAddFeedback(String isAddFeedback) {
            this.isAddFeedback = isAddFeedback;
        }
    }

    public void showBar(){
        builder = new AlertDialog.Builder(FeedbackCandidateList.this);
        progressDialog = new ProgressDialog(FeedbackCandidateList.this);
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
        FeedbackCandidateList.this.finish();
    }

}