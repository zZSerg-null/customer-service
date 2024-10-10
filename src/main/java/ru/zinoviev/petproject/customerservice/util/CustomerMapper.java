package ru.zinovievbank.customerservice.util;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

@Mapper(componentModel = "spring", uses = CustomerStatusConverter.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(source = "birthDate", target = "birthDate", dateFormat = "dd/MMM/yyyy")
    CustomerInfoDto toCustomerInfoDto(Customer customer);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "smsNotification", target = "smsNotification", defaultValue = "false")
    @Mapping(source = "pushNotification", target = "pushNotification", defaultValue = "true")
    @Mapping(source = "emailSubscription", target = "emailSubscription", defaultValue = "false")
    CustomerNotificationsDto toCustomerNotificationsDto(Customer customer);

    void updateCustomerForEmail(EmailDto emailDto, @MappingTarget Customer customer);

    void updateCustomerForSecurityQuestionAndAnswer(
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto,
        @MappingTarget Customer customer);

    @Mapping(source = "notificationStatus", target = "smsNotification")
    void updateNotificationSms(NotificationStatusDto notificationStatusDto,
        @MappingTarget Customer customer);

    @Mapping(source = "notificationStatus", target = "emailSubscription")
    void updateNotificationEmail(NotificationStatusDto notificationStatusDto,
        @MappingTarget Customer customer);

    @Mapping(source = "notificationStatus", target = "pushNotification")
    void updateNotificationPush(NotificationStatusDto notificationStatusDto,
        @MappingTarget Customer customer);




}
