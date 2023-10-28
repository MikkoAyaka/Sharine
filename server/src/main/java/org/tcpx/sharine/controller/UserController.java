package org.tcpx.sharine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tcpx.sharine.dto.UsernamePassword;
import org.tcpx.sharine.service.UserService;

@RestController("/user")
public class UserController extends BaseController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Object login(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.login(usernamePassword));
    }

    @PostMapping("/register")
    public Object register(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.register(usernamePassword));
    }

    @PostMapping("/forget")
    public Object forget(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.forget(usernamePassword));
    }

    @PostMapping("/send")
    public Object sendCode(@RequestBody UsernamePassword usernamePassword) {
        userService.sendCode(usernamePassword);
        return ok();
    }
}