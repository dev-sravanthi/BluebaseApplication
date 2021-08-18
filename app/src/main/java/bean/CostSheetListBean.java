package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CostSheetListBean {
    private String status,status_message;
    @SerializedName("results")
    @Expose
    private List<CostSheetListResultBean> costSheetListResultBeanList;

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

    public List<CostSheetListResultBean> getCostSheetListResultBeanList() {
        return costSheetListResultBeanList;
    }

    public void setCostSheetListResultBeanList(List<CostSheetListResultBean> costSheetListResultBeanList) {
        this.costSheetListResultBeanList = costSheetListResultBeanList;
    }

    public class CostSheetListResultBean{
        private String enquiryId,callType,date,companyName,location,contactNumber,followUpDate,department,employee,status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEnquiryId() {
            return enquiryId;
        }

        public void setEnquiryId(String enquiryId) {
            this.enquiryId = enquiryId;
        }

        public String getCallType() {
            return callType;
        }

        public void setCallType(String callType) {
            this.callType = callType;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getFollowUpDate() {
            return followUpDate;
        }

        public void setFollowUpDate(String followUpDate) {
            this.followUpDate = followUpDate;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

//        public String getResultstatus() {
//            return resultstatus;
//        }
//
//        public void setResultstatus(String resultstatus) {
//            this.resultstatus = resultstatus;
//        }
    }



}
