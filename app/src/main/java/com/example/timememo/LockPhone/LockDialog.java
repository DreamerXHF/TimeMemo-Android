package com.example.timememo.LockPhone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.timememo.R;

@SuppressLint("ValidFragment")
public class LockDialog extends DialogFragment {
    private EditText timeEt = null;
    private Button lock_confirm = null;
    private Button lock_cancel = null;
    private String time = null;

    @SuppressLint("ValidFragment")
    public LockDialog(String time){
        this.time = time;
    }
    public LockDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lock_dialog_layout,container,false);
        timeEt = view.findViewById(R.id.timeEt);
        timeEt.setText(time);
        lock_cancel = view.findViewById(R.id.lock_cancel);
        lock_confirm = view.findViewById(R.id.lock_confirm);

        lock_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        lock_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LockTimeActivity.class);
                intent.putExtra("time",timeEt.getText().toString());
                startActivity(intent);
            }
        });
        return view;
    }
}
