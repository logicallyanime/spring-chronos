package com.timezonescheduler.chronos;

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

    //user clicks add from GCal button
    //user is taken to OAuth
    //grab events right there
    //THEN call addUserCalendar
}
