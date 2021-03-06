package com.bolsheviks.APMS.domain.ProjectProposal;

import com.bolsheviks.APMS.domain.Project.Project;
import com.bolsheviks.APMS.domain.Project.ProjectRepository;
import com.bolsheviks.APMS.domain.Project.ProjectStatus;
import com.bolsheviks.APMS.domain.Stage.Stage;
import com.bolsheviks.APMS.domain.Stage.StageRepository;
import com.bolsheviks.APMS.domain.User.User;
import com.bolsheviks.APMS.domain.User.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProposalService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final StageRepository stageRepository;
    private final StageNamesConverter stageNamesConverter;

    @Transactional
    public Project createProject(User user, User manager, ProjectProposal projectProposal) {
        Project project = createProjectFromProjectProposal(projectProposal, user, manager);
        addProjectToUser(user, project);
        addProjectToUser(manager, project);
        projectProposal.getConsultantList().forEach(consultant -> addProjectToUser(consultant, project));
        return project;
    }

    private Project createProjectFromProjectProposal(ProjectProposal projectProposal,
                                                     User userCreator,
                                                     User userProjectManager) {
        Project project = new Project();
        project.setName(projectProposal.getName());
        project.setUserCaptain(userCreator);
        project.setUserProjectManager(userProjectManager);
        project.setUsersConsultantsList(new ArrayList<>(projectProposal.getConsultantList()));
        project.setProjectStatus(ProjectStatus.IN_PROCESS);
        project.setInformation(projectProposal.getInformation());
        project.setStageList(createStages(projectProposal));
        projectRepository.save(project);
        return project;
    }

    private List<Stage> createStages(ProjectProposal projectProposal) {
        ArrayList<Stage> stages = new ArrayList<>();
        stageNamesConverter
                .convertStageNamesToList(projectProposal.getStageNames())
                .forEach(name -> stages.add(new Stage(name)));
        stageRepository.saveAll(stages);

        return stages;
    }

    private void addProjectToUser(User user, Project project) {
        user.getProjectList().add(project);
        userRepository.save(user);
    }
}
