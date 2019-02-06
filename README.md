# CalendarView
Simple calendar view with single and multiple drag date selection.

Usage

Include CustomCalendarView in your layout XML.

         <com.codehelp.calendarview.CustomCalendarView
            android:id="@+id/calendar_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

In  activity/dialog or fragment, initialize the view with a range of valid dates and selection mode.
      * To select one date at a time we use Selection mode 'SINGLE'
      * To select range of dates use selection mode 'RANGE'

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String dtStart = "2019-05-01"; // end date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",  Locale.getDefault());
        CustomCalendarView cv = (findViewById(R.id.calendar_view));
        cv.loadComponents(this, calendar.getTime(), date, CustomCalendarView.SelectionMode.RANGE);
	
	
	 cv.setOnDateSelectedListener(new CustomCalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Toast.makeText(MainActivity.this, sdf.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnSelected(Date date) {

            }
	    
	    @Override
            public void onMultipleDateSelected(Date startDate, Date endDate) {
                Toast.makeText(
                        MainActivity.this, "Start :" + sdf.format(startDate)
                        + "  End :" + sdf.format(endDate), Toast.LENGTH_SHORT
                ).show();
            }
        });

Add to Project:

Step 1.
Add the JitPack repository to your build file

gradle:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  maven:
  
           <repositories>
		 <repository>
		     <id>jitpack.io</id>
		     <url>https://jitpack.io</url>
		  </repository>
	   </repositories>
  
  Step 2. Add the dependency
  
  gradle:
  
        dependencies {
	        implementation 'com.github.Nikilaps:CalendarView:1.1.1'
	}
  
  maven:
  
        <dependency>
	    <groupId>com.github.Nikilaps</groupId>
	    <artifactId>CalendarView</artifactId>
	    <version>1.1.1</version>
	</dependency>

