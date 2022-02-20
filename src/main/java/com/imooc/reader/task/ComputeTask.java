package com.imooc.reader.task;

import com.imooc.reader.service.BookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 完成自动计算任务
 */
@Component
public class ComputeTask {

    @Resource
    private BookService bookService;

    // 调度
    @Scheduled(cron = "0 * * * * ?")
    public void updateEvaluation(){
        bookService.updateEvaluation();
        System.out.println("计算评分任务调度完成");
    }

}
