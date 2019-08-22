package com.example.searsnotes.navigators;

import java.util.Calendar;

public interface EditNoteActivityNavigator {
    void openGalary();
    void openCamera();
    void setNoteImage(String uri);
    void requestMultiplePermissions(int requestCode);
    void saveButtonClicked();
    void discardButtonClicked();

    void setTime();

    void setDate();

    void modifyReminder(Calendar calendar_alarm, int reminderId);
}
