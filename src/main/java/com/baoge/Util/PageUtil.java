package com.baoge.Util;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api(value = "/data", tags = "全国地址API")
public class PageUtil {
    @ApiModelProperty(value = "当前页", name = "pageNum")
    private int pageNum = Integer.valueOf(1);
    @ApiModelProperty(value = "数据行数", name = "pageSize")
    private int pageSize = Integer.valueOf(15);

}
