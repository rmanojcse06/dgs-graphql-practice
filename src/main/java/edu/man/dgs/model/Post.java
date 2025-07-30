package edu.man.dgs.model;
import java.time.LocalDateTime;

@lombok.Data
@lombok.Builder
public class Post {
    String id;
    String title;
    String content;
    UserModel addedBy;
    LocalDateTime createdAt;
}
