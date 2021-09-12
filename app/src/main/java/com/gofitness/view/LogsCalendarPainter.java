package com.gofitness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.TypedValue;

import com.necer.calendar.ICalendar;
import com.necer.painter.CalendarPainter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 票务绘制类
 */
public class LogsCalendarPainter implements CalendarPainter {


    protected Paint mTextPaint;
    protected Paint mBgPaint;
    private int mCircleRadius;
    private Context mContext;
    private ICalendar mICalendar;

    private Map<LocalDate, String> mLogsMap;

    protected List<LocalDate> mHolidayList;
    protected List<LocalDate> mWorkdayList;

    public LogsCalendarPainter(Context context, ICalendar iCalendar) {

        mContext = context;
        mICalendar = iCalendar;
        mTextPaint = getPaint();
        mBgPaint = getPaint();

        mBgPaint.setColor(Color.parseColor("#7D7DFF"));
        mCircleRadius = (int) dp2px(context, 20);

        mLogsMap = new HashMap<>();
        mHolidayList = new ArrayList<>();
        mWorkdayList = new ArrayList<>();

        List<String> holidayList = CalendarUtil.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        List<String> workdayList = CalendarUtil.getWorkdayList();
        for (int i = 0; i < workdayList.size(); i++) {
            mWorkdayList.add(new LocalDate(workdayList.get(i)));
        }

    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }


    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, selectedDateList.contains(localDate), true);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
        drawLogs(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, selectedDateList.contains(localDate), true);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
        drawLogs(canvas, rectF, localDate, selectedDateList.contains(localDate), true);
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList) {
        drawSelectBg(canvas, rectF, selectedDateList.contains(localDate), false);
        drawSolar(canvas, rectF, localDate, selectedDateList.contains(localDate), false);
        drawLogs(canvas, rectF, localDate, selectedDateList.contains(localDate), false);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {

    }


    //绘制选中背景
    private void drawSelectBg(Canvas canvas, RectF rectF, boolean isisSelected, boolean isCurrectMonthOrWeek) {
        if (isisSelected) {
            mBgPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), mCircleRadius, mBgPaint);
        }
    }

    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate localDate, boolean isSelected, boolean isCurrectMonthOrWeek) {
        mTextPaint.setTextSize(dp2px(mContext, 16));
        mTextPaint.setColor(isSelected ? Color.WHITE : Color.BLACK);
        mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
        canvas.drawText(localDate.getDayOfMonth() + "", rectF.centerX(), TextUtils.isEmpty(mLogsMap.get(localDate)) ? getBaseLineY(rectF) : rectF.centerY(), mTextPaint);
    }

    // 绘制日志信息
    private void drawLogs(Canvas canvas, RectF rectF, LocalDate localDate, boolean isSelected, boolean isCurrectMonthOrWeek) {
        String logs = mLogsMap.get(localDate);
        if (!TextUtils.isEmpty(logs)) {
            mTextPaint.setTextSize(dp2px(mContext, 10));
            mTextPaint.setColor(isSelected ? Color.WHITE : Color.RED);
            mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
            canvas.drawText(logs, rectF.centerX(), rectF.centerY() + dp2px(mContext, 12), mTextPaint);
        }
    }


    //canvas.drawText的基准线
    private int getBaseLineY(RectF rectF) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }


    public void setLogsMap(Map<String, Integer> logsMap) {
        if (logsMap != null) {
            mLogsMap.clear();
            for (String key : logsMap.keySet()) {
                LocalDate localDate;
                try {
                    localDate = new LocalDate(key);
                } catch (Exception e) {
                    throw new RuntimeException("logsMap的参数需要 yyyy-MM-dd 格式的日期");
                }
                int logsCount = logsMap.get(key);
                String logsString = logsCount == 1 ? logsCount + " log" : logsCount + " logs";
                mLogsMap.put(localDate, logsString);
            }
            mICalendar.notifyCalendar();
        }
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}