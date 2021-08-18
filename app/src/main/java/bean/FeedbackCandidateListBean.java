package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackCandidateListBean {
    private String status;
    @SerializedName("candidateList")
    @Expose
    List<FeedbackCandList> feedbackCandLists;

    public List<FeedbackCandList> getFeedbackCandLists() {
        return feedbackCandLists;
    }

    public void setFeedbackCandLists(List<FeedbackCandList> feedbackCandLists) {
        this.feedbackCandLists = feedbackCandLists;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public class FeedbackCandList{
        private String candidateId,firstName,lastName,position,headStatus,status,isAddFeedback;

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
}
