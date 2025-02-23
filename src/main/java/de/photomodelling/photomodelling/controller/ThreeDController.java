package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.ThreeD;
import de.photomodelling.photomodelling.service.ThreeDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/threed")
public class ThreeDController {

    @Autowired
    private ThreeDService threeDService;

    @GetMapping
    public List<ThreeD> getAllThreeDs() {
        return threeDService.findThreeDById(null);
    }

    @GetMapping(path = "/byId/{id}")
    public List<ThreeD> getThreeDById(@PathVariable("id") final Long id) {
        return threeDService.findThreeDById(id);
    }
}
