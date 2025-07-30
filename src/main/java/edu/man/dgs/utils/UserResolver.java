package edu.man.dgs.utils;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsTypeResolver;
import edu.man.dgs.model.AdminUserModel;
import edu.man.dgs.model.GuestUserModel;
import edu.man.dgs.model.UserModel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@DgsComponent
public class UserResolver {

    @DgsTypeResolver(name = "UserModel")
    public String getType(UserModel usermodel) {
        log.info("Inside UserResolver.getType() ==> {} and {}", usermodel, usermodel.getClass().getName());
        if (usermodel instanceof AdminUserModel) {
            return "AdminUser";
        } else if (usermodel instanceof GuestUserModel) {
            return "GuestUser";
        }
        return "UserModel";
    }
}

