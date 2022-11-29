package com.timezonescheduler.chronos;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/group")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    @RequestMapping("get/{groupId}")
    public Optional<Group> getGroup(@PathVariable("groupId") String groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping
    public List<Group> getGroups(){
        return groupService.getGroups();
    }

    @PostMapping
    public void addGroup(@RequestBody Group group){
        groupService.addGroup(group);
    }

    @RequestMapping("remove/{groupId}")
    public void removeGroup(@PathVariable("groupId") String groupId){
        groupService.removeGroup(groupId);
    }

    @PatchMapping(path = "{groupId}")
    public void updateGroup(
            @PathVariable("groupId") String groupId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ArrayList<User> userList,
            @RequestParam(required = false) Event meeting,
            @RequestParam(required = false) ArrayList<Event> eventList) {
        groupService.updateGroup(groupId, name, userList, meeting, eventList);
    }

    @PatchMapping(path = "adduser/{groupId}")
    public void addUserToGroup(
            @PathVariable("groupId") String groupId,
            @RequestBody User user
    ){
        groupService.addUserToGroup(groupId, user);
    }

    @PatchMapping(path = "removeuser/{groupId}")
    public void removeUserFromGroup(
            @PathVariable("groupId") String groupId,
            @RequestBody User user
    ){
        groupService.removeUserFromGroup(groupId, user);
    }

    @PatchMapping(path = "addusercal/{groupId}")
    public void addUserCalendar(
            @PathVariable("groupId") String groupId,
            @RequestParam ArrayList<Event> eventList
    ){
        groupService.addUserCalendar(groupId, eventList);
    }

    @RequestMapping(path = "dtm/{groupId)")
    public ArrayList<ChronosPair<Event, String>> determineMeetingTime(
            @PathVariable("groupId") String groupId,
            @RequestParam String email,
            @RequestParam long meetingTime,
            @RequestParam DateTime date
            ){
        return groupService.determineMeetingTime(groupId, email, meetingTime, date);
    }

    @PatchMapping(path = "meeting/{groupId}")
    public void setMeeting(
            @PathVariable("groupId") String groupId,
            @RequestParam Event meeting
    ){
        groupService.setMeeting(groupId, meeting);
    }
}
