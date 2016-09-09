package org.raliclo.backend.impl;

import org.raliclo.backend.dao.PhotoDao;
import org.raliclo.backend.model.Photo;
import org.raliclo.backend.model.User;
import org.raliclo.backend.service.PhotoService;
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
