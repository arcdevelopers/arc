package net.tsinghua.arc.domain;

import java.util.Date;

/**
 * Created by ji on 16-11-18.
 */
public class PlanItemEvidence {

    private Integer id;

    private Integer planItemId;

    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
