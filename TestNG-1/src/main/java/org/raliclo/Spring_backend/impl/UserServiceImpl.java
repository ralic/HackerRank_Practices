package org.raliclo.Spring_backend.impl;

import org.raliclo.Spring_backend.dao.UserDao;
import org.raliclo.Spring_backend.model.User;
import org.raliclo.Spring_backend.service.UserService;
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
