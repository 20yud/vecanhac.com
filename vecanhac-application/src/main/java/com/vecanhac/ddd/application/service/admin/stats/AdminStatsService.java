package com.vecanhac.ddd.application.service.admin.stats;

import com.vecanhac.ddd.application.dto.stats.RevenueStatDTO;
import com.vecanhac.ddd.application.dto.stats.SystemStatsDTO;

import java.util.List;

public interface AdminStatsService {
    SystemStatsDTO getSystemStats();
    List<RevenueStatDTO> getRevenueStats(String range); // day | month | year
}