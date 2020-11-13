package SMO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MainScreenController implements PropertyChangeListener {
    @FXML
    private VBox VBSources;
    @FXML
    private VBox VBDevices;
    @FXML
    private TextField TFBuffer;
    @FXML
    private TextField TFTotalRequests;
    @FXML
    private TabPane TPMainPane;
    @FXML
    private Tab TStepByStep;
    @FXML
    private Tab TResults;
    @FXML
    private TableView<EventCalendar> TVCalendar;
    @FXML
    private TableColumn<EventCalendar, String> TCEventType;
    @FXML
    private TableColumn<EventCalendar, Double> TCEventTime;
    @FXML
    private TableColumn<EventCalendar, Request> TCEventRequest;
    @FXML
    private TableColumn<EventCalendar, Integer> TCSign;
    @FXML
    private TableView<BufferElement> TVBuffer;
    @FXML
    private TableColumn<BufferElement, Integer> TCBufferNumber;
    @FXML
    private TableColumn<BufferElement, Request> TCBufferRequest;
    @FXML
    private TableColumn<BufferElement, Double> TCRequestTime;
    @FXML
    private CheckBox CBStepByStep;
    @FXML
    private TableView<Source> TVSourceResults;
    @FXML
    private TableColumn<Source, Integer> TCSourceNum;
    @FXML
    private TableColumn<Source, Integer> TCTotalAmount;
    @FXML
    private TableColumn<Source, Double> TCDeniedProb;
    @FXML
    private TableColumn<Source, Double> TCTPreb;
    @FXML
    private TableColumn<Source, Double> TCTWaiting;
    @FXML
    private TableColumn<Source, Double> TCTService;
    @FXML
    private TableColumn<Source, Double> TCWaitingDisp;
    @FXML
    private TableColumn<Source, Double> TCServiceDisp;
    @FXML
    private TableView<Device> TVDeviceResults;
    @FXML
    private TableColumn<Device, Integer> TCDeviceNum;
    @FXML
    private TableColumn<Device, Double> TCUtilizationRate;

    private ObservableList<EventCalendar> eventList;
    private Calculations calc;

    public MainScreenController() {
        eventList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        TStepByStep.setDisable(true);
        TResults.setDisable(true);

        TCEventType.setCellValueFactory(new PropertyValueFactory<>("type"));
        TCEventTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        TCEventRequest.setCellValueFactory(new PropertyValueFactory<>("request"));
        TCSign.setCellValueFactory(new PropertyValueFactory<>("sign"));

        TCBufferNumber.setCellValueFactory(new PropertyValueFactory<>("num"));
        TCBufferRequest.setCellValueFactory(new PropertyValueFactory<>("request"));
        TCRequestTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));

        TCSourceNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        TCTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        TCDeniedProb.setCellValueFactory(new PropertyValueFactory<>("deniedProb"));
        TCTPreb.setCellValueFactory(new PropertyValueFactory<>("avgTPreb"));
        TCTWaiting.setCellValueFactory(new PropertyValueFactory<>("avgTWait"));
        TCTService.setCellValueFactory(new PropertyValueFactory<>("avgServiceTime"));
        TCWaitingDisp.setCellValueFactory(new PropertyValueFactory<>("TWaitDispersion"));
        TCServiceDisp.setCellValueFactory(new PropertyValueFactory<>("TServiceDispersion"));

        TCDeviceNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        TCUtilizationRate.setCellValueFactory(new PropertyValueFactory<>("utilizationRate"));

        TVCalendar.setRowFactory(new Callback<TableView<EventCalendar>, TableRow<EventCalendar>>() {
            @Override
            public TableRow<EventCalendar> call(TableView<EventCalendar> param) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(EventCalendar item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("(even)-fx-background-color: white");
                        setStyle("(odd)-fx-background-color: #f9f9f9");

                        if (item == null) {
                            return;
                        }
                        EventCalendar elem = getEventMinTime();

                        if (elem.equals(item)) {
                            System.out.println("equals!");
                            setStyle("-fx-background-color: lightblue");
                        }

                    }
                };
            }
        });

        TCEventRequest.setCellFactory(new Callback<TableColumn<EventCalendar, Request>, TableCell<EventCalendar, Request>>() {
            @Override
            public TableCell<EventCalendar, Request> call(TableColumn<EventCalendar, Request> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Request item, boolean empty) {
                        super.updateItem(item, empty);
                        String text = "";
                        if (item != null) {
                            text = item.toString();
                        }
                        setText(text);
                    }
                };
            }
        });

        TCBufferRequest.setCellFactory(new Callback<TableColumn<BufferElement, Request>, TableCell<BufferElement, Request>>() {
            @Override
            public TableCell<BufferElement, Request> call(TableColumn<BufferElement, Request> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Request item, boolean empty) {
                        super.updateItem(item, empty);
                        String text = "";
                        if (item != null) {
                            text = item.toString();
                        }
                        setText(text);
                    }
                };
            }
        });
    }

    @FXML
    public void addSourceClicked() {
        TextField temp = new TextField();
        temp.setPromptText("enter lambda");
        VBSources.getChildren().add(temp);
    }

    @FXML
    public void addDeviceClicked() {
        TextField temp = new TextField();
        temp.setPromptText("enter tau");
        VBDevices.getChildren().add(temp);
    }

    @FXML
    public void delSourceClicked() {
        if (!VBSources.getChildren().isEmpty()) {
            VBSources.getChildren().remove(VBSources.getChildren().size() - 1);
        }
    }

    @FXML
    public void delDeviceClicked() {
        if (!VBDevices.getChildren().isEmpty()) {
            VBDevices.getChildren().remove(VBDevices.getChildren().size() - 1);
        }
    }

    @FXML
    public void startClicked() {
        ArrayList<Double> sources = getVBoxInput(VBSources);
        System.out.println(sources);

        ArrayList<Double> devices = getVBoxInput(VBDevices);
        System.out.println(devices);

        if (sources == null || devices == null) {
            return;
        }

        if (CBStepByStep.isSelected()) {
            TStepByStep.setDisable(false);
        }
        TResults.setDisable(false);

        String bufferText = TFBuffer.getText();
        String requestsText = TFTotalRequests.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Wrong input");
        if (bufferText.isEmpty() || requestsText.isEmpty()) {
            alert.setContentText("Some values are missing!");
            alert.showAndWait();
            return;
        }
        int bufferSize;
        int totalRequests;
        try {
            bufferSize = Integer.parseInt(bufferText);
            totalRequests = Integer.parseInt(requestsText);
        } catch (NumberFormatException ex) {
            alert.setContentText("Wrong input type! You should enter Float numbers!");
            alert.showAndWait();
            return;
        }

        Object monitor = new Object();
        calc = new Calculations(sources, devices, bufferSize, totalRequests, monitor, CBStepByStep.isSelected());
        calc.addListener(this);
        calc.start();
        System.out.println("started!");

        SingleSelectionModel<Tab> selectionModel = TPMainPane.getSelectionModel();
        if (CBStepByStep.isSelected()) {
            selectionModel.select(TStepByStep);
        } else {
            selectionModel.select(TResults);
        }
    }

    @FXML
    public void nextButtonClicked() {
        calc.nextStep();
    }

    @FXML
    public void skipClicked() {
        calc.setStepsFlag(false);
        calc.nextStep();
        SingleSelectionModel<Tab> selectionModel = TPMainPane.getSelectionModel();
        selectionModel.select(TResults);
    }

    private EventCalendar getEventMinTime() {
        double min = Double.MAX_VALUE;
        EventCalendar minEvent = null;
        for (EventCalendar elem: eventList) {
            if (elem.getTime() < min && elem.getSign() == 0) {
                min = elem.getTime();
                minEvent = elem;
            }
        }
        return minEvent;
    }

    private ArrayList<Double> getVBoxInput(VBox vbox) {
        ObservableList<Node> nodes = vbox.getChildren();
        ArrayList<Double> list = new ArrayList<>();
        for (Node node: nodes) {
            TextField textField = new TextField();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Wrong input");
            if (node.getClass() != textField.getClass()) {
                alert.setContentText("Wrong nodes in VBox!");
                alert.showAndWait();
                return null;
            }
            textField = (TextField) node;
            String text = textField.getText();
            if (text.isEmpty()) {
                alert.setContentText("Enter values!");
                alert.showAndWait();
                return null;
            }
            double lam;
            try {
                lam = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                alert.setContentText("Wrong input type! You should enter Float numbers!");
                alert.showAndWait();
                return null;
            }

            list.add(lam);
        }
        return list;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        //System.out.println("change started");
        if (property.equals("stepFlag")) {
            ArrayList<Source> sources = calc.getStartSources();
            ArrayList<Device> devices = calc.getStartDevices();
            Buffer buffer = calc.getStartBuffer();
            eventList.clear();
            //TVCalendar.setStyle("-fx-background-color: white");

            for (int i = 0; i < sources.size(); i++) {
                Request r = sources.get(i).getCurrRequest();
                eventList.add(new EventCalendar("source " + i, r.gettPost(), r, 0));
            }

            for (int i = 0; i < devices.size(); i++) {
                Device d = devices.get(i);
                eventList.add(new EventCalendar("device " + i, d.gettOsv(), d.getCurrRequest(), d.getSign()));
            }

            TVCalendar.setItems(eventList);
            //TVCalendar.sort();

            ObservableList<BufferElement> observableBuffer = FXCollections.observableArrayList();
            observableBuffer.addAll(buffer.getElements());
            TVBuffer.setItems(observableBuffer);
        } else if (property.equals("resultFlag")) {
            ArrayList<Source> sources = calc.getSources();
            ArrayList<Device> devices = calc.getDevices();

            ObservableList<Source> observableSources = FXCollections.observableArrayList(sources);
            ObservableList<Device> observableDevices = FXCollections.observableArrayList(devices);

            TVSourceResults.setItems(observableSources);
            TVDeviceResults.setItems(observableDevices);
        }

    }
}

