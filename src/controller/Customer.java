package controller;

public class Customer {
    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Customer> customers;

    public void initialize() {
        // Initialize country combo box
        countryComboBox.setItems(Country.getCountry());
        countryComboBox.getSelectionModel().selectFirst();

        // Initialize first level division combo box
        firstLevelDivisionComboBox.setItems(FirstLevelDivision.getAllFirstLevelDivisions(countryComboBox.getValue()));
        firstLevelDivisionComboBox.getSelectionModel().selectFirst();

        // Initialize table view
        customers = Customer.getAllCustomers();
        customerTableView.setItems(customers);

        // Add event listeners to buttons
        addButton.setOnAction(event -> {
            // Add a new customer record
            // ...
        });

        updateButton.setOnAction(event -> {
            // Update the selected customer record
            // ...
        });

        deleteButton.setOnAction(event -> {
            // Delete the selected customer record
            // ...
        });
    }
}
