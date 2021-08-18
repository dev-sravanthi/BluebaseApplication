package data.network;

import bean.CandidateListBean;
import bean.CandidateViewBean;
import bean.CostSheetListBean;
import bean.CostSheetViewBean;
import bean.FeedbackCandidateListBean;
import bean.FeedbackFormColumnsBean;
import bean.LoginDataBean;
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
            @Field("rounds")String rounds,
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

}
