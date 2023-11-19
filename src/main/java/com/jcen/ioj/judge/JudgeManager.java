package com.jcen.ioj.judge;

import com.jcen.ioj.judge.strategy.DefaultJudgeStrategy;
import com.jcen.ioj.judge.strategy.JavaLangJudgeStrategy;
import com.jcen.ioj.judge.strategy.JudgeContext;
import com.jcen.ioj.judge.strategy.JudgeStrategy;
import com.jcen.ioj.judge.codesandbox.model.JudgeInfo;
import com.jcen.ioj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     * @param judgeContext - 上下文信息
     * @return JudeInfo
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        String language = questionSubmit.getLanguage();

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("Java".equals(language)) {
            judgeStrategy = new JavaLangJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
