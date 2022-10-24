package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.DatabaseErrorHandler;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.todolist.Utils.DBHandler;
import com.example.todolist.model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText editTextAddTask;
    private Button buttonAddTask;
    private DBHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_new_task , container , false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        editTextAddTask = getView().findViewById(R.id.edit_text_add_new_text);
        buttonAddTask = getView().findViewById(R.id.button_add_new_task);

        db = new DBHandler(getActivity());
        db.openDataBase();


        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            editTextAddTask.setText(task);
            if (task.length() > 0)
                buttonAddTask.setTextColor(ContextCompat.getColor(getContext(), R.color.purple_200));
        }
        editTextAddTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    buttonAddTask.setEnabled(false);
                    buttonAddTask.setTextColor(Color.GRAY);
                }else {
                    buttonAddTask.setEnabled(true);
                    buttonAddTask.setTextColor(ContextCompat.getColor(getContext(), R.color.purple_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        boolean finalIsUpdate = isUpdate;
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextAddTask.getText().toString();
                if (finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text);
                }else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
