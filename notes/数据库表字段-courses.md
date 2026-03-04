# 课程信息表（courses）字段清单

来源：`edums/教务管理系统/edums.sql`

| 编号 | 字段名 | 类型 | 长度 | 是否非空 | 是否主键 | 注释 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | id | int | — | 是 | 是 | 未提供 |
| 2 | course_id | varchar | 255 | 是 | 否 | 未提供 |
| 3 | course_name | varchar | 255 | 是 | 否 | 未提供 |
| 4 | course_code | varchar | 255 | 是 | 否 | 未提供 |
| 5 | class_hours | int | — | 否 | 否 | 未提供 |
| 6 | class_type | enum | — | 否 | 否 | 取值：Undergraduate/Associate/Adult Education/Graduate |
| 7 | course_level | enum | — | 否 | 否 | 取值：College/Teacher |
