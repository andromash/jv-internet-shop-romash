package com.internet.shop.security;

import com.internet.shop.exception.AuthenticationException;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import com.internet.shop.util.HashUtil;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> userFromDB = userService.findByLogin(login);
        if (userFromDB.isEmpty() || !HashUtil.isValid(password, userFromDB.get())) {
            throw new AuthenticationException("Incorrect login or password");
        }
        return userFromDB.get();
    }
}
