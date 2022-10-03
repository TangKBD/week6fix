package com.example.week6fix;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value= "/mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullName, dollars;
    private ComboBox position, school, house;
    private RadioButtonGroup<String> radio_sex;

    private Button b_back, b_create, b_update, b_delete, b_go;

    private HorizontalLayout panel;

    private Wizards wizards;

    private int index_person;

    public MainWizardView() {
        wizards = new Wizards();
        panel = new HorizontalLayout();
        fullName = new TextField("Fullname");
        dollars = new TextField("Dollars");
        position = new ComboBox<>();
        position.setLabel("Position");
        school = new ComboBox<>();
        school.setLabel("School");
        house = new ComboBox<>();
        house.setLabel("House");
        radio_sex = new RadioButtonGroup<>();
        radio_sex.setLabel("Gender :");
        radio_sex.setItems("m", "f");
        dollars.setPrefixComponent(new Span("$"));

        position.setItems("Student", "Teacher");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");

        b_back = new Button("<<<");
        b_create = new Button("Create");
        b_update = new Button("Update");
        b_delete = new Button("Delete");
        b_go = new Button(">>>");

        panel.add(b_back, b_create, b_update, b_delete, b_go);

        this.add(fullName, radio_sex, position, dollars, school, house, panel);

        data_load();

//        this.wizards.model = WebClient.create().get()
//                .uri("http://127.0.0.1:8080/wizards")
//                .retrieve().bodyToMono(ArrayList.class).block();


        b_back.addClickListener(event ->{
            if(index_person - 1 < 0){
                index_person = 0;
                show_data();
            }
            else {
                index_person -= 1;
                show_data();
            }
        });

        b_go.addClickListener(event ->{
            if(index_person + 1 >= this.wizards.model.size()){
                index_person = this.wizards.model.size() - 1;
                show_data();
            }
            else{
                index_person += 1;
                show_data();
            }
        });

        b_create.addClickListener(event -> {
            Wizard out = WebClient.create().post().uri("http://127.0.0.1:8080/addWizard?sex="+ radio_sex.getValue() +"&name="+ fullName.getValue() +"&position="+position.getValue() +"&dollars="+ dollars.getValue() +"&school="+school.getValue()+"&house=" + house.getValue())
                    .retrieve().bodyToMono(Wizard.class).block();
            Notification noti = Notification.show("Wizard Has Been Create");
            noti.setPosition(Notification.Position.BOTTOM_START);
            data_load();
        });

        b_update.addClickListener(event -> {
            boolean out = WebClient.create().post().uri("http://127.0.0.1:8080/updateWizard?sex="
                            + radio_sex.getValue() +"&name="+ fullName.getValue() +"&position="
                            +position.getValue() +"&dollars="+ dollars.getValue() +"&school="
                            +school.getValue()+"&house=" + house.getValue() + "&old_name="
                            + this.wizards.model.get(index_person).getName())
                    .retrieve().bodyToMono(Boolean.class).block();
            Notification noti = Notification.show("Wizard Has Been Update");
            noti.setPosition(Notification.Position.BOTTOM_START);
            data_load();
        });

        b_delete.addClickListener(event -> {
            boolean out = WebClient.create().post()
                    .uri("http://127.0.0.1:8080/deleteWizard?name="
                            + this.wizards.model.get(index_person).getName()).retrieve()
                    .bodyToMono(Boolean.class).block();
            Notification noti = Notification.show("Wizard Has Been Delete");
            noti.setPosition(Notification.Position.BOTTOM_START);
            index_person = 0;
            data_load();
            show_data();
        });
    }

    private void show_data(){
        if (this.wizards.model.size() != 0){
            this.fullName.setValue(this.wizards.model.get(index_person).getName());
            this.dollars.setValue(this.wizards.model.get(index_person).getMoney());
            this.position.setValue(this.wizards.model.get(index_person).getPosition());
            this.school.setValue(this.wizards.model.get(index_person).getSchool());
            this.house.setValue(this.wizards.model.get(index_person).getHouse());
            this.radio_sex.setValue(this.wizards.model.get(index_person).getSex());

        }
        else{
            this.fullName.setValue("");
            this.dollars.setValue("");
            this.position.setValue("");
            this.school.setValue("");
            this.house.setValue("");
            this.radio_sex.setValue("m");
        }
    }
    private void data_load(){
        this.wizards.model = WebClient.create().get()
                .uri("http://127.0.0.1:8080/wizards")
                .retrieve().bodyToMono(new ParameterizedTypeReference<ArrayList<Wizard>>() {}).block();

    }
}

