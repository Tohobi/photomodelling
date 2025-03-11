package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import de.photomodelling.photomodelling.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    private Photo photo1;
    private Photo photo2;

    @BeforeEach
    void setUp() {
        photo1 = new Photo();
        photo1.setId(1L);
        photo1.setFilename("image1.jpg");

        photo2 = new Photo();
        photo2.setId(2L);
        photo2.setFilename("image2.jpg");
    }

    @Test
    void testFindAllPhotos() {
        when(photoRepository.findAll()).thenReturn(Arrays.asList(photo1, photo2));

        List<Photo> photos = photoService.findPhotoById(null);
        assertEquals(2, photos.size());
    }

    @Test
    void testFindPhotoById() {
        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo1));

        List<Photo> photos = photoService.findPhotoById(1L);
        assertEquals(1, photos.size());
        assertEquals("image1.jpg", photos.get(0).getFilename());
    }
}