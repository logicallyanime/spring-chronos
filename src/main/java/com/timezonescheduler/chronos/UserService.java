package com.timezonescheduler.chronos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public Optional<User> getUser(String userId) {
        boolean exists = userRepo.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exist");
        }
        return userRepo.findById(userId);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public void addNewUser(User user) {
        Optional<User> userOptional = userRepo.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        userRepo.save(user);
    }

    public void removeUser(String userId) {
        boolean exists = userRepo.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exist");
        }
        userRepo.deleteById(userId);
    }

    @Transactional
    public void updateUser(String userId, String name, String email) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> userOptional = userRepo.findUserByEmail(email);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }
}
