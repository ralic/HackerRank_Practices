package org.raliclo.Spring_backend.service;

import org.raliclo.Spring_backend.model.User;

public interface UserService {
    User save(User user);

    User findByUserName(String userName);


}
