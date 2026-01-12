package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.*;
import com.example.clinic_management.entity.*;
import com.example.clinic_management.exception.ApiException;
import com.example.clinic_management.repository.*;
import com.example.clinic_management.service.DoctorScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private static final ZoneId ZONE = ZoneId.of("Europe/Bucharest");

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorScheduleBreakRepository breakRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorScheduleServiceImpl(
            DoctorRepository doctorRepository,
            DoctorScheduleRepository scheduleRepository,
            DoctorScheduleBreakRepository breakRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        this.breakRepository = breakRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public DoctorScheduleResponse upsertDay(Long doctorId, short dayOfWeek, DoctorScheduleUpsertRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));

        if (request.getStartTime().compareTo(request.getEndTime()) >= 0) {
            throw new ApiException("SCHEDULE_INVALID_INTERVAL", "startTime must be before endTime");
        }

        DoctorSchedule s = scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek).orElse(null);
        if (s == null) {
            s = new DoctorSchedule();
            s.setDoctor(doctor);
            s.setDayOfWeek(dayOfWeek);
        }

        s.setStartTime(request.getStartTime());
        s.setEndTime(request.getEndTime());
        if (request.getActive() != null) {
            s.setActive(request.getActive());
        } else {
            s.setActive(true);
        }

        return toResponse(scheduleRepository.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> list(Long doctorId) {
        return scheduleRepository.findByDoctorIdOrderByDayOfWeekAsc(doctorId).stream().map(this::toResponse).toList();
    }

    @Override
    public DoctorScheduleBreakResponse addBreak(Long doctorId, short dayOfWeek, DoctorScheduleBreakCreateRequest request) {
        DoctorSchedule s = scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                .orElseThrow(() -> new ApiException("SCHEDULE_NOT_FOUND", "Schedule not found"));

        if (!s.isActive()) {
            throw new ApiException("SCHEDULE_INACTIVE", "Schedule inactive");
        }

        if (request.getStartTime().compareTo(request.getEndTime()) >= 0) {
            throw new ApiException("BREAK_INVALID_INTERVAL", "startTime must be before endTime");
        }

        if (request.getStartTime().isBefore(s.getStartTime()) || request.getEndTime().isAfter(s.getEndTime())) {
            throw new ApiException("BREAK_OUTSIDE_SCHEDULE", "Break must be within schedule interval");
        }

        DoctorScheduleBreak b = new DoctorScheduleBreak();
        b.setSchedule(s);
        b.setStartTime(request.getStartTime());
        b.setEndTime(request.getEndTime());
        b.setNote(request.getNote());

        return toBreakResponse(breakRepository.save(b));
    }

    @Override
    public void deleteBreak(Long breakId) {
        if (!breakRepository.existsById(breakId)) {
            throw new ApiException("BREAK_NOT_FOUND", "Break not found");
        }
        breakRepository.deleteById(breakId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> availableSlots(Long doctorId, LocalDate date, int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new ApiException("SLOT_INVALID_DURATION", "durationMinutes must be > 0");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ApiException("DOCTOR_NOT_FOUND", "Doctor not found"));
        if (!doctor.isActive() || !doctor.getClinic().isActive()) {
            throw new ApiException("DOCTOR_INACTIVE", "Doctor or clinic inactive");
        }

        short dow = (short) date.getDayOfWeek().getValue();
        DoctorSchedule s = scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dow)
                .orElseThrow(() -> new ApiException("SCHEDULE_NOT_FOUND", "Schedule not found"));
        if (!s.isActive()) {
            throw new ApiException("SCHEDULE_INACTIVE", "Schedule inactive");
        }

        ZonedDateTime dayStart = date.atStartOfDay(ZONE);
        OffsetDateTime from = dayStart.toOffsetDateTime();
        OffsetDateTime to = dayStart.plusDays(1).toOffsetDateTime();

        List<Appointment> existing = appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, from, to).stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .toList();

        List<DoctorScheduleBreak> breaks = breakRepository.findByScheduleIdOrderByStartTimeAsc(s.getId());

        OffsetDateTime workStart = ZonedDateTime.of(date, s.getStartTime(), ZONE).toOffsetDateTime();
        OffsetDateTime workEnd = ZonedDateTime.of(date, s.getEndTime(), ZONE).toOffsetDateTime();

        List<Interval> blocked = new ArrayList<>();
        for (DoctorScheduleBreak b : breaks) {
            OffsetDateTime bs = ZonedDateTime.of(date, b.getStartTime(), ZONE).toOffsetDateTime();
            OffsetDateTime be = ZonedDateTime.of(date, b.getEndTime(), ZONE).toOffsetDateTime();
            blocked.add(new Interval(bs, be));
        }
        for (Appointment a : existing) {
            blocked.add(new Interval(a.getStartTime(), a.getEndTime()));
        }

        blocked.sort(Comparator.comparing(i -> i.start));

        List<AvailableSlotResponse> out = new ArrayList<>();
        Duration slot = Duration.ofMinutes(durationMinutes);

        OffsetDateTime cursor = workStart;
        while (!cursor.plus(slot).isAfter(workEnd)) {
            OffsetDateTime slotEnd = cursor.plus(slot);
            if (!overlapsAny(cursor, slotEnd, blocked)) {
                AvailableSlotResponse r = new AvailableSlotResponse();
                r.setStartTime(cursor);
                r.setEndTime(slotEnd);
                out.add(r);
            }
            cursor = cursor.plus(slot);
        }

        return out;
    }

    private boolean overlapsAny(OffsetDateTime start, OffsetDateTime end, List<Interval> intervals) {
        for (Interval i : intervals) {
            if (start.isBefore(i.end) && end.isAfter(i.start)) {
                return true;
            }
        }
        return false;
    }

    private DoctorScheduleResponse toResponse(DoctorSchedule s) {
        DoctorScheduleResponse r = new DoctorScheduleResponse();
        r.setId(s.getId());
        r.setDayOfWeek(s.getDayOfWeek());
        r.setStartTime(s.getStartTime());
        r.setEndTime(s.getEndTime());
        r.setActive(s.isActive());
        List<DoctorScheduleBreakResponse> br = breakRepository.findByScheduleIdOrderByStartTimeAsc(s.getId()).stream()
                .map(this::toBreakResponse)
                .toList();
        r.setBreaks(br);
        return r;
    }

    private DoctorScheduleBreakResponse toBreakResponse(DoctorScheduleBreak b) {
        DoctorScheduleBreakResponse r = new DoctorScheduleBreakResponse();
        r.setId(b.getId());
        r.setStartTime(b.getStartTime());
        r.setEndTime(b.getEndTime());
        r.setNote(b.getNote());
        return r;
    }

    private static class Interval {
        private final OffsetDateTime start;
        private final OffsetDateTime end;

        private Interval(OffsetDateTime start, OffsetDateTime end) {
            this.start = start;
            this.end = end;
        }
    }
}
