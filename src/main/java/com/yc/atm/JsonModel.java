package com.yc.atm;

import lombok.Data;

import java.io.Serializable;

//  存储数据
@Data
public class JsonModel<T> implements Serializable {
    private int code;
    private String error;
    private T obj;
}
