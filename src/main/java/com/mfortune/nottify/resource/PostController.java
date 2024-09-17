package com.mfortune.nottify.resource;

import com.mfortune.nottify.domain.Post;
import com.mfortune.nottify.dto.PostDto;
import com.mfortune.nottify.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.mfortune.nottify.constant.FileConstant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> createPost(@PathVariable int userId, @RequestBody PostDto postDto) {
        boolean isCreated = postService.createPost(userId, postDto);
        return isCreated ? new ResponseEntity<>("Post was successfully created!", HttpStatus.CREATED)
                : new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") int postId, @RequestBody PostDto postDto, @RequestParam int userId) {
        boolean isUpdated = postService.updatePost(postId, postDto, userId);
        return isUpdated ? new ResponseEntity<>("Post is updated successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Unauthorized or post was not found!", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") int postId, @RequestParam("userId") int userId) {
        boolean isDeleted = postService.deletePost(postId, userId);
        return isDeleted ? new ResponseEntity<>("Post is deleted successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Incorrect post id or user id!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable int userId) {
        List<Post> posts = postService.findAllPostsByUserId(userId);
        return posts != null ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(posts, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPostImage(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }


    @Operation(summary = "Upload post photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post photo uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/photo/{postId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(@PathVariable("postId") int postId, @RequestParam("userId") int userId,
                                              @RequestParam("file") MultipartFile file) {
        String url = postService.uploadPostPhoto(userId, postId, file);
        return !url.isEmpty() ? new ResponseEntity<>("Post photo with the url: " + url + " uploaded successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
