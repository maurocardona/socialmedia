package ar.com.wolox.model.socialmedia.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder(toBuilder = true)
public class SharedAlbum {
    @Id
    private String id;
    private String ownerUserId;
    private String albumId;
    private Guest guest;
}
