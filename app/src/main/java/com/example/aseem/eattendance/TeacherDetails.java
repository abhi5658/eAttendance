/**
 * Created by Abhishek on 13-Jun-17.
 */

package com.example.aseem.eattendance;

public class TeacherDetails {

    private String name;
    private String mUid;
    private String subjectCode;

    public TeacherDetails(){
    }

    public TeacherDetails(String name, String mUid, String subjectCode){
        this.name = name;
        this.mUid = mUid;
        this.subjectCode = subjectCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
