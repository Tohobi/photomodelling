package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public List<Photo> getAllPhotos() {
        return photoService.findPhotoById(null);
    }

    @GetMapping(path = "/byId/{id}")
    public List<Photo> getPhotoById(@PathVariable("id") final Long id) {
        return photoService.findPhotoById(id);
    }
}
