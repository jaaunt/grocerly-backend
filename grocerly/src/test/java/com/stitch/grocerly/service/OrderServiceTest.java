package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.OrderDto;
import com.stitch.grocerly.mapper.OrderMapper;
import com.stitch.grocerly.repository.OrderEntity;
import com.stitch.grocerly.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST OrderService jaoks
 *
 * Põhineb TEGELIKUL OrderEntity ja OrderDto struktuuril:
 * - OrderEntity.id = Long
 * - OrderDto EI OLE id välja
 * - orderItems on String (JSON?)
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private OrderEntity orderEntity;
    private List<OrderEntity> orderEntityList;

    @BeforeEach
    void setUp() {
        // Test OrderDto (sisend - EI OLE ID'd!)
        orderDto = new OrderDto();
        orderDto.setUserId(100L);
        orderDto.setName("John Doe");
        orderDto.setEmail("john@example.com");
        orderDto.setPhone("+372123456");
        orderDto.setAddress("Tallinn, Estonia");
        orderDto.setOrderItems("[{\"productId\":1,\"quantity\":2}]");
        orderDto.setTotalPrice(new BigDecimal("99.99"));

        // Test OrderEntity (andmebaasi objekt - ON ID!)
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setUserId(100L);
        orderEntity.setName("John Doe");
        orderEntity.setEmail("john@example.com");
        orderEntity.setPhone("+372123456");
        orderEntity.setAddress("Tallinn, Estonia");
        orderEntity.setOrderItems("[{\"productId\":1,\"quantity\":2}]");
        orderEntity.setTotalPrice(new BigDecimal("99.99"));

        // Test list'id
        orderEntityList = Arrays.asList(orderEntity);
    }

    /**
     * TEST 1: createOrder - Edukas tellimuse loomine
     *
     * Voog:
     * 1. DTO (ilma ID'ta) → Entity (ilma ID'ta)
     * 2. Repository salvestab → Entity (ID'ga!)
     * 3. Entity → DTO (ilma ID'ta)
     */
    @Test
    void createOrder_Success() {
        // ========== ARRANGE ==========
        // orderDto ilma ID'ta teisendatakse Entity'ks (samuti ilma ID'ta)
        OrderEntity entityBeforeSave = new OrderEntity();
        entityBeforeSave.setUserId(100L);
        entityBeforeSave.setName("John Doe");
        entityBeforeSave.setEmail("john@example.com");
        entityBeforeSave.setPhone("+372123456");
        entityBeforeSave.setAddress("Tallinn, Estonia");
        entityBeforeSave.setOrderItems("[{\"productId\":1,\"quantity\":2}]");
        entityBeforeSave.setTotalPrice(new BigDecimal("99.99"));

        when(orderMapper.toEntity(orderDto)).thenReturn(entityBeforeSave);

        // Repository salvestab ja tagastab entity ID'ga
        when(orderRepository.save(entityBeforeSave)).thenReturn(orderEntity);

        // Mapper teisendab tagasi DTO'ks
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);

        // ========== ACT ==========
        OrderDto result = orderService.createOrder(orderDto);

        // ========== ASSERT ==========
        assertNotNull(result, "Tagastatud OrderDto ei tohi olla null");
        assertEquals(Long.valueOf(100L), result.getUserId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("+372123456", result.getPhone());
        assertEquals("Tallinn, Estonia", result.getAddress());
        assertEquals(new BigDecimal("99.99"), result.getTotalPrice());

        // VERIFY
        verify(orderMapper, times(1)).toEntity(orderDto);
        verify(orderRepository, times(1)).save(entityBeforeSave);
        verify(orderMapper, times(1)).toDto(orderEntity);
    }

    /**
     * TEST 3: createOrder - Mapper ebaõnnestub
     */
    @Test
    void createOrder_WhenMapperToEntityFails_ThrowsException() {
        // ========== ARRANGE ==========
        when(orderMapper.toEntity(orderDto))
                .thenThrow(new RuntimeException("Mapping to entity failed"));

        // ========== ACT & ASSERT ==========
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(orderDto)
        );

        assertEquals("Mapping to entity failed", exception.getMessage());

        verify(orderMapper, times(1)).toEntity(orderDto);
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).toDto(any());
    }

    /**
     * TEST 4: createOrder - Repository save ebaõnnestub
     */
    @Test
    void createOrder_WhenRepositorySaveFails_ThrowsException() {
        // ========== ARRANGE ==========
        OrderEntity entity = new OrderEntity();
        when(orderMapper.toEntity(orderDto)).thenReturn(entity);
        when(orderRepository.save(entity))
                .thenThrow(new RuntimeException("Database connection failed"));

        // ========== ACT & ASSERT ==========
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(orderDto)
        );

        assertEquals("Database connection failed", exception.getMessage());

        verify(orderMapper, times(1)).toEntity(orderDto);
        verify(orderRepository, times(1)).save(entity);
        verify(orderMapper, never()).toDto(any());
    }

    /**
     * TEST 5: getAllOrders - Edukas kõigi tellimuste päring
     */
    @Test
    void getAllOrders_Success() {
        // ========== ARRANGE ==========
        when(orderRepository.findAll()).thenReturn(orderEntityList);
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);

        // ========== ACT ==========
        List<OrderDto> result = orderService.getAllOrders();

        // ========== ASSERT ==========
        assertNotNull(result, "Tulemus ei tohi olla null");
        assertEquals(1, result.size(), "Peaks olema 1 order");
        assertEquals(Long.valueOf(100L), result.get(0).getUserId());
        assertEquals("John Doe", result.get(0).getName());

        // VERIFY
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDto(orderEntity);
    }

    /**
     * TEST 6: getAllOrders - Tühi list
     */
    @Test
    void getAllOrders_WhenNoOrders_ReturnsEmptyList() {
        // ========== ARRANGE ==========
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // ========== ACT ==========
        List<OrderDto> result = orderService.getAllOrders();

        // ========== ASSERT ==========
        assertNotNull(result, "Tulemus ei tohi olla null, isegi kui list on tühi");
        assertTrue(result.isEmpty(), "List peaks olema tühi");
        assertEquals(0, result.size());

        // VERIFY
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, never()).toDto(any());
    }

    /**
     * TEST 7: getAllOrders - Mitu tellimust
     */
    @Test
    void getAllOrders_WithMultipleOrders_Success() {
        // ========== ARRANGE ==========
        OrderEntity order2 = new OrderEntity();
        order2.setId(2L);
        order2.setUserId(200L);
        order2.setName("Jane Doe");
        order2.setEmail("jane@example.com");
        order2.setPhone("+372654321");
        order2.setAddress("Tartu, Estonia");
        order2.setOrderItems("[{\"productId\":2,\"quantity\":1}]");
        order2.setTotalPrice(new BigDecimal("49.99"));

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setUserId(200L);
        orderDto2.setName("Jane Doe");
        orderDto2.setEmail("jane@example.com");

        List<OrderEntity> multipleOrders = Arrays.asList(orderEntity, order2);

        when(orderRepository.findAll()).thenReturn(multipleOrders);
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);
        when(orderMapper.toDto(order2)).thenReturn(orderDto2);

        // ========== ACT ==========
        List<OrderDto> result = orderService.getAllOrders();

        // ========== ASSERT ==========
        assertNotNull(result);
        assertEquals(2, result.size(), "Peaks olema 2 order'it");
        assertEquals(Long.valueOf(100L), result.get(0).getUserId());
        assertEquals(Long.valueOf(200L), result.get(1).getUserId());

        // VERIFY
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(2)).toDto(any(OrderEntity.class));
    }

    /**
     * TEST 8: getOrdersByUserId - Edukas ühe kasutaja tellimuste päring
     */
    @Test
    void getOrdersByUserId_Success() {
        // ========== ARRANGE ==========
        Long userId = 100L;

        when(orderRepository.findByUserId(userId)).thenReturn(orderEntityList);
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);

        // ========== ACT ==========
        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        // ========== ASSERT ==========
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId(),
                "Tagastatud order peaks kuuluma õigele kasutajale");

        // VERIFY
        verify(orderRepository, times(1)).findByUserId(userId);
        verify(orderMapper, times(1)).toDto(orderEntity);
    }

    /**
     * TEST 9: getOrdersByUserId - Kasutajal pole tellimusi
     */
    @Test
    void getOrdersByUserId_WhenUserHasNoOrders_ReturnsEmptyList() {
        // ========== ARRANGE ==========
        Long userId = 999L;

        when(orderRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // ========== ACT ==========
        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        // ========== ASSERT ==========
        assertNotNull(result, "Peaks tagastama tühja listi, mitte null'i");
        assertTrue(result.isEmpty());

        // VERIFY
        verify(orderRepository, times(1)).findByUserId(userId);
        verify(orderMapper, never()).toDto(any());
    }

    /**
     * TEST 10: getOrdersByUserId - Mitme tellimus sama kasutaja jaoks
     */
    @Test
    void getOrdersByUserId_WithMultipleOrders_Success() {
        // ========== ARRANGE ==========
        Long userId = 100L;

        OrderEntity order2 = new OrderEntity();
        order2.setId(2L);
        order2.setUserId(userId);
        order2.setName("John Doe");
        order2.setEmail("john@example.com");
        order2.setPhone("+372123456");
        order2.setAddress("Tallinn, Estonia");
        order2.setOrderItems("[{\"productId\":3,\"quantity\":5}]");
        order2.setTotalPrice(new BigDecimal("149.99"));

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setUserId(userId);
        orderDto2.setName("John Doe");

        List<OrderEntity> userOrders = Arrays.asList(orderEntity, order2);

        when(orderRepository.findByUserId(userId)).thenReturn(userOrders);
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);
        when(orderMapper.toDto(order2)).thenReturn(orderDto2);

        // ========== ACT ==========
        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        // ========== ASSERT ==========
        assertNotNull(result);
        assertEquals(2, result.size());

        // Kontrolli, et mõlemad orderid kuuluvad õigele kasutajale
        result.forEach(order ->
                assertEquals(userId, order.getUserId(),
                        "Kõik orderid peaksid kuuluma samale kasutajale")
        );

        // VERIFY
        verify(orderRepository, times(1)).findByUserId(userId);
        verify(orderMapper, times(2)).toDto(any(OrderEntity.class));
    }

    /**
     * TEST 11: createOrder - Null userId
     */
    @Test
    void createOrder_WithNullUserId_StillProcesses() {
        // ========== ARRANGE ==========
        orderDto.setUserId(null);
        OrderEntity entity = new OrderEntity();
        entity.setUserId(null);

        when(orderMapper.toEntity(orderDto)).thenReturn(entity);
        when(orderRepository.save(entity)).thenReturn(entity);
        when(orderMapper.toDto(entity)).thenReturn(orderDto);

        // ========== ACT ==========
        OrderDto result = orderService.createOrder(orderDto);

        // ========== ASSERT ==========
        assertNotNull(result);
        assertNull(result.getUserId(), "UserId peaks olema null");

        // VERIFY
        verify(orderMapper, times(1)).toEntity(orderDto);
        verify(orderRepository, times(1)).save(entity);
        verify(orderMapper, times(1)).toDto(entity);
    }

    /**
     * TEST 12: createOrder - Tühi orderItems
     */
    @Test
    void createOrder_WithEmptyOrderItems_Success() {
        // ========== ARRANGE ==========
        orderDto.setOrderItems("");
        OrderEntity entity = new OrderEntity();
        entity.setOrderItems("");

        when(orderMapper.toEntity(orderDto)).thenReturn(entity);
        when(orderRepository.save(entity)).thenReturn(orderEntity);
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);

        // ========== ACT ==========
        OrderDto result = orderService.createOrder(orderDto);

        // ========== ASSERT ==========
        assertNotNull(result);

        // VERIFY
        verify(orderRepository, times(1)).save(entity);
    }
}