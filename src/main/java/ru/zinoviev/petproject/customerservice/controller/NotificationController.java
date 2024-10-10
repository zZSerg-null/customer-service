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
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;
import ru.zinovievbank.customerservice.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * This controller calls the corresponding method from the service, which allows you to make
     * changes to the Customer account in terms of settings for receiving SMS notifications.
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     * @return void
     */
    @Operation(summary = "CRS-12 Изменение статуса настройки получения SMS-уведомлений.",
            description = "Данный метод позволяет вносить изменения в учетной записи Пользователя в части настроек получения SMS-уведомлений. " +
                    "В настройках пользователь может активировать или деактивировать чек-бокс на получение SMS-уведомлений. \n" +
                    "Доступ к изменению настроек имеет только авторизованный пользователь.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Если ничего не было передано или переданы не валидные данные", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Если не удалось обнаружить указанный URL (URL не валидный)", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Если метод запроса не PATCH / OPTIONS", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "415", description = "Если тип параметра notificationStatus не является boolean", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае ошибки на стороне сервера", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/settings/sms")
    public ResponseEntity<Void> setNotificationSms(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid NotificationStatusDto statusDto) {
        notificationService.setNotificationSms(token, statusDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Controller for updating the Customer account in terms of settings for receiving email
     * newsletters.
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     * @return void
     */
    @Operation(summary = "CRS-13 Изменение статуса настройки получения email-рассылки.",
            description = "Данный метод позволяет вносить изменения в учетной записи Пользователя в части настроек получения email_subscription. " +
                    "В настройках пользователь может активировать или деактивировать чек-бокс на получение email-рассылки. \n" +
                    "Доступ к изменению настроек имеет только авторизованный пользователь. " +
                    "бновление учетной записи Пользователя происходит в настройках получения email-рассылки.")
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
    @PatchMapping("/settings/email")
    public ResponseEntity<Void> setNotificationEmail(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid NotificationStatusDto statusDto) {
        notificationService.setNotificationEmail(token, statusDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Controller that updates customer accounts regarding notification settings
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     * @return void
     */
    @Operation(summary = "CRS-13 Изменение статуса настройки получения email-рассылки.",
            description = "Данный метод позволяет вносить изменения в учетной записи Пользователя в части настроек получения email_subscription. " +
                    "В настройках пользователь может активировать или деактивировать чек-бокс на получение email-рассылки. ")
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
            @ApiResponse(responseCode = "415", description = "При успешной валидации токена, но если тип параметра notificationStatus не является boolean", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "В случае ошибки на стороне сервера", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/settings/push")
    public ResponseEntity<Void> setNotificationPush(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid NotificationStatusDto statusDto) {
        notificationService.setNotificationPush(token, statusDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Controller that gives customer accounts notification settings
     *
     * @param token
     * @return {@link CustomerNotificationsDto}
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
    @GetMapping
    public ResponseEntity<CustomerNotificationsDto> getCustomerNotificationsSettings(
            @RequestHeader("Authorization") String token) {
        CustomerNotificationsDto customerProfileNotifications =
                notificationService.getCustomerNotificationsSettings(token);
        return ResponseEntity.ok(customerProfileNotifications);
    }
}
