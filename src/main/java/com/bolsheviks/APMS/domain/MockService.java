package com.bolsheviks.APMS.domain;

import com.bolsheviks.APMS.domain.ProjectProposal.ProjectProposal;
import com.bolsheviks.APMS.domain.ProjectProposal.ProjectProposalRepository;
import com.bolsheviks.APMS.domain.Stage.Stage;
import com.bolsheviks.APMS.domain.Stage.StageRepository;
import com.bolsheviks.APMS.domain.User.User;
import com.bolsheviks.APMS.domain.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class MockService {

    private final ProjectProposalRepository projectProposalRepository;
    private final StageRepository stageRepository;
    private final UserRepository userRepository;

    public MockService(ProjectProposalRepository projectProposalRepository,
                       StageRepository stageRepository,
                       UserRepository userRepository) {
        this.projectProposalRepository = projectProposalRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;

        createMocks();
    }

    private void createMocks() {
        createProjectProposalMocks();

    }

    private void createProjectProposalMocks() {
        ProjectProposal projectProposal = new ProjectProposal();
        projectProposal.setName("Тестовое проектное предложение");
        projectProposal.setInformation("Твоя бабушка курит трубку");
        List<User> users = new ArrayList<>();
        users.add(createUser());
        projectProposal.setProjectManagersList(users);
        projectProposal.setStageList(Stream.of(createStage()).toList());
        projectProposalRepository.save(projectProposal);
    }

    private User createUser() {
        User user = new User();
        user.setFirstName("Антон");
        user.setLastName("Фекалис");
        user.setPatronymic("Павлович");

        userRepository.save(user);
        return user;
    }

    private Stage createStage() {
        Stage stage = new Stage();
        stage.setName("Тестовый стейдж");
        stageRepository.save(stage);
        return stage;
    }
}
