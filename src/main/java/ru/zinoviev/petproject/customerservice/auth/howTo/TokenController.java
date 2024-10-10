package ru.zinovievbank.customerservice.auth.howTo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zinovievbank.customerservice.dto.MobilePhoneReceiverDto;
import ru.zinovievbank.customerservice.service.CustomerService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final JwtProviderTokenGeneration jwtProvider;
    private final CustomerService customerService;

    public TokenController(JwtProviderTokenGeneration jwtProvider, CustomerService customerService) {
        this.jwtProvider = jwtProvider;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<String> getToken(@RequestBody MobilePhoneReceiverDto phoneNumber) {
        var a = customerService.getCustomerIdByMobilePhone(phoneNumber.getMobilePhone());
        String token = jwtProvider.generateAccessToken(a);
        return ResponseEntity.ok(token);
        //return ResponseEntity.ok(sendRequest(token));
    }

    private String sendRequest(String token){
        String regUrl = "http://127.0.0.1:8080/customer/api/v1/user/information";
        String header = "Authorization";
        String headerValue = "Bearer "+token;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(regUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header(header, headerValue)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("No: "+response.statusCode());
            }
            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("No: "+e);
        }
    }

}
