package ru.zinovievbank.customerservice.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.zinovievbank.customerservice.dto.AuthenticationMessageDto;
import ru.zinovievbank.customerservice.dto.ChangePasswordDto;
import ru.zinovievbank.customerservice.dto.MobilePhoneAndVerificationCodeDto;
import ru.zinovievbank.customerservice.dto.NewPasswordDto;
import ru.zinovievbank.customerservice.dto.RecoveryPasswordDto;
import ru.zinovievbank.customerservice.model.UserProfile;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    AuthenticationMessageDto toAuthenticationMessageDto (UserProfile userProfile);

    ChangePasswordDto toChangePasswordDto (UserProfile userProfile);

    @Mapping(source = "lastVerificationCode", target = "verificationCode")
    MobilePhoneAndVerificationCodeDto toMobilePhoneAndVerificationCodeDto (UserProfile userProfile);

    @Mapping(source = "password", target = "newPassword")
    NewPasswordDto toNewPasswordDto (UserProfile userProfile);

    @Mapping(source = "password", target = "newPassword")
    RecoveryPasswordDto toRecoveryPasswordDto(UserProfile userProfile);

    void updatePasswordFromChangePassword(ChangePasswordDto changePasswordDto, @MappingTarget UserProfile userProfile);

    @Mapping(source = "newPassword", target = "password")
    void updatePasswordFromNewPassword(NewPasswordDto newPasswordDto, @MappingTarget UserProfile userProfile);

    @Mapping(source = "newPassword", target = "password")
    void updatePasswordFromRecoveryPassword(RecoveryPasswordDto recoveryPasswordDto, @MappingTarget UserProfile userProfile);

    default byte[] mapToBytes(String string) {
        return string != null ? string.getBytes() : null;
    }

    default String mapToString(byte[] value) {
        return new String(value);
    }
}
