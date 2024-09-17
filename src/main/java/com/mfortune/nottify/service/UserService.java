package com.mfortune.nottify.service;

import com.mfortune.nottify.domain.User;
import com.mfortune.nottify.dto.UpdateUserDto;
import com.mfortune.nottify.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
// Transaction - unit of work on a database. ACID.
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired // Can be omitted
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(int userId, UpdateUserDto updateUserDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if(updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if(updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if(updateUserDto.getPassword() != null) {
            user.setPassword(updateUserDto.getPassword());
        }
        if(updateUserDto.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findAllByName(name);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByUsername(phoneNumber);
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public String uploadPhoto(int userId, MultipartFile file) {
        User user = getUserById(userId).orElse(null);
        String photoUrl = photoFunction.apply(userId, file);
        user.setPhotoUrl(photoUrl);
        saveUser(user);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<Integer, MultipartFile, String> photoFunction = (userId, image) -> {
        String filename = String.valueOf(userId) + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.
                    fromCurrentContextPath().path("/users/image/" + filename).toUriString();
        }catch(Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Username '%s' not found", username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Stream.of(user.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
