package com.codehelp.calendarview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * class {@link CalendarViewAdapter}
 * is used to set view and click actions of each cells(items)
 * in  calender view (RecyclerView).
 */
public class CalendarViewAdapter extends
        RecyclerView.Adapter<CalendarViewAdapter.ItemHolder> {
    private Calendar mCurrentDate;
    private Context mContext;
    private Date mStartDate;
    private Date mEndDate;
    private ArrayList<DateCellModel> mDateCellModels;
    private CalenderCommunicator mCalenderCommunicator;

    /**
     * {@link CalendarViewAdapter}.
     * @param context {@link Context}
     * @param days active month cells
     * @param currentDate represents current date or active month
     * @param startDate calendar view start date
     * @param endDate calendar view end date
     * @param calenderCommunicator {@link CalenderCommunicator}
     */
    CalendarViewAdapter(final Context context,
                        final ArrayList<DateCellModel> days,
                        final Calendar currentDate,
                        final Date startDate,
                        final Date endDate,
                        final CalenderCommunicator calenderCommunicator) {
        this.mCurrentDate = currentDate;
        this.mCalenderCommunicator = calenderCommunicator;
        this.mDateCellModels = days;
        this.mContext = context;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }


    /**
     * set cell models.
     * @param cellModels active month cells
     */
    public void setcellModels(final ArrayList<DateCellModel> cellModels) {
        this.mDateCellModels = cellModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                         final int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_calendar_day, parent, false));
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder,
                                 final int position) {
        final DateCellModel date = getItem(position);
        int month = date.getDate().getMonth();
        holder.dayView.setTypeface(null, Typeface.NORMAL);
        holder.dayView.setText(String.valueOf(date.getDate().getDate()));
        holder.dayView.setTag(date.getDate());
        if (date.getDate().after(mStartDate)
                && date.getDate().before(mEndDate)) {
            if (month != mCurrentDate.getTime().getMonth()) {
                holder.dayView.setTextColor(
                        mContext.getResources().getColor(R.color.colorGray));
                holder.dayView.setTag(null);
            } else {
                if (date.isSelected()) {
                    holder.dayView.setTypeface(null, Typeface.BOLD);
                    holder.dayView.setBackgroundResource(R.color.colorBlue);
                    if (date.isStartDate()) {
                        holder.dayView.setText(mContext.getString(R.string.start));
                        holder.dayView.setBackgroundResource(
                                R.drawable.bg_border_layout_day);
                    }
                    if (date.isEndDate()) {
                        holder.dayView.setText(mContext.getString(R.string.end));
                        holder.dayView.setBackgroundResource(
                                R.drawable.bg_border_layout_day);
                    }
                } else {
                    holder.dayView.setBackgroundResource(0);
                    holder.dayView.setTextColor(Color.BLACK);
                    holder.dayView.setTag(date.getDate());
                }
            }
        } else {
            holder.dayView.setTextColor(
                    mContext.getResources().getColor(R.color.colorGray));
            holder.dayView.setTag(null);
        }

    }

    private DateCellModel getItem(final int position) {
        return mDateCellModels.get(position);
    }

    @Override
    public int getItemCount() {
        return mDateCellModels.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView dayView;

        ItemHolder(final View itemView) {
            super(itemView);
            dayView = itemView.findViewById(R.id.day_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mCalenderCommunicator != null) {
                        mCalenderCommunicator.onClickListener(
                                getItem(getAdapterPosition()), v);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    if (mCalenderCommunicator != null) {
                        mCalenderCommunicator.onItemLongClick(
                                getItem(getAdapterPosition()), view);
                    }
                    return false;
                }
            });
        }


    }

    /**
     * cell click actions communicate with {@link CustomCalendarView}.
     */
    public interface CalenderCommunicator {

        /**
         * item click listener.
         * @param dateCellModel selected cell value
         * @param view selected cell view
         */
        void onClickListener(DateCellModel dateCellModel, View view);

        /**
         * item long click listener.
         * @param dateCellModel selected cell value
         * @param view selected cell view
         */
        void onItemLongClick(DateCellModel dateCellModel, View view);
    }
}
