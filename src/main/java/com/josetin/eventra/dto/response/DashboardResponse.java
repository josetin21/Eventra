package com.josetin.eventra.dto.response;

public record DashboardResponse(
        Long totalUser,
        Long totalEvents,
        Long totalRegistration,
        Long totalAttendance,
        Long pendingEventApprovals,
        Long activeEvents,
        Long cancelledEvents
) {
}
