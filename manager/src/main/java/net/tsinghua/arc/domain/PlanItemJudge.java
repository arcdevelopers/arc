package net.tsinghua.arc.domain;

import java.util.Date;

/**
 * Created by ji on 16-11-19.
 */
public class PlanItemJudge {

    private Integer id;

    private Integer planItemId;

    private Integer userId;

    private Integer judge;

    private String comment;

    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanItemId() {
        return planItemId;
    }

    public void setPlanItemId(Integer planItemId) {
        this.planItemId = planItemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getJudge() {
        return judge;
    }

    public void setJudge(Integer judge) {
        this.judge = judge;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
