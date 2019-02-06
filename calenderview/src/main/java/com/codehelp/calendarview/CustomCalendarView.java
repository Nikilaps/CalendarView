
package com.codehelp.calendarview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * class for set view of calender
 * <p>
 * Created by Nikila on 12/27/2017.
 */

public class CustomCalendarView extends LinearLayout
        implements CalendarViewAdapter.CalenderCommunicator {
    /**
     * Day count is the total no of cells in a month
     * (simple logic).
     */
    private static final int DAYS_COUNT = 42;
    private DragSelectTouchListener mDragSelectTouchListener;
    private DragSelectionProcessor.Mode mMode =
            DragSelectionProcessor.Mode.Simple;
    private DragSelectionProcessor mDragSelectionProcessor;
    private LinearLayout headerView;
    AppCompatImageView prevBtn;
    AppCompatImageView nextBtn;
    private AppCompatTextView currentMnthTxt;
    private RecyclerView dayViewGrid;
    private Calendar currentDate = Calendar.getInstance();
    private OnDateSelectedListener dateSelectionListener;
    private CalendarViewAdapter calenderViewAdapter;
    private ArrayList<DateCellModel> cells;
    private List<DateCellModel> selectedDates = new ArrayList<>();
    private SelectionMode mSelectionMode;
    private Context mContext;

    /**
     * CalenderView (RecyclerView)
     * cell click listener.
     *
     * @param dateCellModel selected cell value
     * @param view          selected cell view
     */
    @Override
    public void onClickListener(final DateCellModel dateCellModel,
                                final View view) {
        TextView textView = (TextView) view;
        if (textView.getTag() != null) {
            if (dateSelectionListener != null) {
                if (setSelection(dateCellModel, mSelectionMode)) {
                    selectedDates.add(dateCellModel);
                    updateCalender();
                    if (mSelectionMode.equals(SelectionMode.SINGLE)) {
                        dateSelectionListener.onDateSelected(
                                (Date) textView.getTag());
                    } else if (selectedDates.size() > 1) {
                        dateSelectionListener.onMultipleDateSelected(selectedDates.get(0).getDate(),
                                selectedDates.get(1).getDate());
                    }
                } else {
                    selectedDates.remove(new DateCellModel(
                            (Date) textView.getTag(), true));
                    updateCalender();
                    dateSelectionListener.onDateUnSelected(
                            (Date) textView.getTag());
                }
            }
        } else {
            Toast.makeText(mContext, "Please select a valid date",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * CalenderView (RecyclerView)
     * cell long click(start drag) listener.
     *
     * @param dateCellModel selected cell value
     * @param view          selected cell view
     */
    @Override
    public void onItemLongClick(final DateCellModel dateCellModel,
                                final View view) {
        if (mSelectionMode.equals(SelectionMode.RANGE)) {
            clearAllSelections();
            mDragSelectTouchListener.startDragSelection(
                    cells.indexOf(dateCellModel));
        }
    }

    /**
     * Calender view selection modes.
     */
    public enum SelectionMode {
        /**
         * Only one date will be selectable.
         * If there is already a selected date and you select a new
         * one, the old date will be unselected.
         */
        SINGLE,
        /**
         * Multiple dates will be selectable.
         * Selecting an already-selected date will un-select it.
         */
        MULTIPLE,
        /**
         * Allows you to select a date range.
         * Previous selections are cleared when you either:
         * <ul>
         * <li>Have a range selected and select another date
         * (even if it's in the current range).</li>
         * <li>Have one date selected and then select an earlier date.</li>
         * </ul>
         */
        RANGE
    }

    public CustomCalendarView(final Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * load calender view.
     *
     * @param context       ({@link Context})
     * @param startDate     calendar view start date
     * @param endDate       calendar view end date
     * @param selectionMode ({@link SelectionMode})
     */
    public void loadComponents(final Context context,
                               final Date startDate,
                               final Date endDate,
                               final SelectionMode selectionMode) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSelectionMode = selectionMode;
        this.mContext = context;
        inflater.inflate(R.layout.layout_calendar_main, this);
        headerView = findViewById(R.id.cal_header_view);
        prevBtn = findViewById(R.id.cal_prev_button);
        nextBtn = findViewById(R.id.cal_next_button);
        currentMnthTxt = findViewById(R.id.cal_current_mnth);
        dayViewGrid = findViewById(R.id.cal_grid_day);
        dayViewGrid.setLayoutManager(new GridLayoutManager(context, 7));
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (currentDate.getTime().before(endDate)) {
                    currentDate.add(Calendar.MONTH, 1);
                    loadCalendar(startDate, endDate);
                }
            }
        });
        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (startDate.before(currentDate.getTime())) {
                    currentDate.add(Calendar.MONTH, -1);
                    loadCalendar(startDate, endDate);
                }
            }
        });

        mDragSelectionProcessor = new DragSelectionProcessor(
                new DragSelectionProcessor.ISelectionHandler() {
                    @Override
                    public HashSet<Integer> getSelection() {
                        return null;
                    }

                    @Override
                    public boolean isSelected(final int index) {
                        return false;
                    }

                    @Override
                    public void updateSelection(final int start,
                                                final int end,
                                                final boolean isSelected,
                                                final boolean calledFromOnStart
                    ) {
                        if (calledFromOnStart) {
                            DateCellModel startCellModel = cells.get(start);
                            if (selectedDates.size() > 0) {
                                clearAllSelections();
                            }
                            selectedDates.add(0, startCellModel);
                        } else {
                            DateCellModel endCellModel = cells.get(end);
                            if (selectedDates.size() > 2) {
                                DateCellModel startCellModel =
                                        selectedDates.get(0);
                                clearAllSelections();
                                selectedDates.add(startCellModel);
                            }
                            selectedDates.add(1, endCellModel);
                        }
                        if (selectedDates.size() > 1) {
                            if (selectedDates.get(0).getDate().before(
                                    selectedDates.get(1).getDate())) {
                                setDragSelection();
                            }
                        }

                    }
                })
                .withMode(mMode);
        mDragSelectTouchListener = new DragSelectTouchListener()
                .withSelectListener(mDragSelectionProcessor);
        updateSelectionListener();
        dayViewGrid.addOnItemTouchListener(mDragSelectTouchListener);
        loadCalendar(startDate, endDate);
    }

    /**
     * set calendar view drag selection.
     */
    private void setDragSelection() {
        Date start = selectedDates.get(0).getDate();
        Date end = selectedDates.get(1).getDate();
        selectedDates.get(0).setSelected(true);
        selectedDates.get(0).setStartDate(true);
        selectedDates.get(1).setSelected(true);
        selectedDates.get(1).setEndDate(true);
        ArrayList<DateCellModel> dateArray = getDateArray(start, end);
        if (dateArray != null && dateArray.size() > 0) {
            dateArray.add(selectedDates.get(1));
            selectedDates.addAll(dateArray);
        }
        if (selectedDates.size() > 1) {
            dateSelectionListener.onMultipleDateSelected(selectedDates.get(0).getDate(),
                    selectedDates.get(1).getDate());
        }
        updateCalender();
    }

    private void updateSelectionListener() {
        mDragSelectionProcessor.withMode(mMode);
    }

    /**
     * set calendar view cell selection.
     *
     * @param dateCellModel selected cell value
     * @param selectionMode ({@link SelectionMode})
     * @return isSelected
     */
    private boolean setSelection(final DateCellModel dateCellModel,
                                 final SelectionMode selectionMode) {
        boolean isSelected;
        if (!dateCellModel.isSelected()) {
            isSelected = true;
            if (selectionMode.equals(SelectionMode.SINGLE)) {
                clearAllSelections();
            } else if (selectionMode.equals(SelectionMode.RANGE)) {
                if (selectedDates.size() >= 2) {
                    clearAllSelections();
                } else if (selectedDates.size() >= 1) {
                    if (dateCellModel.getDate().before(
                            selectedDates.get(0).getDate())) {
                        clearAllSelections();
                    } else {
                        Date start = selectedDates.get(0).getDate();
                        Date end = dateCellModel.getDate();
                        selectedDates.get(0).setStartDate(true);
                        dateCellModel.setEndDate(true);
                        ArrayList<DateCellModel> dateArray =
                                getDateArray(start, end);
                        if (dateArray != null && dateArray.size() > 0) {
                            selectedDates.addAll(dateArray);
                        }
                    }
                }
            }
        } else {
            isSelected = false;
            selectedDates.remove(dateCellModel);
            if (selectionMode.equals(SelectionMode.SINGLE)) {
                clearAllSelections();
            } else if (selectionMode.equals(SelectionMode.RANGE)) {
                if (selectedDates.size() >= 2) {
                    clearAllSelections();
                }
            }
        }
        dateCellModel.setSelected(isSelected);
        return isSelected;
    }

    /**
     * update calendar cell selection.
     */
    public void updateCalender() {
        if (selectedDates != null
                && selectedDates.size() > 0
                && cells != null
                && cells.size() > 0) {
            for (DateCellModel dateCellModel : cells) {
                for (DateCellModel dateCellModel1 : selectedDates) {
                    if (dateCellModel.getDate().equals(
                            dateCellModel1.getDate())) {
                        dateCellModel.setSelected(
                                dateCellModel1.isSelected());
                        dateCellModel.setStartDate(
                                dateCellModel1.isStartDate());
                        dateCellModel.setEndDate(
                                dateCellModel1.isEndDate());
                    }
                }
            }
        }
        calenderViewAdapter.notifyDataSetChanged();
    }

    /***
     * clear all selection in calender view.
     */
    public void clearAllSelections() {
        selectedDates.clear();
        for (DateCellModel dateCellModel : cells) {
            dateCellModel.setSelected(false);
            dateCellModel.setStartDate(false);
            dateCellModel.setEndDate(false);
        }
    }

    /**
     * get range dates.
     *
     * @param startDate range selection start date
     * @param endDate   range selection end date
     * @return dateArrayList
     */
    private ArrayList<DateCellModel> getDateArray(final Date startDate,
                                                  final Date endDate) {
        ArrayList<DateCellModel> dateArrayList = new ArrayList<>();
        if (endDate.after(startDate)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            while (endDate.after(cal.getTime())
                    || endDate.equals(cal.getTime())) {
                dateArrayList.add(new DateCellModel(cal.getTime(), true));
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        if (dateArrayList.size() > 2) {
            dateArrayList.remove(0);
            dateArrayList.remove(dateArrayList.size() - 1);
        }
        return dateArrayList;
    }

    /**
     * load calendar view.
     *
     * @param startDate calendar view start date
     * @param endDate   calendar view end date
     */
    private void loadCalendar(final Date startDate, final Date endDate) {
        cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (cells.size() < DAYS_COUNT) {
            cells.add(new DateCellModel(calendar.getTime(), false));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (selectedDates != null && selectedDates.size() > 0) {
            for (DateCellModel dateCellModel : cells) {
                for (DateCellModel date : selectedDates) {
                    if (date.getDate().equals(dateCellModel.getDate())) {
                        dateCellModel.setSelected(true);
                        dateCellModel.setStartDate(date.isStartDate());
                        dateCellModel.setEndDate(date.isEndDate());
                    }
                }
            }
        }
        dayViewGrid.setAdapter(calenderViewAdapter =
                new CalendarViewAdapter(getContext(), cells, currentDate,
                        startDate, endDate, this));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        currentMnthTxt.setText(sdf.format(currentDate.getTime()).toUpperCase());
    }

    public void setOnDateSelectedListener(
            final OnDateSelectedListener listener) {
        dateSelectionListener = listener;
    }

    /**
     * Interface to be notified when a new date
     * is selected or unselected. This will only be called
     * when the user initiates the date selection.
     *
     * @see #setOnDateSelectedListener(OnDateSelectedListener)
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Date date);

        void onDateUnSelected(Date date);

        void onMultipleDateSelected(Date startDate, Date endDate);
    }

    /**
     * set calendar view header background programmatically.
     *
     * @param bg background resource id
     */
    public void setHeaderBackground(final int bg) {
        headerView.setBackgroundResource(bg);
    }
}
