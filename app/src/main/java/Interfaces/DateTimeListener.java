package Interfaces;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

public interface DateTimeListener {
    void showTimeDialog(RadialTimePickerDialogFragment fragment);
    void showDateDialog(CalendarDatePickerDialogFragment fragment);
}
