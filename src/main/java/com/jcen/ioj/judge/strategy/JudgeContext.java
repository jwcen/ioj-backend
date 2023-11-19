package com.jcen.ioj.judge.strategy;

import com.jcen.ioj.model.dto.question.JudgeCase;
import com.jcen.ioj.judge.codesandbox.model.JudgeInfo;
import com.jcen.ioj.model.entity.Question;
import com.jcen.ioj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;
}
