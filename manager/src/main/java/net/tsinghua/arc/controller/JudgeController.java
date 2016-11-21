package net.tsinghua.arc.controller;

import com.alibaba.fastjson.JSONObject;
import net.tsinghua.arc.domain.PlanItemJudge;
import net.tsinghua.arc.exception.ArcException;
import net.tsinghua.arc.exception.ParamException;
import net.tsinghua.arc.service.JudgeService;
import net.tsinghua.arc.service.PlanService;
import net.tsinghua.arc.util.PageResult;
import net.tsinghua.arc.util.RequestUtil;
import net.tsinghua.arc.web.ResponseCodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ji on 16-11-19.
 */
@Controller
@RequestMapping("/judge/")
public class JudgeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgeController.class);

    @Autowired
    private JudgeService judgeService;

    @ResponseBody
    @RequestMapping("judge")
    public JSONObject judge(String message) {

        PageResult result = new PageResult();
        try {
            PlanItemJudge judge = (PlanItemJudge) RequestUtil.toClassBean(message, PlanItemJudge.class);
            if (judge.getUserId() == null || judge.getPlanItemId() == null || judge.getJudge() == null) {
                throw new ParamException("参数不正确");
            }

            int count = judgeService.checkIsAlreadyJudge(judge);
            if (count > 0) {
                throw new ArcException("已经评判过该计划项，不能重复评判");
            }
            judgeService.judgePlanItem(judge);

            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (ParamException pa){
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            result.setMessage(pa.getMessage());
            LOGGER.error("judge error,{}", pa.getMessage());
        }catch (ArcException ae){
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            result.setMessage(ae.getMessage()   );
            LOGGER.error("judge error,{}", ae.getMessage());
        }catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("judge error,{}", message, e);
        }
        return result.toJson();
    }
}
