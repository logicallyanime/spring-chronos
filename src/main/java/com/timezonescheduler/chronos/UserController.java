package com.timezonescheduler.chronos;

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

    @RequestMapping("get/{userId}")
    public Optional<User> getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @RequestMapping("remove/{userId}")
    public void removeUser(@PathVariable("userId") String userId) {
        userService.removeUser(userId);
    }

    public void updateUser(
            @PathVariable("userId") String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        userService.updateUser(userId, name, email);
    }
}
