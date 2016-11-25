package net.tsinghua.arc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.tsinghua.arc.domain.*;
import net.tsinghua.arc.enums.PlanStatus;
import net.tsinghua.arc.exception.ParamException;
import net.tsinghua.arc.service.PlanService;
import net.tsinghua.arc.util.PageResult;
import net.tsinghua.arc.util.RequestUtil;
import net.tsinghua.arc.web.ResponseCodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ji on 16-11-16.
 */
@Controller
@RequestMapping("/arc/plan/")
public class PlanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);


    @Autowired
    private PlanService planService;

    /**
     * 增加计划
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("addPlan")
    public JSONObject addPlan(String message) {

        PageResult result = new PageResult();
        try {
            Plan plan = (Plan) RequestUtil.toClassBean(message, Plan.class);

            if (plan.getPlanName() == null || plan.getMoney() == null || plan.getStartTime() == null || plan.getEndTime() == null) {
                throw new ParamException("参数不完整");
            }


            //保存计划项
            Date startTime = plan.getStartTime();
            Date endTime = plan.getEndTime();

            Calendar sc = Calendar.getInstance();
            sc.setTime(startTime);
            sc.set(Calendar.HOUR_OF_DAY, 0);
            sc.set(Calendar.MINUTE, 0);
            sc.set(Calendar.SECOND, 0);
            sc.set(Calendar.MILLISECOND, 0);

            Calendar ec = Calendar.getInstance();
            ec.setTime(endTime);
            ec.set(Calendar.HOUR_OF_DAY, 0);
            ec.set(Calendar.MINUTE, 0);
            ec.set(Calendar.SECOND, 0);
            ec.set(Calendar.MILLISECOND, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            if (sc.compareTo(ec) > 0 || ec.compareTo(today) < 0) {
                throw new ParamException("不能创建早于今天的计划");
            }

            if (sc.compareTo(today) <= 0) {
                plan.setStatus(PlanStatus.START);
            } else {
                plan.setStatus(PlanStatus.WAIT);
            }

            // insert plan
            planService.addPlan(plan, sc, ec, today);

        } catch (ParamException pe) {
            result.setCode(ResponseCodeConstants.PARAM_ERROR_CODE);
            result.setMessage(pe.getMessage());
            LOGGER.error("addPlan error", pe.getMessage());
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("addPlan error", e);
        }
        return result.toJson();
    }

    /**
     * 参与监督计划
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("joinPlan")
    public JSONObject joinPlan(String message) {
        PageResult result = new PageResult();
        try {
            PlanSupervisor planSupervisor = (PlanSupervisor) RequestUtil.toClassBean(message, PlanSupervisor.class);
            Integer userId = planSupervisor.getUserId();
            Integer planId = planSupervisor.getPlanId();
            if (userId == null || planId == null) {
                throw new RuntimeException("userId and planId can not be null");
            }

            planService.joinPlan(planSupervisor);
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            result.setMessage(e.getMessage());
            LOGGER.error("join plan error", e);
        }
        return result.toJson();
    }

    /**
     * 获取计划列表
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("getPlanList")
    public JSONObject getPlanList(@RequestParam(required = false) String message) {

        PageResult result = new PageResult();
        try {
            Plan plan;
            if (message == null) {
                plan = new Plan();
            } else {
                plan = (Plan) RequestUtil.toClassBean(message, Plan.class);
            }

            List<Plan> planList = planService.queryPlanList(plan);

            if (planList != null && planList.size() > 0) {
                for (Plan tmpPlan : planList) {
                    List<User> supervisors = planService.querySupervisorById(tmpPlan.getId());
                    tmpPlan.setSupervisors(supervisors);
                }
            } else {
                planList = new ArrayList<>();
            }

            result.setList(planList);
            if (plan != null && plan.getStart() != null) {
                result.setStart(plan.getStart() + planList.size());
                result.setLimit(plan.getLimit());
            }
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("getPlanList error", e);
        }
        return result.toJson();
    }

    /**
     * 获取plan详情
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("getPlanDetail")
    public JSONObject getPlanDetail(String message) {
        PageResult result = new PageResult();
        try {
            JSONObject paramObj = JSON.parseObject(message);
            Integer planId = paramObj.getInteger("planId");
            if (planId == null) {
                throw new RuntimeException("planId can not be null");
            }
            Plan plan = planService.queryPlanById(planId);
            result.setObj(plan);
            if (plan != null) {
                List<User> users = planService.querySupervisorById(planId);
                plan.setSupervisors(users);
                List<PlanItem> planItemList = planService.queryPlanItemByPlanId(planId);
                result.setList(planItemList);
            }
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("getPlanDetail error", e);
        }
        return result.toJson();
    }

    /**
     * 上传证据
     *
     * @param planItemId 计划项id
     * @param file       图片文件
     * @param comment    描述，说明
     * @return
     */
    @ResponseBody
    @RequestMapping("addEvidence")
    public JSONObject addEvidence(Integer planItemId, MultipartFile file, @RequestParam(value = "comment", required = false) String comment) {
        PageResult result = new PageResult();
        try {
            if (planItemId == null) {
                throw new RuntimeException("planItemId can not be null");
            }

            String name = file.getOriginalFilename();
            String url = "/media/ji/document/school/arc/evidence/" + planItemId + "_" + System.currentTimeMillis();
            File dir = new File(url);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = url + "/" + name;
            file.transferTo(new File(filePath));

            PlanItemEvidence evidence = new PlanItemEvidence();
            evidence.setAvatar(filePath);
            evidence.setComment(comment);
            evidence.setPlanItemId(planItemId);

            planService.addEvidence(evidence);
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("uploadPhoto Exception", e);
        }
        return result.toJson();
    }

}
