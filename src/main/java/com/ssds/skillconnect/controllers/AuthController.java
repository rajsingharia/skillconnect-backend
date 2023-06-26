package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.model.UserLoginModel;
import com.ssds.skillconnect.model.UserRegisterModel;
import com.ssds.skillconnect.service.AuthService;
import com.ssds.skillconnect.model.AuthenticationResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    @PostMapping("/register")
    private AuthenticationResponseModel registerUser(@RequestBody UserRegisterModel userRegisterModel) {
        return authService.registerUser(userRegisterModel);
    }

    @PostMapping("/authenticate")
    private AuthenticationResponseModel loginUser(@RequestBody UserLoginModel userLoginModel) {
        return authService.authenticateUser(userLoginModel);
    }

    @GetMapping("/logout")
    private void logoutUser(@RequestHeader(value="Authorization") String authorizationHeader) {
        authService.logoutUser(authorizationHeader);
    }

    @GetMapping("/test")
    private String test() {
        return "test";
    }

}
