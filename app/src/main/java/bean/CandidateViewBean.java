package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CandidateViewBean {
    private String status;
    @SerializedName("candidateDetails")
    @Expose
    private CV_candidateDetails cv_candidateDetails;
    @SerializedName("feedBacks")
    @Expose
    private CV_feedbacks cv_feedbacks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CV_candidateDetails getCv_candidateDetails() {
        return cv_candidateDetails;
    }

    public void setCv_candidateDetails(CV_candidateDetails cv_candidateDetails) {
        this.cv_candidateDetails = cv_candidateDetails;
    }

    public CV_feedbacks getCv_feedbacks() {
        return cv_feedbacks;
    }

    public void setCv_feedbacks(CV_feedbacks cv_feedbacks) {
        this.cv_feedbacks = cv_feedbacks;
    }

    public class CV_candidateDetails{
        private String candidateStatus,designationName,deptName,firstName,lastName,gender,fatherName,dob,address,permanentAddress,phone,
                alternativePhone,email,aadharNo,panNo,voterNo,educationalDetails,employeeStatus,yearOfPass;

        public String getCandidateStatus() {
            return candidateStatus;
        }

        public void setCandidateStatus(String candidateStatus) {
            this.candidateStatus = candidateStatus;
        }

        public String getDesignationName() {
            return designationName;
        }

        public void setDesignationName(String designationName) {
            this.designationName = designationName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPermanentAddress() {
            return permanentAddress;
        }

        public void setPermanentAddress(String permanentAddress) {
            this.permanentAddress = permanentAddress;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAlternativePhone() {
            return alternativePhone;
        }

        public void setAlternativePhone(String alternativePhone) {
            this.alternativePhone = alternativePhone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAadharNo() {
            return aadharNo;
        }

        public void setAadharNo(String aadharNo) {
            this.aadharNo = aadharNo;
        }

        public String getPanNo() {
            return panNo;
        }

        public void setPanNo(String panNo) {
            this.panNo = panNo;
        }

        public String getVoterNo() {
            return voterNo;
        }

        public void setVoterNo(String voterNo) {
            this.voterNo = voterNo;
        }

        public String getEducationalDetails() {
            return educationalDetails;
        }

        public void setEducationalDetails(String educationalDetails) {
            this.educationalDetails = educationalDetails;
        }

        public String getEmployeeStatus() {
            return employeeStatus;
        }

        public void setEmployeeStatus(String employeeStatus) {
            this.employeeStatus = employeeStatus;
        }

        public String getYearOfPass() {
            return yearOfPass;
        }

        public void setYearOfPass(String yearOfPass) {
            this.yearOfPass = yearOfPass;
        }

    }

    public class CV_feedbacks{
        private String aplitudeStatus,technicalStatus,hodStatus,mdStatus;
        @SerializedName("aplitude")
        @Expose
        private Feedback_aplitude feedback_aplitude;
        @SerializedName("technical")
        @Expose
        private Feedback_technicalStatus feedback_technicalStatus;
        @SerializedName("hod")
        @Expose
        private Feedback_hod feedback_hod;
        @SerializedName("md")
        @Expose
        private Feedback_md feedback_md;

        public String getAplitudeStatus() {
            return aplitudeStatus;
        }

        public void setAplitudeStatus(String aplitudeStatus) {
            this.aplitudeStatus = aplitudeStatus;
        }

        public String getTechnicalStatus() {
            return technicalStatus;
        }

        public void setTechnicalStatus(String technicalStatus) {
            this.technicalStatus = technicalStatus;
        }

        public String getHodStatus() {
            return hodStatus;
        }

        public void setHodStatus(String hodStatus) {
            this.hodStatus = hodStatus;
        }

        public String getMdStatus() {
            return mdStatus;
        }

        public void setMdStatus(String mdStatus) {
            this.mdStatus = mdStatus;
        }

        public Feedback_aplitude getFeedback_aplitude() {
            return feedback_aplitude;
        }

        public void setFeedback_aplitude(Feedback_aplitude feedback_aplitude) {
            this.feedback_aplitude = feedback_aplitude;
        }

        public Feedback_technicalStatus getFeedback_technicalStatus() {
            return feedback_technicalStatus;
        }

        public void setFeedback_technicalStatus(Feedback_technicalStatus feedback_technicalStatus) {
            this.feedback_technicalStatus = feedback_technicalStatus;
        }

        public Feedback_hod getFeedback_hod() {
            return feedback_hod;
        }

        public void setFeedback_hod(Feedback_hod feedback_hod) {
            this.feedback_hod = feedback_hod;
        }

        public Feedback_md getFeedback_md() {
            return feedback_md;
        }

        public void setFeedback_md(Feedback_md feedback_md) {
            this.feedback_md = feedback_md;
        }

        public class Feedback_aplitude{
            private String totalMarks;

            public String getTotalMarks() {
                return totalMarks;
            }

            public void setTotalMarks(String totalMarks) {
                this.totalMarks = totalMarks;
            }
        }

        public class Feedback_technicalStatus{
            private String technicalName,departmentName;
            @SerializedName("technicalFeedback")
            @Expose
            private List<technicalFeedback> technicalFeedbackList;

            public String getTechnicalName() {
                return technicalName;
            }

            public void setTechnicalName(String technicalName) {
                this.technicalName = technicalName;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public List<technicalFeedback> getTechnicalFeedbackList() {
                return technicalFeedbackList;
            }

            public void setTechnicalFeedbackList(List<technicalFeedback> technicalFeedbackList) {
                this.technicalFeedbackList = technicalFeedbackList;
            }

            public class technicalFeedback{
                private String sectionName,feedBack;

                public String getSectionName() {
                    return sectionName;
                }

                public void setSectionName(String sectionName) {
                    this.sectionName = sectionName;
                }

                public String getFeedBack() {
                    return feedBack;
                }

                public void setFeedBack(String feedBack) {
                    this.feedBack = feedBack;
                }
            }

        }

        public class Feedback_hod{
            private String hodName;
            @SerializedName("hodFeedback")
            @Expose
            private List<hodFeedback> hodFeedbackList;

            public List<hodFeedback> getHodFeedbackList() {
                return hodFeedbackList;
            }

            public void setHodFeedbackList(List<hodFeedback> hodFeedbackList) {
                this.hodFeedbackList = hodFeedbackList;
            }

            public String getHodName() {
                return hodName;
            }

            public void setHodName(String hodName) {
                this.hodName = hodName;
            }

            public class hodFeedback{
                private String sectionName,feedBack;

                public String getSectionName() {
                    return sectionName;
                }

                public void setSectionName(String sectionName) {
                    this.sectionName = sectionName;
                }

                public String getFeedBack() {
                    return feedBack;
                }

                public void setFeedBack(String feedBack) {
                    this.feedBack = feedBack;
                }
            }
        }

        public class Feedback_md{
            private String mdName;
            @SerializedName("mdFeedback")
            @Expose
            private List<mdFeedback> mdFeedbackList;

            public List<mdFeedback> getMdFeedbackList() {
                return mdFeedbackList;
            }

            public void setMdFeedbackList(List<mdFeedback> mdFeedbackList) {
                this.mdFeedbackList = mdFeedbackList;
            }

            public String getMdName() {
                return mdName;
            }

            public void setMdName(String mdName) {
                this.mdName = mdName;
            }

            public class mdFeedback{
                private String sectionName,feedBack;

                public String getSectionName() {
                    return sectionName;
                }

                public void setSectionName(String sectionName) {
                    this.sectionName = sectionName;
                }

                public String getFeedBack() {
                    return feedBack;
                }

                public void setFeedBack(String feedBack) {
                    this.feedBack = feedBack;
                }
            }
        }

    }

}
