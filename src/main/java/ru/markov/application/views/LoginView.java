package ru.markov.application.views;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;



@Route("login")
@PageTitle("BrigApp א Вход")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();


	public LoginView(){
		addClassName("login-view");
		getStyle().set("background-color", "var(--lumo-contrast-0pct)")
				.set("display", "flex").set("justify-content", "center")
				.set("padding", "var(--lumo-space-l)");
		login.setForgotPasswordButtonVisible(false);
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.START);
		login.setAction("login");
		login.getElement().setAttribute("no-autofocus", "");




		add(new H1("BrigApp"), login);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if(beforeEnterEvent.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
	}
}