package com.timezonescheduler.chronos.application.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.model.User;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

import static javax.mail.Message.RecipientType.TO;

@Service
public class MeetingService {

    private static final String APPLICATION_NAME = "Meeting Service";
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

    private static final String TEST_EMAIL = "spinnerunknown2@gmail.com";

    private static final String TEST_SEND_TO_EMAIL = "jengler2@oswego.edu";

    private static final NetHttpTransport httpTransport;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Gmail gmailService;

    private static Calendar calendarService;

    private static PeopleService peopleService;

    private static GroupService groupService;


    @Autowired
    public MeetingService(GroupService groupService) throws IOException {

        this.groupService = groupService;

        gmailService =
                new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                        .setApplicationName(APPLICATION_NAME)
                        .build();


        calendarService =
                new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        peopleService =
                new PeopleService.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
    }




    public static void main (String[] args) throws Exception {


        //new MeetingService();
        //Test Send Email
        //createAndSendEmail();


        //Test Google Contacts
        //readContacts();


        //Test Google Calendar


//        createCalendar();
//
//
//        readEvents();
//
//        createEvent();
//
//        readEvents();
//
//
//        String deleteSummary = "Event Dummy";
//        deleteEvent(deleteSummary);
//
//        readEvents();

//        String newUserEmail = "pschmitt@oswego.edu";
//        addContact(newUserEmail);



    }





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
        InputStream in = MeetingService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
                .setApprovalPrompt("force")
                .build();


        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;


    }


    //This starts the Google Calendar Functions
    public static void deleteEvent (String deleteSummary) throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());

        String calendarSummary = "chronosTest";
        String calendarId = "";
        CalendarList calendarList = calendarService.calendarList().list().execute();
        List<CalendarListEntry> calendarListItems = calendarList.getItems();

        for (CalendarListEntry calendarListEntry : calendarListItems) {
            if (Objects.equals(calendarListEntry.getSummary(), calendarSummary)) {
                calendarId = calendarListEntry.getId();
            }
        }

        Events events = calendarService.events().list(calendarId)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<com.google.api.services.calendar.model.Event> items = events.getItems();

        boolean removedEvent = false;
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            for (com.google.api.services.calendar.model.Event event : items) {
                System.out.println(event.getSummary());
                if (event.getSummary().equals(deleteSummary)) {
                    String eventId = event.getId();
                    System.out.println("Deleting Event with Summary of: Event and ID: " + eventId);
                    calendarService.events().delete(calendarId, eventId).execute();
                    removedEvent = true;
                }
            }
            if (!removedEvent) {
                System.out.println("The event was not found");
            }
        }
    }

    public static void createEvent (String groupId) throws IOException {

        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        Event meetingEvent = group.getMeeting();

        ArrayList<User> userArrayList = group.getUserList();

//        com.google.api.services.calendar.model.Event event = new com.google.api.services.calendar.model.Event()
//                .setSummary("Chronos Scheduled Meeting")
//                .setLocation("Anywhere")
//                .setDescription("Meeting Through Chronos Setup");
//
//        DateTime startDateTime = new DateTime("2022-11-18T10:20:00-05:00");
//        EventDateTime start = new EventDateTime()
//                .setDateTime(startDateTime)
//                .setTimeZone("America/New_York");
//        event.setStart(start);
//
//        DateTime endDateTime = new DateTime("2022-11-18T11:15:00-05:00");
//        EventDateTime end = new EventDateTime()
//                .setDateTime(endDateTime)
//                .setTimeZone("America/New_York");
//        event.setEnd(end);
//
        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        meetingEvent.setRecurrence(Arrays.asList(recurrence));
//
        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("jengler2@oswego.edu"),
                //new EventAttendee().setEmail("tvanalst@oswego.edu"),
        };

        for (int i = 0; i < userArrayList.size(); i++) {
            EventAttendee eventAttendee = new EventAttendee().setEmail(userArrayList.get(i).getEmail());
            attendees[i] = eventAttendee;
        }
        meetingEvent.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        com.google.api.services.calendar.model.Event.Reminders reminders = new com.google.api.services.calendar.model.Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        meetingEvent.setReminders(reminders);

        String calendarSummary = "chronosTest";
        String calendarId = "";
        CalendarList calendarList = calendarService.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();

        for (CalendarListEntry calendarListEntry : items) {
            if (Objects.equals(calendarListEntry.getSummary(), calendarSummary)) {
                calendarId = calendarListEntry.getId();
            }
        }

        meetingEvent = calendarService.events().insert(calendarId, meetingEvent).execute();
        System.out.printf("Event created: %s\n", meetingEvent.getHtmlLink());
    }


    public static void createCalendar () throws IOException {
        // Create a new calendarList
        CalendarList calendarList = calendarService.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();

        boolean calendarExists = false;
        for (CalendarListEntry calendarListEntry : items) {
            if (Objects.equals(calendarListEntry.getSummary(), "chronosTest")) {
                calendarExists = true;
            }
        }


        if (!calendarExists) {
            com.google.api.services.calendar.model.Calendar calendar;
            calendar = new com.google.api.services.calendar.model.Calendar()
                    .setSummary("chronosTest")
                    .setTimeZone("America/New_York");

            // Insert the new calendar
            com.google.api.services.calendar.model.Calendar createdCalendar = calendarService.calendars().
                    insert(calendar).execute();

            System.out.println(createdCalendar.getId());
        } else {
            System.out.println("Calendar already exists");
        }

    }


    public static ArrayList<Event> readEvents () throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());

//        String calendarSummary = "primary";
//        String calendarId = "";
//        CalendarList calendarList = calendarService.calendarList().list().execute();
//        List<CalendarListEntry> calendarListItems = calendarList.getItems();
//
//        for (CalendarListEntry calendarListEntry : calendarListItems) {
//            if (Objects.equals(calendarListEntry.getSummary(), calendarSummary)) {
//                calendarId = calendarListEntry.getId();
//            }
//        }


        Events events = calendarService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<com.google.api.services.calendar.model.Event> items = events.getItems();
        System.out.println(items);
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                List<EventAttendee> attendees = event.getAttendees();
                String creatorEmail = event.getCreator().getEmail();

                System.out.println("The creator is: " + creatorEmail);

                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
                if (attendees != null) {
                    for (EventAttendee attendee : attendees) {
                        String attendeeEmail = attendee.getEmail();
                        System.out.println("The attendees are: " + attendeeEmail);
                    }
                } else {
                    System.out.println("There are no attendees for the above event");
                }
            }
        }
        return (ArrayList<Event>) items;
    }
    //Ends the Google Calendar Functions




    //This starts the Google Contact Functions
    public static void readContacts() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Request 10 connections.
        ListConnectionsResponse response = peopleService.people().connections()
                .list("people/me")
                .setPageSize(10)
                .setPersonFields("names,emailAddresses")
                .execute();

        // Print display name of connections if available.
        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                List<Name> names = person.getNames();
                if (names != null && names.size() > 0) {
                    System.out.println("Name: " + person.getNames().get(0)
                            .getDisplayName());
                    System.out.println("Email: " + person.getEmailAddresses().get(0).getValue());
                } else {
                    System.out.println("No names available for connection.");
                }
            }
        } else {
            System.out.println("No connections found.");
        }
    }

    public static void addContact(String newUserEmail) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Request 10 connections.
        ListConnectionsResponse response = peopleService.people().connections()
                .list("people/me")
                .setPageSize(10)
                .setPersonFields("names,emailAddresses")
                .execute();

        // Print display name of connections if available.
        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                List<Name> names = person.getNames();
                if (names != null && names.size() > 0) {
                    System.out.println("Name: " + person.getNames().get(0)
                            .getDisplayName());
                    System.out.println("Email: " + person.getEmailAddresses().get(0).getValue());

                    if (person.getEmailAddresses().get(0).getValue().equals(newUserEmail)) {
                        System.out.println("User can be added!!");

                    }


                } else {
                    System.out.println("No names available for connection.");
                }
            }
        } else {
            System.out.println("No connections found.");
        }
    }


    public static void sendMail(String subject, String message, String EmailRecipient) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(EmailRecipient));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = gmailService.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }

    public static void createAndSendEmail(String groupId) throws Exception {
        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        Event meeting = group.getMeeting();
        DateTime dt = meeting.getStart().getDateTime();
        String date = dt.toString();
        ArrayList<User> userList = group.getUserList();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userEmail = user.getEmail();
            sendMail("Chronos Meeting Reminder",
                    "Hello there, you have a meeting on" + date + "\n Meet you there! :)",
                    userEmail);
        }
    }
}
