package control;

import view.MainFrame;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.gson.*;

import NET.Connection;

public class ClientControl {

    public static final int PORT = 1234;
    private final String HOST = "localhost";
    private Connection connection;
    private MainFrame view;

    public ClientControl() {
        connection = new Connection(HOST, PORT);
        start();
    }

    private void start() {
        view = new MainFrame(this);
    }

    public void closeConnection(){
        String petition = "EXIT";
        connection.send(petition);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    public void register(String userName, String password, String email, String location, String phoneNumber) {
        String userJson = "{"
                + "\"username\": \"" + userName + "\","
                + "\"password\": \"" + password + "\","
                + "\"email\": \"" + email + "\","
                + "\"location\": \"" + location + "\","
                + "\"phoneNumber\": \"" + phoneNumber + "\""
                + "}";
        connection.send("REGISTER:" + userJson);
        String response = connection.receive();
        validateRegisterResponse(response);
    }

    private void validateRegisterResponse(String response) {
        String[] parts = response.split(":");
        switch (parts[0]) {
            case "ERROR":
                view.couldNotRegister();
                break;
            default:
                view.registered();
                break;
        }
    }

    public void login(String email, String password) {
        String loginJson = "{" + "\"email\": \"" + email + "\","
                + "\"password\": \"" + password + "\""
                + "}";
        connection.send("LOGIN:" + loginJson);
        String response = connection.receive();
        validateLoginResponse(response);
    }

    private void validateLoginResponse(String response) {
        String[] parts = response.split(":");
        switch (parts[0]) {
            case "ERROR":
                view.couldNotLogIn();
                break;
            default:
                view.loggedIn(parts[1]);
                break;
        }
    }

    public void saveEmail(String email) {
        try {
            FileWriter writer = new FileWriter("resources/registro_email.txt", false);
            writer.write(email + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al escribir el email en el archivo: " + e.getMessage());
        }
    }

    public void search(String searchText) {
        String searchJson = "{" + "\"searchText\": \"" + searchText + "\""
                + "}";
        connection.send("SEARCH:" + searchJson);
        String listings = connection.receive();
        chargeListings(listings);
    }

    private String readEmail() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/registro_email.txt"))) {
            String email = reader.readLine();
            return email;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    public void deleteAccount() {
        String email = readEmail();
        connection.send("DELETE_ACC:" + email);
    }

    public void chargeListings(String listingsJson) {
        view.getMainPanel().clearListings();
        JsonArray array = JsonParser.parseString(listingsJson).getAsJsonArray();
        for (JsonElement element : array) {
            chargeOneListing(element);
        }
    }

    private void chargeOneListing(JsonElement listing) {
        JsonObject obj = listing.getAsJsonObject();
        String reference = obj.get("reference").getAsString();
        double price = obj.get("price").getAsDouble();
        int year = obj.get("year").getAsInt();
        int km = obj.get("km").getAsInt();
        String location = obj.get("location").getAsString();
        String id = obj.get("id").getAsString();
        Image carImage = decodeBase64(obj.get("image").getAsString());
        view.getMainPanel().addListing(reference, price, year, km, location, id, carImage);
    }

    private Image decodeBase64(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getMyListings() {
        view.getMyListingsPanel().clearListings();
        JsonObject listingJson = new JsonObject();
        listingJson.addProperty("email", readEmail());
        connection.send("GET_MY_LISTINGS:" + listingJson.toString());
        String myListingsJson = connection.receive();
        chargeMyListings(myListingsJson);
    }

    public void chargeMyListings(String myListingsJson) {
        JsonArray array = JsonParser.parseString(myListingsJson).getAsJsonArray();
        for (JsonElement element : array) {
            chargeOneMyListing(element);
        }
    }

    private void chargeOneMyListing(JsonElement listing) {
        JsonObject obj = listing.getAsJsonObject();
        String reference = obj.get("reference").getAsString();
        double price = obj.get("price").getAsDouble();
        int year = obj.get("year").getAsInt();
        int km = obj.get("km").getAsInt();
        String location = obj.get("location").getAsString();
        String id = obj.get("id").getAsString();
        Image carImage = decodeBase64(obj.get("image").getAsString());
        view.getMyListingsPanel().addListing(reference, price, year, km, location, id, carImage);
    }

    public void createListing(String brand, String line, String model, String mileage, String description, String price,
            String location, String vehicleType, File selectedImage) {
        JsonObject listingJson = new JsonObject();
        listingJson.addProperty("brand", brand);
        listingJson.addProperty("line", line);
        listingJson.addProperty("model", model);
        listingJson.addProperty("mileage", mileage);
        listingJson.addProperty("description", description);
        listingJson.addProperty("price", price);
        listingJson.addProperty("location", location);
        listingJson.addProperty("vehicleType", vehicleType);
        listingJson.addProperty("image", convertImageToBase64(selectedImage, 200, 150));
        listingJson.addProperty("email", readEmail());
        connection.send("ADD_LISTING:" + listingJson.toString());
    }

    public static String convertImageToBase64(File imageFile, int targetWidth, int targetHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedScaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedScaled.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedScaled, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getBrands() {
        String instruction = "GET_BRANDS";
        connection.send(instruction);
        String brandsJson = connection.receive();
        JsonObject obj = JsonParser.parseString(brandsJson).getAsJsonObject();
        JsonArray brandsArray = obj.getAsJsonArray("brands");
        List<String> brands = new ArrayList<>();
        for (JsonElement element : brandsArray) {
            brands.add(element.getAsString());
        }
        return brands;
    }

    public List<String> getLines(String brand) {
        String instruction = "GET_LINES: {\"brand\":\"" + brand + "\"}";
        connection.send(instruction);
        String linesJson = connection.receive();
        JsonObject obj = JsonParser.parseString(linesJson).getAsJsonObject();
        JsonArray linesArray = obj.getAsJsonArray("lines");
        List<String> lines = new ArrayList<>();
        for (JsonElement element : linesArray) {
            lines.add(element.getAsString());
        }
        return lines;
    }

    public List<String> getModels(String brand, String line) {
        String instruction = "GET_MODELS: {\"brand\":\"" + brand + "\",\"line\":\"" + line + "\"}";
        connection.send(instruction);
        String modelsJson = connection.receive();
        JsonObject obj = JsonParser.parseString(modelsJson).getAsJsonObject();
        JsonArray modelsArray = obj.getAsJsonArray("models");
        List<String> models = new ArrayList<>();
        for (JsonElement element : modelsArray) {
            models.add(element.getAsString());
        }
        return models;
    }

    public void deleteListing(String id) {
        JsonObject listingJson = new JsonObject();
        listingJson.addProperty("id", id);
        listingJson.addProperty("email", readEmail());
        connection.send("DELETE_LISTING:" + listingJson.toString());
    }

    public void filterByPrice(Integer maxPrice, Integer minPrice) {
        JsonObject listingJson = new JsonObject();
        listingJson.addProperty("maxPrice", maxPrice);
        listingJson.addProperty("minPrice", minPrice);
        connection.send("FILTER_PRICE:" + listingJson.toString());
        String filteredListingsJson = connection.receive();
        chargeListings(filteredListingsJson);
    }

    public void filterByModel(Integer maxModel, Integer minModel) {
        JsonObject listingJson = new JsonObject();
        listingJson.addProperty("maxModel", maxModel);
        listingJson.addProperty("minModel", minModel);
        connection.send("FILTER_MODEL:" + listingJson.toString());
        String filteredListingsJson = connection.receive();
        chargeListings(filteredListingsJson);
    }
}
