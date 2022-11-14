package com.timezonescheduler.chronos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
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

//    @Transactional
//    public ResponseEntity<User> updateUser(String userId, JsonPatch userPatch) {
//        ResponseEntity<User> respUser = applyPatchToUser(userPatch, userId);
//        if (respUser.getStatusCode() != HttpStatus.OK || respUser.getBody().getName() == null || respUser.getBody().getName().length() <= 0
//                || respUser.getBody().getEmail() != null || respUser.getBody().getEmail().length() <= 0) {
//            if(respUser.getStatusCode() == HttpStatus.OK){
//
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        }
//        return respUser;
//        userRepo.save(user);
//        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
//        Optional<User> userOptional = userRepo.findUserByEmail(email);
//        if (userOptional.isPresent()) {
//            throw new IllegalStateException("email taken");
//        }
//        user.setEmail(email);
//        userRepo.save(user);
//    }
//    }


//

//    private ResponseEntity<User> applyPatchToUser(
//            JsonPatch patch, String userId) {
//        try {
//            User user = userRepo.findById(userId)
//                    .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));
//
//            ObjectMapper objMap = new ObjectMapper();
//            JsonNode patched = patch.apply(objMap.convertValue(user, JsonNode.class));
//            User newUser = objMap.treeToValue(patched, User.class);
//
//            return ResponseEntity.ok(newUser);
//        } catch (JsonPatchException | JsonProcessingException e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        } catch (IllegalStateException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    public void patchResource(String userId, User newUser) {

        User saveUser = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));

        boolean needUpdate = false;

        if (StringUtils.hasLength(newUser.getName())) {
            saveUser.setName(newUser.getName());
            needUpdate = true;
        }

        if (StringUtils.hasLength(newUser.getEmail())) {
            saveUser.setEmail(newUser.getEmail());
            needUpdate = true;
        }

        if (needUpdate) {
            userRepo.save(saveUser);
        }
    }

}
