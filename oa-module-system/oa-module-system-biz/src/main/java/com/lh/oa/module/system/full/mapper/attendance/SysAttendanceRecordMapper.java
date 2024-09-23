package com.lh.oa.module.system.full.mapper.attendance;

import cn.hutool.core.util.StrUtil;
import com.lh.oa.framework.common.util.StringUtils;
import com.lh.oa.framework.common.util.time.TimeTransUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRecordEntity;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

import static com.lh.oa.module.system.full.constants.ISysConstant.DEFAULT_PAGEABLE_PARAM_NAME;
import static com.lh.oa.module.system.full.enums.FlagStateEnum.DELETED;
import static com.lh.oa.module.system.full.utils.Utils.withPageable;

@Mapper
public interface SysAttendanceRecordMapper {

    @InsertProvider(type = SqlProvider.class, method = "saveAttendanceRecord")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveAttendanceRecord(SysAttendanceRecordEntity attendanceRecord);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordByUserIdAndUnixDate")
    SysAttendanceRecordEntity getAttendanceRecordByUserIdAndUnixDate(int userId, int attendanceDate, int projectId);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordByUserIdAndDate")
    SysAttendanceRecordEntity getAttendanceRecordByUserIdAndDate(int userId, String attendanceDateStr);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordList")
    List<SysAttendanceRecordEntity> getAttendanceRecordList(int deptId, int projectId, int userId, int startTime, int endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordCount")
    int getAttendanceRecordCount(int deptId, int projectId, int userId, int startTime, int endTime);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordByProject")
    List<SysAttendanceRecordEntity> getAttendanceRecordByProject(int projectId, int startTime, int endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable);

    @Deprecated
    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordByParams")
    List<SysAttendanceRecordEntity> getAttendanceRecordByParams(Long projectId, String userIds, Integer startTime, Integer endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordListByParams")
    List<SysAttendanceRecordEntity> getAttendanceRecordListByParams(String userIds, int startTime, int endTime);

    @SelectProvider(type = SqlProvider.class, method = "getHireAttendanceRecordPageByParams")
    List<SysAttendanceRecordEntity> getHireAttendanceRecordPageByParams(Long projectId, String userIds, int startTime, int endTime, int pageNo, int pageSize);

    @SelectProvider(type = SqlProvider.class, method = "getHireAttendanceRecordCountByParams")
    Integer getHireAttendanceRecordCountByParams(Long projectId, String userIds, int startTime, int endTime);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordCountByParams")
    Integer getAttendanceRecordCountByParams(Long projectId, String userIds, int startTime, int endTime);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRecordByParamsV2")
    List<SysAttendanceRecordEntity> getAttendanceRecordByParamsV2(Long projectId, String userIds, Integer startTime, Integer endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable);

    class SqlProvider {
        private final String SYS_ATTENDANCE_RECORD_TABLE = "sys_attendance_record";
        private final String SYS_ATTENDANCE_RULE_TABLE = "sys_attendance_rule";
        private final String SYS_ATTENDANCE_RULE_POSITION_TABLE = "sys_attendance_rule_position";
        private final String PRO_PROJECT_TABLE = "pro_project";
        private final String SYSTEM_DEPT_TABLE = "system_dept";
        private final String SYSTEM_USERS_TABLE = "system_users";

        public SQL getSql() {
            SQL sql = new SQL();
            sql.SELECT("ar.id");
            sql.SELECT("(case when ar.dept_id = 0 then null else ar.dept_id end) `deptId`");
            sql.SELECT("(case when ar.project_id = 0 then null else ar.project_id end) `projectId`");
            sql.SELECT("ar.user_id `userId`");
            sql.SELECT("ar.attendance_rule_id `attendanceRuleId`");
            sql.SELECT("ar.attendance_date `attendanceDate`");
            sql.SELECT("ar.attendance_type `attendanceType`");
            sql.SELECT("ar.attendance_date_str `attendanceDateStr`");
            sql.SELECT("ar.attendance_status `attendanceStatus`");
            sql.SELECT("ar.clock_in_time `clockInTime`");
            sql.SELECT("ar.clock_in_position `clockInPosition`");
            sql.SELECT("ar.clock_in_longitude_latitude `clockInLongitudeLatitude`");
            sql.SELECT("ar.clock_in_status `clockInStatus`");
            sql.SELECT("ar.clock_in_photo_url `clockInPhotoUrl`");
            sql.SELECT("ar.clock_off_time `clockOffTime`");
            sql.SELECT("ar.clock_off_position `clockOffPosition`");
            sql.SELECT("ar.clock_off_longitude_latitude `clockOffLongitudeLatitude`");
            sql.SELECT("ar.clock_off_status `clockOffStatus`");
            sql.SELECT("ar.clock_off_photo_url `clockOffPhotoUrl`");
            sql.SELECT("ar.order_number `orderNumber`");
            sql.SELECT("ar.created_time `createdTime`");
            sql.SELECT("ar.created_by `createdBy`");
            sql.SELECT("ar.flag");
            sql.FROM(SYS_ATTENDANCE_RECORD_TABLE + " ar");
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");

            sql.SELECT("aru.id `attendanceRule.id`");
            sql.SELECT("(case when aru.dept_id = 0 then null else aru.dept_id end) `attendanceRule.deptId`");
            sql.SELECT("(case when aru.project_id = 0 then null else aru.project_id end) `attendanceRule.projectId`");
            sql.SELECT("aru.name `attendanceRule.name`");
            sql.SELECT("aru.attendance_type `attendanceRule.attendanceType`");
            sql.SELECT("aru.clock_in_time `attendanceRule.clockInTime`");
            sql.SELECT("aru.clock_off_time `attendanceRule.clockOffTime`");
            sql.SELECT("aru.weekday `attendanceRule.weekday`");
            sql.SELECT("aru.cut_off_time `attendanceRule.cutOffTime`");
            sql.SELECT("aru.legal_holiday_state `attendanceRule.legalHolidayState`");
            sql.SELECT("aru.offsite_clock_state `attendanceRule.offsiteClockState`");
            sql.SELECT("aru.flag `attendanceRule.flag`");
            sql.INNER_JOIN(SYS_ATTENDANCE_RULE_TABLE + " aru on aru.id = ar.attendance_rule_id");
            return sql;
        }

        public String saveAttendanceRecord(SysAttendanceRecordEntity attendanceRecord) {
            SQL sql = new SQL();
            sql.INSERT_INTO(SYS_ATTENDANCE_RECORD_TABLE);
            if (attendanceRecord.getId() > 0)
                sql.VALUES("id", "#{id}");
            if (attendanceRecord.getDeptId() != null && attendanceRecord.getDeptId() > 0)
                sql.VALUES("dept_id", "#{deptId}");
            if (attendanceRecord.getProjectId() != null && attendanceRecord.getProjectId() > 0)
                sql.VALUES("project_id", "#{projectId}");
            sql.VALUES("user_id", "#{userId}");
            sql.VALUES("attendance_rule_id", "#{attendanceRuleId}");
            sql.VALUES("attendance_date", "#{attendanceDate}");
            sql.VALUES("attendance_type", "#{attendanceType}");
            sql.VALUES("attendance_date_str", "#{attendanceDateStr}");
            if (attendanceRecord.getAttendanceStatus() != null)
                sql.VALUES("attendance_status", "#{attendanceStatus}");
            sql.VALUES("clock_in_time", "#{clockInTime}");
            sql.VALUES("clock_in_position", "#{clockInPosition}");
            sql.VALUES("clock_in_longitude_latitude", "#{clockInLongitudeLatitude}");
            if (attendanceRecord.getClockInStatus() != null)
                sql.VALUES("clock_in_status", "#{clockInStatus}");
            if (StrUtil.isNotBlank(attendanceRecord.getClockInPhotoUrl()))
                sql.VALUES("clock_in_photo_url", "#{clockInPhotoUrl}");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffTime()))
                sql.VALUES("clock_off_time", "#{clockOffTime}");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffPosition()))
                sql.VALUES("clock_off_position", "#{clockOffPosition}");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffLongitudeLatitude()))
                sql.VALUES("clock_off_longitude_latitude", "#{clockOffLongitudeLatitude}");
            if (attendanceRecord.getClockOffStatus() != null)
                sql.VALUES("clock_off_status", "#{clockOffStatus}");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffPhotoUrl()))
                sql.VALUES("clock_off_photo_url", "#{clockOffPhotoUrl}");
            if (attendanceRecord.getOrderNumber() > 0)
                sql.VALUES("order_number", "#{orderNumber}");
            sql.VALUES("created_time", "#{createdTime}");
            sql.VALUES("created_by", "#{createdBy}");
            if (attendanceRecord.getFlag() > 0)
                sql.VALUES("flag", "#{flag}");
            StringBuilder builder = new StringBuilder(sql.toString());
            builder.append(" ON DUPLICATE KEY UPDATE");
            builder.append(" user_id = #{userId},");
            builder.append(" attendance_rule_id = #{attendanceRuleId},");
            if (attendanceRecord.getAttendanceDate() > 0)
                builder.append(" attendance_date = #{attendanceDate},");
            if (attendanceRecord.getAttendanceType() != null)
                builder.append(" attendance_type = #{attendanceType},");
            if (StrUtil.isNotBlank(attendanceRecord.getAttendanceDateStr()))
                builder.append(" attendance_date_str = #{attendanceDateStr},");
            if (attendanceRecord.getAttendanceStatus() != null)
                builder.append(" attendance_status = #{attendanceStatus},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockInTime()))
                builder.append(" clock_in_time = #{clockInTime},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockInPosition()))
                builder.append(" clock_in_position = #{clockInPosition},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockInLongitudeLatitude()))
                builder.append(" clock_in_longitude_latitude = #{clockInLongitudeLatitude},");
            if (attendanceRecord.getClockInStatus() != null)
                builder.append(" clock_in_status = #{clockInStatus},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockInPhotoUrl()))
                builder.append(" clock_in_photo_url = #{clockInPhotoUrl},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffTime()))
                builder.append(" clock_off_time = #{clockOffTime},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffPosition()))
                builder.append(" clock_off_position = #{clockOffPosition},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffLongitudeLatitude()))
                builder.append(" clock_off_longitude_latitude = #{clockOffLongitudeLatitude},");
            if (attendanceRecord.getClockOffStatus() != null)
                builder.append(" clock_Off_status = #{clockOffStatus},");
            if (StrUtil.isNotBlank(attendanceRecord.getClockOffPhotoUrl()))
                builder.append(" clock_off_photo_url = #{clockOffPhotoUrl},");
            if (attendanceRecord.getOrderNumber() > 0)
                builder.append(" order_number = #{orderNumber},");
            builder.append(" modified_time = #{createdTime},");
            builder.append(" modified_by = #{createdBy}");
            if (attendanceRecord.getFlag() > 0)
                builder.append(", flag = #{flag}");
            return builder.toString();
        }

        public String getAttendanceRecordByUserIdAndUnixDate(int userId, int attendanceDate, int projectId) {
            SQL sql = this.getSql();
            sql.LEFT_OUTER_JOIN(SYSTEM_USERS_TABLE + " su on su.id = ar.user_id");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("su.nickname `userName`");
            sql.SELECT("sd.name `deptName`");
            sql.SELECT("p.name `projectName`");
            sql.WHERE("user_id = #{param1}");
            sql.WHERE("attendance_date = #{param2}");
            if (projectId != 0) {
                sql.WHERE("ar.project_id = #{param3}");
            }
            return sql.toString();
        }

        public String getAttendanceRecordByUserIdAndDate(int userId, String attendanceDateStr) {
            SQL sql = this.getSql();
            sql.LEFT_OUTER_JOIN(SYSTEM_USERS_TABLE + " su on su.id = ar.user_id");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("su.nickname `userName`");
            sql.SELECT("sd.name `deptName`");
            sql.SELECT("p.name `projectName`");
            sql.WHERE("user_id = #{param1}");
            sql.WHERE("attendance_date_str = #{param2}");
            return sql.toString();
        }

        public String getAttendanceRecordList(int deptId, int projectId, int userId, int startTime, int endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable) {
            SQL sql = this.getSql();
            sql.LEFT_OUTER_JOIN(SYSTEM_USERS_TABLE + " su on su.id = ar.user_id");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("su.nickname `userName`");
            sql.SELECT("sd.name `deptName`");
            sql.SELECT("p.name `projectName`");
            if (deptId > 0)
                sql.WHERE("ar.dept_id = #{param1}");
            if (projectId > 0)
                sql.WHERE("ar.project_id = #{param2}");
            if (userId > 0)
                sql.WHERE("ar.user_id = #{param3}");
            if (startTime > 0 && endTime > 0)
                sql.WHERE("ar.attendance_date between #{param4} and #{param5}");
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");
            sql.ORDER_BY("ar.created_time DESC");
            if (pageable != null)
                return withPageable(sql, pageable);
            else
                return sql.toString();
        }

        public String getAttendanceRecordByParams(Long projectId, String userIds, Integer startTime, Integer endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable) {
            SQL sql = this.getSql();
            sql.LEFT_OUTER_JOIN(SYSTEM_USERS_TABLE + " su on su.id = ar.user_id");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = su.dept_id");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("su.nickname `userName`");
            sql.SELECT("sd.name `deptName`");
            sql.SELECT("p.name `projectName`");
            if (Objects.nonNull(projectId)) {
                sql.WHERE("ar.project_id = " + projectId);
            }
            if (StringUtils.isNotEmpty(userIds)) {
                sql.WHERE("ar.user_id IN " + userIds);
            }
            if (Objects.nonNull(startTime) && !Objects.equals(0, startTime)
                    && Objects.nonNull(endTime) && !Objects.equals(0, endTime)) {
                sql.WHERE("ar.attendance_date between " + startTime + " and " + endTime);
            }
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");
            sql.ORDER_BY("ar.attendance_date DESC");
            if (pageable != null)
                return withPageable(sql, pageable);
            else
                return sql.toString();
        }

        public String getAttendanceRecordByParamsV2(Long projectId, String userIds, Integer startTime, Integer endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable) {
            SQL sql = new SQL();
//            SQL sql = this.getSql();
            sql.SELECT(" * ");
            sql.FROM(SYS_ATTENDANCE_RECORD_TABLE);
            sql.WHERE(" flag & " + DELETED.value() + " = 0 ");
            if (Objects.nonNull(projectId)) {
                sql.WHERE(" project_id = " + projectId);
            }
            if (StringUtils.isNotEmpty(userIds)) {
                sql.WHERE(" user_id IN " + userIds);
            }
            if (Objects.nonNull(startTime) && !Objects.equals(0, startTime)
                    && Objects.nonNull(endTime) && !Objects.equals(0, endTime)) {
                sql.WHERE(" attendance_date between " + startTime + " and " + endTime);
            }
            sql.ORDER_BY(" user_id DESC, attendance_date DESC ");
            if (pageable != null)
                return withPageable(sql, pageable);
            else
                return sql.toString();
        }

        public String getAttendanceRecordCountByParams(Long projectId, String userIds, int startTime, int endTime) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT COUNT(*) FROM sys_attendance_record WHERE flag & ")
                    .append(DELETED.value())
                    .append(" = 0 ");
            if (startTime != 0 && endTime != 0) {
                sqlBuilder.append(" AND attendance_date >= ").append(startTime)
                        .append(" AND attendance_date <= ").append(endTime);
            }

            if (Objects.nonNull(projectId)) {
                sqlBuilder.append(" AND project_id = ")
                        .append(projectId);
            }
            if (StringUtils.isNotEmpty(userIds)) {
                sqlBuilder.append(" AND user_id IN ")
                        .append(userIds);
            }
            return sqlBuilder.toString();
        }

        public String getAttendanceRecordByProject(int projectId, int startTime, int endTime, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable) {
            SQL sql = this.getSql();
            sql.LEFT_OUTER_JOIN(SYSTEM_USERS_TABLE + " su on su.id = ar.user_id");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("su.nickname `userName`");
            sql.SELECT("p.name `projectName`");
            if (projectId > 0)
                sql.WHERE("ar.project_id = #{param1}");
            if (startTime > 0 && endTime > 0)
                sql.WHERE("ar.attendance_date between #{param2} and #{param3}");
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");
            sql.ORDER_BY("ar.attendance_date_str");
            if (pageable != null)
                return withPageable(sql, pageable);
            else
                return sql.toString();
        }

        public String getAttendanceRecordCount(int deptId, int projectId, int userId, int startTime, int endTime) {
            SQL sql = new SQL();
            sql.SELECT("count(id)");
            sql.FROM(SYS_ATTENDANCE_RECORD_TABLE);
            if (deptId > 0)
                sql.WHERE("dept_id = #{param1}");
            if (projectId > 0)
                sql.WHERE("project_id = #{param2}");
            if (userId > 0)
                sql.WHERE("user_id = #{param3}");
            if (startTime > 0 && endTime > 0)
                sql.WHERE("attendance_date between #{param4} and #{param5}");
            sql.WHERE("flag & " + DELETED.value() + " = 0");
            return sql.toString();
        }

        public String getAttendanceRecordListByParams(String userIds, int startTime, int endTime) {
            return "SELECT * FROM " + SYS_ATTENDANCE_RECORD_TABLE +
                    " WHERE flag & " + DELETED.value() + " = 0 " +
                    " AND user_id IN " + userIds +
                    " AND attendance_date >= " + startTime +
                    " AND attendance_date <= " + endTime;
        }

        public String getHireAttendanceRecordPageByParams(Long projectId, String userIds, int startTime, int endTime, int pageNo, int pageSize) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT project.user_id, project.project_id FROM user_project AS project ")
                    .append(" JOIN user_information AS info ON info.deleted = 0 AND info.user_id = project.user_id AND info.hire_date <= '")
                    .append(TimeUtils.formatAsDate(TimeTransUtils.transSecondTimestamp2Date(endTime)))
                    .append("' ")
                    .append(" JOIN sys_attendance_rule AS rule ON rule.flag & ")
                    .append(DELETED.value())
                    .append(" = 0 AND rule.project_id = project.project_id ")
                    .append(" LEFT JOIN sys_attendance_record AS record ON record.flag & ")
                    .append(DELETED.value())
                    .append(" = 0 AND record.user_id = project.user_id AND record.project_id = project.project_id ")
                    .append(" AND record.attendance_date >= ")
                    .append(startTime)
                    .append(" AND record.attendance_date <= ")
                    .append(endTime)
                    .append(" WHERE project.deleted = 0 ");
            if (Objects.nonNull(projectId)) {
                sqlBuilder.append(" AND project.project_id = ")
                        .append(projectId);
            }
            if (StringUtils.isNotEmpty(userIds)) {
                sqlBuilder.append(" AND project.user_id IN ")
                        .append(userIds);
            }
            sqlBuilder.append(" GROUP BY project.user_id, project.project_id ")
                    .append(" ORDER BY record.user_id DESC ");
            if (pageNo > 0 && pageSize > 0) {
                sqlBuilder.append(" LIMIT ").append((pageNo - 1) * pageSize).append(",").append(pageSize);
            }
            return sqlBuilder.toString();
        }

        public String getHireAttendanceRecordCountByParams(Long projectId, String userIds, int startTime, int endTime) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT COUNT(*) FROM (")
                    .append("SELECT project.user_id, project.project_id FROM user_project AS project ")
                    .append(" JOIN user_information AS info ON info.deleted = 0 AND info.user_id = project.user_id AND info.hire_date <= '")
                    .append(TimeUtils.formatAsDate(TimeTransUtils.transSecondTimestamp2Date(endTime)))
                    .append("' ")
                    .append(" JOIN sys_attendance_rule AS rule ON rule.flag & ")
                    .append(DELETED.value())
                    .append(" = 0 AND rule.project_id = project.project_id ")
                    .append(" LEFT JOIN sys_attendance_record AS record ON record.flag & ")
                    .append(DELETED.value())
                    .append(" = 0 AND record.user_id = project.user_id AND record.project_id = project.project_id ")
                    .append(" AND record.attendance_date >= ")
                    .append(startTime)
                    .append(" AND record.attendance_date <= ")
                    .append(endTime)
                    .append(" WHERE project.deleted = 0 ");
            if (Objects.nonNull(projectId)) {
                sqlBuilder.append(" AND project.project_id = ")
                        .append(projectId);
            }
            if (StringUtils.isNotEmpty(userIds)) {
                sqlBuilder.append(" AND project.user_id IN ")
                        .append(userIds);
            }
            sqlBuilder.append(" GROUP BY project.user_id, project.project_id ")
                    .append(") a");
            return sqlBuilder.toString();
        }

    }

}
