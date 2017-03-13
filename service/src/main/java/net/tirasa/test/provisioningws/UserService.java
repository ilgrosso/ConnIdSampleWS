package net.tirasa.test.provisioningws;

import java.util.List;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface UserService {

    List<User> getUsers();

    User getUser(@WebParam(name = "initials") String initials) throws NotFoundException;
}
