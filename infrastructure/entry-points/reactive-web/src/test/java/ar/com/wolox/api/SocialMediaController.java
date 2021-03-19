package ar.com.wolox.api;
import ar.com.wolox.usecase.users.GetAllUsersUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SocialMediaController {

    private final GetAllUsersUseCase getAllUsersUseCase;

    @Autowired
    public SocialMediaController(GetAllUsersUseCase getAllUsersUseCase){
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @GetMapping("/users")
    public Flux<String> getRepairShops(){
        return getAllUsersUseCase.searchAllUsers();
    }
}
