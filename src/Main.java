import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static final String API_KEY = "525859ea3e2e83da7f6a1e75";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Selecione uma opção:");
            System.out.println("1. USD para EUR");
            System.out.println("2. USD para GBP");
            System.out.println("3. USD para JPY");
            System.out.println("4. USD para AUD");
            System.out.println("5. USD para CAD");
            System.out.println("6. USD para CHF");
            System.out.println("7. Sair");

            int option = scanner.nextInt();

            if (option == 7) {
                break;
            }

            String toCurrency = null;
            switch (option) {
                case 1:
                    toCurrency = "EUR";
                    break;
                case 2:
                    toCurrency = "GBP";
                    break;
                case 3:
                    toCurrency = "JPY";
                    break;
                case 4:
                    toCurrency = "AUD";
                    break;
                case 5:
                    toCurrency = "CAD";
                    break;
                case 6:
                    toCurrency = "CHF";
                    break;
            }

            double exchangeRate = getExchangeRate(toCurrency);

            System.out.println("Digite a quantidade em USD:");
            double amountInUsd = scanner.nextDouble();

            double amountInToCurrency = amountInUsd * exchangeRate;

            System.out.println(amountInUsd + " USD é igual a " + amountInToCurrency + " " + toCurrency + ".");
        }

        scanner.close();
    }

    private static double getExchangeRate(String toCurrency) throws Exception {
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        conn.disconnect();

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

        return jsonObject.getAsJsonObject("conversion_rates").get(toCurrency).getAsDouble();
    }
}