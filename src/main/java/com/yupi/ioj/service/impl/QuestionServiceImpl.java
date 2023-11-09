package com.yupi.ioj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.ioj.model.entity.Question;
import com.yupi.ioj.service.QuestionService;
import com.yupi.ioj.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author jcen
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2023-11-06 22:19:03
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




