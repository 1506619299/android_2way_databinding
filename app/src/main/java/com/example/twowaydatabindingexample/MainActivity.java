package com.example.twowaydatabindingexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.twowaydatabindingexample.databinding.ActivityMainBinding;
import com.example.twowaydatabindingexample.interfaces.GetSpinnerSelectCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<GetSpinnerSelectCallBack> list = new ArrayList<>();

        MainVM vm = new MainVM("This is 2way databinding example", true);
        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
        binding.setItem(vm);

        binding.setValueBean(new ValueBean("1",1));

        binding.setSpList(list);
        list.add(new Dict("1","是"));
        list.add(new Dict("0","否"));
    }
}
