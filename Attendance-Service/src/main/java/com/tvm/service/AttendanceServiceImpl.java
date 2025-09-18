package com.tvm.service;

import com.tvm.client.EmployeeClient;
import com.tvm.dto.Employee;
import com.tvm.entity.Attendance;
import com.tvm.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeClient employeeClient;

    @Override
    public Attendance checkIn(Long employeeId) {
        LocalDate today = LocalDate.now();

        Employee emp = employeeClient.getEmployeeById(employeeId);
        if (emp == null) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }

        if(attendanceRepo.findByEmployeeIdAndDate(employeeId, today).isPresent()){
            throw new RuntimeException("Already Checked In Today");
        }

        Attendance attendance = Attendance.builder()
                .employeeId(employeeId)
                .date(today)
                .checkInTime(LocalTime.now())
                .build();


        return attendanceRepo.save(attendance);
    }

    @Override
    public Attendance checkOut(Long employeeId) {

        Employee emp = employeeClient.getEmployeeById(employeeId);
        if (emp == null) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepo.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No-Check in Record Found Today"));

        if(attendance.getCheckOutTime() != null){
            throw new RuntimeException("Already Checked Out Today");
        }

        LocalTime checkOutTime = LocalTime.now();
        attendance.setCheckOutTime(checkOutTime);

        Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        attendance.setWorkingHours(duration.toMinutes());

        return attendanceRepo.save(attendance);
    }

    @Override
    public List<Attendance> getEmployeeAttendance(Long employeeId) {
        return attendanceRepo.findAllByEmployeeId(employeeId);
    }

    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepo.findAllByDate(date);
    }


    public Map<String, Object> getMonthlySummary(Long employeeId, String month) {
        // month must be like "2025-07"
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(month + "-01");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("month must be in format yyyy-MM, e.g. 2025-07");
        }
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Attendance> records =
                attendanceRepo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);

        long totalMinutes = records.stream()
                .filter(r -> r.getWorkingHours() != null)
                .mapToLong(Attendance::getWorkingHours)   // your field is Long
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("employeeId", employeeId);
        summary.put("month", month);
        summary.put("totalDaysPresent", records.size());
        summary.put("totalHoursWorkedMinutes", totalMinutes);
        summary.put("totalHoursWorkedHHmm", formatMinutes(totalMinutes)); // optional pretty format

        return summary;
    }


    private String formatMinutes(long minutes) {
        long hrs = minutes / 60;
        long mins = minutes % 60;
        return String.format("%02d:%02d", hrs, mins);
    }
    public List<Attendance> getAttendanceReport(LocalDate startDate, LocalDate endDate) {
        return attendanceRepo.findByDateBetween(startDate, endDate);
    }


}
