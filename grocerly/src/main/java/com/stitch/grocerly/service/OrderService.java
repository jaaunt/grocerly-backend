package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.OrderDto;
import com.stitch.grocerly.mapper.OrderMapper;
import com.stitch.grocerly.repository.OrderEntity;
import com.stitch.grocerly.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        OrderEntity order = orderMapper.toEntity(orderDto);
        OrderEntity saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }  // admin saab koiki ordereid naha tulevikus

    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }  // minu orderite jaoks profiili alla tulevikus

}