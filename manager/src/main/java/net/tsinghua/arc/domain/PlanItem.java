package net.tsinghua.arc.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by ji on 16-11-16.
 */
public class PlanItem {

    private Integer id;

    private Integer planId;

    private Date planDate;

    private Integer status;


    private List<UserJudgeResult> judges;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<UserJudgeResult> getJudges() {
        return judges;
    }

    public void setJudges(List<UserJudgeResult> judges) {
        this.judges = judges;
    }
}
