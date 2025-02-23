package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoService {

    @Autowired
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> findPhotoById(Long idFilter) {
        List<Photo> photoList = new ArrayList<>();
        if (idFilter == null) {
            photoRepository.findAll().forEach(photoList::add);
        } else {
            photoRepository.findById(idFilter).ifPresent(photoList::add);
        }
        return photoList;
    }
}
