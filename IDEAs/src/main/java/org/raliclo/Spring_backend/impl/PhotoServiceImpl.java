package org.raliclo.Spring_backend.impl;

import org.raliclo.Spring_backend.dao.PhotoDao;
import org.raliclo.Spring_backend.model.Photo;
import org.raliclo.Spring_backend.model.User;
import org.raliclo.Spring_backend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoDao photoDao;

    public Photo save(Photo photo) {
        return photoDao.save(photo);
    }

    public List<Photo> findByUser(User user) {
        return photoDao.findByUser(user);
    }

    public Photo findByPhotoId(Long photoId) {
        return photoDao.findByPhotoId(photoId);
    }

    public List<Photo> findAll() {
        return photoDao.findAll();
    }
}
