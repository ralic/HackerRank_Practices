package org.raliclo.Spring_backend.service;

import org.raliclo.Spring_backend.model.Photo;
import org.raliclo.Spring_backend.model.User;

import java.util.List;

public interface PhotoService {
    Photo save(Photo photo);

    List<Photo> findByUser(User user);

    Photo findByPhotoId(Long photoId);

    List<Photo> findAll();
}
