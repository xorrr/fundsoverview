package org.xorrr.fundsoverview.login;

import org.xorrr.fundsoverview.EnvironmentVariables;
import org.xorrr.fundsoverview.di.Module;
import org.xorrr.fundsoverview.eventbus.EventBus;
import org.xorrr.fundsoverview.eventbus.NotificationEventHandler;
import org.xorrr.fundsoverview.eventbus.events.WrongCredentialsEvent;
import org.xorrr.fundsoverview.events.LoggedInEvent;
import org.xorrr.fundsoverview.util.SessionAttributes;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.server.VaadinSession;

public class UserServiceImpl implements UserService {

    private EventBus bus;
    private Injector injector;

    @Inject
    public UserServiceImpl(EventBus bus) {
        this.bus = bus;
        this.injector = Guice.createInjector(new Module());
        bus.addHandler(injector.getInstance(NotificationEventHandler.class));
    }

    @Override
    public void login(String username, String password) {
        if (credentialsAreCorrect(username, password)) {
            VaadinSession.getCurrent().setAttribute(SessionAttributes.USERNAME,
                    EnvironmentVariables.USER); // works currently because only
                                                // one user exists!
            bus.fireEvent(new LoggedInEvent());
        }
        else 
            bus.fireEvent(new WrongCredentialsEvent());
    }

    private boolean credentialsAreCorrect(String username, String password) {
        return isUsernameCorrect(username) && isPasswordCorrect(password);
    }

    private boolean isPasswordCorrect(String password) {
        return password.equals(System.getenv(EnvironmentVariables.PASS));
    }

    private boolean isUsernameCorrect(String username) {
        return username.equals(System.getenv("USER"));
    }
}
