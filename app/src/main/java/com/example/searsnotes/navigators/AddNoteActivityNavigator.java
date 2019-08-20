package com.example.searsnotes.navigators;


import java.util.Calendar;

public interface AddNoteActivityNavigator {

    void requestMultiplePermissions(int requestCode);
    void openGalary();
    void openCamera();
    void setDate();
    void setTime();

    void setNoteImage(String uri);
    void saveButtonClicked();
    void discardButtonClicked();
}
