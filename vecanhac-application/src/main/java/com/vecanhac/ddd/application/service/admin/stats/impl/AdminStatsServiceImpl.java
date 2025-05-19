package com.vecanhac.ddd.application.service.admin.stats.impl;

import com.vecanhac.ddd.application.dto.stats.RevenueStatDTO;
import com.vecanhac.ddd.application.dto.stats.SystemStatsDTO;
import com.vecanhac.ddd.application.service.admin.stats.AdminStatsService;
import com.vecanhac.ddd.domain.discountcode.DiscountCodeRepository;
import com.vecanhac.ddd.domain.event.EventRepository;
import com.vecanhac.ddd.domain.order.OrderRepository;
import com.vecanhac.ddd.domain.showing.ShowingRepository;
import com.vecanhac.ddd.domain.ticket.TicketRepository;
import com.vecanhac.ddd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ShowingRepository showingRepository;
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final DiscountCodeRepository discountCodeRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SystemStatsDTO getSystemStats() {
        SystemStatsDTO dto = new SystemStatsDTO();

        dto.setTotalUsers(userRepository.count());
        dto.setTotalEvents(eventRepository.count());
        dto.setTotalShowings(showingRepository.count());
        dto.setTotalTickets(ticketRepository.count());
        dto.setTotalOrders(orderRepository.count());
        dto.setActiveDiscountCodes(discountCodeRepository.countByIsActiveTrue());

        BigDecimal revenue = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'SUCCESS'", BigDecimal.class);
        dto.setTotalRevenue(revenue);

        dto.setEventStatusMap(getCountGroupBy("events", "status"));
        dto.setOrderStatusMap(getCountGroupBy("orders", "status"));
        dto.setTicketStatusMap(getCountGroupBy("tickets", "status"));

        return dto;
    }

    @Override
    public List<RevenueStatDTO> getRevenueStats(String range) {
        String dateFormat;
        switch (range.toLowerCase()) {
            case "day": dateFormat = "%Y-%m-%d"; break;
            case "year": dateFormat = "%Y"; break;
            default: dateFormat = "%Y-%m"; // default month
        }

        String sql = "SELECT DATE_FORMAT(paid_at, ?) as time, SUM(amount) as total " +
                "FROM payments WHERE status = 'SUCCESS' GROUP BY time ORDER BY time";

        return jdbcTemplate.query(sql, new Object[]{dateFormat}, (rs, rowNum) ->
                new RevenueStatDTO(rs.getString("time"), rs.getBigDecimal("total"))
        );
    }

    private Map<String, Long> getCountGroupBy(String table, String column) {
        String sql = String.format("SELECT %s, COUNT(*) AS total FROM %s GROUP BY %s", column, table, column);
        return jdbcTemplate.query(sql, rs -> {
            Map<String, Long> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString(column), rs.getLong("total"));
            }
            return map;
        });
    }
}