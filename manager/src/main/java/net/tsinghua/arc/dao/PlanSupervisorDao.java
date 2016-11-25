package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.PlanSupervisor;

/**
 * Created by ji on 16-11-17.
 */
public interface PlanSupervisorDao {

    void joinPlan(PlanSupervisor planSupervisor)throws Exception;

    int countSupervisor(Integer planItemId);

    int checkIsJoin(PlanSupervisor planSupervisor) throws Exception;
}
