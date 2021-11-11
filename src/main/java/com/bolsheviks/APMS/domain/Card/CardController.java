package com.bolsheviks.APMS.domain.Card;

import com.bolsheviks.APMS.domain.Project.Project;
import com.bolsheviks.APMS.domain.Project.ProjectRepository;
import com.bolsheviks.APMS.domain.Stage.Stage;
import com.bolsheviks.APMS.domain.Stage.StageRepository;
import com.bolsheviks.APMS.domain.User.User;
import com.bolsheviks.APMS.domain.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.bolsheviks.APMS.security.SecurityFilter.USER_UUID;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ProjectRepository projectRepository;
    private final CardConverter cardConverter;
    private final StageRepository stageRepository;
    private final CardService cardService;

    @GetMapping("/{uuid}")
    public CardDto get(@RequestAttribute(USER_UUID) UUID userId,
                       @PathVariable("uuid") UUID id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Project project = projectRepository.selectByCard(card)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!project.containsUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return cardConverter.convertCardToDto(card);
    }

    @PostMapping
    public UUID post(@RequestAttribute(USER_UUID) UUID userId,
                     @RequestParam("projectUuid") UUID projectUuid,
                     @RequestParam("stageUuid") UUID stageUuid,
                     @RequestBody CardDto cardDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Project project = projectRepository.findById(projectUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!project.containsUser(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Stage stage = findStageByUuidInProject(project, stageUuid);
        return cardService.createCard(user, cardDto, stage).getId();
    }

    private Stage findStageByUuidInProject(Project project, UUID stageUuid) {
        Stage stage = null;
        for (Stage s : project.getStageList()) {
            if (s.getId().equals(stageUuid)) {
                stage = s;
                break;
            }
        }
        if (stage == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return stage;
    }
}
