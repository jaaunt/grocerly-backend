package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.UsersResponseDto;
import com.stitch.grocerly.repository.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "first_name", target = "firstName")
    UsersResponseDto mapToDto(Users users);
}
