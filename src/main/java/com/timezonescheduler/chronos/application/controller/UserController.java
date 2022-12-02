package com.timezonescheduler.chronos.application.controller;

//import com.github.fge.jsonpatch.JsonPatch;
import com.google.gson.JsonObject;
import com.timezonescheduler.chronos.application.model.User;
import com.timezonescheduler.chronos.application.security.CurrentUser;
import com.timezonescheduler.chronos.application.security.UserPrincipal;
import com.timezonescheduler.chronos.application.security.oauth.exception.ResourceNotFoundException;
import com.timezonescheduler.chronos.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

    @GetMapping("/all")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping
    public Optional<User> getUser(@RequestBody User userId) {
        return userService.getUser(userId.getId());
    }

    @PostMapping("/register")
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @PostMapping("/addContact")
    public void addContactAsUser(@RequestBody User name) throws GeneralSecurityException, IOException {
        String newName = name.getName();
        String n1 = String.valueOf(newName.subSequence(1, newName.length() - 1));
        userService.addContact(n1);
    }

    @DeleteMapping
    public void removeUser(@RequestBody User userId) {
        userService.removeUser(userId.getId());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUser(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }


    /*
    @PatchMapping(value ="{userId}")
    public void updateUser(
            @PathVariable("userId") String userId,
            @RequestBody(required = false) JsonPatch user)
    {
        userService.updateUser(userId, user);
    }

     */

    @PatchMapping("/update")
    public void patchResource(
            @RequestBody User user)
    {
        userService.patchResource(user.getId(), user);
    }
}
