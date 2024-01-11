package ru.markov.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import ru.markov.application.security.SecurityService;

public class GuestLayout extends AppLayout {
    private final SecurityService securityService;
    public GuestLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        addClassName("main-layout-app-layout-1");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        addToDrawer(horizontalLayout);
    }

    private void createHeader() {
        H1 logo = new H1("BrigApp");
        logo.addClassName("main-layout-h1-1");
        Tabs tabs = getTabs();
        addToDrawer(tabs);
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        Button theme = new Button("Переключить тему", new Icon(VaadinIcon.ADJUST), buttonClickEvent -> {
            ThemeList t = UI.getCurrent().getElement().getThemeList();
            if (t.contains(Lumo.DARK)){
                t.remove(Lumo.DARK);}
            else{
                t.add(Lumo.DARK);
            }
        });
        Button logout = new Button("Выйти ", new Icon(VaadinIcon.EXIT), e -> securityService.logout());
        //<theme-editor-local-classname>
        logout.addClassName("main-layout-button-1");
        logout.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logout.addClassName("main-layout-button-1");

        var header = new HorizontalLayout(new DrawerToggle(), logo, theme, logout);
        header.addClassName("main-layout-horizontal-layout-1");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("app-header");
        addToNavbar(header);
    }
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.EYE, "Текущий план", new RouterLink(TechGuest.class)));
        tabs.add(createTab(VaadinIcon.ABSOLUTE_POSITION, "Рассадка техников", new RouterLink(WhoNext.class)));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
    private Tab createTab(VaadinIcon viewIcon, String viewName, RouterLink routerLink) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-xs)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        routerLink.add(icon, new Span(viewName));
        routerLink.setTabIndex(0);
        return new Tab(routerLink);
    }


}
