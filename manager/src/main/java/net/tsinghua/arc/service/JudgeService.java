package net.tsinghua.arc.service;

import net.tsinghua.arc.dao.*;
import net.tsinghua.arc.domain.Plan;
import net.tsinghua.arc.domain.PlanItemJudge;
import net.tsinghua.arc.domain.UserJudgeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ji on 16-11-19.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class JudgeService {

    @Autowired
    private JudgeDao judgeDao;

    @Autowired
    private PlanSupervisorDao supervisorDao;

    @Autowired
    private PlanItemDao planItemDao;

    @Autowired
    private PlanDao planDao;

    @Autowired
    private UserDao userDao;

    public void judgePlanItem(PlanItemJudge judge) throws Exception {

        judgeDao.addJudgeResult(judge);

        int supervisorCount = supervisorDao.countSupervisor(judge.getPlanItemId());
        if (supervisorCount != 0) {
            if (judge.getJudge().intValue() == 0) {
                int agreeCount = judgeDao.countJudge(judge.getPlanItemId(), 0);
                if ((agreeCount * 1.0 / supervisorCount) > 0.5) {
                    planItemDao.updateStatus(judge.getPlanItemId(), 2);
                    List<Integer> completePlanIdList = planDao.queryPlanTobeComplete();
                    if (completePlanIdList != null && completePlanIdList.size() > 0) {
                        planDao.completePlan(completePlanIdList);
                    }
                }
            } else {
                int argueCount = judgeDao.countJudge(judge.getPlanItemId(), 1);
                if ((argueCount * 1.0 / supervisorCount) >= 0.5) {
                    planItemDao.updateStatus(judge.getPlanItemId(), 3);
                    int isSuccess = planDao.updateToFail(judge.getPlanItemId());
                    if (isSuccess == 1) {
                        Plan plan = planDao.queryPlanByItemId(judge.getPlanItemId());
                        int money = plan.getMoney();
                        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
                        double personMoney = Double.parseDouble(decimalFormat.format((money * 1.0) / supervisorCount));

                        userDao.increBalance(plan.getId(), personMoney);
                    }
                }
            }
        }


    }

    public int checkIsAlreadyJudge(PlanItemJudge judge) throws Exception {
        return judgeDao.checkIsAlreadyJudge(judge);
    }

    public List<UserJudgeResult> queryItemJudge(Integer planId, Integer planItemId) throws Exception {
        return judgeDao.queryItemJudge(planId, planItemId);
    }
}
