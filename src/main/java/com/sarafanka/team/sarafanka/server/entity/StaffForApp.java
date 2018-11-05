package com.sarafanka.team.sarafanka.server.entity;

public class StaffForApp {
    private Long accountID;
    private String staffName;
    private String staffWorkingPlace;

    public StaffForApp() {
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffWorkingPlace() {
        return staffWorkingPlace;
    }

    public void setStaffWorkingPlace(String staffWorkingPlace) {
        this.staffWorkingPlace = staffWorkingPlace;
    }
}
