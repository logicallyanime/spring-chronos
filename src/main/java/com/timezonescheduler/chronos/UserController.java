package com.timezonescheduler.chronos;

import com.github.fge.jsonpatch.JsonPatch;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("{userId}")
    public Optional<User> getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping("{userId}")
    public void removeUser(@PathVariable("userId") String userId) {
        userService.removeUser(userId);
    }

    @PatchMapping(value ="{userId}")
    public void updateUser(
            @PathVariable("userId") String userId,
            @RequestBody(required = false) JsonPatch user)
    {
        userService.updateUser(userId, user);
    }
}
