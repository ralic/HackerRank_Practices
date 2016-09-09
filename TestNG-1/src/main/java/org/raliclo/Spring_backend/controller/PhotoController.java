package org.raliclo.Spring_backend.controller;

import org.raliclo.Spring_backend.model.Photo;
import org.raliclo.Spring_backend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @RequestMapping("/allPhotos")
    public List<Photo> getAllPhotos() {
        return photoService.findAll();
    }
}
