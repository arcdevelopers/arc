package net.tsinghua.arc.service;

import net.tsinghua.arc.dao.PlanDao;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ji on 16-11-28.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class ScheduleComponent {

    @Autowired
    private PlanDao planDao;

    @Scheduled(cron = "30 0 0 * * ?")
    public void completePlanItem() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, -1);
        String timeStr = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        planDao.completePlanItem(timeStr);
        List<Integer> completePlanIdList = planDao.queryPlanTobeComplete();
        if (completePlanIdList != null && completePlanIdList.size() > 0) {
            planDao.completePlan(completePlanIdList);
        }
    }
}
