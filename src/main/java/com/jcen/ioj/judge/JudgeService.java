package com.jcen.ioj.judge;

import com.jcen.ioj.model.entity.QuestionSubmit;

public interface JudgeService {

    /**
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
