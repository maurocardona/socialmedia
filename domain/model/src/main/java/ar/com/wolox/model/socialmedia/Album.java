package ar.com.wolox.model.socialmedia;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Album {
    private String userId;
    private String id;
    private String title;
}
