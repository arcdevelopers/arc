package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.PlanItemJudge;
import net.tsinghua.arc.domain.UserJudgeResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ji on 16-11-19.
 */
public interface JudgeDao {

    void addJudgeResult(PlanItemJudge judge) throws Exception;

    int countJudge(Integer planItemId) throws Exception;

    int checkIsAlreadyJudge(PlanItemJudge judge) throws Exception;

    List<UserJudgeResult> queryItemJudge(@Param("planId") Integer planId, @Param("planItemId") Integer planItemId) throws Exception;
}
