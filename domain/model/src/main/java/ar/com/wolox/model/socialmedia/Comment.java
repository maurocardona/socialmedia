package ar.com.wolox.model.socialmedia;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    private String postId;
    private String id;
    private String name;
    private String email;
    private String body;
}
