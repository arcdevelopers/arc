package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.Plan;
import net.tsinghua.arc.domain.PlanItem;
import net.tsinghua.arc.domain.User;

import java.util.List;

/**
 * Created by ji on 16-11-16.
 */
public interface PlanDao {

    void addPlan(Plan plan) throws Exception;

    List<Plan> queryPlanList(Plan plan) throws Exception;

    List<User> querySupervisorById(Integer planId) throws Exception;

    Plan queryPlanById(Integer planId) throws Exception;

    List<PlanItem> queryPlanItemByPlanId(Integer planId) throws Exception;

    int updateToFail(Integer planItemId);

    Plan queryPlanByItemId(Integer planItemId);
}
