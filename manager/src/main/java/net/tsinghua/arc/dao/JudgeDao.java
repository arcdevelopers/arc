package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.PlanItemJudge;

/**
 * Created by ji on 16-11-19.
 */
public interface JudgeDao {

    void addJudgeResult(PlanItemJudge judge) throws Exception;

    int countJudge(Integer planItemId) throws Exception;

    int checkIsAlreadyJudge(PlanItemJudge judge) throws Exception;
}
