package org.raliclo.backend.impl;

import org.raliclo.backend.dao.UserDao;
import org.raliclo.backend.model.User;
import org.raliclo.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }


}
