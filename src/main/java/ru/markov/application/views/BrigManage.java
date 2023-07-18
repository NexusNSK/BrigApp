package ru.markov.application.views;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "brigmanage", layout = MainLayout.class)
@PageTitle("Управление бригадой | BrigApp")
public class BrigManage extends Div {

}