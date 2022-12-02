package com.timezonescheduler.chronos.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.fge.jsonpatch.*;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.timezonescheduler.chronos.application.model.User;
import com.timezonescheduler.chronos.application.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jdo.annotations.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private static final String APPLICATION_NAME = "Meeting Service";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

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
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public Optional<User> getUser(String userId) {
        boolean exists = userRepo.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exist");
        }
        return userRepo.findById(userId);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public void addNewUser(User user) {
        Optional<User> userOptional = userRepo.findUserByEmail(user.getEmail());
        System.out.println(userOptional.isPresent());
        if (userOptional.isPresent() && userOptional.get().getEmail().equals(user.getEmail())) {
            throw new IllegalStateException("email taken");
        }
        userRepo.save(user);
    }

    public void addContact(String newUserName) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        PeopleService peopleService =
                new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        // Request 10 connections.
        ListConnectionsResponse response = peopleService.people().connections()
                .list("people/me")
                .setPageSize(10)
                .setPersonFields("names,emailAddresses")
                .execute();

        System.out.println(newUserName);
        // Print display name of connections if available.
        List<Person> connections = response.getConnections();
        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                List<Name> names = person.getNames();
                if (names != null && names.size() > 0) {
                    String contactName = person.getNames().get(0)
                            .getDisplayName();
                    String contactEmail = person.getEmailAddresses().get(0).getValue();
                    //System.out.println("Name: " + contactName);
                    //System.out.println("Email: " + contactEmail);

                    if (contactName.equals(newUserName)) {
                        System.out.println("User can be added!!");

                        User newContactUser = new User(contactName, contactEmail);
                        System.out.println(newContactUser);
                        addNewUser(newContactUser);
                    }
                } else {
                    System.out.println("No names available for connection.");
                }
            }
        } else {
            System.out.println("No connections found.");
        }
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
        InputStream in = UserService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

    public void removeUser(String userId) {
        boolean exists = userRepo.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exist");
        }
        userRepo.deleteById(userId);
    }

    public void patchResource(String userId, User newUser) {

        User saveUser = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));

        boolean needUpdate = false;

        //TODO Fix this
        if (StringUtils.hasLength(newUser.getName())) {
            saveUser.setName(newUser.getName());
            needUpdate = true;
        }

        if (StringUtils.hasLength(newUser.getEmail())) {
            saveUser.setEmail(newUser.getEmail());
            needUpdate = true;
        }
        if (newUser != null || !newUser.getGroups().isEmpty()) {
            saveUser.setGroups(newUser.getGroups());
            needUpdate = true;
        }
        if (newUser.getCalendar() != null) {
            saveUser.setCalendar(newUser.getCalendar());
            needUpdate = true;
        }
        if (newUser.getTimezone() != saveUser.getTimezone()) {
            saveUser.setTimezone(newUser.getTimezone());
            needUpdate = true;
        }

        if (needUpdate) {
            userRepo.save(saveUser);
        }
    }

}
