package com.example.twowaydatabindingexample;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by asus on 2016/9/18.
 */
@BindingMethods({
        @BindingMethod(type = AView.class, attribute = "name", method = "setName")//定义绑定的方法
})

@InverseBindingMethods({
        @InverseBindingMethod(type = AView.class, attribute = "name")//这里有个event属性，如果不填默认event为 attribute+"AttrChanged" ,比如这里就是nameAttrChanged
})
public class AView extends EditText {

    private String name;

    public AView(Context context) {
        super(context);
    }

    public AView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getName() {
        return getText().toString();
    }

    public void setName(String name) {
        this.name = name;
        if(getText() == null || !getText().toString().equals(name)) {
            setText(name);
//            if(this.onValChanged != null)
//                this.onValChanged.onValChanged(this,name);
        }

//        final CharSequence oldText = getText();
//        if (name == oldText || (name == null && oldText.length() == 0)) {
//            return;
//        }
//        if (name instanceof Spanned) {
//            if (name.equals(oldText)) {
//                return; // No change in the spans, so don't set anything.
//            }
//        } else if (!haveContentsChanged(name, oldText)) {
//            return; // No content changes, so don't set anything.
//        }
//        setText(name);
//        this.name = name;
    }



    @BindingAdapter(value = {"nameAttrChanged"}, requireAll = false)//这里的evnet 与 @InverseBindingMethod 的event属性相同，requireAll这里意义不明，有可能在多个参数的时候要求所有都要注入
    public static void setNameWatcher(AView aView, final InverseBindingListener nameAttrChange) {

//        if(nameAttrChange == null){
//            aView.setOnValChanged(null);
//        }else{
//            aView.setOnValChanged(new OnValueChanged() {
//                @Override
//                public void onValChanged(AView view, String name) {
//                    nameAttrChange.onChange();
//                }
//            });
//        }

        final TextWatcher newWatcher;
        if(nameAttrChange == null){
            newWatcher = null;
        }else {
            newWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nameAttrChange.onChange();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
        }

        TextWatcher oldWatcher = ListenerUtil.trackListener(aView, newWatcher, R.id.textWatcher);

        if (nameAttrChange != null) {
            aView.addTextChangedListener(newWatcher);
        }

        if (oldWatcher != null) {
            aView.removeTextChangedListener(oldWatcher);
        }
    }

    private static boolean haveContentsChanged(CharSequence str1, CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            return true;
        } else if (str1 == null) {
            return false;
        }
        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }
}