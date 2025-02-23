package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Rating;
import de.photomodelling.photomodelling.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> findRatingById(Long idFilter) {
        List<Rating> ratingList = new ArrayList<>();
        if (idFilter == null) {
            ratingRepository.findAll().forEach(ratingList::add);
        } else {
            ratingRepository.findById(idFilter).ifPresent(ratingList::add);
        }
        return ratingList;
    }
}
