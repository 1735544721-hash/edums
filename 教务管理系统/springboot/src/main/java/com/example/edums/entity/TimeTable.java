package com.example.edums.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {
    private  int dayOfWeek;
    private String courseName;
    private String courseId;
    private String teacherName;
    private String content;
    private int start;
    private int end;

}
