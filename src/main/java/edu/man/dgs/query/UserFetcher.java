package edu.man.dgs.query;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.man.dgs.model.AdminUserModel;
import edu.man.dgs.model.GuestUserModel;
import edu.man.dgs.model.UserModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@DgsComponent
public class UserFetcher {

    private final List<String> usernames = List.of(
            "Manoj","Ravi","Kumar","John","Manuel","Saroneita","Sanjana",
            "Adlin","Johanna","Asher","Nathaniel","Mary","Roselin","Annate","Anjana",
            "Naveen","Timothy","Philip","Jeswin","Joshua"
    );
    private final AtomicInteger counter = new AtomicInteger(1);
    /*
    private final List<UserModel> users = java.util.stream.IntStream.range(0, usernames.size())
            .mapToObj(i -> new UserModel(String.valueOf(i + 1), usernames.get(i)))
            .toList();
     */
    private List<UserModel> users;

    @PostConstruct
    private void init() {
        users = new ArrayList<>(
                usernames.stream()
                        .map(name -> new AdminUserModel(String.valueOf(counter.getAndIncrement()), name.toLowerCase()))
                        .toList()
        );
        log.info("Inside UserFetcher.init() ==> {}", users.size());
    }

    @DgsQuery
    public List<UserModel> users() {
        return users;
    }

    @DgsQuery
    public UserModel user(@InputArgument String id) {
        val a = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        log.info("Inside UserFetcher.user() ==> {}", a);
        return a;
    }

    @DgsQuery(field = "get_guests")
    public List<UserModel> getAllGuestUsers() {
        log.info("Inside UserFetcher.getAllGuestUsers() ==> {}", users.size());
        val guestUsers = users.stream().filter(u -> u instanceof GuestUserModel).collect(java.util.stream.Collectors.toList());
        return guestUsers;
    }

    @DgsQuery(field = "get_admins")
    public List<UserModel> getAllAdminUsers() {
        log.info("Inside UserFetcher.getAllAdminUsers() ==> {}", users.size());
        val adminUsers = users.stream().filter(u -> u instanceof AdminUserModel).collect(java.util.stream.Collectors.toList());
        return adminUsers;
    }

    @DgsMutation
    public UserModel addUser(@InputArgument String name, @InputArgument String phoneNumber, @InputArgument String email) {
        GuestUserModel user = new GuestUserModel(String.valueOf(counter.getAndIncrement()), name.toLowerCase());
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        users.add(user);
        return user;
    }

}
