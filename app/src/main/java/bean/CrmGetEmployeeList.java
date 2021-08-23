package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrmGetEmployeeList {
    private String status_message,status;
    @SerializedName("results")
    @Expose
    private List<CGEL_EmployeeList> cgel_employeeListList;

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

    public List<CGEL_EmployeeList> getCgel_employeeListList() {
        return cgel_employeeListList;
    }

    public void setCgel_employeeListList(List<CGEL_EmployeeList> cgel_employeeListList) {
        this.cgel_employeeListList = cgel_employeeListList;
    }

    public class CGEL_EmployeeList{
        private String employeeId,empName;

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }
    }
}
