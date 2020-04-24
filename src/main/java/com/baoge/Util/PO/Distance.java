package com.baoge.Util.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: Distance
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/21 13:52
 */
@Data
public class Distance implements Serializable {
    private double X;
    private double Y;

    public Distance(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }

}
