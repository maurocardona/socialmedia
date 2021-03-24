package ar.com.wolox.usecase.comments;

import ar.com.wolox.model.socialmedia.gateways.SocialMediaRepository;
import ar.com.wolox.model.socialmedia.media.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetCommentsUseCase {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    public Flux<Comment> getCommentsByCriteria(Optional<String> name, Optional<String> userId){
        Flux<Comment> comments = socialMediaRepository.searchAllComments();
        return name.map(nameCriteria ->
                        comments.collectList().map(allComments -> allComments.stream()
                                .filter(comment -> comment.getName().toLowerCase().contains(nameCriteria.toLowerCase()))
                                .collect(Collectors.toList())
                        ))
                .orElse(userId.map(userIdCriteria -> Mono.zip(
                        socialMediaRepository.searchPostByUserId(userIdCriteria).collectList(),
                        comments.collectList(),
                        (posts, allComments) -> allComments.stream()
                                .filter(comment -> posts.stream()
                                        .anyMatch(post -> comment.getPostId().equals(post.getId())))
                                .collect(Collectors.toList())))
                        .orElse(comments.collectList()))
                .flatMapMany(Flux::fromIterable);
    }
}