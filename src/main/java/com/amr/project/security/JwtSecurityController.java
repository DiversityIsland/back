package com.amr.project.security;

import com.amr.project.dao.abstracts.UserDao;
import com.amr.project.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Контроллер для передачи данных пользователя на фронт-сервер, чтобы создать JWT для пользователя
 */

@RestController
@RequestMapping("/")
public class JwtSecurityController {

    private final UserDao userDao;

    JwtSecurityController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/authentication")
    //TODO переделать на ДТО и отправлять только нужные поля
    public ResponseEntity<User> getUserCredentials(@RequestBody User user) {

        Optional<User> tempUser = userDao.findByUsername(user.getUsername());
        if (tempUser.isPresent() && user.getPassword().equals(tempUser.get().getPassword())) {
            User userCredentials = new User(tempUser.get().getId(), tempUser.get().getUsername(), tempUser.get().getRoles());
            return new ResponseEntity<>(userCredentials, HttpStatus.OK);
        } else

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}