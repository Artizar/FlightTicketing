package com.smk.view;

import com.smk.MainView;
import com.smk.dao.BookingDao;
import com.smk.dao.LocationDao;
import com.smk.dao.ScheduleDao;
import com.smk.model.Booking;
import com.smk.model.Location;
import com.smk.model.Schedule;
import com.smk.model.dtd.ScheduleDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@PageTitle("Create Booking")
@Route(value = "create-booking", layout = MainView.class)
public class CreateBooking extends VerticalLayout {
    private LocationDao locationDao;
    private final ScheduleDao scheduleDao;

    private static final BookingDao bookingDao = new BookingDao();
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

    private static class CreateBookingFormlayout extends FormLayout{
        private final TextField idTextField = new TextField();
        private final TextField fromTextField = new TextField("Dari");
        private final TextField toTextField = new TextField("ke");
        private  final DatePicker departureDatePicker = new DatePicker("Tanggal keberangkatan");
        private final TextField nameTextField = new TextField("Nama");
        private final TextField priceTextField = new TextField("Harga");
        private final Button saveBooking = new Button("save");

        public CreateBookingFormlayout() {
            idTextField.setVisible(false);
            add(idTextField);
            fromTextField.setReadOnly(true);
            toTextField.setReadOnly(true);
            departureDatePicker.setReadOnly(true);
            Stream.of(fromTextField, toTextField,departureDatePicker, nameTextField,priceTextField,saveBooking).forEach(this::add);
            saveBooking.addClickListener(ClickEvent->{
                Booking booking = new Booking();
                booking.setScheduleId(Long.parseLong(idTextField.getValue()));
                booking.setName(nameTextField.getValue());
                booking.setPrice(Double.parseDouble(priceTextField.getValue()));
                Optional<Schedule> id = bookingDao.save(booking);
            });
            getId().ifPresent(interger ->{
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setText("booking created with id = " + interger);
                confirmDialog.setCancelable(false);
                confirmDialog.setRejectable(false);
                confirmDialog.setConfirmText("ok");
                confirmDialog.addConfirmListener(event -> {
                    confirmDialog.close();
                });
                add(confirmDialog);
                confirmDialog.open();
            });
        }
        public void setScheduleDTO(ScheduleDTO scheduleDTO){
            idTextField.setValue(String.valueOf(scheduleDTO.getId()));
            fromTextField.setValue(scheduleDTO.getDepartureLocation());
            toTextField.setValue(scheduleDTO.getArrivalLocation());
            departureDatePicker.setValue(LocalDate.parse(scheduleDTO.getDepartureLocation()));
        }
    }

}