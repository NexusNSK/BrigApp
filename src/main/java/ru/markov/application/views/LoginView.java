package ru.markov.application.views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
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
	Button visitorMode = new Button("Режим гостя", new Icon(VaadinIcon.EYE));



	public LoginView(){
		LoginI18n login18n = LoginI18n.createDefault();

		LoginI18n.Header login18nHeader = new LoginI18n.Header();
		login18nHeader.setTitle("BrigApp");
		login18n.setHeader(login18nHeader);

		LoginI18n.Form i18nForm = login18n.getForm();
		i18nForm.setTitle("Авторизация");
		i18nForm.setUsername("Логин:");
		i18nForm.setPassword("Пароль:");
		i18nForm.setSubmit("Войти");
		i18nForm.setForgotPassword("Гостевой режим");
		login18n.setForm(i18nForm);

		addClassName("login-view");
		getStyle().set("background-color", "var(--lumo-contrast-100pct)")
				.set("display", "flex").set("justify-content", "center")
				.set("padding", "var(--lumo-space-l)");
		login.setForgotPasswordButtonVisible(false);
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.AROUND);
		login.setAction("login");
		login.getElement().setAttribute("no-autofocus", "");
		visitorMode.addClickListener(event -> visitorMode.getUI().ifPresent(ui -> ui.navigate("plan")));
		LoginOverlay loginOverlay = new LoginOverlay();
		loginOverlay.setI18n(login18n);
		loginOverlay.setAction("login");
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		loginOverlay.addForgotPasswordListener(event -> visitorMode.getUI().ifPresent(ui -> ui.navigate("plan")));



		add(/*new H1("BrigApp"),*/loginOverlay /*/login*/, visitorMode);
		//<theme-editor-local-classname>
				loginOverlay.setOpened(true);
		addClassName("login-view-vertical-layout-1");
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