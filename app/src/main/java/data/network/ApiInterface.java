package data.network;

import org.json.JSONObject;

import bean.CandidateListBean;
import bean.CandidateViewBean;
import bean.CostSheetListBean;
import bean.CostSheetSubmitBean;
import bean.CostSheetViewBean;
import bean.CrmEnquiryFormViewBean;
import bean.CrmEnquiryListBean;
import bean.CrmGetClientDetails;
import bean.CrmGetClientList;
import bean.CrmGetDepartmentList;
import bean.CrmGetEmployeeList;
import bean.FeedbackCandidateListBean;
import bean.FeedbackFormColumnsBean;
import bean.LoginDataBean;
import bean.NewEnquiryFormBean;
import bean.SetFeedbackFormBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login/login.php")
    Call<LoginDataBean> checkLogin(
            @Field("userName")String userName,
            @Field("password")String password
    );

    @FormUrlEncoded
    @POST("recruitment/candidate_list.php")
    Call<CandidateListBean> getCandidateList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("recruitment/candidate_view.php")
    Call<CandidateViewBean> getCandidateView(
            @Field("token")String token,
            @Field("candidateId")String candidateId
    );

    @FormUrlEncoded
    @POST("recruitment/feedback_list.php")
    Call<FeedbackCandidateListBean> getFeedbackList(
            @Field("token")String token,
            @Field("candidateId")String candidateId
    );

    @FormUrlEncoded
    @POST("recruitment/feedback_form_columns.php")
    Call<FeedbackFormColumnsBean> getFeedbackFormColumns(
            @Field("token")String token,
            @Field("candidateId")String candidateId
    );

    @FormUrlEncoded
    @POST("recruitment/feedback_form.php")
    Call<SetFeedbackFormBean> setFeedbackForm(
            @Field("token")String token,
            @Field("intervieweeCandidateId")String intervieweeCandidateId,
            @Field("interviewerCandidateId")String interviewerCandidateId,
            @Field("rounds") JSONObject rounds,
            @Field("isSelected")String isSelected
    );

    @FormUrlEncoded
    @POST("crm/cost_sheet_list.php")
    Call<CostSheetListBean> getCostSheetList(
            @Field("token")String token,
            @Field("candidateId")String candidateId
    );

    @FormUrlEncoded
    @POST("crm/view_cost_sheet.php")
    Call<CostSheetViewBean> getViewCostSheetData(
            @Field("token")String token,
            @Field("enquiryId")String enquiryId
    );

    @FormUrlEncoded
    @POST("crm/cost_sheet_submit.php")
    Call<CostSheetSubmitBean> costSheetSubmit(
            @Field("token")String token,
            @Field("enquiryId")String enquiryId,
            @Field("isApproved")String isApproved
    );

    @FormUrlEncoded
    @POST("crm/enquiry_list.php")
    Call<CrmEnquiryListBean> getCrmEnquiryList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/enquiry_view.php")
    Call<CrmEnquiryFormViewBean> getCrmEnquiryFormView(
            @Field("token")String token,
            @Field("enquiryId")String enquiryId
    );

    @FormUrlEncoded
    @POST("crm/get_client_list.php")
    Call<CrmGetClientList> getClientList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/get_client_details.php")
    Call<CrmGetClientDetails> getClientDetails(
            @Field("token")String token,
            @Field("clientId")String clientId
    );

    @FormUrlEncoded
    @POST("crm/get_department_list.php")
    Call<CrmGetDepartmentList> getDepartmentList(
            @Field("token")String token
    );

    @FormUrlEncoded
    @POST("crm/get_employee_list.php")
    Call<CrmGetEmployeeList> getEmployeeDetails(
            @Field("token")String token,
            @Field("departmentId")String departmentId
    );

    @FormUrlEncoded
    @POST("crm/add_enquiry.php")
    Call<NewEnquiryFormBean> setNewEnquiryForm(
            @Field("token")String token,
            @Field("callType")String callType,
            @Field("date")String date,
            @Field("clientType")String clientType,
            @Field("companyName")String companyName,
            @Field("location")String location,
            @Field("address")String address,
            @Field("client")String client,
            @Field("designation")String designation,
            @Field("mobile")String mobile,
            @Field("email")String email,
            @Field("product")String product,
            @Field("feedback")String feedback,
            @Field("followUp")String followUp,
            @Field("department")String department,
            @Field("employeeId")String employeeId,
            @Field("createdBy")String createdBy
    );

}