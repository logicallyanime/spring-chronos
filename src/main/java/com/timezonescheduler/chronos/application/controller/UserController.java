package com.timezonescheduler.chronos.application.controller;

//import com.github.fge.jsonpatch.JsonPatch;
import com.timezonescheduler.chronos.application.security.CurrentUser;
import com.timezonescheduler.chronos.application.security.UserPrincipal;
import com.timezonescheduler.chronos.application.security.oauth.exception.ResourceNotFoundException;
import com.timezonescheduler.chronos.application.service.UserService;
import com.timezonescheduler.chronos.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
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

//    @PatchMapping(value ="{userId}")
//    public void updateUser(
//            @PathVariable("userId") String userId,
//            @RequestBody(required = false) JsonPatch user)
//    {
//        userService.updateUser(userId, user);
//    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUser(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
    @GetMapping("/email")
    @PermitAll
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @PatchMapping("/update/{userId}")
    public void patchResource(
            @PathVariable String userId,
            @RequestBody User newUser)
    {
        userService.patchResource(userId, newUser);
    }
}
