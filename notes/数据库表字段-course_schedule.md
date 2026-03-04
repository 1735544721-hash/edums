# 课程安排表（course_schedule）字段清单

来源：`edums/教务管理系统/edums.sql`

| 编号 | 字段名 | 类型 | 长度 | 是否非空 | 是否主键 | 注释 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | id | int | — | 是 | 是 | 未提供 |
| 2 | class_id | varchar | 255 | 是 | 否 | 班级编号 |
| 3 | class_name | varchar | 255 | 是 | 否 | 班级名称 |
| 4 | teacher_id | varchar | 255 | 是 | 否 | 教师ID |
| 5 | course_id | varchar | 255 | 是 | 否 | 课程ID |
| 6 | day_of_week | enum | — | 是 | 否 | 周几（周一/周二/周三/周四/周五/周六/周日） |
| 7 | class_period | int | — | 是 | 否 | 节次 |
