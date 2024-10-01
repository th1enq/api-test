import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class main {

    private static final String API_KEY = "AIzaSyA1oZfoquk50HHMqNSnT4Gew8wrxUvIfDA";  // Thay YOUR_API_KEY bằng API key của bạn

    public static void main(String[] args) {
        String query = "Java programming";  // Từ khóa để tìm kiếm
        searchBooks(query);
    }

    public static void searchBooks(String query) {
        try {
            // Mã hóa từ khóa tìm kiếm
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

            // Tạo URL tìm kiếm với từ khóa đã mã hóa
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY;

            // Tạo HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Tạo yêu cầu HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Xử lý JSON từ phản hồi
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

            // Kiểm tra xem có mảng "items" trong phản hồi hay không
            if (jsonObject.has("items")) {
                // Nếu có "items", lấy và in thông tin sách
                jsonObject.getAsJsonArray("items").forEach(item -> {
                    JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                    String title = volumeInfo.get("title").getAsString();
                    String authors = volumeInfo.getAsJsonArray("authors").toString();
                    System.out.println("Title: " + title);
                    System.out.println("Authors: " + authors);
                    System.out.println("----------------------");
                });
            } else {
                // Nếu không có "items", in ra thông báo không tìm thấy sách
                System.out.println("No books found for the query: " + query);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
