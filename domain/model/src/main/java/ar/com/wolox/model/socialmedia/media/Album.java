package ar.com.wolox.model.socialmedia.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Album {
    private String userId;
    private String id;
    private String title;
}
