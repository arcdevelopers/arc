package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.PlanItem;
import net.tsinghua.arc.domain.PlanItemEvidence;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ji on 16-11-16.
 */
public interface PlanItemDao {

    void addPlanItem(PlanItem planItem) throws Exception;

    void addEvidence(PlanItemEvidence evidence) throws Exception;

    void updateStatus(@Param("planItemId") Integer planItemId,@Param("status") Integer status);

    List<PlanItemEvidence> queryEvidenceByPlanItemId(Integer planItemId) throws Exception;

    PlanItem queryPlanItemById(Integer planItemId) throws Exception;
}
