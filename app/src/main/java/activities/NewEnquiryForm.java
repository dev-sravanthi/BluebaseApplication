package activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bluebase.activities.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.CrmGetClientDetails;
import bean.CrmGetClientList;
import bean.CrmGetDepartmentList;
import bean.CrmGetEmployeeList;
import bean.LoginDataBean;
import bean.NewEnquiryFormBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ScrollTextView;
import util.Utility;

public class NewEnquiryForm extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    EditText ed_client,ed_address,ed_city,ed_clientname,ed_designation,ed_contactno,ed_emailid,ed_service,ed_feedback,ed_followupdate,ed_account_manager_dept,
            ed_account_manager,ed_dp_date,ed_dp_followupdate;
    Spinner spin_clienttype,spin_calltype,spin_client,spin_assignto_department,spin_assignto_employee;
    LinearLayout ll_acc_manager_dept,ll_acc_manager,ll_spin_assignto_employee,ll_spin_assignto_department;
    String token,login_id,status,status_message,clientId,companyName,departmentId,deptName,employeeId,empName,
            address,city,clientName,designation,mobileNo,email,cv_deptName,cv_empName,cv_status,cv_status_message,st_spin_clienttype,st_spin_calltype,selected_clienttype,
    selected_calltype;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    ArrayList<String> list_clientid,list_clientname,list_deptid,list_deptname,list_empid,list_empname;
    boolean networkAvailability=false;
    String department="",selected_dept_code="",selected_employee_code="",selected_client_code="", employee="",dept_code="",client="";
    int spin_dept_pstn=0,spin_emp_pstn=0,spin_client_pstn=0;
    private ArrayAdapter<CharSequence> adapter;
    Button btn_submit_nef,btn_dp_date,btn_dp_followupdate;
    ArrayList<String> listsub_ids;
    static final int DATE_DIALOG_ID = 1;
    protected static final String Selected_id = null;
    protected static final String Selected_schema = null;
    private int year = 0,month,day,myear,mmonth,mday;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_enquiry_form);

        Intent i=getIntent();
        token=i.getStringExtra("token");
        login_id=i.getStringExtra("login_id");

        ScrollTextView scrolltext=findViewById(R.id.scrolltext);
        scrolltext.setText(R.string.footer);
        scrolltext.startScroll();

        networkAvailability= Utility.isConnectingToInternet(NewEnquiryForm.this);

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(NewEnquiryForm.this, "Internet Connection");
        }

        loadJSON_getClientList();
     //   loadJSON_getClientView();
        loadJSON_getDepartmentList();
      //  loadJSON_getEmployeeList();
    }

    private void findviewbyids(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        list_clientid=new ArrayList<>();
        list_clientname=new ArrayList<>();
        list_deptid=new ArrayList<>();
        list_deptname=new ArrayList<>();
        list_empid=new ArrayList<>();
        list_empname=new ArrayList<>();

        list_clientname.add("-Select-");
        list_deptname.add("-Select-");
        list_empname.add("-Select-");

        spin_calltype=findViewById(R.id.spin_calltype);
        spin_calltype.setOnItemSelectedListener(this);

        spin_client=findViewById(R.id.spin_client);
        spin_client.setOnItemSelectedListener(this);

        spin_assignto_department=findViewById(R.id.spin_assignto_department);
        spin_assignto_department.setOnItemSelectedListener(this);

        spin_clienttype=findViewById(R.id.spin_clienttype);
        spin_clienttype.setOnItemSelectedListener(this);

        spin_assignto_employee=findViewById(R.id.spin_assignto_employee);
        spin_assignto_employee.setOnItemSelectedListener(this);

        ll_acc_manager=findViewById(R.id.ll_acc_manager);
        ll_acc_manager_dept=findViewById(R.id.ll_acc_manager_dept);
        ll_spin_assignto_department=findViewById(R.id.ll_spin_assignto_department);
        ll_spin_assignto_employee=findViewById(R.id.ll_spin_assignto_employee);

        adapter = ArrayAdapter.createFromResource(NewEnquiryForm.this,
                R.array.clienttype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_clienttype.setAdapter(adapter);

        spin_clienttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_spin_clienttype = spin_clienttype.getSelectedItem().toString();
                if(st_spin_clienttype.equals("Existing")){
                    ll_acc_manager_dept.setVisibility(View.VISIBLE);
                    ll_acc_manager.setVisibility(View.VISIBLE);
                    ll_spin_assignto_employee.setVisibility(View.GONE);
                    ll_spin_assignto_department.setVisibility(View.GONE);
                    selected_clienttype="1";
                }else if(st_spin_clienttype.equals("New")){
                    ll_spin_assignto_department.setVisibility(View.VISIBLE);
                    ll_spin_assignto_employee.setVisibility(View.VISIBLE);
                    ll_acc_manager_dept.setVisibility(View.GONE);
                    ll_acc_manager.setVisibility(View.GONE);
                    selected_clienttype="2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        adapter = ArrayAdapter.createFromResource(NewEnquiryForm.this,
                R.array.calltype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_calltype.setAdapter(adapter);

        spin_calltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st_spin_calltype = spin_calltype.getSelectedItem().toString();
                if(st_spin_calltype.equals("Incoming")){
                    selected_calltype="1";
                }else if(st_spin_calltype.equals("Outgoing")){
                    selected_calltype="2";
                }else if(st_spin_calltype.equals("Direct")){
                    selected_calltype="3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ed_client=findViewById(R.id.ed_client);
        ed_address=findViewById(R.id.ed_address);
        ed_city=findViewById(R.id.ed_city);
        ed_clientname=findViewById(R.id.ed_clientname);
        ed_designation=findViewById(R.id.ed_designation);
        ed_contactno=findViewById(R.id.ed_contactno);
        ed_emailid=findViewById(R.id.ed_emailid);
        ed_service=findViewById(R.id.ed_service);
        ed_feedback=findViewById(R.id.ed_feedback);
        ed_followupdate=findViewById(R.id.ed_followupdate);
        ed_account_manager_dept=findViewById(R.id.ed_account_manager_dept);
        ed_account_manager=findViewById(R.id.ed_account_manager);
        ed_dp_followupdate=findViewById(R.id.ed_dp_followupdate);
        ed_dp_date=findViewById(R.id.ed_dp_date);
        showDate(year, month+1, day);
        showFollowUpDate(year,month+1,day);

        btn_submit_nef=findViewById(R.id.btn_submit_nef);
        btn_submit_nef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("calltype===="+selected_calltype);
                System.out.println("date====="+ed_dp_date.getText().toString());
                System.out.println("clienttype======="+selected_clienttype);
                System.out.println("companyuname========="+client);
                System.out.println("location"+"=============="+city);
                System.out.println("address==="+address);
                System.out.println("clientName"+clientName);
                System.out.println("designation"+designation);
                System.out.println("email===="+email);
                System.out.println("mobile===="+mobileNo);
                System.out.println("product===="+ed_service.getText().toString());
                System.out.println("feedback===="+ed_feedback.getText().toString());
                System.out.println("followUp===="+ed_dp_followupdate);
                System.out.println("department===="+selected_dept_code);
                System.out.println("emplid"+selected_employee_code);
                System.out.println("createdby===="+login_id);

            }
        });

        btn_dp_date=findViewById(R.id.btn_dp_date);
        btn_dp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        ed_dp_date.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showFollowUpDate(int year, int month, int day) {
        ed_dp_followupdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void setNewEnquiryForm() {
        showBar();

        Call<NewEnquiryFormBean> call= RetrofitClient.getInstance().getApi().setNewEnquiryForm("c33f17bfd02f8496665c1e0a0c2248df",
                selected_calltype,ed_dp_date.getText().toString(),selected_clienttype,client,city,address,clientName,designation,mobileNo,email,"service",
                ed_feedback.getText().toString(),ed_dp_followupdate.getText().toString(),selected_dept_code,selected_employee_code,selected_employee_code);
        call.enqueue(new Callback<NewEnquiryFormBean>() {

            @Override
            public void onResponse(Call<NewEnquiryFormBean> call, Response<NewEnquiryFormBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    NewEnquiryFormBean crmGetClientList=response.body();

                    status=crmGetClientList.getStatus();

                    if(status.equals("true")){
                        Toast.makeText(NewEnquiryForm.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        status_message=crmGetClientList.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
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
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<NewEnquiryFormBean> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }


    private void loadJSON_getClientList() {
        showBar();

        Call<CrmGetClientList> call= RetrofitClient.getInstance().getApi().getClientList("c33f17bfd02f8496665c1e0a0c2248df");
        call.enqueue(new Callback<CrmGetClientList>() {

            @Override
            public void onResponse(Call<CrmGetClientList> call, Response<CrmGetClientList> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmGetClientList crmGetClientList=response.body();

                    status=crmGetClientList.getStatus();

                    if(status.equals("true")){
                        List<CrmGetClientList.CGCL_ClientNames> cgcl_clientNamesList=crmGetClientList.getCgcl_clientNamesList();
                        for(int i=0;i<cgcl_clientNamesList.size();i++){
                            clientId=cgcl_clientNamesList.get(i).getClientId();
                            companyName=cgcl_clientNamesList.get(i).getCompanyName();

                            list_clientid.add(clientId);
                            list_clientname.add(companyName);
                        }

                        ArrayAdapter<String> adapter_spin_client = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_clientname);
                        spin_client.setAdapter(adapter_spin_client);


                    }else{
                        status_message=crmGetClientList.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
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
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<CrmGetClientList> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getClientView() {
        showBar();
//        pending check
        Call<CrmGetClientDetails> call= RetrofitClient.getInstance().getApi().getClientDetails("c33f17bfd02f8496665c1e0a0c2248df",selected_client_code);
        call.enqueue(new Callback<CrmGetClientDetails>() {

            @Override
            public void onResponse(Call<CrmGetClientDetails> call, Response<CrmGetClientDetails> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmGetClientDetails crmGetClientDetails=response.body();

                    cv_status=crmGetClientDetails.getStatus();

                    if(cv_status.equals("true")){

                        CrmGetClientDetails.CGCD_ClientDetails cgcdClientDetails=crmGetClientDetails.getCgcd_clientDetails();
                        address=cgcdClientDetails.getAddress();
                        city=cgcdClientDetails.getCity();
                        clientName=cgcdClientDetails.getClientName();
                        designation=cgcdClientDetails.getDesignation();
                        mobileNo=cgcdClientDetails.getMobileNo();
                        email=cgcdClientDetails.getEmail();
                        deptName=cgcdClientDetails.getDeptName();
                        empName=cgcdClientDetails.getEmpName();

                        if(st_spin_clienttype.equals("Existing")){
                            ed_address.setText(address);
                            ed_city.setText(city);
                            ed_clientname.setText(clientName);
                            ed_designation.setText(designation);
                            ed_contactno.setText(mobileNo);
                            ed_emailid.setText(email);
                            ed_account_manager_dept.setText(deptName);
                            ed_account_manager.setText(empName);
                            ed_account_manager_dept.setEnabled(false);
                            ed_account_manager.setEnabled(false);
                            ed_account_manager_dept.setBackgroundResource(R.drawable.edittext_rounded_bg_grey);
                            ed_account_manager.setBackgroundResource(R.drawable.edittext_rounded_bg_grey);
                        }else{
                            ed_address.setText("");
                            ed_city.setText("");
                            ed_clientname.setText("");
                            ed_designation.setText("");
                            ed_contactno.setText("");
                            ed_emailid.setText("");
                            ed_account_manager_dept.setText("");
                            ed_account_manager.setText("");
                        }

                    }else{
                        cv_status_message=crmGetClientDetails.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
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
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<CrmGetClientDetails> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getDepartmentList() {
       // showBar();

        Call<CrmGetDepartmentList> call= RetrofitClient.getInstance().getApi().getDepartmentList("c33f17bfd02f8496665c1e0a0c2248df");
        call.enqueue(new Callback<CrmGetDepartmentList>() {

            @Override
            public void onResponse(Call<CrmGetDepartmentList> call, Response<CrmGetDepartmentList> response) {

              //  progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmGetDepartmentList crmGetDepartmentList=response.body();

                    status=crmGetDepartmentList.getStatus();

                    if(status.equals("true")){
                        List<CrmGetDepartmentList.CGDL_Departments> cgdl_departmentsList=crmGetDepartmentList.getCgdl_departmentsList();
                        for(int i=0;i<cgdl_departmentsList.size();i++){
                            departmentId=cgdl_departmentsList.get(i).getDepartmentId();
                            deptName=cgdl_departmentsList.get(i).getDeptName();

                            list_deptid.add(departmentId);
                            list_deptname.add(deptName);
                        }

                        ArrayAdapter<String> adapter_ass_to_dept = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_deptname);
                        spin_assignto_department.setAdapter(adapter_ass_to_dept);

                    }else{
                        status_message=crmGetDepartmentList.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    }

                }else{
                 //   progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<CrmGetDepartmentList> call, Throwable t) {
             //   progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    private void loadJSON_getEmployeeList() {
        System.out.println("=====entered emplyueee list method====");
        showBar();

        Call<CrmGetEmployeeList> call= RetrofitClient.getInstance().getApi().getEmployeeDetails("c33f17bfd02f8496665c1e0a0c2248df",
                selected_dept_code);
        call.enqueue(new Callback<CrmGetEmployeeList>() {

            @Override
            public void onResponse(Call<CrmGetEmployeeList> call, Response<CrmGetEmployeeList> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    CrmGetEmployeeList crmGetEmployeeList=response.body();

                    status=crmGetEmployeeList.getStatus();

                    if(status.equals("true")){
                        List<CrmGetEmployeeList.CGEL_EmployeeList> cgel_employeeLists=crmGetEmployeeList.getCgel_employeeListList();
                        for(int i=0;i<cgel_employeeLists.size();i++){
                            employeeId=cgel_employeeLists.get(i).getEmployeeId();
                            empName=cgel_employeeLists.get(i).getEmpName();

                            list_empid.add(employeeId);
                            list_empname.add(empName);
                        }

                        ArrayAdapter<String> adapter_ass_to_emp = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_text,list_empname);
                        spin_assignto_employee.setAdapter(adapter_ass_to_emp);

                    }else{
                        status_message=crmGetEmployeeList.getStatus_message();
                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Info")
                                .setMessage(status_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
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
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                                .setCancelable(false)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                        i.putExtra("token",token);
                                        i.putExtra("login_id",login_id);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<CrmGetEmployeeList> call, Throwable t) {
                progressDialog.dismiss();
                new android.app.AlertDialog.Builder(NewEnquiryForm.this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(status_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(NewEnquiryForm.this,MenuScreen.class);
                                i.putExtra("token",token);
                                i.putExtra("login_id",login_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

        if(parent==spin_client)
        {
            spin_client_pstn=position;
            if(spin_client_pstn>0)
            {
                System.out.println("spin_client_pstn===="+spin_client_pstn);
                selected_client_code=list_clientid.get(spin_client_pstn-1).toString().trim();
                client=list_clientname.get(spin_client_pstn).toString().trim();
                System.out.println("selected_client_code===="+selected_client_code);
                loadJSON_getClientView();
            }
        }

        if(parent==spin_assignto_department)
        {
            spin_dept_pstn=position;
            if(spin_dept_pstn>0)
            {
                selected_dept_code=list_deptid.get(spin_dept_pstn-1).toString().trim();
                department=list_deptname.get(spin_dept_pstn).toString().trim();
                System.out.println("selected_dept_code===="+selected_dept_code);
                loadJSON_getEmployeeList();
            }
        }

        if(parent==spin_assignto_employee)
        {
            spin_emp_pstn=position;
            if(spin_emp_pstn>0)
            {
                selected_employee_code=list_empid.get(spin_emp_pstn-1).toString().trim();
                employee=list_empname.get(spin_emp_pstn).toString().trim();
                System.out.println("selected_employee_code===="+selected_employee_code);

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showBar(){
        builder = new AlertDialog.Builder(NewEnquiryForm.this);
        progressDialog = new ProgressDialog(NewEnquiryForm.this);
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
        NewEnquiryForm.this.finish();
    }

}