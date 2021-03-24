package ar.com.wolox.model.socialmedia.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Photo {
    private String albumId;
    private String id;
    private String title;
    private String url;
    private String thumbnailUrl;
}
