package com.example.moviesexplorer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieApiService {

    // API key for accessing The Movie Database (TMDb) API
    private static final String API_KEY = "805c3d99d14a2b31b9942d04a3b1719e";

    // The base URL for TMDb API to discover movies
    private static final String TMDB_API_URL = "https://api.themoviedb.org/3/discover/movie";

    // Method to get a list of movies from TMDb
    public List<JsonNode> getMovies() {
        try {
            // Construct the URL for the API request
            URL url = new URL(TMDB_API_URL + "?api_key=" + API_KEY);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (HTTP_OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the API response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse and return the list of movies
                return parseMovieResponse(response.toString());
            } else {
                // Print an error message if the request was not successful
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to parse the JSON response and extract the list of movies
    private List<JsonNode> parseMovieResponse(String jsonResponse) {
        List<JsonNode> movies = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Extract the "results" node from the JSON response
            JsonNode resultsNode = objectMapper.readTree(jsonResponse).get("results");

            // Add each movie node to the list
            for (JsonNode movieNode : resultsNode) {
                movies.add(movieNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    // Method to get more movies based on the specified page number
    public List<JsonNode> getMoreMovies(int currentPage) {
        try {
            // Construct the URL for the API request with the specified page number
            URL url = new URL(TMDB_API_URL + "?api_key=" + API_KEY + "&page=" + currentPage);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (HTTP_OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the API response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse and return the list of movies
                return parseMovieResponse(response.toString());
            } else {
                // Print an error message if the request was not successful
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
