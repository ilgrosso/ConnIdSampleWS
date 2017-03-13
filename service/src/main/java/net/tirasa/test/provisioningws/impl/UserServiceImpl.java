package net.tirasa.test.provisioningws.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tirasa.test.provisioningws.NotFoundException;
import net.tirasa.test.provisioningws.User;
import net.tirasa.test.provisioningws.UserService;

public class UserServiceImpl implements UserService {

    private static final Map<String, User> USERS = new HashMap<>();

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 1977);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 8);
        User mr = new User();
        mr.setInitials("MR");
        mr.setFirstname("Mario");
        mr.setSurname("Rossi");
        mr.setBirhdate(calendar.getTime());
        USERS.put(mr.getInitials(), mr);

        calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 1977);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        User fb = new User();
        fb.setInitials("FB");
        fb.setFirstname("Filippo");
        fb.setSurname("Bianchi");
        fb.setBirhdate(calendar.getTime());
        USERS.put(fb.getInitials(), fb);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(USERS.values());
    }

    @Override
    public User getUser(final String initials) throws NotFoundException {
        if (USERS.containsKey(initials)) {
            return USERS.get(initials);
        }
        throw new NotFoundException(initials);
    }

}
