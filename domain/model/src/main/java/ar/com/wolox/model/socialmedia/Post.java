package ar.com.wolox.model.socialmedia;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
    private String userId;
    private String id;
    private String title;
    private String body;
}
