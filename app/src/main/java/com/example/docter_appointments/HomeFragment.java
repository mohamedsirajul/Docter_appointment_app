package com.example.docter_appointments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private EditText etHospitalName;
    private EditText etDoctorName;
    private EditText etAppointmentDate;
    private EditText etAppointmentTime;
    private EditText etPatientName;
    private Button btnSubmit;
    private DatabaseReference databaseReference;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("appointments");

        // Initialize Views
        etHospitalName = view.findViewById(R.id.etHospitalName);
        etDoctorName = view.findViewById(R.id.etDoctorName);
        etAppointmentDate = view.findViewById(R.id.etAppointmentDate);
        etAppointmentTime = view.findViewById(R.id.etAppointmentTime);
        etPatientName = view.findViewById(R.id.etPatientName);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        // Initialize Calendar instance
        calendar = Calendar.getInstance();

        // Set click listener for appointment date field to show DatePickerDialog
        etAppointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Set click listener for appointment time field to show TimePickerDialog
        etAppointmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Set the selected date to the appointment date field
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());

                // Set the selected date to the appointment date field
                etAppointmentDate.setText(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Set the selected time to the appointment time field
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Format the time
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String selectedTime = timeFormat.format(calendar.getTime());

                // Set the selected time to the appointment time field
                etAppointmentTime.setText(selectedTime);
            }
        }, hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void submitForm() {
        String hospitalName = etHospitalName.getText().toString().trim();
        String doctorName = etDoctorName.getText().toString().trim();
        String appointmentDate = etAppointmentDate.getText().toString().trim();
        String appointmentTime = etAppointmentTime.getText().toString().trim();
        String patientName = etPatientName.getText().toString().trim();

        // Perform input validation
        if (hospitalName.isEmpty()) {
            etHospitalName.setError("Hospital name is required");
            etHospitalName.requestFocus();
            return;
        }

        if (doctorName.isEmpty()) {
            etDoctorName.setError("Doctor name is required");
            etDoctorName.requestFocus();
            return;
        }

        if (appointmentDate.isEmpty()) {
            etAppointmentDate.setError("Appointment date is required");
            etAppointmentDate.requestFocus();
            return;
        }

        if (appointmentTime.isEmpty()) {
            etAppointmentTime.setError("Appointment time is required");
            etAppointmentTime.requestFocus();
            return;
        }

        if (patientName.isEmpty()) {
            etPatientName.setError("Patient name is required");
            etPatientName.requestFocus();
            return;
        }

        // Create a new Appointment object
        Appointment appointment = new Appointment(hospitalName, doctorName, appointmentDate, appointmentTime, patientName);

        // Generate a unique key for the appointment
        String appointmentId = databaseReference.push().getKey();

        // Store the appointment data in Firebase Realtime Database
        databaseReference.child(appointmentId).setValue(appointment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    // Error occurred while storing the data
                    showErrorDialog(databaseError.getMessage());
                } else {
                    // Data was successfully stored
                    showSuccessDialog();
                    // Clear the form fields
                    etHospitalName.setText("");
                    etDoctorName.setText("");
                    etAppointmentDate.setText("");
                    etAppointmentTime.setText("");
                    etPatientName.setText("");
                }
            }
        });
    }


    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Success")
                .setMessage("Appointment data inserted successfully.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Error")
                .setMessage("Failed to insert appointment data.\nError: " + errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }
}
