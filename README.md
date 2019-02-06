# CalendarView
Simple calendar view with multiple and drag date selection.

How to use

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
	        implementation 'com.github.Nikilaps:CalendarView:0.1.0'
	}
  
  maven:
  <dependency>
	    <groupId>com.github.Nikilaps</groupId>
	    <artifactId>CalendarView</artifactId>
	    <version>0.1.0</version>
	</dependency>
