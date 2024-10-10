package ru.zinovievbank.customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.globalexeptionsspringbootstarter.exception.ErrorResponse;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.service.CustomerService;


@RestController
@RequestMapping("/user")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * A controller that sends information with the customer’s personal data for subsequent display
     * in the main menu and personal account when viewing general information about the customer.
     *
     * @param token
     * @return {@link CustomerInfoDto}
     */
    @Operation(summary = "CAS-8. Получение информации о пользователе",
            description = "В данном endpoint необходимо отправить информацию c личными данными пользователя для последующего " +
                    "отображения в основном меню и личном кабинете при просмотре общей информации о пользователе.\n" +
                    "Пользователь ввел данные для входа, и нажал кнопку \"войти\". После успешной авторизации на сервер направляется запрос информации о персональных данных клиента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Если Пользователь передал некорректные данные, возвращается", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Если не удалось обнаружить данные", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Если метод запроса не GET / PATCH / OPTIONS", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Если тип параметра customerId не является string", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае неудачи получения информации из базы данных", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
    })

    @GetMapping("/information")
    public ResponseEntity<String> getCustomerInfo(
            @RequestHeader("Authorization") String token) {
        String dto = customerService.getCustomerInfoById(token);
        return ResponseEntity.ok(dto);
    }

    /**
     * This endpoint allows you to update the email address associated with the Customer account in
     * the database
     *
     * @param emailDto {@link EmailDto}
     * @param token
     * @return void
     */
    @Operation(summary = "CRS-9 Обновление email Клиента",
            description = "Данный endpoint позволяет обновить адрес электронной почты, привязанный к учетной записи Пользователя в базе данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Если ничего не было передано или переданы не валидные данные", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "При отсутствии прав доступа к содержимому ", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Если не удалось обнаружить указанный URL (URL не валидный)", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Если метод запроса не PATCH/OPTIONS", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае ошибки на стороне сервера", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/settings/email")
    public ResponseEntity<Void> updateEmail(
            @RequestBody @Valid EmailDto emailDto,
            @RequestHeader("Authorization") String token) {
        customerService.updateEmail(token, emailDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Controller for changing the security question and answer in the Customer's Personal Account
     *
     * @param securityQuestionAndAnswerDto {@link SecurityQuestionAndAnswerDto}
     * @param token
     * @return void
     */
    @Operation(summary = "CRS-15 Изменение контрольного вопроса / ответа.",
            description = "Изменение контрольного вопроса и ответа на него в личном кабинете Пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Если ничего не было передано или переданы не валидные данные", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "При неуспешной валидации токена (при запросе в ISTIO)", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Если не удалось обнаружить указанный URL (URL не валидный)", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Если метод запроса не PATCH / OPTIONS", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае ошибки на стороне сервера", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/settings/controls")
    public ResponseEntity<SecurityQuestionAndAnswerDto> updateSecurityQuestionAndAnswer(
            @RequestBody @Valid SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto,
            @RequestHeader("Authorization") String token) {
        customerService.updateSecurityQuestionAndAnswer(token, securityQuestionAndAnswerDto);
        return ResponseEntity.ok(securityQuestionAndAnswerDto);
    }

    /**
     * Controller for checking a customer in a database by phone number
     *
     * @param phoneNumber
     * @return void
     */
    @Operation(summary = "CRS-1 Проверка регистрации пользователя по номеру телефона",
            description = "Данный эндпоинт предназначен для получения номера телефона от пользователя c фронтенда и передачи этого номера в Customer-Service 2.0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Если номер телефона, предоставленный пользователем, не соответствует заданному формату", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Если не удалось обнаружить указанный URL (URL не валидный)", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Использован некорректный тип метода", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Если формат переданных данных не соответствует требованиям", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае ошибки на стороне сервера", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Если сервер не готов к обработке запроса по техническим причинам", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/checking/{phoneNumber}")
    public ResponseEntity<Void> checkCustomerByPhoneNumber(
            @PathVariable("phoneNumber") String phoneNumber) {
        customerService.checkCustomerByPhoneNumber(phoneNumber);
        return ResponseEntity.ok().build();
    }
}
