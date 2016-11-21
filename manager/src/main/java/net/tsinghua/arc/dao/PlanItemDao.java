package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.PlanItem;
import net.tsinghua.arc.domain.PlanItemEvidence;

/**
 * Created by ji on 16-11-16.
 */
public interface PlanItemDao {

    void addPlanItem(PlanItem planItem) throws Exception;

    void addEvidence(PlanItemEvidence evidence) throws Exception;

    void updateToFail(Integer planItemId);

}
