# 课程安排表（course_schedule）字段清单

来源：`edums/教务管理系统/edums.sql`

| 编号 | 字段名 | 类型 | 长度 | 是否非空 | 是否主键 | 注释 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | id | int | — | 是 | 是 | 未提供 |
| 2 | class_id | varchar | 255 | 是 | 否 | 未提供 |
| 3 | class_name | varchar | 255 | 是 | 否 | 未提供 |
| 4 | teacher_id | varchar | 255 | 是 | 否 | 未提供 |
| 5 | course_id | varchar | 255 | 是 | 否 | 未提供 |
| 6 | day_of_week | enum | — | 是 | 否 | 未提供 |
| 7 | class_period | int | — | 是 | 否 | 未提供 |
