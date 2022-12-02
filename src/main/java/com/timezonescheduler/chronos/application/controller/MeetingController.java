package com.timezonescheduler.chronos.application.controller;


import com.google.api.services.calendar.model.Event;
import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.service.GroupService;
import com.timezonescheduler.chronos.application.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "api/v1/meeting")
public class MeetingController {


    private final MeetingService meetingService;
    private final GroupService groupService;

    @Autowired
    public MeetingController (MeetingService meetingService, GroupService groupService) {
        this.meetingService = meetingService;
        this.groupService = groupService;
    }

    @PostMapping("addToGCal/{groupId}")
    public void addToGroupEvents (@PathVariable("groupId") String groupId) throws IOException {
        ArrayList<Event> events = meetingService.readEvents();
        System.out.println(events);
        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        ArrayList<Event> oldEvents = group.getEventList();

        for (int i = 0; i < oldEvents.size(); i++) {
            events.add(oldEvents.get(i));
        }
        group.setEventList(events);
        groupService.updateGroup(groupId, group.getName(), group.getUserList(), group.getMeeting(), events);

    }

    @PostMapping("sendEmails/{groupId}")
    public void sendEmails (@PathVariable("groupId") String groupId) throws Exception {
        meetingService.createAndSendEmail(groupId);
    }

    @PostMapping("createevent/{groupId}")
    public void createMeetingEvent (@PathVariable("groupId") String groupId) throws IOException {
        meetingService.createEvent(groupId);
    }
}
