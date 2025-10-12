package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.repository.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {
    @Mapping(source = "firstName", target = "first_name")
    @Mapping(source = "lastName", target = "last_name")
    Users mapToEntity(UsersDto dto);

    @Mapping(source = "first_name", target = "firstName")
    @Mapping(source = "last_name", target = "lastName")
    UsersDto mapToDto(Users users);
}