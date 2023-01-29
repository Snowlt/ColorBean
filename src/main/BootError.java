package main;

import fit.simplification.Check;

/**
 * Application Boot Error
 *
 * @author Snow
 */
public class BootError extends Exception {
    private boolean canResume = false;

    public BootError(String message) {
        super(message);
    }

    public BootError(String message, boolean canResume, Throwable cause) {
        super(message, cause);
        this.canResume = canResume;
    }

    public boolean canResume() {
        return canResume;
    }

    @Override
    public void printStackTrace() {
        if (getCause() != null) getCause().printStackTrace();
        System.err.println("\n[Error] Failed to boot " + ColorBean.APPLICATION_NAME + ": " + getNoticeMessage());
    }

    public String getNoticeMessage() {
        String message = getMessage();
        return Check.notEmpty(message) ? message : " Unknown reason";
    }
}
