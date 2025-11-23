package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.OrderDto;
import com.stitch.grocerly.repository.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderEntity toEntity(OrderDto dto);

    OrderDto toDto(OrderEntity entity);

}