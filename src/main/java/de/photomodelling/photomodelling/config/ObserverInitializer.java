package de.photomodelling.photomodelling.config;

import de.photomodelling.photomodelling.observer.ProjectNotifier;
import de.photomodelling.photomodelling.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObserverInitializer {

    @Autowired
    public ObserverInitializer(ProjectNotifier projectNotifier, EmailNotificationService emailNotificationService) {
        projectNotifier.addObserver(emailNotificationService);
    }
}
