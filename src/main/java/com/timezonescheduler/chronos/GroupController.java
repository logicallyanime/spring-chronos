package com.timezonescheduler.chronos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getGroups(){
        return groupService.getGroups();
    }

    @PostMapping
    public void addGroup(@RequestBody Group group){
        groupService.addGroup(group);
    }

    @DeleteMapping(path = "{groupId}")
    public void removeGroup(@PathVariable("groupId") long groupId){
        groupService.removeGroup(groupId);
    }

    @PutMapping(path = "{groupId}")
    public void updateGroup(
            @PathVariable("groupId") Long groupId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ArrayList<User> userList) {
        groupService.updateGroup(groupId, name, userList);
    }
}
