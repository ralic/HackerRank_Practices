package org.raliclo.backend.service;

import org.raliclo.backend.model.Photo;
import org.raliclo.backend.model.User;

import java.util.List;

public interface PhotoService {
    Photo save(Photo photo);

    List<Photo> findByUser(User user);

    Photo findByPhotoId(Long photoId);

    List<Photo> findAll();
}
