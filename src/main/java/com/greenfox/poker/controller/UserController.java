package com.greenfox.poker.controller;
import com.greenfox.poker.model.LoginRequest;
import com.greenfox.poker.model.PokerUser;
import com.greenfox.poker.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  UserService userService;

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<?> registerUser(@RequestBody @Valid PokerUser userRegister,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
      return new ResponseEntity(userService.respondToMissingParameters(bindingResult), HttpStatus.BAD_REQUEST);
    } else if (userService.isEmailOccupied(userRegister)) {
        return new ResponseEntity(userService.registerWithOccupiedEmail(), HttpStatus.CONFLICT);
      } else if (userService.isUsernameOccupied(userRegister)) {
        return new ResponseEntity(userService.registerWithOccupiedUsername(), HttpStatus.CONFLICT);
    }
    return new ResponseEntity(userService.responseToSuccessfulRegister(userRegister),HttpStatus.OK);
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity(userService.respondToMissingParameters(bindingResult),
          HttpStatus.BAD_REQUEST);
    } else if (!loginRequest.getUsername().equals("Bond") || !loginRequest
          .getPassword().equals("password123")) {
        return new ResponseEntity(userService.loginWithIvalidUsernameOrPassword(), HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity(userService.responseToSuccessfulLogin(loginRequest), HttpStatus.OK);
  }
}
