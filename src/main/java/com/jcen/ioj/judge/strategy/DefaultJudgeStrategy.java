package com.jcen.ioj.judge.strategy;

import com.jcen.ioj.model.entity.Question;

import cn.hutool.json.JSONUtil;
import com.jcen.ioj.model.dto.question.JudgeCase;
import com.jcen.ioj.model.dto.question.JudgeConfig;
import com.jcen.ioj.judge.codesandbox.model.JudgeInfo;
import com.jcen.ioj.model.enums.JudgeInfoMsgEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMsgEnum judgeInfoMsgEnum = JudgeInfoMsgEnum.ACCEPTED; // 初始状态

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);

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
        if (time > expectTime) {
            judgeInfoMsgEnum = JudgeInfoMsgEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
            return judgeInfoResponse;
        }


        judgeInfoResponse.setMessage(judgeInfoMsgEnum.getValue());
        return judgeInfoResponse;
    }
}
