/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.entity;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "setting",schema = "public")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "is required")
    @Range(min = 1,max = 12, message = "is required")
    @Column(name = "expire_date")
    private Integer expireDate;

    @NotNull(message = "is required")
    @Range(min = 1,max = 72, message = "is required")
    @Column(name = "lock_time_duration")
    private Integer lockTimeDuration;

    @NotNull(message = "is required")
    @Range(min = 1,max = 10, message = "is required")
    @Column(name = "max_failed_attempts")
    private Integer maxFailedAttempts;

    public Setting(Integer expireDate, Integer lockTimeDuration, Integer maxFailedAttempts) {
        this.expireDate = expireDate;
        this.lockTimeDuration = lockTimeDuration;
        this.maxFailedAttempts = maxFailedAttempts;
    }

    public Setting(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Integer expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getLockTimeDuration() {
        return lockTimeDuration;
    }

    public void setLockTimeDuration(Integer lockTimeDuration) {
        this.lockTimeDuration = lockTimeDuration;
    }

    public Integer getMaxFailedAttempts() {
        return maxFailedAttempts;
    }

    public void setMaxFailedAttempts(Integer maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", expireDate=" + expireDate +
                ", lockTimeDuration=" + lockTimeDuration +
                ", maxFailedAttempts=" + maxFailedAttempts +
                '}';
    }
}
