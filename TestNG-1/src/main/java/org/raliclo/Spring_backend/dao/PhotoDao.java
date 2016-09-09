package org.raliclo.Spring_backend.dao;

import org.raliclo.Spring_backend.model.Photo;
import org.raliclo.Spring_backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoDao extends CrudRepository<Photo, Long> {

    Photo save(Photo photo);

    List<Photo> findByUser(User user);

    Photo findByPhotoId(Long photoId);

    List<Photo> findAll();
}