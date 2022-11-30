package com.timezonescheduler.chronos;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
//import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.timezonescheduler.chronos.application.test.CalendarQuickStart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;


public class DetermineTimeTest{

    //A bunch of datetimes so I don't have to keep using them
    //DateTime format: "yyyy-mm-ddThh:mm:ss-ZZ:ZZ"
    private static final long intervalDay = 24 * 60 * 60 * 1000;

    public static void sort(ArrayList<ChronosPair<DateTime, Boolean>> arr, int low, int high){
        //Recursive Quicksort. Hopefully not too memory intensive.
        if(low < high){
            int pi = partition(arr, low, high);
            sort(arr, low, pi - 1);
            sort(arr,pi + 1, high);
        }
    }

    public static void swap(ArrayList<ChronosPair<DateTime, Boolean>> arr, int i, int j){
        //swap method for sorting algorithm
        ChronosPair temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    public static int partition(ArrayList<ChronosPair<DateTime, Boolean>> arr, int low, int high){
        //partition method for sorting algorithm
        ChronosPair<DateTime, Boolean> pivot = arr.get(high);
        int i = (low - 1);
        for(int j = low; j <= high - 1; j++){
            if(arr.get(j).key.getValue() < pivot.key.getValue()){
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    public static String findOverlap(Event e, ArrayList<Event> events){
        ArrayList<Event.Creator> overlapNames = new ArrayList();
        for(Event ev : events){
            if(overlaps(e, ev)){
                overlapNames.add(ev.getCreator());
            }
        }
        if(overlapNames.size() > 0){
            String r = "time conflict for ";
            for(Event.Creator c : overlapNames){
                if( c.getDisplayName() == null || c.getDisplayName().equals("null") || c.getDisplayName().equals("")){
                    r += c.getEmail();
                }else{
                    r += c.getDisplayName();
                }
                r += ", ";
            }
            r = r.substring(0, r.length() - 2); //cut off end comma and space
            return r;
        }
        return "";
    }

    //returns true when events overlap. if an end and a start share a time it will not return true.
    //Assumes both events have end times after or equal to their start times
    public static boolean overlaps(Event e1, Event e2){
        long e1Start = e1.getStart().getDateTime().getValue();
        long e2Start = e2.getStart().getDateTime().getValue();
        long e1End = e1.getEnd().getDateTime().getValue();
        long e2End = e2.getEnd().getDateTime().getValue();

        if(e1Start >= e2Start && e1Start < e2End){
            return true;
        }
        if(e1End > e2Start && e1End <= e2End){
            return true;
        }
        return false;
    }

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_EVENTS,
            PeopleServiceScopes.CONTACTS_READONLY,
            GmailScopes.GMAIL_SEND,
            CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickStart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void createCalendar (Calendar service) throws IOException {
        // Create a new calendarList
        CalendarList calendarList = service.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();

        boolean calendarExists = false;
        for (CalendarListEntry calendarListEntry : items) {
            if (Objects.equals(calendarListEntry.getSummary(), "chronosTest2")) {
                calendarExists = true;
            }
        }


        if (!calendarExists) {
            com.google.api.services.calendar.model.Calendar calendar;
            calendar = new com.google.api.services.calendar.model.Calendar()
                    .setSummary("chronosTest2")
                    .setTimeZone("America/New_York");

            // Insert the new calendar
            com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().
                    insert(calendar).execute();

            System.out.println(createdCalendar.getId());
        } else {
            System.out.println("Calendar already exists");
        }

    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        DateTime dayStart = new DateTime("2022-11-29T00:00:00-05:00");

        long meetingLength = 7200000;

        //determineMeetingTime(meetingLength, dayStart);
    }

    public static ArrayList<ChronosPair<Event, String>> determineMeetingTime(long meetingLength, DateTime dayStart, ArrayList<Event> events) throws IOException, GeneralSecurityException {
        DateTime dayEnd = new DateTime(dayStart.getValue() + intervalDay);

        //create list of times when events start or end
        ArrayList<ChronosPair<DateTime, Boolean>> timesList = new ArrayList();
        timesList.add(new ChronosPair<>(dayStart, Boolean.TRUE));
        timesList.add(new ChronosPair<>(dayStart, Boolean.FALSE));
        for(Event e : events){
            if(e.getEnd().getDateTime().getValue() <= dayStart.getValue()){
                //entirely before time of interest
                continue;
            }
            if(e.getStart().getDateTime().getValue() >= dayEnd.getValue()){
                //entirely after time of interest
                continue;
            }
            if(e.getStart().getDateTime().getValue() < dayStart.getValue()){
                //partially before time of interest; chop off part before dayStart
                timesList.add(new ChronosPair(dayStart, Boolean.TRUE));
            }else {
                //start within day
                timesList.add(new ChronosPair(e.getStart().getDateTime(), Boolean.TRUE));
            }
            if(e.getEnd().getDateTime().getValue() > dayEnd.getValue()){
                //partially after time of interest; chop off part after dayEnd
                timesList.add(new ChronosPair(dayEnd, Boolean.FALSE));
            }else {
                //end within day
                timesList.add(new ChronosPair(e.getEnd().getDateTime(), Boolean.FALSE));
            }
        }
        timesList.add(new ChronosPair<>(dayEnd, Boolean.TRUE));
        timesList.add(new ChronosPair<>(dayEnd, Boolean.FALSE));

        //Sort by DateTime (earliest to latest)
        if(!timesList.isEmpty()) {
            sort(timesList, 0, timesList.size() - 1);
        }


        System.out.println("Sorted DateTimes Test:");
        for(ChronosPair<DateTime, Boolean> p : timesList){
            System.out.print(p.key + " (");
            if(p.val){
                System.out.println("Start)");
            }else{
                System.out.println("End)");
            }
        }

        //create ordered list of intervals between datetimes
        //Items in list interval are organized as <# unavailable, start time, end time>
        ArrayList<ChronosTriplet<Integer, DateTime, DateTime>> intervals = new ArrayList();
        int c = 0;
        for(int i = 0; i < timesList.size() - 1; i++){
            if(timesList.get(i).val.booleanValue()){
                //start of event
                ++c;
            }else{
                //end of event
                --c;
            }
            if(timesList.get(i).key.getValue() == timesList.get(i + 1).key.getValue()){
                //this item & next item happen at the same time
                //no need to make interval
            }else{
                //make interval
                intervals.add(new ChronosTriplet<Integer, DateTime, DateTime>(c, timesList.get(i).key, timesList.get(i + 1).key));
            }
        }

        System.out.println("\nIntervals Test:");
        for(ChronosTriplet<Integer, DateTime, DateTime> t : intervals){
            System.out.println("from " + t.val1 + " until " + t.val2 + " with " + t.key + " concurrent events");
        }

        //now that we have list of intervals, we can acquire suitable meeting times!
        //long meetingLength = 7200000; //User tells us how long they want the meeting. I put 2 hours as a placeholder.
        long startIterator = 1800000; //Suggested meeting times iterate by this val, currently 30 minutes.
        int threshold = 0; //# of concurrent events we're willing to overlap. Increases until we can fit the full length meeting
        boolean meetingDoesntFit = true; //ends loop when meeting does fit
        ArrayList<ChronosPair<Event, String>> potentialMeetings = new ArrayList(); //List of potential meetings for the user to choose from

        while(meetingDoesntFit){
            ArrayList<ChronosPair<Event, String>> shortCompromise = new ArrayList(); //contains longest free space if interval isn't long enough
            //test for intervals where interval fits well
            for(int i = 0; i < intervals.size(); i++){
                if(intervals.get(i).key.intValue() <= threshold){
                    //interval within threshold
                    long start = intervals.get(i).val1.getValue();
                    long end = intervals.get(i).val2.getValue();
                    int j = i + 1;
                    while(j < intervals.size() && intervals.get(j).key <= threshold){
                        //Covers all adjacent intervals below threshold (essentially making them act as one large interval)
                        end = intervals.get(j).val2.getValue();
                        j++;
                    }
                    if(end - start >= meetingLength) {
                        while (end - start >= meetingLength) {
                            meetingDoesntFit = false;
                            //meeting fits
                            Event e = new Event();
                            e.setStart(new EventDateTime().setDateTime(new DateTime(start)));
                            e.setEnd(new EventDateTime().setDateTime(new DateTime(start + meetingLength)));
                            start -= start % startIterator;
                            start += startIterator;

                            potentialMeetings.add(new ChronosPair(e, findOverlap(e, events)));
                        }
                    }else{
                        Event e = new Event();
                        e.setStart(new EventDateTime().setDateTime(new DateTime(start)));
                        e.setEnd(new EventDateTime().setDateTime(new DateTime(end)));
                        shortCompromise.add(new ChronosPair(e, findOverlap(e, events)));
                    }
                    //make i skip intervals already covered
                    i = j - 1;
                }
            }
            ++threshold;
            if(shortCompromise.size() > 0 && meetingDoesntFit) {
                ChronosPair<Event, String> e = new ChronosPair(new Event(), "");
                e.key.setStart(new EventDateTime().setDateTime(new DateTime(0)));
                e.key.setEnd(new EventDateTime().setDateTime(new DateTime(0)));
                //get longest event in shortCompromise
                for (ChronosPair<Event, String> ev : shortCompromise) {
                    if (e.key.getEnd().getDateTime().getValue() - e.key.getStart().getDateTime().getValue() <
                            ev.key.getEnd().getDateTime().getValue() - ev.key.getStart().getDateTime().getValue()) {
                        e = ev;
                    }
                }
                potentialMeetings.add(e);
            }
        }


        System.out.println("\nProposed Meetings Test:");
        for(ChronosPair<Event, String> p : potentialMeetings){
            System.out.print("from " + p.key.getStart().getDateTime() + " until " + p.key.getEnd().getDateTime());
            if(p.key.getEnd().getDateTime().getValue() - p.key.getStart().getDateTime().getValue() < meetingLength){
                System.out.print(" Short");
            }
            if(!p.val.equals("")){
                System.out.print(" " + p.val);
            }
            System.out.print("\n");
        }

        return potentialMeetings;
        //================================================================
        //                ~:THINGS THAT BREAK MY BOY:~
        //================================================================
        // 1) Events that start before the day and/or end after it's done -- fixed
        // 2) Making a meeting length that's longer than a day            -- fixed
        // 3) Other things probably
        //
    }

}
