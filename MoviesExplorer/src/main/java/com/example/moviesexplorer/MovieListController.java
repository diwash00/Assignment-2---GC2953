package com.example.moviesexplorer;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class MovieListController {

    @FXML
    private ScrollPane moviesScrollPane;

    @FXML
    private TextField searchField;

    @FXML
    private VBox moviesVBox;
    @FXML
    private Button loadMoreButton;
    @FXML
    private Button quit;

    private ObservableList<JsonNode> movieList;
    private FilteredList<JsonNode> filteredMovies;

    private int currentPage = 1;
    public void initialize() {
        movieList = FXCollections.observableArrayList();
        populateMovieList();
        setupSearch();
        loadMoreButton.setOnAction(event -> loadMoreMovies());

    }

    private void populateMovieList() {
        MovieApiService movieApiService = new MovieApiService();
        List<JsonNode> movies = movieApiService.getMovies();

        if (movies != null) {
            movieList.addAll(movies);

            HBox currentRow = new HBox();
            currentRow.setSpacing(10);
            currentRow.setPadding(new Insets(10));

            for (int i = 0; i < movieList.size(); i++) {
                JsonNode movie = movieList.get(i);
                HBox movieCard = createMovieCard(movie);
                System.out.println(movie.toString());
                currentRow.getChildren().add(movieCard);

                // Start a new row after every three cards
                if ((i + 1) % 4 == 0 || i == movieList.size() - 1) {
                    moviesVBox.getChildren().add(currentRow);
                    currentRow = new HBox();
                    currentRow.setSpacing(10);
                    currentRow.setPadding(new Insets(10));
                }
            }
        } else {
            System.out.println("Failed to retrieve movie data");
        }
    }

    private void setupSearch() {
        filteredMovies = new FilteredList<>(movieList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMovies.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Filter by title or any other property you want to include in the search
                return movie.get("title").asText().toLowerCase().contains(lowerCaseFilter);
            });
            updateMoviesView();
        });
    }

    private void updateMoviesView() {
        moviesVBox.getChildren().clear();

        HBox currentRow = new HBox();
        currentRow.setSpacing(10);
        currentRow.setPadding(new Insets(10));

        for (int i = 0; i < filteredMovies.size(); i++) {
            JsonNode movie = filteredMovies.get(i);
            HBox movieCard = createMovieCard(movie);

            currentRow.getChildren().add(movieCard);

            // Start a new row after every three cards
            if ((i + 1) % 4 == 0 || i == filteredMovies.size() - 1) {
                moviesVBox.getChildren().add(currentRow);
                currentRow = new HBox();
                currentRow.setSpacing(10);
                currentRow.setPadding(new Insets(10));
            }
        }
    }

    private ImageView createImageView(String imageUrl) {
        Image image = new Image("https://image.tmdb.org/t/p/w500" + imageUrl); // Assuming the base URL is "https://image.tmdb.org/t/p/w500"
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(196); // Adjust the width as needed
        imageView.setFitHeight(120); // Adjust the height as needed
        return imageView;
    }

    // Modify the createMovieCard method
    private HBox createMovieCard(JsonNode movie) {
        HBox cardContainer = new HBox();
        cardContainer.setSpacing(5);
        cardContainer.setPadding(new Insets(5));

        VBox card = new VBox();
        card.getStyleClass().add("movie-card");
        card.setMinWidth(200);
        card.setMaxWidth(200);
        card.setMinHeight(100);
        card.setStyle("-fx-background-color: brown;");

        ImageView imageView = createImageView(getPropertyValue(movie, "backdrop_path"));
        Label nameLabel = createLabel("Title: " + getPropertyValue(movie, "title"));
        Label genreLabel = createLabel("Original Language: " + getPropertyValue(movie, "original_language"));
        Label releaseDateLabel = createLabel("Release Date: " + getPropertyValue(movie, "release_date"));
        Label directorLabel = createLabel("Popularity: " + getPropertyValue(movie, "popularity"));
        Label earningLabel = createLabel("vote_average: " + getPropertyValue(movie, "vote_average"));

        // Add the image view to the card
        card.getChildren().add(imageView);

        // Add other labels to the card
        card.getChildren().addAll(nameLabel, genreLabel, releaseDateLabel, directorLabel, earningLabel);

        card.setOnMouseClicked(this::handleMovieCardClick);

        cardContainer.getChildren().add(card);
        return cardContainer;
    }

    private void handleMovieCardClick(MouseEvent event) {
        VBox card = (VBox) event.getSource();

        ImageView imageView = (ImageView) card.getChildren().get(0);
        Image image = imageView.getImage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Movie Details");

        // Create a VBox to hold the image and other details
        VBox alertContent = new VBox();
        alertContent.setSpacing(10);

        // Add the image to the VBox
        ImageView alertImageView = new ImageView(image);
        alertImageView.setPreserveRatio(true);
        alertImageView.setFitWidth(400);
        alertImageView.setFitHeight(240);
        alertContent.getChildren().add(alertImageView);

        // Add other details (you can customize this part)
        card.getChildren().stream()
                .skip(1)
                .map(label -> {
                    Label labelCopy = new Label(((Label) label).getText());
                    labelCopy.setStyle("-fx-text-fill: black;");
                    return labelCopy;
                })
                .forEach(alertContent.getChildren()::add);

        alert.getDialogPane().setContent(alertContent);
        alert.showAndWait();
    }



    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;");
        return label;
    }

    private String getPropertyValue(JsonNode movie, String propertyName) {
        JsonNode propertyNode = movie.get(propertyName);
        return (propertyNode != null) ? propertyNode.asText() : "";
    }




    @FXML
    private void loadMoreMovies() {
        currentPage++; // Increment the page number
        MovieApiService movieApiService = new MovieApiService();
        List<JsonNode> additionalMovies = movieApiService.getMoreMovies(currentPage);

        if (additionalMovies != null && !additionalMovies.isEmpty()) {
            movieList.addAll(additionalMovies);
            updateMoviesView();
        } else {
            loadMoreButton.setDisable(true);
            System.out.println("No more movies to load");
        }
    }

    @FXML
    private void quitPage(){
        System.exit(0);
    }
}
