package com.mfortune.nottify.resource;

import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.UpdateUserDto;
import com.mfortune.nottify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PatchMapping("/user/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable int userId, @RequestBody UpdateUserDto updateUserDto) {
        User user = userService.updateUser(userId, updateUserDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam("id") int id) {
        User user = userService.getUserById(id).orElse(null);
        if(user != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> searchUser(@PathVariable String name) {
        List<User> listOfUsers = userService.getUsersByName(name);
        return new ResponseEntity<>(listOfUsers, HttpStatus.OK);
    }

    @Operation(summary = "Upload user photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/photo", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") int id,
                                              @RequestParam("file") MultipartFile file) {
        String url = userService.uploadPhoto(id, file);
        return !url.isEmpty() ? new ResponseEntity<>("User photo with the url: " + url + " uploaded successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
