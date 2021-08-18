package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CandidateListBean {
    private String status;
    @SerializedName("results")
    @Expose
    List<CandidateListData> candidateListDataList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CandidateListData> getCandidateListDataList() {
        return candidateListDataList;
    }

    public void setCandidateListDataList(List<CandidateListData> candidateListDataList) {
        this.candidateListDataList = candidateListDataList;
    }

    public class CandidateListData{
        private String firstName,lastName,deptName,designationName,phone,email,candidateId,status,status_message;

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
}
