package edu.man.dgs.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true, exclude = {"email", "phoneNumber"})
@RequiredArgsConstructor
public class AdminUserModel extends UserModel {
    public AdminUserModel(Object id, String name) {
        super(id, name);
        setRole("ADMIN");
    }
    private String email = "not provided";
    private String phoneNumber = "not provided";
    private long seed = System.currentTimeMillis();
}
