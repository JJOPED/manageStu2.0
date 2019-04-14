package com.example.administrator.managestu;

public class Result {
        String theNumber;
        String theTime;
        String theSchool;
        String theConfirmName;

        public Result(String  _number,String _time, String _school, String _confirmname) {
            this.theNumber = _number;
            this.theTime = _time;
            this.theSchool = _school;
            this.theConfirmName = _confirmname;
        }

        public String getTheNumber() {
            return  theNumber;
        }
        public String getTheTime() {
            return theTime;
        }
        public String getTheSchool() {
            return theSchool;
        }
        public String getTheConfirmName() {
            return theConfirmName;
        }
}
