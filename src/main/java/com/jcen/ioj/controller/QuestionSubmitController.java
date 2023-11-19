package com.jcen.ioj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcen.ioj.model.entity.QuestionSubmit;
import com.jcen.ioj.service.QuestionSubmitService;
import com.jcen.ioj.common.BaseResponse;
import com.jcen.ioj.common.ErrorCode;
import com.jcen.ioj.common.ResultUtils;
import com.jcen.ioj.exception.BusinessException;
import com.jcen.ioj.model.dto.qustionsubmit.QuestionSubmitAddRequest;
import com.jcen.ioj.model.dto.qustionsubmit.QuestionSubmitQueryRequest;
import com.jcen.ioj.model.entity.User;
import com.jcen.ioj.model.vo.QuestionSubmitVO;
import com.jcen.ioj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/jwcen">jcen</a>
 * 
 */
@RestController
@RequestMapping("/question/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交题目的 id
     */
    @PostMapping("/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                         HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);

        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));

        Page<QuestionSubmitVO> questionSubmitVOPage = questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, userService.getLoginUser(request));
        // 脱敏
        return ResultUtils.success(questionSubmitVOPage);
    }

}
