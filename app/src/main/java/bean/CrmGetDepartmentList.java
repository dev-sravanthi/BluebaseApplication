package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrmGetDepartmentList {
    private String status_message,status;
    @SerializedName("departments")
    @Expose
    private List<CGDL_Departments> cgdl_departmentsList;

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

    public List<CGDL_Departments> getCgdl_departmentsList() {
        return cgdl_departmentsList;
    }

    public void setCgdl_departmentsList(List<CGDL_Departments> cgdl_departmentsList) {
        this.cgdl_departmentsList = cgdl_departmentsList;
    }

    public class CGDL_Departments{
        private String departmentId,deptName;

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
