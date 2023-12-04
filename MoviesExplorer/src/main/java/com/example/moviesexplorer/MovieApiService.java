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

    private static final String API_KEY = "805c3d99d14a2b31b9942d04a3b1719e";
    private static final String TMDB_API_URL = "https://api.themoviedb.org/3/discover/movie";

    public List<JsonNode> getMovies() {
        try {
            URL url = new URL(TMDB_API_URL + "?api_key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return parseMovieResponse(response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<JsonNode> parseMovieResponse(String jsonResponse) {
        List<JsonNode> movies = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode resultsNode = objectMapper.readTree(jsonResponse).get("results");

            for (JsonNode movieNode : resultsNode) {
                movies.add(movieNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<JsonNode> getMoreMovies(int currentPage) {
        try {
            URL url = new URL(TMDB_API_URL + "?api_key=" + API_KEY + "&page=" + currentPage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return parseMovieResponse(response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
