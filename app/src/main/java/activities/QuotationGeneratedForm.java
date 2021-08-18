package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

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
    private String token,enquiryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_generated_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        enquiryId=i.getStringExtra("enquiryId");

        System.out.println("token===="+token);

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(QuotationGeneratedForm.this);

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(QuotationGeneratedForm.this, "Internet Connection");
        }

        loadJSON_getCandidateView();
        loadJSON();
    }

    private void findviewbyids(){
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
    }

    private void loadJSON() {

    }

    private void loadJSON_getCandidateView() {
     //   showBar();

        for(int j=0;j<2;j++) {

            List<QGF_bean> qgf_beanList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                text_costsheet_entrydetails.setText("Cost Sheet Entry Details");

                qgf_beanList.add(new QGF_bean("1", "1", "1", "1"));
            }

            QGF_parent_bean qgf_parent_bean=new QGF_parent_bean(qgf_beanList);
            qgf_parent_beans.add(qgf_parent_bean);


        }

        qgf_adapter_parent.notifyDataSetChanged();

    }

    public class QGF_Adapter_parent extends RecyclerView.Adapter<QGF_Adapter_parent.ViewHolder> {
        private List<QGF_parent_bean> qgf_beans;
        Context context;
        private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        public class ViewHolder extends RecyclerView.ViewHolder {
            private RecyclerView recyview_child_phaselist;
            private TextView text_total,text_gst,text_grandtotal;

            public ViewHolder(View view) {
                super(view);
                recyview_child_phaselist=(RecyclerView)view. findViewById(R.id.recyview_child_phaselist);
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

            holder.text_total.setText("total");
            holder.text_gst.setText("GST");
            holder.text_grandtotal.setText("grand total");

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
            public TextView text_phases,text_specifications,text_monthhoursdays,text_amount;

            public ViewHolder(View view) {
                super(view);
                text_phases = (TextView) view.findViewById(R.id.text_phases);
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

            holder.text_phases.setText(data.getPhases());
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
        private List<QGF_bean> qgf_beanList;

        public QGF_parent_bean(List<QGF_bean> qgf_beanList){
            this.qgf_beanList=qgf_beanList;
        }

        public List<QGF_bean> getQgf_beanList() {
            return qgf_beanList;
        }

        public void setQgf_beanList(List<QGF_bean> qgf_beanList) {
            this.qgf_beanList = qgf_beanList;
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

}