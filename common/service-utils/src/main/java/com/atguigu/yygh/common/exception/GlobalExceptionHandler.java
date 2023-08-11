package com.atguigu.yygh.common.exception;


import com.atguigu.yygh.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DuplicateKeyException;

/**
 * @Author Weizhu
 * @Date 2023/7/25 19:34
 * @注释
 * 异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        //e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return Result.fail().message("数据解析失败");
    }

//    @ExceptionHandler(DuplicateKeyException.class)
//    public Result sqlException(DuplicateKeyException e){
//
//        log.error(ExceptionUtils.getStackTrace(e));
//        return Result.fail().message(("医院编号已存在"));
//    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
//    @ResponseBody
    public Result yyghException(YyghException e){
        //e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        Integer code = e.getCode();
        String msg = e.getMsg();
        return Result.fail().code(code).message(msg);
    }
}
