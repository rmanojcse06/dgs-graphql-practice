package edu.man.dgs.model;
import com.netflix.graphql.dgs.DgsDefaultTypeResolver;
import edu.man.dgs.utils.UserResolver;
import lombok.*;

@Data
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(exclude = {"email"})
public class UserModel {
    public UserModel(Object id, String name) {
        this.id = String.valueOf(id);
        this.name = name;
    }
    String id = "not provided";
    String name = "not provided";
    String email = "not provided";
    String role = "NOROLE";
}
