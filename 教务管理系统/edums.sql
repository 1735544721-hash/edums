/*
 Navicat Premium Data Transfer

 Source Server         : jx
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : edums

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 16/08/2024 23:00:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `admin_id`(`admin_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'a00001', 'admin123');
INSERT INTO `admin` VALUES (2, 'a00002', 'encrypted_admin_password2');
INSERT INTO `admin` VALUES (3, 'a00003', 'encrypted_admin_password3');
INSERT INTO `admin` VALUES (4, 'a00004', 'encrypted_admin_password4');
INSERT INTO `admin` VALUES (5, 'a00005', 'encrypted_admin_password5');

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_id`(`class_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '班级信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES (1, 'cl01', '计算机科学与技术1班', '计算机学院1');
INSERT INTO `classes` VALUES (2, 'cl02', '数学与应用数学1班', '数学学院');
INSERT INTO `classes` VALUES (3, 'cl03', '物理1班', '物理学院');
INSERT INTO `classes` VALUES (4, 'cl04', '软件工程1班', '软件学院');
INSERT INTO `classes` VALUES (5, 'cl05', '信息安全1班', '信息学院');
INSERT INTO `classes` VALUES (7, 'cl00001', '1111111', '111111');
INSERT INTO `classes` VALUES (11, 'cl00006', '2123', '32112');

-- ----------------------------
-- Table structure for course_open
-- ----------------------------
DROP TABLE IF EXISTS `course_open`;
CREATE TABLE `course_open`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `left_hours` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `teacher_id`(`teacher_id` ASC) USING BTREE,
  INDEX `class_id`(`class_id` ASC) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `course_open_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_open_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_open_ibfk_3` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '授课计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_open
-- ----------------------------
INSERT INTO `course_open` VALUES (1, 't00001', 'cl01', 'c00001', 45, 1);
INSERT INTO `course_open` VALUES (2, 't00002', 'cl01', 'c00002', 52, 1);
INSERT INTO `course_open` VALUES (3, 't00003', 'cl01', 'c00003', 12, 1);
INSERT INTO `course_open` VALUES (4, 't00004', 'cl01', 'c00004', 36, 1);
INSERT INTO `course_open` VALUES (5, 't00005', 'cl01', 'c00005', 45, 1);
INSERT INTO `course_open` VALUES (9, 't00001', 'cl04', 'c00001', NULL, 0);

-- ----------------------------
-- Table structure for course_schedule
-- ----------------------------
DROP TABLE IF EXISTS `course_schedule`;
CREATE TABLE `course_schedule`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级编号',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称',
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师ID',
  `course_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程ID',
  `day_of_week` enum('周一','周二','周三','周四','周五','周六','周日') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '周几',
  `class_period` int NOT NULL COMMENT '节次',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_schedule
-- ----------------------------
INSERT INTO `course_schedule` VALUES (4, 'class004', '二年级一班', 'teacher004', 'english004', '周四', 4);
INSERT INTO `course_schedule` VALUES (5, 'class005', '二年级二班', 'teacher005', 'history005', '周五', 5);
INSERT INTO `course_schedule` VALUES (6, 'class006', '二年级三班', 'teacher006', 'geography006', '周六', 6);
INSERT INTO `course_schedule` VALUES (7, 'class007', '三年级一班', 'teacher007', 'physics007', '周日', 7);
INSERT INTO `course_schedule` VALUES (8, 'class008', '三年级二班', 'teacher008', 'chemistry008', '周一', 8);
INSERT INTO `course_schedule` VALUES (9, 'class009', '三年级三班', 'teacher009', 'biology009', '周二', 9);
INSERT INTO `course_schedule` VALUES (10, 'class010', '四年级一班', 'teacher010', 'computer010', '周三', 10);
INSERT INTO `course_schedule` VALUES (65, 'cl01', '计算机科学与技术1班', 't00001', 'c00005', '周二', 5);
INSERT INTO `course_schedule` VALUES (66, 'cl01', '计算机科学与技术1班', 't00001', 'c00003', '周三', 1);
INSERT INTO `course_schedule` VALUES (67, 'cl01', '计算机科学与技术1班', 't00001', 'c00002', '周五', 2);
INSERT INTO `course_schedule` VALUES (77, 'cl01', '计算机科学与技术1班', 't00001', 'c00005', '周三', 3);
INSERT INTO `course_schedule` VALUES (78, 'cl01', '计算机科学与技术1班', 't00002', 'c00002', '周六', 2);
INSERT INTO `course_schedule` VALUES (79, 'cl01', '计算机科学与技术1班', 't00004', 'c00004', '周四', 4);
INSERT INTO `course_schedule` VALUES (80, 'cl01', '计算机科学与技术1班', 't00003', 'c00003', '周日', 4);
INSERT INTO `course_schedule` VALUES (81, 'cl01', '计算机科学与技术1班', 't00005', 'c00005', '周三', 5);
INSERT INTO `course_schedule` VALUES (82, 'cl01', '计算机科学与技术1班', 't00005', 'c00005', '周三', 6);
INSERT INTO `course_schedule` VALUES (84, 'cl01', '计算机科学与技术1班', 't00005', 'c00005', '周日', 1);

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_hours` int NULL DEFAULT NULL,
  `class_type` enum('Undergraduate','Associate','Adult Education','Graduate') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `course_level` enum('College','Teacher') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_id`(`course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 'c00001', '计算机科学导论', 'CS101', 48, 'Undergraduate', 'Teacher');
INSERT INTO `courses` VALUES (2, 'c00002', '线性代数', 'MA101', 64, 'Associate', 'College');
INSERT INTO `courses` VALUES (3, 'c00003', '大学物理', 'PH101', 80, 'Undergraduate', 'Teacher');
INSERT INTO `courses` VALUES (4, 'c00004', '数据结构', 'CS102', 64, 'Graduate', 'College');
INSERT INTO `courses` VALUES (5, 'c00005', '概率论与数理统计', 'MA201', 48, 'Adult Education', 'College');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (2, '运营通知', '后天重新开始运营11', '2024-07-20 21:03:13');
INSERT INTO `notice` VALUES (3, '公司年会通知', '尊敬的员工们，我们将于2024年7月25日举行公司年会，地点在公司大会议室。请大家准时参加。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (4, '系统维护通知', '尊敬的用户，我们的系统将于2024年7月21日00:00至06:00进行维护，期间服务将不可用。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (5, '紧急安全演练', '全体员工请注意，公司将于2024年7月22日进行紧急安全演练，请务必参加。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (8, '产品发布公告', '我们的产品将于2024年8月1日正式发布，敬请期待。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (9, '节假日安排', '根据国家节假日安排，公司将在2024年10月1日至7日放假。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (10, '员工生日祝福', '祝7月份生日的员工生日快乐，公司为你们准备了生日礼物，请到人事部领取。', '2024-07-02 21:34:36');
INSERT INTO `notice` VALUES (13, '环境美化倡议', '公司发起环境美化倡议，请大家积极参与，共同营造良好的工作环境。', '2024-06-06 21:34:36');
INSERT INTO `notice` VALUES (14, '团队建设活动', '本月的团队建设活动将在2024年7月28日举行，请大家积极参与。', '2024-06-24 21:34:36');
INSERT INTO `notice` VALUES (15, '网络安全培训', '网络安全至关重要，公司将为员工提供网络安全培训，时间待定，请留意后续通知。', '2024-06-25 21:34:36');
INSERT INTO `notice` VALUES (16, '健康知识讲座', '公司将邀请健康专家在2024年8月5日进行健康知识讲座，欢迎大家参加。', '2024-06-26 21:34:36');
INSERT INTO `notice` VALUES (17, '员工满意度调查', '为了改善工作环境，公司将开展员工满意度调查，请在2024年7月30日前完成。', '2024-06-27 21:34:36');
INSERT INTO `notice` VALUES (18, '新项目启动通知', '公司新项目将于2024年8月10日启动，相关团队请做好准备。', '2024-07-04 21:34:36');
INSERT INTO `notice` VALUES (19, '员工手册更新', '公司员工手册已更新，请所有员工查阅最新版本。', '2024-07-10 21:34:36');
INSERT INTO `notice` VALUES (20, '节能减排倡议', '公司提倡节能减排，请大家在日常工作中注意节约用电。', '2024-07-12 21:34:36');
INSERT INTO `notice` VALUES (21, '年中总结会议', '公司将于2024年7月30日举行年中总结会议，请各部门负责人准备汇报材料。', '2024-07-13 21:34:36');
INSERT INTO `notice` VALUES (22, '优秀员工表彰', '公司将在年会上表彰上半年表现优秀的员工，请被提名的员工做好准备。', '2024-07-20 21:34:36');
INSERT INTO `notice` VALUES (23, '员工离职通知', '我们很遗憾地通知大家，张三同事将于2024年7月31日离职，公司将为他举办告别会。', '2024-07-23 21:34:36');

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting`  (
  `id` int NOT NULL,
  `main_courses_count` int NULL DEFAULT NULL,
  `admin_officer_teaching_hours` int NULL DEFAULT NULL,
  `regular_teacher_teaching_hours` int NULL DEFAULT NULL,
  `classes_counted` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setting
-- ----------------------------
INSERT INTO `setting` VALUES (1, 43, 3, 8, 10);

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gender` enum('male','female') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` int NOT NULL,
  `course_count` int NOT NULL,
  `class_count` int NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '教师审核状态，0表示未审核，1表示审核通过',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `weekly_class_hours` int NULL DEFAULT 0 COMMENT '周课时数',
  `teacher_type` tinyint NULL DEFAULT 1 COMMENT '教师身份: 0-行政干部, 1-普通教师',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `teacher_id`(`teacher_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, 't00001', 'jx152523', '张三', 'female', 1, 2, 1, '15252393509', 1, 'zhangsan', 6, 1);
INSERT INTO `teachers` VALUES (2, 't00002', 'encrypted_password2', '李四', 'female', 2, 1, 2, '12345678902', 1, 'lisi', 2, 1);
INSERT INTO `teachers` VALUES (3, 't00003', 'encrypted_password3', '王五', 'male', 3, 3, 1, '12345678903', 1, '王五', 6, 1);
INSERT INTO `teachers` VALUES (4, 't00004', 'encrypted_password4', '赵六', 'female', 4, 2, 3, '12345678904', 0, '赵六', 1, 1);
INSERT INTO `teachers` VALUES (5, 't00005', 'encrypted_password5', '孙七', 'male', 1, 1, 2, '12345678905', 0, '孙七', 5, 1);

-- ----------------------------
-- Table structure for teaching_incidents
-- ----------------------------
DROP TABLE IF EXISTS `teaching_incidents`;
CREATE TABLE `teaching_incidents`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `teacher_id`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `teaching_incidents_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教学事故记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching_incidents
-- ----------------------------
INSERT INTO `teaching_incidents` VALUES (1, 't00005', '实验室设备故障导致实验课取消111', '2024-01-17');
INSERT INTO `teaching_incidents` VALUES (2, 'T00002', '教学材料准备不足，部分学生无法进行课堂练习', '2024-02-20');
INSERT INTO `teaching_incidents` VALUES (3, 'T00003', '教师迟到，课程开始时间延迟', '2024-03-10');
INSERT INTO `teaching_incidents` VALUES (4, 'T00004', '教室多媒体设备故障，无法播放教学视频', '2024-04-05');
INSERT INTO `teaching_incidents` VALUES (5, 'T00005', '学生投诉教师授课内容与课程大纲不符', '2024-05-12');

SET FOREIGN_KEY_CHECKS = 1;
