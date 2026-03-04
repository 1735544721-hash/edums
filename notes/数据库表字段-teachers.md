# 教师信息表（teachers）字段清单

来源：`edums/教务管理系统/edums.sql`

| 编号 | 字段名 | 类型 | 长度 | 是否非空 | 是否主键 | 注释 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | id | int | — | 是 | 是 | 未提供 |
| 2 | teacher_id | varchar | 255 | 是 | 否 | 未提供 |
| 3 | password | varchar | 255 | 是 | 否 | 未提供 |
| 4 | name | varchar | 255 | 是 | 否 | 未提供 |
| 5 | gender | enum | — | 是 | 否 | 未提供 |
| 6 | title | int | — | 是 | 否 | 未提供 |
| 7 | course_count | int | — | 是 | 否 | 未提供 |
| 8 | class_count | int | — | 是 | 否 | 未提供 |
| 9 | phone | varchar | 255 | 否 | 否 | 未提供 |
| 10 | status | tinyint | — | 是 | 否 | 教师审核状态，0表示未审核，1表示审核通过 |
| 11 | username | varchar | 255 | 是 | 否 | 未提供 |
| 12 | weekly_class_hours | int | — | 否 | 否 | 周课时数 |
| 13 | teacher_type | tinyint | — | 否 | 否 | 教师身份: 0-行政干部, 1-普通教师 |
