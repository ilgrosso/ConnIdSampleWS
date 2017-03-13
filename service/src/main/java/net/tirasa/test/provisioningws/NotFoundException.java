package net.tirasa.test.provisioningws;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = -3373554342587710469L;

    public NotFoundException(final String initials) {
        super("Could not find user with initial " + initials);
    }

}
