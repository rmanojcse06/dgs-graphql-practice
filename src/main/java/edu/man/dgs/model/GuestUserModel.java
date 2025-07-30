package edu.man.dgs.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true, exclude = {"email"})
@RequiredArgsConstructor
public class GuestUserModel extends UserModel {
    public GuestUserModel(Object id, String name) {
        super(id, name);
        setRole("GUEST");
    }
    private String email = "not provided";
    private String phoneNumber = "not provided";
}