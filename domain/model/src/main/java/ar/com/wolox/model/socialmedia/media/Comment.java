package ar.com.wolox.model.socialmedia.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {
    private String postId;
    private String id;
    private String name;
    private String email;
    private String body;
}
