package com.example.schedulemanagerplus.schedule.controller;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.schedule.dto.request.ScheduleRequestDto;
import com.example.schedulemanagerplus.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getAllSchedules(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return ResponseEntity.ok(scheduleService.getAllSchedules(pageNumber, pageSize));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> getScheduleById(@PathVariable Long id){
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<?>> saveSchedule(@Valid @RequestBody ScheduleRequestDto request) {
        return ResponseEntity.ok(scheduleService.saveSchedule(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto request) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.deleteSchedule(id));
    }

}
