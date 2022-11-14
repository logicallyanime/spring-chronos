package com.timezonescheduler.chronos.application;

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

    @PutMapping(path = "{groupId}")
    public void updateGroup(
            @PathVariable("groupId") String groupId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ArrayList<User> userList) {
        groupService.updateGroup(groupId, name, userList);
    }
}
