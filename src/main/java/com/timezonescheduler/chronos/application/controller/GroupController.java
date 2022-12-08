package com.timezonescheduler.chronos.application.controller;

import com.google.api.services.calendar.model.EventDateTime;
import com.timezonescheduler.chronos.application.service.UserService;
import com.timezonescheduler.chronos.application.util.ChronosPair;
import com.timezonescheduler.chronos.application.service.GroupService;
import com.timezonescheduler.chronos.application.model.Group;
import com.timezonescheduler.chronos.application.model.User;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/group")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService){
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("get/{groupId}")
    public Optional<Group> getGroup(@PathVariable("groupId") String groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping("/getUserList/{groupId}")
    public ArrayList<String> getUserList(@PathVariable("groupId") String groupId) {
        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        ArrayList<User> list = group.getUserList();
        ArrayList<String> emailList = new ArrayList<>();
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            emailList.add(i,list.get(i).getEmail());
        }
        return emailList;
    }

    @GetMapping({"{userid}"})
    public ArrayList<Group> getGroups(@PathVariable String userid){

        User user = userService.getUser(userid).orElseThrow(RuntimeException::new);
        System.out.println(user);

//        //Changes here
//        ArrayList<Group> groupList = user.getGroups();
//        ArrayList<Group> groups = new ArrayList<>();
//        for (int i = 0; i < groupList.toArray().length; i++) {
//
//        }

        //Ends changes here
        return user.getGroups();
    }

    @GetMapping("/getgroupname/{groupId}")
    public String getGroupName(@PathVariable("groupId") String groupId){
        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        return group.getName();
    }

    @PostMapping("/create/{userid}")
    public void createGroup(@PathVariable String userid) {
        User gLUser = userService.getUser(userid).orElseThrow(RuntimeException::new);
        Group newGroup = new Group(gLUser.getName() + "'s Group", gLUser);
        addGroup(newGroup);
        gLUser.addGroup(newGroup);
        userService.patchResource(userid, gLUser);
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

//        String name = newName.substring(1, newName.length() - 1);
//        System.out.println(name);

        groupService.updateGroup(groupId, name, userList, meeting, eventList);
    }

    @PatchMapping(path = "updateGroupName/{groupId}")
    public void updateGroupName(
            @PathVariable("groupId") String groupId,
            @RequestBody String newName) {

        String name = newName.substring(1, newName.length() - 1);
        System.out.println(name);
        groupService.updateGroup(groupId, name, null, null, null);
    }

    @PatchMapping(path = "adduser/{groupId}")
    public void addUserToGroup(
            @PathVariable("groupId") String groupId,
            @RequestBody String plusEmail
    ){
        String email = plusEmail.substring(1, plusEmail.length() - 1);
        System.out.println(email);
        User user = userService.getUserByEmail(email).orElseThrow(RuntimeException::new);
        System.out.println(user);
        groupService.addUserToGroup(groupId, user);

        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        System.out.println(group);
        user.addGroup(group);
        userService.patchResource(user.getId(), user);
    }

    @PatchMapping(path = "removeuser/{groupId}")
    public void removeUserFromGroup(
            @PathVariable("groupId") String groupId,
            @RequestBody String removeEmail
    ){
        String email = removeEmail.substring(1, removeEmail.length() - 1);
        System.out.println(email);
        User user = userService.getUserByEmail(email).orElseThrow();
        groupService.removeUserFromGroup(groupId, user);
        Group group = groupService.getGroup(groupId).orElseThrow(RuntimeException::new);
        user.removeGroup(group);
        userService.patchResource(user.getId(), user);
    }

    @PatchMapping(path = "addusercal/{groupId}")
    public void addUserCalendar(
            @PathVariable("groupId") String groupId,
            @RequestBody ArrayList<Event> eventList
    ){
        groupService.addUserCalendar(groupId, eventList);
    }

    @PatchMapping(path = "dtm/{groupId}")
    public ArrayList<ChronosPair<Event, String>> determineMeetingTime(
            @PathVariable("groupId") String groupId,
            @RequestParam String email,
            @RequestParam long meetingTime,
            @RequestParam String sDate
            ){
        System.out.println("Check 1");
        //sDate = URLDecoder.decode(sDate, StandardCharsets.UTF_8);
        return groupService.determineMeetingTime(groupId, email, meetingTime, new DateTime(sDate));
    }

    @PatchMapping(path = "meeting/{groupId}")
    public void setMeeting(
            @PathVariable("groupId") String groupId,
            @RequestParam long startTime,
            @RequestParam long endTime
    ){
        Event meeting = new Event();
        meeting.setStart(new EventDateTime().setDateTime(new DateTime(startTime)));
        meeting.setEnd(new EventDateTime().setDateTime(new DateTime(endTime)));
        System.out.println(meeting);
        groupService.setMeeting(groupId, meeting);
    }
}
