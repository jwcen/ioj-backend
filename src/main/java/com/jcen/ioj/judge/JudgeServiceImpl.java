package com.jcen.ioj.judge;

import com.jcen.ioj.judge.strategy.JudgeContext;
import com.jcen.ioj.judge.codesandbox.model.JudgeInfo;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.json.JSONUtil;
import com.jcen.ioj.common.ErrorCode;
import com.jcen.ioj.exception.BusinessException;
import com.jcen.ioj.judge.codesandbox.CodeSandBox;
import com.jcen.ioj.judge.codesandbox.CodeSandBoxFactory;
import com.jcen.ioj.judge.codesandbox.CodeSandBoxProxy;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeResponse;
import com.jcen.ioj.model.dto.question.JudgeCase;
import com.jcen.ioj.model.entity.Question;
import com.jcen.ioj.model.entity.QuestionSubmit;
import com.jcen.ioj.model.enums.QuestionSubmitStatusEnum;
import com.jcen.ioj.service.QuestionService;
import com.jcen.ioj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type}")
    private String type;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1. 根据题目提交信息，拿到对应题目、提交的代码、语言等
        QuestionSubmit questionSubmitInfo = questionSubmitService.getById(questionSubmitId);
        if (questionSubmitInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }

        long questionId = questionSubmitInfo.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 2. 如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmitInfo.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        // 3. 更改题目提交状态->判题中，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean updateOK = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateOK) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 4. 调用沙箱，获取执行结果
        String code = questionSubmitInfo.getCode();
        String language = questionSubmitInfo.getLanguage();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);

        // 4. 根据2.的结果判断、设置题目提交状态和信息(上下文）
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmitInfo);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 5. 更新数据库判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        updateOK = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateOK) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        return questionSubmitService.getById(questionId);
    }
}
