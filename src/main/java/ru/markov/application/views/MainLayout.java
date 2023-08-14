package ru.markov.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
@PermitAll
@UIScope

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        addClassName("main-layout-app-layout-1");
    }
    private void createHeader() {
        H1 logo = new H1("BrigApp");
        Tabs tabs = getTabs();
        addToDrawer(tabs);
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Выйти " + u, new Icon(VaadinIcon.EXIT), e -> securityService.logout());
        logout.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logout.addClassName("main-layout-button-1");

        Button theme = new Button("Переключить тему", new Icon(VaadinIcon.ADJUST), buttonClickEvent -> {
            ThemeList t = UI.getCurrent().getElement().getThemeList();
            if (t.contains(Lumo.DARK)){
                t.remove(Lumo.DARK);}
            else{
                t.add(Lumo.DARK);
                }
        });

        var header = new HorizontalLayout(new DrawerToggle(), logo, theme, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.USER_HEART,"Редактор бригады", new RouterLink(GridEdit.class)),
                createTab(VaadinIcon.TIMER, "Учёт времени", new RouterLink(WorkTime.class)),
                createTab(VaadinIcon.FILE_TABLE, "Отчёты", new RouterLink(Reports.class)));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
    private Tab createTab(VaadinIcon viewIcon, String viewName, RouterLink routerLink) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        routerLink.add(icon, new Span(viewName));
        routerLink.setTabIndex(-1);
        return new Tab(routerLink);
    }

}