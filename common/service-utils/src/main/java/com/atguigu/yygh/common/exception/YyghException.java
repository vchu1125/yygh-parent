package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @Author Weizhu
 * @Date 2023/7/25 19:21
 * @注释
 *封装自定义的异常信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YyghException extends RuntimeException{
    //状态码
    private Integer code;
   //返回给前端的错误信息
    private String msg;

    public YyghException(Integer code, String msg,Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;

    }

    public YyghException(ResultCodeEnum resultCodeEnum,Throwable cause) {
        super(cause);
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();

    }


    public YyghException(ResultCodeEnum signError) {
        this.code = signError.getCode();
        this.msg = signError.getMessage();
    }
}
