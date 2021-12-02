package com.bolsheviks.APMS.domain.User;

import com.bolsheviks.APMS.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.bolsheviks.APMS.security.SecurityFilter.USER_UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    public static final int USER_PAGE_SIZE = 10;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @GetMapping("/all")
    public List<UUID> getAll(@RequestAttribute(USER_UUID) UUID userId,
                             @RequestParam(defaultValue = "1") int page) {
        // Пагинация начинается с нуля
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be > 0");
        }
        page -= 1;
        return userRepository.findAllByIdNotOrderByLastName(userId,
                        Pageable.ofSize(USER_PAGE_SIZE).withPage(page))
                .stream().map(BaseEntity::getId).toList();
    }

    @GetMapping("/all/page_count")
    public int getPageCount() {
        int userCount = userRepository.countAllBy();
        return userCount / USER_PAGE_SIZE
                + (userCount % USER_PAGE_SIZE == 0 ? 0 : 1);
    }

    @GetMapping("/find")
    public List<UUID> find(@RequestParam String request) {
        if (request.length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request must be not empty");
        }
        List<String> s = Arrays.stream(request.split(" ", -1))
                .map(this::setNeededCase).toList();
        if (s.size() == 1) {
            return userRepository.find(s.get(0)).stream().map(BaseEntity::getId).toList();
        }
        if (s.size() == 2) {
            return userRepository.find(s.get(0), s.get(1)).stream().map(BaseEntity::getId).toList();
        }
        return userRepository.find(s.get(0), s.get(1), s.get(2)).stream().map(BaseEntity::getId).toList();
    }

    @GetMapping("/self")
    public UserDto get(@RequestAttribute(USER_UUID) UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return userConverter.convertUserToDto(user);
    }

    @GetMapping("/{uuid}")
    public UserDto getUser(@PathVariable("uuid") UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return userConverter.convertUserToDto(user);
    }

    @PutMapping("/self")
    public void put(@RequestAttribute(USER_UUID) UUID userId, @RequestBody UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        userConverter.fillUserByDto(user, userDto);
        userRepository.save(user);
    }

    @PutMapping("/{uuid}")
    public void changeRole(@RequestAttribute(USER_UUID) UUID adminId,
                           @PathVariable("uuid") UUID userId,
                           @RequestBody UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setRole(userDto.role);
        userRepository.save(user);
    }

    private String setNeededCase(String s) {
        StringBuilder sb = new StringBuilder(s.toLowerCase());
        sb.setCharAt(0, Character.toUpperCase(s.charAt(0)));
        return sb.toString();
    }
}
