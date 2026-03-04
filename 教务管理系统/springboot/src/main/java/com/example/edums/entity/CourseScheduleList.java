package com.example.edums.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseScheduleList {
    private String classId;
    private List<CourseSchedule> scheduleList;
}
