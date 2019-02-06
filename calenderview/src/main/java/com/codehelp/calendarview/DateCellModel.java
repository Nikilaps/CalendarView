package com.codehelp.calendarview;

import java.util.Date;

/**
 * {@link DateCellModel} represents
 * single cell data
 * Created by Nikila on 12/28/2017.
 */

class DateCellModel {
    private Date date;
    private boolean isSelected;
    private boolean isStartDate;
    private boolean isEndDate;

    DateCellModel(final Date date,
                  final boolean isSelected) {
        this.date = date;
        this.isSelected = isSelected;
    }

    Date getDate() {
        return date;
    }

    void setDate(final Date date) {
        this.date = date;
    }

    boolean isSelected() {
        return isSelected;
    }

    void setSelected(final boolean selected) {
        isSelected = selected;
    }

    boolean isStartDate() {
        return isStartDate;
    }

    void setStartDate(final boolean startDate) {
        isStartDate = startDate;
    }

    boolean isEndDate() {
        return isEndDate;
    }

    void setEndDate(final boolean endDate) {
        isEndDate = endDate;
    }
}
