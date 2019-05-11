package com.example.administrator.managestu;

public class Course {
    String courseName;
    String coursegrade;

    public Course(String _name,String _garde){
        this.courseName = _name;
        this.coursegrade = _garde;
    }

    public void setCoursegrade(String coursegrade) {
        this.coursegrade = coursegrade;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCoursegrade() {
        return coursegrade;
    }
}
