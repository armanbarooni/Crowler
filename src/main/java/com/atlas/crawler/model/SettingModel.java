/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.model;

public class SettingModel {

    private String monthExpirePassword;

    private Integer numberOfAttempts;

    private String lockTime;

    public SettingModel(){

    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public String getMonthExpirePassword() {
        return monthExpirePassword;
    }

    public void setMonthExpirePassword(String monthExpirePassword) {
        this.monthExpirePassword = monthExpirePassword;
    }
}
