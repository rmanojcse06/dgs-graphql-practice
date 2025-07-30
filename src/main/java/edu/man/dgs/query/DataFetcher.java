package edu.man.dgs.query;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.man.dgs.model.*;
import graphql.execution.DataFetcherResult;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class DataFetcher {

    private final List<String> usernames = List.of(
            "Manoj","Ravi","Kumar","John","Manuel","Saroneita","Sanjana",
            "Adlin","Johanna","Asher","Nathaniel","Mary","Roselin","Annate","Anjana",
            "Naveen","Timothy","Philip","Jeswin","Joshua"
    );
    private final AtomicInteger userCounter = new AtomicInteger(1);
    private final AtomicInteger postCounter = new AtomicInteger(1);
    /*
    private final List<UserModel> users = java.util.stream.IntStream.range(0, usernames.size())
            .mapToObj(i -> new UserModel(String.valueOf(i + 1), usernames.get(i)))
            .toList();
     */
    private List<UserModel> userRepo;
    private List<Post> postRepo;

    @PostConstruct
    private void init() {
        userRepo = new ArrayList<>(
                usernames.stream()
                        .map(name -> new AdminUserModel(String.valueOf(userCounter.getAndIncrement()), name.toLowerCase()))
                        .toList()
        );
        log.info("Inside UserFetcher.init() ==> {}", userRepo.size());
        postRepo = new ArrayList<>();
        log.info("Inside PostFetcher.init() ==> {}", postRepo.size());
    }

    @DgsQuery
    public List<UserModel> users() {
        return userRepo;
    }

    @DgsQuery
    public List<Post> posts() {
        return postRepo;
    }

    @DgsQuery(field = "post_by_user")
    public DataFetcherResult<List<Post>> postByUser(@InputArgument("userid") String userId) {
        log.info("Inside UserFetcher.postByUser() ==> userId: {}", userId);
        return DataFetcherResult.<List<Post>>newResult()
                .data(postRepo.stream()
                        .filter(post -> post.getAddedBy() != null && post.getAddedBy().getId().equals(userId))
                        .collect(Collectors.toList()))
                .localContext(userRepo.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null))
                .build();
    }

    @DgsQuery(field = "posts_before")
    public List<Post> postsBefore(@InputArgument("dateTime") LocalDateTime dateTime) {
        return postRepo.stream()
                .filter(post -> post.getCreatedAt().isBefore(dateTime))
                .collect(Collectors.toList());
    }

    @DgsQuery
    public UserModel user(@InputArgument String id) {
        val a = userRepo.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        log.info("Inside UserFetcher.user() ==> {}", a);
        return a;
    }

    @DgsQuery(field = "get_guests")
    public List<UserModel> getAllGuestUsers() {
        log.info("Inside UserFetcher.getAllGuestUsers() ==> {}", userRepo.size());
        val guestUsers = userRepo.stream().filter(u -> u instanceof GuestUserModel).collect(java.util.stream.Collectors.toList());
        return guestUsers;
    }

    @DgsQuery(field = "get_admins")
    public List<UserModel> getAllAdminUsers() {
        log.info("Inside UserFetcher.getAllAdminUsers() ==> {}", userRepo.size());
        val adminUsers = userRepo.stream().filter(u -> u instanceof AdminUserModel).collect(java.util.stream.Collectors.toList());
        return adminUsers;
    }

    @DgsMutation(field = "add_post")
    public Post addPost(@InputArgument String title, @InputArgument String content, @InputArgument UserModelInput userInput) {
        log.info("Inside UserFetcher.addPost() ==> title: {}, content: {}, userId: {}", title, content, userInput);
        UserModel user = userRepo.stream().filter(u -> u.getId().equals(userInput.getId())).findFirst().orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userInput.getId());
        }
        else if (user.getName().trim().toLowerCase().equals(userInput.getName().trim().toLowerCase())){
            Post post = Post.builder()
                    .id(String.valueOf(postCounter.getAndIncrement()))
                    .title(title)
                    .content(content)
                    .addedBy(user)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            postRepo.add(post);
            log.info("Post created: {}", post);
            return post;
        }else{
            log.error("Nothing Posted!!! User name mismatch: expected {}, but got {}", user.getName(), userInput.getName());
            throw new IllegalArgumentException("User name mismatch: expected " + user.getName() + ", but got " + userInput.getName());
        }

    }

    @DgsMutation(field = "add_user")
    public UserModel addUser(@InputArgument String name, @InputArgument String phoneNumber, @InputArgument String email) {
        GuestUserModel user = new GuestUserModel(String.valueOf(userCounter.getAndIncrement()), name.toLowerCase());
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepo.add(user);
        return user;
    }

}
