package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrmGetClientList {
    private String status_message,status;
    @SerializedName("clientNames")
    @Expose
    private List<CGCL_ClientNames> cgcl_clientNamesList;

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

    public List<CGCL_ClientNames> getCgcl_clientNamesList() {
        return cgcl_clientNamesList;
    }

    public void setCgcl_clientNamesList(List<CGCL_ClientNames> cgcl_clientNamesList) {
        this.cgcl_clientNamesList = cgcl_clientNamesList;
    }

    public class CGCL_ClientNames{
        private String clientId,companyName;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }
}
