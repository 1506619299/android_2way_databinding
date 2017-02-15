package com.example.twowaydatabindingexample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by asus on 2016/9/18.
 */

public class ValueBean extends BaseObservable{

    private String value;
    private double money;


    public ValueBean(String value, double money) {
        this.value = value;
        this.money = money;
    }
    @Bindable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        notifyChange();
        System.out.println("ValueBean's 'value' field was changed");
    }

    @Bindable
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
        notifyChange();
    }

    public int getImg(String value){
        if(value.equals("0"))
            return R.mipmap.sh_1;
        if(value.equals("1"))
            return R.mipmap.sh_2;
        return R.mipmap.sh_3;
    }
}
