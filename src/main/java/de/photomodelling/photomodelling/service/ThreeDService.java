package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.ThreeD;
import de.photomodelling.photomodelling.repository.ThreeDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ThreeDService {

    @Autowired
    private final ThreeDRepository threeDRepository;

    @Autowired
    public ThreeDService(ThreeDRepository threeDRepository) {
        this.threeDRepository = threeDRepository;
    }

    public List<ThreeD> findThreeDById(Long idFilter) {
        List<ThreeD> threeDList = new ArrayList<>();
        if (idFilter == null) {
            threeDRepository.findAll().forEach(threeDList::add);
        } else {
            threeDRepository.findById(idFilter).ifPresent(threeDList::add);
        }
        return threeDList;
    }
}
