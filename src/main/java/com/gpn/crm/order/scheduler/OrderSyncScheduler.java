package com.gpn.crm.order.scheduler;

import com.gpn.crm.order.service.OrderSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderSyncScheduler {

    private final OrderSyncService orderSyncService;

    /**
     * Once a day at 03:00 Europe/Kyiv (off-peak). Adjust the cron expression/zone here if a
     * different time is wanted - nothing else needs to change.
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Kyiv")
    public void syncAllOrders() {
        log.info("Starting scheduled full order sync from KeyCRM");
        try {
            int synced = orderSyncService.syncAllOrders();
            log.info("Scheduled full order sync finished: {} orders synced", synced);
        } catch (Exception e) {
            log.error("Scheduled full order sync failed", e);
        }
    }

}
