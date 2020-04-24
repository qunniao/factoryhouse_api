package com.baoge.Util;

import com.baoge.Util.Enums.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;



@Data
public class SCException extends RuntimeException {

    private Integer code;

    public SCException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

}
