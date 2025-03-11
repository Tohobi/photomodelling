package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import de.photomodelling.photomodelling.observer.ProjectNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ProjectNotifier projectNotifier;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
    }

    @Test
    void findProjectById_shouldReturnProjectWhenExists() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        List<Project> result = projectService.findProjectById(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getName());
    }

    @Test
    void findProjectById_shouldReturnEmptyListWhenNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        List<Project> result = projectService.findProjectById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createProject_shouldSaveProjectAndNotify() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(project);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectNotifier, times(1)).notifyProjectCreated(result);
    }

    @Test
    void addPhotosToProject_shouldAddPhotosWhenProjectExists() {
        Photo photo = new Photo();
        photo.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        Project result = projectService.addPhotosToProject(1L, List.of(photo));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void addPhotosToProject_shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                projectService.addPhotosToProject(1L, List.of(new Photo())));

        assertEquals("Projekt mit ID 1 nicht gefunden!", exception.getMessage());
    }
}
