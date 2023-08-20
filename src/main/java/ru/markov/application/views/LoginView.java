package ru.markov.application.views;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
		login.setForgotPasswordButtonVisible(false);
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		//setJustifyContentMode(JustifyContentMode.START);
		login.setAction("login");
		Image img = new Image("images/alef.png", "");
		img.setWidth("300px");


		add(new H1("BrigApp"), img, login);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(false);
        }
	}
}