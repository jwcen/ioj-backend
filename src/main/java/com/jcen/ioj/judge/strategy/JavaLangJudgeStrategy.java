package com.jcen.ioj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.jcen.ioj.model.dto.question.JudgeCase;
import com.jcen.ioj.model.dto.question.JudgeConfig;
import com.jcen.ioj.judge.codesandbox.model.JudgeInfo;
import com.jcen.ioj.model.entity.Question;
import com.jcen.ioj.model.enums.JudgeInfoMsgEnum;

import java.util.List;

/**
 * Java 程序判题策略
 */
public class JavaLangJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);

        JudgeInfoMsgEnum judgeInfoMsgEnum = JudgeInfoMsgEnum.ACCEPTED; // 初始状态
        if (outputList.size() != inputList.size()) {
            judgeInfoMsgEnum = JudgeInfoMsgEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
            return judgeInfoResponse;
        }

        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMsgEnum = JudgeInfoMsgEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
                return judgeInfoResponse;
            }
        }

        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long expectMemory = judgeConfig.getMemoryLimit();
        Long expectTime = judgeConfig.getTimeLimit();
        if (memory > expectMemory) {
            judgeInfoMsgEnum = JudgeInfoMsgEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
            return judgeInfoResponse;
        }

        // Java程序本身需要额外执行 10000 ms
        long JAVA_APP_TIME_COST = 10L;
        if ((time - JAVA_APP_TIME_COST) > expectTime) {
            judgeInfoMsgEnum = JudgeInfoMsgEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());

        return judgeInfoResponse;
    }
}
