package model;


import java.io.Serializable;
import java.time.LocalDateTime;

import utils.GenericFormatter;


public class Presence implements Comparable<Presence>, Serializable {
    private Integer id;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;

    public LocalDateTime getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(LocalDateTime enterTime) {
        this.enterTime = enterTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Presence(Integer id, LocalDateTime enterTime) {
        this.id = id;
        this.enterTime = enterTime;
    }

    public Presence(Integer id, LocalDateTime enterTime, LocalDateTime leaveTime) {
        this.id = id;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Presence [id=" + id +", enterTime=" + GenericFormatter.formatDateTime(enterTime) + ", leaveTime=" + GenericFormatter.formatDateTime(leaveTime) + "]";
    }

    // Ordeno numericamente por el id y si tienen id ordeno por fecha y hora de
    // entrada
    @Override
    public int compareTo(Presence o) {
        if (this.id != o.id) {
            return this.id.compareTo(o.id);
        } else {
            return this.enterTime.compareTo(o.enterTime);
        }
    }
}
