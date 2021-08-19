package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.activities.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.CostSheetListBean;
import bean.CostSheetSubmitBean;
import bean.CostSheetViewBean;
import bean.LoginDataBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class QuotationGeneratedForm extends AppCompatActivity {
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    LinearLayout ll_costsheet_entrydetails;
    CardView cv_costsheet_entrydetails;
    TextView text_costsheet_entrydetails;
    private List<QGF_bean> list=new ArrayList<>();
    private RecyclerView recyview_phaselist,recyview_child_phaselist;
    private QGF_Adapter qgf_adapter;
    private QGF_Adapter_parent qgf_adapter_parent;
    private List<QGF_parent_bean> qgf_parent_beans=new ArrayList<>();
    boolean networkAvailability =false;
    private String token,enquiryId,status,status_message,gst,showButtons,proposal,client,date,version,empName,emailId,telephoneNo,scope,proposalStatement,termsAndConditions,
            name,total,grandTotal,specification,days,amount,isApproved,btnsubmitstatus,btnsubmitstatusmsg,login_id;
    private EditText ed_proposal_for,ed_client,ed_proposal_date,ed_version,ed_emp_name,ed_emp_emailid,ed_emp_telephone_no,ed_scope_statement,ed_proposal_bb,
            ed_terms_conditions;
    private Button btn_approve,btn_reject;
    private LinearLayout ll_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_generated_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        enquiryId=i.getStringExtra("enquiryId");
        login_id=i.getStringExtra("login_id");

        System.out.println("token===="+token+" ===== "+enquiryId);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(QuotationGeneratedForm.this);

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(QuotationGeneratedForm.this, "Internet Connection");
        }


       // loadJSON_getCandidateView();
      //  loadJSON();
    }

    private void findviewbyids(){
        ll_buttons=findViewById(R.id.ll_buttons);

        ed_proposal_for=findViewById(R.id.ed_proposal_for);
        ed_client=findViewById(R.id.ed_client);
        ed_proposal_date=findViewById(R.id.ed_proposal_date);
        ed_version=findViewById(R.id.ed_version);
        ed_emp_name=findViewById(R.id.ed_emp_name);
        ed_emp_emailid=findViewById(R.id.ed_emp_emailid);
        ed_emp_telephone_no=findViewById(R.id.ed_emp_telephone_no);
        ed_scope_statement=findViewById(R.id.ed_scope_statement);
        ed_proposal_bb=findViewById(R.id.ed_proposal_bb);
        ed_terms_conditions=findViewById(R.id.ed_terms_conditions);

        cv_costsheet_entrydetails=findViewById(R.id.cv_costsheet_entrydetails);
        ll_costsheet_entrydetails=findViewById(R.id.ll_costsheet_entrydetails);
        text_costsheet_entrydetails=findViewById(R.id.text_costsheet_entrydetails);

        recyview_phaselist=findViewById(R.id.recyview_phaselist);
        recyview_phaselist.setHasFixedSize(true);
        qgf_adapter_parent = new QGF_Adapter_parent(qgf_parent_beans);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuotationGeneratedForm.this);
        recyview_phaselist.setLayoutManager(layoutManager);
        recyview_phaselist.addItemDecoration(new DividerItemDecoration(QuotationGeneratedForm.this, LinearLayoutManager.VERTICAL));
        recyview_phaselist.setItemAnimator(new DefaultItemAnimator());
        recyview_phaselist.setAdapter(qgf_adapter_parent);

        loadJSON();
    }

    private void loadJSON() {

        showBar();
        Call<CostSheetViewBean> call= RetrofitClient.getInstance().getApi().getViewCostSheetData(token,enquiryId);
        call.enqueue(new Callback<CostSheetViewBean>() {

            @Override
            public void onResponse(Call<CostSheetViewBean> call, Response<CostSheetViewBean> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CostSheetViewBean costSheetViewBean=response.body();
                    status=costSheetViewBean.getStatus();
                    gst=costSheetViewBean.getGst();
                    showButtons=costSheetViewBean.getShowButtons();

                    if(showButtons.equals("false")){
                        ll_buttons.setVisibility(View.GONE);
                    }else{
                        ll_buttons.setVisibility(View.VISIBLE);
                        btn_approve=findViewById(R.id.btn_approve);
                        btn_reject=findViewById(R.id.btn_reject);

                        btn_approve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadJSOSSubmitCostSheet("true");
                            }
                        });

                        btn_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadJSOSSubmitCostSheet("false");
                            }
                        });
                    }

                    if(status.equals("true")){
                        CostSheetViewBean.CS_EnquiryDetails cs_enquiryDetails=costSheetViewBean.getCs_enquiryDetails();

                        proposal=cs_enquiryDetails.getProposal();
                        client=cs_enquiryDetails.getClient();
                        date=cs_enquiryDetails.getDate();
                        version=cs_enquiryDetails.getVersion();
                        empName=cs_enquiryDetails.getEmpName();
                        emailId=cs_enquiryDetails.getEmailId();
                        telephoneNo=cs_enquiryDetails.getTelephoneNo();
                        scope=cs_enquiryDetails.getScope();
                        proposalStatement=cs_enquiryDetails.getProposalStatement();
                        termsAndConditions=cs_enquiryDetails.getTermsAndConditions();

                        ed_proposal_for.setText(proposal);
                        ed_client.setText(client);
                        ed_proposal_date.setText(date);
                        ed_version.setText(version);
                        ed_emp_name.setText(empName);
                        ed_emp_emailid.setText(emailId);
                        ed_emp_telephone_no.setText(telephoneNo);
                        ed_scope_statement.setText(scope);
                        ed_proposal_bb.setText(proposalStatement);
                        ed_terms_conditions.setText(termsAndConditions);

                        List<CostSheetViewBean.CS_Phases> cs_phasesList=costSheetViewBean.getCs_phasesList();
                        for(int i=0;i<cs_phasesList.size();i++){
                            name=cs_phasesList.get(i).getName();
                            total=cs_phasesList.get(i).getTotal();
                            grandTotal=cs_phasesList.get(i).getGrandTotal();

                            List<QGF_bean> qgf_beanList = new ArrayList<>();

                            List<CostSheetViewBean.CS_Phases.CS_Phases_Entries> cs_phases_entriesList=cs_phasesList.get(i).getCs_phases_entriesList();
                            for(int j=0;j<cs_phases_entriesList.size();j++){
                                specification=cs_phases_entriesList.get(j).getSpecification();
                                days=cs_phases_entriesList.get(j).getDays();
                                amount=cs_phases_entriesList.get(j).getAmount();

                                qgf_beanList.add(new QGF_bean(name, specification, days, amount));

                            }

                            QGF_parent_bean qgf_parent_bean=new QGF_parent_bean(name,total,grandTotal,gst,qgf_beanList);
                            qgf_parent_beans.add(qgf_parent_bean);
                        }
                        qgf_adapter_parent.notifyDataSetChanged();

                    }else{
                        status_message=costSheetViewBean.getStatus_message();
                        Utility.showMessageDialogue(QuotationGeneratedForm.this,status_message,"Info");
                        new android.app.AlertDialog.Builder(QuotationGeneratedForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(QuotationGeneratedForm.this,CostSheet.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CostSheetViewBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
                progressDialog.dismiss();
            }

        });

    }

    private void loadJSOSSubmitCostSheet(String isApprovedStatus){
        showBar();

        Call<CostSheetSubmitBean> call=RetrofitClient.getInstance().getApi().costSheetSubmit(token,enquiryId,isApprovedStatus);
        call.enqueue(new Callback<CostSheetSubmitBean>() {

            @Override
            public void onResponse(Call<CostSheetSubmitBean> call, Response<CostSheetSubmitBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CostSheetSubmitBean costSheetSubmitBean=response.body();

                    btnsubmitstatus=costSheetSubmitBean.getStatus();

                    if(btnsubmitstatus.equals("true")){

                    }else{
                        btnsubmitstatusmsg=costSheetSubmitBean.getStatus_message();

                        new android.app.AlertDialog.Builder(QuotationGeneratedForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(btnsubmitstatusmsg)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(QuotationGeneratedForm.this,MenuScreen.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        Utility.showAlertDialog(QuotationGeneratedForm.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<CostSheetSubmitBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(QuotationGeneratedForm.this,"Error",t.getMessage(),false);
            }
        });

    }

    public class QGF_Adapter_parent extends RecyclerView.Adapter<QGF_Adapter_parent.ViewHolder> {
        private List<QGF_parent_bean> qgf_beans;
        Context context;
        private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        public class ViewHolder extends RecyclerView.ViewHolder {
            private RecyclerView recyview_child_phaselist;
            private TextView text_phase_name,text_total,text_gst,text_grandtotal;

            public ViewHolder(View view) {
                super(view);
                recyview_child_phaselist=(RecyclerView)view. findViewById(R.id.recyview_child_phaselist);
                text_phase_name=(TextView)view.findViewById(R.id.text_phase_name);
                text_total=(TextView)view.findViewById(R.id.text_total);
                text_gst=(TextView)view.findViewById(R.id.text_gst);
                text_grandtotal=(TextView)view.findViewById(R.id.text_grandtotal);
            }
        }

        public QGF_Adapter_parent(List<QGF_parent_bean> mlist){
            this.qgf_beans = mlist;
            this.context=context;
        }

        @Override
        public QGF_Adapter_parent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_quotation_generatedform, parent, false);

            return new QGF_Adapter_parent.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(QGF_Adapter_parent.ViewHolder holder, int position) {
            QGF_parent_bean data = qgf_beans.get(position);

            holder.text_phase_name.setText(data.getPhasename());
            holder.text_total.setText(data.getTotal());
            holder.text_gst.setText(gst);
            holder.text_grandtotal.setText(data.getGrandTotal());

            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyview_child_phaselist.getContext(),
                    LinearLayoutManager.VERTICAL, false);
            layoutManager.setInitialPrefetchItemCount(data.getQgf_beanList().size());
            QGF_Adapter childItemAdapter = new QGF_Adapter(data.getQgf_beanList());
            holder.recyview_child_phaselist.setLayoutManager(layoutManager);
            holder.recyview_child_phaselist.setAdapter(childItemAdapter);
            holder.recyview_child_phaselist.setRecycledViewPool(viewPool);

        }

        @Override
        public int getItemCount() {
            return qgf_beans.size();
        }
    }

    public class QGF_Adapter extends RecyclerView.Adapter<QGF_Adapter.ViewHolder> {
        private List<QGF_bean> qgf_beans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_specifications,text_monthhoursdays,text_amount;

            public ViewHolder(View view) {
                super(view);
                text_specifications = (TextView) view.findViewById(R.id.text_specifications);
                text_monthhoursdays = (TextView) view.findViewById(R.id.text_monthhoursdays);
                text_amount = (TextView) view.findViewById(R.id.text_amount);
            }
        }

        public QGF_Adapter(List<QGF_bean> mlist)
        {
            this.qgf_beans = mlist;
            this.context=context;
        }

        @Override
        public QGF_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_recycler_quotation_generatedform, parent, false);

            return new QGF_Adapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(QGF_Adapter.ViewHolder holder, int position) {
            QGF_bean data = qgf_beans.get(position);

            holder.text_specifications.setText(data.getSpecifications());
            holder.text_monthhoursdays.setText(data.getMon_hour_day());
            holder.text_amount.setText(data.getAmount());

        }

        @Override
        public int getItemCount() {
            return qgf_beans.size();
        }
    }

    public class QGF_parent_bean{
        private String phasename,total,grandTotal,gst;
        private List<QGF_bean> qgf_beanList;

        public QGF_parent_bean(String phasename,String total,String grandTotal,String gst,List<QGF_bean> qgf_beanList) {
            this.phasename=phasename;
            this.total=total;
            this.grandTotal=grandTotal;
            this.gst=gst;
            this.qgf_beanList = qgf_beanList;
        }

        public String getPhasename() {
            return phasename;
        }

        public void setPhasename(String phasename) {
            this.phasename = phasename;
        }

        public List<QGF_bean> getQgf_beanList() {
            return qgf_beanList;
        }

        public void setQgf_beanList(List<QGF_bean> qgf_beanList) {
            this.qgf_beanList = qgf_beanList;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(String grandTotal) {
            this.grandTotal = grandTotal;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }



    }

    public class QGF_bean {
        private String phases,specifications,mon_hour_day,amount;

        public QGF_bean(String phases,String specifications,String mon_hour_day,String amount) {
            this.phases = phases;
            this.specifications = specifications;
            this.mon_hour_day = mon_hour_day;
            this.amount=amount;
        }

        public String getPhases() {
            return phases;
        }

        public void setPhases(String phases) {
            this.phases = phases;
        }

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public String getMon_hour_day() {
            return mon_hour_day;
        }

        public void setMon_hour_day(String mon_hour_day) {
            this.mon_hour_day = mon_hour_day;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }


    public void showBar(){
        builder = new AlertDialog.Builder(QuotationGeneratedForm.this);
        progressDialog = new ProgressDialog(QuotationGeneratedForm.this);
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
        QuotationGeneratedForm.this.finish();
    }

}