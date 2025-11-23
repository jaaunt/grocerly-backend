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

    public OrderEntity createOrder(OrderDto orderDto) {
        OrderEntity order = orderMapper.toEntity(orderDto);
        return orderRepository.save(order);
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderEntity> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}