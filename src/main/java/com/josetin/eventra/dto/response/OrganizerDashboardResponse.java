package com.josetin.eventra.dto.response;

public record OrganizerDashboardResponse(
        Long myTotalEvents,
        Long myActiveEvents,
        Long myTotalRegistration,
        Long myTotalAttendance,
        Long myCancelledEvents
) {
}
