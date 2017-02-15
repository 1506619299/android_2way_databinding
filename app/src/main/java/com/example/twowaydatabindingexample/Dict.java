package com.example.twowaydatabindingexample;

import com.example.twowaydatabindingexample.interfaces.GetSpinnerSelectCallBack;

/**
 * Created by asus on 2016/9/23.
 */

public class Dict implements GetSpinnerSelectCallBack{
    private String key;
    private String value;

    public Dict(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getSelect() {
        return key;
    }
}
