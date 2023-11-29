package com.smk.view;

import com.smk.MainView;
import com.smk.dao.LocationDao;
import com.smk.dao.ScheduleDao;
import com.smk.model.Location;
import com.smk.model.Schedule;
import com.smk.model.dtd.ScheduleDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.*;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

@PageTitle("Create Booking")
@Route(value = "create-booking", layout = MainView.class)
public class CreateBooking extends VerticalLayout {
    private LocationDao locationDao;
    private final ScheduleDao scheduleDao;
    public CreateBooking() {
        locationDao = new LocationDao();
        scheduleDao = new ScheduleDao();
        createForm();
    }

    private void createForm(){
        setAlignItems(Alignment.STRETCH);
        ComboBox<Location> fromComboBox = new ComboBox<>("Dari");
        fromComboBox.setItems(locationDao.getAll());
        fromComboBox.setItemLabelGenerator(Location::getName);

        ComboBox<Location> toCombobox = new ComboBox<>("ke");
        fromComboBox.setItems(locationDao.getAll());
        fromComboBox.setItemLabelGenerator(Location::getName);

        DatePicker departureDatePicker = new DatePicker("Tanggal keberangkatan");
        DatePicker arrivalDatePicker = new DatePicker("Tanggal kepulangan");
        Button searchButton =new Button("Search");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(fromComboBox, toCombobox, departureDatePicker, arrivalDatePicker, searchButton);

        Grid<ScheduleDTO> grid = new Grid<>(ScheduleDTO.class, false);
        grid.addColumn(ScheduleDTO::getId).setHeader("Id");
        grid.addColumn(ScheduleDTO::getFlightNumber).setHeader("Nomor pesawat");
        grid.addColumn(ScheduleDTO::getDepartureLocation).setHeader("Keberangkatan");
        grid.addColumn(ScheduleDTO::getArrivalLocation).setHeader("kedatangan");
        grid.addColumn(ScheduleDTO::getDepartureDate).setHeader("Waktu keberangkatan");

        add(fromComboBox,toCombobox,departureDatePicker,arrivalDatePicker,searchButton,grid);
        searchButton.addClickListener(ClickEvent -> {
            Collection<ScheduleDTO> scheduleDTOCollection = scheduleDao.searchSchedule(
                    fromComboBox.getValue().getId(),
                    toCombobox.getValue().getId(),
                    Date.from(departureDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            grid.setItems(scheduleDTOCollection);
        });
    }


}
