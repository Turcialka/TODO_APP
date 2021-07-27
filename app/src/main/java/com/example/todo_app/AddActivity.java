package com.example.todo_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText taskNameEditText, taskDatePicker;
    Spinner taskCategorySpinner;
    Button buttonAccept, buttonCancel;
    String selectedCategoryToSave;
    String dateToSave;
    long feedbackCheckoutInAddActivity;

    ArrayList<String> chooseCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.setTitle("Dodaj zadanie");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2D7698"));
        actionBar.setBackgroundDrawable(colorDrawable);

        taskNameEditText = findViewById(R.id.taskTitle);
        taskDatePicker = findViewById(R.id.datePicker);
        taskCategorySpinner = findViewById(R.id.spinnerTaskCategory);
        buttonAccept = findViewById(R.id.buttonAccept);
        buttonCancel = findViewById(R.id.buttonCancel);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        taskNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                taskTitleFocusChange(view, hasFocus);
            }
        });

        taskDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                taskDateFocusChange(view, hasFocus);
            }
        });

        taskDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        dateToSave = day + "." + month + "." + year;
                        taskDatePicker.setText(dateToSave);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        chooseCategory.add("Praca");
        chooseCategory.add("Zakupy");
        chooseCategory.add("Inne");

        ArrayAdapter<CharSequence> adapterTaskCategory = ArrayAdapter.createFromResource(AddActivity.this, R.array.TaskCategory, android.R.layout.simple_spinner_item);
        adapterTaskCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskCategorySpinner.setAdapter(adapterTaskCategory);
        taskCategorySpinner.setOnItemSelectedListener(this);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }

        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void taskTitleFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            return;
        }
        if (taskNameEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Pole nazwy zadania nie może być puste", Toast.LENGTH_LONG).show();
        }
    }

    private void taskDateFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            return;
        }
        if (taskDatePicker.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Wybierz datę", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int L = taskCategorySpinner.getSelectedItemPosition();
        selectedCategoryToSave = chooseCategory.get(L);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private boolean checkTitle(View view) {
        String getUsername = taskNameEditText.getText().toString();

        if (getUsername.equals("")) {
            Toast.makeText(view.getContext(), "Pole nazwy zadania nie może być puste", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDate(View view) {
        String getUsername = taskDatePicker.getText().toString();

        if (getUsername.equals("")) {
            Toast.makeText(view.getContext(), "Nie wybrano daty", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void addTask(View v) {
        if (checkTitle(v) && checkDate(v)) {
            sendDataToDB();
            ToDoBD feedbackFromDB = new ToDoBD(AddActivity.this);
            feedbackCheckoutInAddActivity = feedbackFromDB.getFeed();
            if (feedbackCheckoutInAddActivity == -1) {
                showDialog();
            } else {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(AddActivity.this, "Pomyślnie dodano zadanie", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(AddActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        AppCompatButton buttonTryDialog = (AppCompatButton) dialog.findViewById(R.id.buttonTryAlert);
        AppCompatButton buttonBackDialog = (AppCompatButton) dialog.findViewById(R.id.buttonBackAlert);

        dialog.show();
        buttonTryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
                dialog.cancel();
            }
        });
        buttonBackDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    public void sendDataToDB() {
        ToDoBD myDB = new ToDoBD(AddActivity.this);
        myDB.addTask(taskNameEditText.getText().toString().trim(),
                dateToSave.trim(),
                selectedCategoryToSave.trim());
    }
}
