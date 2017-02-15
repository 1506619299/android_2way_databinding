package com.example.twowaydatabindingexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2016/7/28.
 */
//
@BindingMethods({
        @BindingMethod(type = OneItemEditView.class, attribute = "value", method = "setValue"),//定义绑定的方法
        @BindingMethod(type = OneItemEditView.class, attribute = "valueDouble", method = "setValueDouble")
})
@InverseBindingMethods({@InverseBindingMethod(type = OneItemEditView.class, attribute = "value", event = "valueAttrChanged", method = "getValue"),
        @InverseBindingMethod(type = OneItemEditView.class, attribute = "valueDouble", event = "valueAttrChanged", method = "getValueDouble")})
public class OneItemEditView extends LinearLayout{
    private Context context;
    private TextView nameTv;
    private EditText valueTv;

    private String name;
    private String value;
    private int valueBg;
    private float textSize;
    private int textColor;
    private float padLeft;
    private float padRight;

    private double oldValue;

    public OneItemEditView(Context context) {
        this(context, null);
    }

    public OneItemEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OneItemEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.OneItemView, defStyleAttr, 0);
        name = typeArray.getString(R.styleable.OneItemView_name);
        value = typeArray.getString(R.styleable.OneItemView_value);
        valueBg = typeArray.getResourceId(R.styleable.OneItemView_valueBg,0);
        textSize = typeArray.getDimension(R.styleable.OneItemView_oneTextSize, sp2px(context,12));
        textColor = typeArray.getResourceId(R.styleable.OneItemView_oneTextColor, R.color.gray_66);
        padLeft = typeArray.getDimension(R.styleable.OneItemView_padLeft, 0);
        padRight = typeArray.getDimension(R.styleable.OneItemView_padRight, 0);
        typeArray.recycle();

        init();
    }

    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.one_item_edit_layout, this);

        nameTv = (TextView)view.findViewById(R.id.name);
        valueTv = (EditText)view.findViewById(R.id.value);

        nameTv.setText(name);
        nameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        nameTv.setTextColor(textColor);
        nameTv.setPadding((int) padLeft,0,0,0);

        valueTv.setText(value);
        valueTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        valueTv.setTextColor(textColor);
        valueTv.setBackgroundResource(valueBg);
        valueTv.setPadding(0,0,(int) padRight,0);
    }

    public void setValue(String valueString) {
        this.value = valueString;
        String text =  valueTv.getText().toString();
        if(text == null || !text.equals(valueString)) {
            valueTv.setText(valueString);
        }
    }

    public String getValue() {
        return valueTv.getText().toString();
    }

    public void setValueDouble(double valueDouble) {
        this.value = String.valueOf(valueDouble);
        double text = 0;
        if(!TextUtils.isEmpty(valueTv.getText().toString()) && isDecimal(valueTv.getText().toString()))
            text =  Double.valueOf(valueTv.getText().toString());
        if(!(text == valueDouble)) {
            valueTv.setText(String.valueOf(valueDouble));
        }
    }
    public double getValueDouble() {
        if(isDecimal(valueTv.getText().toString())) {
            oldValue = Double.valueOf(valueTv.getText().toString());
            return Double.valueOf(valueTv.getText().toString());
        }
        else {
            return oldValue;
        }
    }



    @BindingAdapter(value = {"valueAttrChanged"}, requireAll = false)//这里的evnet 与 @InverseBindingMethod 的event属性相同，requireAll这里意义不明，有可能在多个参数的时候要求所有都要注入
    public static void setValueWatcher(OneItemEditView view, final InverseBindingListener valueAttrChange) {

        final TextWatcher newWatcher;
        if(valueAttrChange == null){
            newWatcher = null;
        }else {
            newWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    valueAttrChange.onChange();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
        }

        TextWatcher oldWatcher = ListenerUtil.trackListener(view.valueTv, newWatcher, R.id.textWatcher);

        if (valueAttrChange != null) {
            view.valueTv.addTextChangedListener(newWatcher);
        }

        if (oldWatcher != null) {
            view.valueTv.removeTextChangedListener(oldWatcher);
        }
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isDecimal(String orginal){
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    private static boolean isMatch(String regex, String orginal){
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }
}
