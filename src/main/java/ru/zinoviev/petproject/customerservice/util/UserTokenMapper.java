package ru.zinovievbank.customerservice.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.zinovievbank.customerservice.dto.JwtResponseDto;
import ru.zinovievbank.customerservice.model.UserToken;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTokenMapper {

    @Mapping(target = "refreshToken", source = "tokenValue")
    JwtResponseDto toJwtResponseDto(UserToken userToken);

}
