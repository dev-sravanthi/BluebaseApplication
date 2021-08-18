package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CostSheetViewBean {
    private String status,status_message,gst,showButtons;
    @SerializedName("enquiryDetails")
    @Expose
    private CS_EnquiryDetails cs_enquiryDetails;
    @SerializedName("phases")
    @Expose
    private List<CS_Phases> cs_phasesList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getShowButtons() {
        return showButtons;
    }

    public void setShowButtons(String showButtons) {
        this.showButtons = showButtons;
    }

    public CS_EnquiryDetails getCs_enquiryDetails() {
        return cs_enquiryDetails;
    }

    public void setCs_enquiryDetails(CS_EnquiryDetails cs_enquiryDetails) {
        this.cs_enquiryDetails = cs_enquiryDetails;
    }

    public List<CS_Phases> getCs_phasesList() {
        return cs_phasesList;
    }

    public void setCs_phasesList(List<CS_Phases> cs_phasesList) {
        this.cs_phasesList = cs_phasesList;
    }

    public class CS_EnquiryDetails{
        private String proposal,client,date,version,empName,emailId,telephoneNo,scope,proposalStatement,termsAndConditions;

        public String getProposal() {
            return proposal;
        }

        public void setProposal(String proposal) {
            this.proposal = proposal;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getTelephoneNo() {
            return telephoneNo;
        }

        public void setTelephoneNo(String telephoneNo) {
            this.telephoneNo = telephoneNo;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getProposalStatement() {
            return proposalStatement;
        }

        public void setProposalStatement(String proposalStatement) {
            this.proposalStatement = proposalStatement;
        }

        public String getTermsAndConditions() {
            return termsAndConditions;
        }

        public void setTermsAndConditions(String termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
        }
    }

    public class CS_Phases{
        private String name,total,grandTotal;
        @SerializedName("entries")
        @Expose
        private List<CS_Phases_Entries> cs_phases_entriesList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public List<CS_Phases_Entries> getCs_phases_entriesList() {
            return cs_phases_entriesList;
        }

        public void setCs_phases_entriesList(List<CS_Phases_Entries> cs_phases_entriesList) {
            this.cs_phases_entriesList = cs_phases_entriesList;
        }

        public class CS_Phases_Entries{
            private String specification,days,amount;

            public String getSpecification() {
                return specification;
            }

            public void setSpecification(String specification) {
                this.specification = specification;
            }

            public String getDays() {
                return days;
            }

            public void setDays(String days) {
                this.days = days;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }
        }
    }

}
