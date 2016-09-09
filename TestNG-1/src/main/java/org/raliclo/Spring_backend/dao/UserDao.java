package org.raliclo.Spring_backend.dao;

import org.raliclo.Spring_backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User save(User user);

    User findByUserName(String userName);


}
