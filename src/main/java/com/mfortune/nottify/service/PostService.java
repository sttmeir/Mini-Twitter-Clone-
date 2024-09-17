package com.mfortune.nottify.service;

import com.mfortune.nottify.domain.Post;
import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.PostDto;
import com.mfortune.nottify.exceptions.ResourceNotFoundException;
import com.mfortune.nottify.repository.PostRepository;
import com.mfortune.nottify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.mfortune.nottify.constant.FileConstant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public boolean createPost(int userId, PostDto postDto) {
        Optional<User> user = userService.getUserById(userId);
        Post post = new Post();
        if(user.isPresent()) {
            post.setUser(user.get());
            post.setTitle(postDto.getTitle());
            post.setBody(postDto.getBody());
            postRepository.save(post);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updatePost(int postId, PostDto postDto, int userId) {
        Post postForUpdate = postRepository.findById(postId).orElseThrow(()
                -> new ResourceNotFoundException("Post to update was not found"));
        User authorOfPost = userService.getUserById(userId).orElseThrow(()
                -> new ResourceNotFoundException("User not found"));
        if(postForUpdate.getUser().getUserId() != authorOfPost.getUserId()) {
            throw new AccessDeniedException("You do not have permission to update this post");
        }
        if(postDto.getTitle() != null) {
            postForUpdate.setTitle(postDto.getTitle());
        }
        if(postDto.getBody() != null) {
            postForUpdate.setBody(postDto.getBody());
        }
        postRepository.save(postForUpdate);
        return true;
    }

    public Post findPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findAllPostsByUserId(int userId) {
        User user = userService.getUserById(userId).orElse(null);
        if(user != null) {
            return user.getPosts();
        }
        return null;
    }

    public String uploadPostPhoto(int userId, int postId, MultipartFile file) {
        Post post = findPostById(postId);
        if(post != null) {
            if(post.getUser().getUserId() == userId) {
                String photoUrl = photoFunction.apply(userId, file);
                post.setPostPhotoUrl(photoUrl);
                postRepository.save(post);
                return photoUrl;
            }
        }
        return null;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<Integer, MultipartFile, String> photoFunction = (userId, image) -> {
        String filename = userId + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.
                    fromCurrentContextPath().path("/posts/image/" + filename).toUriString();
        }catch(Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };


    public boolean deletePost(int userId, int postId) {
        Post postForDeletion = postRepository.findById(postId).orElse(null);
        if(postForDeletion != null) {
            if(postForDeletion.getUser().getUserId() == userId) {
                postRepository.delete(postForDeletion);
                return true;
            }
            else {
                throw new AccessDeniedException("User is not allowed to delete this post");
            }
        }
        return false;
    }
}
