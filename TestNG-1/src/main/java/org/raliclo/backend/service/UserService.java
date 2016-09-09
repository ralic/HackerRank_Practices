package org.raliclo.backend.service;

import org.raliclo.backend.model.User;

public interface UserService {
    User save(User user);

    User findByUserName(String userName);


}
