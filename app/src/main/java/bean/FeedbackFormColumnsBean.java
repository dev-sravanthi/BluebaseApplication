package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackFormColumnsBean {
    private String status,status_message;
    @SerializedName("results")
    @Expose
    List<FFC_result> ffc_resultList;

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FFC_result> getFfc_resultList() {
        return ffc_resultList;
    }

    public void setFfc_resultList(List<FFC_result> ffc_resultList) {
        this.ffc_resultList = ffc_resultList;
    }

    public class FFC_result{
        private String roundNameId,roundName;

        public String getRoundNameId() {
            return roundNameId;
        }

        public void setRoundNameId(String roundNameId) {
            this.roundNameId = roundNameId;
        }

        public String getRoundName() {
            return roundName;
        }

        public void setRoundName(String roundName) {
            this.roundName = roundName;
        }
    }
}
