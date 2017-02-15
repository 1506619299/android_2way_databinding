package com.example.twowaydatabindingexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.DataBindingUtil;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.Observable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.twowaydatabindingexample.databinding.OneItemSpinnerLayoutBinding;
import com.example.twowaydatabindingexample.interfaces.GetSpinnerSelectCallBack;

import java.util.List;
import java.util.Objects;

/**
 * Created by asus on 2016/9/22.
 * value 必须是list.get(position)的一个值，或者是list.get(position)的对象的一个属性的一个值
 * ist 必须实现GetSpinnerSelectCallBack接口，返回要获取的spinner的值
 */
@BindingMethods({
        @BindingMethod(type = OneItemSpinnerView.class, attribute = "value", method = "setValue"),//定义绑定的方法
        @BindingMethod(type = OneItemSpinnerView.class, attribute = "list", method = "setList")
})
@InverseBindingMethods({
        @InverseBindingMethod(type = OneItemSpinnerView.class, attribute = "value", event = "valueAttrChanged", method = "getValue"),
        @InverseBindingMethod(type = OneItemSpinnerView.class, attribute = "list", event = "listAttrChanged", method = "getList")
})
public class OneItemSpinnerView extends LinearLayout {
    private Context context;
    private MySpinner valueSp;

    private String name;
    private String value;
    private int valueBg;
    private float textSize;
    private int textColor;
    private float padLeft;
    private float padRight;

    private List list;
    private OnValueChanged valueChanged;
    private OnListChanged listChanged;

    private final OneItemSpinnerLayoutBinding binding;

    public OneItemSpinnerView(Context context) {
        this(context, null);
    }

    public OneItemSpinnerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OneItemSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.OneItemView, defStyleAttr, 0);
        name = typeArray.getString(R.styleable.OneItemView_name);
        valueBg = typeArray.getResourceId(R.styleable.OneItemView_valueBg, 0);
        textSize = typeArray.getDimension(R.styleable.OneItemView_oneTextSize, sp2px(context, 12));
        textColor = typeArray.getResourceId(R.styleable.OneItemView_oneTextColor, R.color.gray_66);
        padLeft = typeArray.getDimension(R.styleable.OneItemView_padLeft, 0);
        padRight = typeArray.getDimension(R.styleable.OneItemView_padRight, 0);
        typeArray.recycle();

        this.binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.one_item_spinner_layout, this,true);
        this.binding.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == com.example.twowaydatabindingexample.BR.value) {
                    setValue(binding.getValue());
                }
            }
        });
        init();
    }

    private void init() {
        View view = binding.getRoot();
//        View view = LayoutInflater.from(context).inflate(R.layout.one_item_spinner_layout, this);

        TextView nameTv = (TextView) view.findViewById(R.id.name);
        valueSp = (MySpinner) view.findViewById(R.id.value);

        nameTv.setText(name);
        nameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        nameTv.setTextColor(textColor);
        nameTv.setPadding((int) padLeft, 0, 0, 0);

        valueSp.setBackgroundResource(valueBg);
        valueSp.setPadding(0, 0, (int) padRight, 0);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valueSp.setFocusable(true);
                valueSp.setFocusableInTouchMode(true);
                valueSp.requestFocus();
                valueSp.requestFocusFromTouch();
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
        if(!value.equals(this.value)){
            this.value = value;
            this.binding.setValue(value);
            if(valueChanged != null){
                valueChanged.onValChanged(this,value);
            }
        }
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        if (list == null){
            return;
        }
        if(this.list != list){
            this.list = list;
            this.binding.setList(list);
            if(listChanged != null)
                listChanged.onValChanged(this, list);
        }

    }

    @BindingAdapter(value = {"valueAttrChanged"}, requireAll = false)
    public static void setValueLister(OneItemSpinnerView view, final InverseBindingListener valueChange){
        if(valueChange == null){
            view.setValueChanged(null);
        }else{
            view.setValueChanged(new OnValueChanged() {
                @Override
                public void onValChanged(OneItemSpinnerView view, String value) {
                    valueChange.onChange();
                }
            });
        }
    }

    @BindingAdapter(value = {"listAttrChanged"}, requireAll = false)
    public static void setListLister(OneItemSpinnerView view, final InverseBindingListener listChange){
        if(listChange == null){
            view.setListChanged(null);
        }else{
            view.setListChanged(new OnListChanged() {
                @Override
                public void onValChanged(OneItemSpinnerView view, List list) {
                    listChange.onChange();
                }
            });
        }
    }

    public interface OnValueChanged{
        void onValChanged(OneItemSpinnerView view, String value);
    }

    public interface OnListChanged{
        void onValChanged(OneItemSpinnerView view, List list);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
