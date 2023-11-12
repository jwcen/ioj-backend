package com.jcen.ioj.model.dto.qustionsubmit;

import lombok.Data;

/**
 * 题目用例
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗时间 ms
     */
    private Long time;

    /**
     * 消耗内存 kb
     */
    private Long memory;
}
