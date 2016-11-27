package net.tsinghua.arc.service;

import net.tsinghua.arc.dao.PlanDao;
import net.tsinghua.arc.dao.PlanItemDao;
import net.tsinghua.arc.dao.PlanSupervisorDao;
import net.tsinghua.arc.dao.UserDao;
import net.tsinghua.arc.domain.*;
import net.tsinghua.arc.enums.PlanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ji on 16-11-16.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class PlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    private PlanDao planDao;

    @Autowired
    private PlanItemDao planItemDao;

    @Autowired
    private PlanSupervisorDao planSupervisorDao;

    @Autowired
    private UserDao userDao;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, Throwable.class})
    public void addPlan(Plan plan, Calendar sc, Calendar ec, Calendar today) throws Exception {

//        userDao.minusBalance(plan.getUserId(), plan.getMoney());

        planDao.addPlan(plan);
        while (sc.before(ec)) {
            if (sc.compareTo(today) >= 0) {
                this.addPlanItem(plan, sc, today);
                sc.add(Calendar.DATE, 1);
            } else {
                sc.add(Calendar.DATE, 1);
            }
        }
        if (sc.compareTo(today) >= 0) {
            this.addPlanItem(plan, sc, today);
        }
    }

    private void addPlanItem(Plan plan, Calendar itemTime, Calendar today) throws Exception {
        LOGGER.debug("新增计划项，计划id:{}时间：{}", plan.getId(), itemTime.getTime());
        PlanItem planItem = new PlanItem();
        planItem.setPlanId(plan.getId());
        planItem.setPlanDate(itemTime.getTime());
        if (itemTime.compareTo(today) <= 0) {
            planItem.setStatus(PlanStatus.START);
        } else {
            planItem.setStatus(PlanStatus.WAIT);
        }

        planItemDao.addPlanItem(planItem);
    }

    /**
     * 加入计划监督
     */
    public void joinPlan(PlanSupervisor planSupervisor) throws Exception {
        int hasJoin = planSupervisorDao.checkIsJoin(planSupervisor);
        if (hasJoin > 0) {
            throw new RuntimeException("已经监督过改计划，不能重复监督");
        }
        planSupervisorDao.joinPlan(planSupervisor);
    }

    public List<Plan> queryPlanList(Plan plan) throws Exception {
        return planDao.queryPlanList(plan);
    }

    public List<User> querySupervisorById(Integer planId) throws Exception {
        return planDao.querySupervisorById(planId);
    }

    public List<PlanItem> queryPlanItemByPlanId(Integer planId) throws Exception {
        return planDao.queryPlanItemByPlanId(planId);
    }

    public Plan queryPlanById(Integer planId) throws Exception {
        return planDao.queryPlanById(planId);
    }

    public void addEvidence(PlanItemEvidence evidence) throws Exception {
        planItemDao.addEvidence(evidence);
    }

    public List<PlanItemEvidence> queryEvidenceByPlanItemId(Integer planItemId) throws Exception{
        return planItemDao.queryEvidenceByPlanItemId(planItemId);
    }
}
