package com.yupi.ioj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.ioj.model.dto.question.QuestionQueryRequest;
import com.yupi.ioj.model.dto.qustionsubmit.QuestionSubmitAddRequest;
import com.yupi.ioj.model.dto.qustionsubmit.QuestionSubmitQueryRequest;
import com.yupi.ioj.model.entity.Question;
import com.yupi.ioj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.ioj.model.entity.User;
import com.yupi.ioj.model.vo.QuestionSubmitVO;
import com.yupi.ioj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author jcen
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-11-06 22:20:00
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
