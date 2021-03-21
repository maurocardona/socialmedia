package ar.com.wolox.model.socialmedia;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Photo {
    private String albumId;
    private String id;
    private String title;
    private String url;
    private String thumbnailUrl;
}
