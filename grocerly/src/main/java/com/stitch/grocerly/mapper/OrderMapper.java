package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.OrderDto;
import com.stitch.grocerly.repository.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toEntity(OrderDto dto);

    OrderDto toDto(OrderEntity entity);
}