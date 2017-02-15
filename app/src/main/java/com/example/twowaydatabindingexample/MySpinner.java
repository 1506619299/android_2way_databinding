package com.example.twowaydatabindingexample;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.twowaydatabindingexample.interfaces.GetSpinnerSelectCallBack;

import java.util.List;

/**
 * Created by asus on 2016/9/22.l
 *  * value 必须是list.get(position)的一个值，或者是list.get(position)的对象的一个属性的一个值
 * ist 必须实现GetSpinnerSelectCallBack接口，返回要获取的spinner的值
 */
@BindingMethods({
        @BindingMethod(type = MySpinner.class, attribute = "value", method = "setValue"),//定义绑定的方法
        @BindingMethod(type = MySpinner.class, attribute = "list", method = "setList")
})
@InverseBindingMethods({
        @InverseBindingMethod(type = MySpinner.class, attribute = "value", event = "valueAttrChanged", method = "getValue"),
        @InverseBindingMethod(type = MySpinner.class, attribute = "list", event = "listAttrChanged", method = "getList")
})
public class MySpinner extends Spinner {

    private String value;
    private List<GetSpinnerSelectCallBack> list;
    private Context context;
    private OnValueChanged valueChanged;
    private OnListChanged listChanged;

    public MySpinner(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                setValue(((GetSpinnerSelectCallBack)getList().get(position)).getSelect());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setValueChanged(OnValueChanged valueChanged){
        this.valueChanged = valueChanged;
    }

    private void setListChanged(OnListChanged listChanged){
        this.listChanged = listChanged;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if(list == null || value == null)
            return;
        if(value.equals(this.value))
            return;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getSelect().contains(value)){
                this.value = value;
                this.setSelection(i);
                valueChanged.onValChanged(this,value);
            }
        }
    }


    public List getList() {
        return list;
    }

    public void setList(List list) {
        if(list == null)
            return;
        if(list.equals(this.list) || this.list == list)
            return;
        Object obj = list.get(0);
        if( ! (obj instanceof GetSpinnerSelectCallBack))
            throw new RuntimeException(list.getClass().getGenericSuperclass()
                    + " must implement GetSpinnerSelectCallBack");
        this.list = list;
        ArrayAdapter<GetSpinnerSelectCallBack> adapter = new ArrayAdapter<GetSpinnerSelectCallBack>(context, R.layout.support_simple_spinner_dropdown_item, list);
        setAdapter(adapter);
        listChanged.onValChanged(this, list);
    }

    @BindingAdapter(value = {"valueAttrChanged"}, requireAll = false)
    public static void setValueLister(MySpinner view, final InverseBindingListener valueChange){
        if(valueChange == null){
            view.setValueChanged(null);
        }else{
            view.setValueChanged(new OnValueChanged() {
                @Override
                public void onValChanged(MySpinner view, String value) {
                    valueChange.onChange();
                }
            });
        }
    }

    @BindingAdapter(value = {"listAttrChanged"}, requireAll = false)
    public static void setListLister(MySpinner view, final InverseBindingListener listChange){
        if(listChange == null){
            view.setListChanged(null);
        }else{
            view.setListChanged(new OnListChanged() {
                @Override
                public void onValChanged(MySpinner view, List list) {
                    listChange.onChange();
                }
            });
        }
    }

    public interface OnValueChanged{
        void onValChanged(MySpinner view, String value);
    }

    public interface OnListChanged{
        void onValChanged(MySpinner view, List list);
    }
}
