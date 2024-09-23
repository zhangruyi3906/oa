package com.lh.oa.module.system.full.mapper.attendance;

import com.google.common.base.Joiner;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.lh.oa.module.system.full.constants.ISysConstant.DEFAULT_PAGEABLE_PARAM_NAME;
import static com.lh.oa.module.system.full.enums.FlagStateEnum.DELETED;
import static com.lh.oa.module.system.full.utils.Utils.withPageable;

@Mapper
public interface SysAttendanceRuleMapper {

    @InsertProvider(type = SqlProvider.class, method = "saveAttendanceRule")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveAttendanceRule(SysAttendanceRuleEntity attendanceRule);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleList")
    List<SysAttendanceRuleEntity> getAttendanceRuleList(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleListByOthers")
    List<SysAttendanceRuleEntity> getAttendanceRuleListByOthers(Integer deptId, Integer projectId, AttendanceTypeEnum attendanceType);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleListByName")
    SysAttendanceRuleEntity getAttendanceRuleListByName(String name);
    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleCount")
    int getAttendanceRuleCount(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleById")
    SysAttendanceRuleEntity getAttendanceRuleById(int id);

    @UpdateProvider(type = SqlProvider.class, method = "deleteAttendanceRule")
    void deleteAttendanceRule(int id, int unixTime, int modifier);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleByDeptId")
    SysAttendanceRuleEntity getAttendanceRuleByDeptId(int deptId);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleByProjectId")
    SysAttendanceRuleEntity getAttendanceRuleByProjectId(int projectId);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRuleByProjectIds")
    List<SysAttendanceRuleEntity> getAttendanceRuleByProjectIds(String projectIds);

    @SelectProvider(type = SqlProvider.class, method = "selectAll")
    List<SysAttendanceRuleEntity> selectAll();

    @UpdateProvider(type = SqlProvider.class, method = "deleteByIds")
    void deleteByIds(Set<Integer> ids);

    @SelectProvider(type = SqlProvider.class, method = "selectListByType")
    List<SysAttendanceRuleEntity> selectListByType(AttendanceTypeEnum attendanceType);

    class SqlProvider {
        private final String SYS_ATTENDANCE_RULE_TABLE = "sys_attendance_rule";
        private final String SYSTEM_DEPT_TABLE = "system_dept";
        private final String PRO_PROJECT_TABLE = "pro_project";

        private SQL getSql() {
            SQL sql = new SQL();
            sql.SELECT("ar.id");
            sql.SELECT("(case when ar.dept_id = 0 then null else ar.dept_id end) `deptId`");
            sql.SELECT("(case when ar.project_id = 0 then null else ar.project_id end) `projectId`");
            sql.SELECT("ar.name");
            sql.SELECT("ar.attendance_type `attendanceType`");
            sql.SELECT("ar.clock_in_time `clockInTime`");
            sql.SELECT("ar.clock_off_time `clockOffTime`");
            sql.SELECT("ar.weekday `weekday`");
            sql.SELECT("ar.cut_off_time `cutOffTime`");
            sql.SELECT("ar.legal_holiday_state `legalHolidayState`");
            sql.SELECT("ar.offsite_clock_state `offsiteClockState`");
            sql.SELECT("ar.order_number `orderNumber`");
            sql.SELECT("ar.created_time `createdTime`");
            sql.SELECT("ar.created_by `createdBy`");
            sql.SELECT("ar.flag");
            sql.SELECT("ar.noon_rest_start_time");
            sql.SELECT("ar.noon_rest_end_time");
            sql.SELECT("ar.enable_custom_holiday");
            sql.FROM(SYS_ATTENDANCE_RULE_TABLE + " ar");
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");
            return sql;
        }

        public String saveAttendanceRule(SysAttendanceRuleEntity attendanceRule) {
            SQL sql = new SQL();
            sql.INSERT_INTO(SYS_ATTENDANCE_RULE_TABLE);
            if (attendanceRule.getId() > 0)
                sql.VALUES("id", "#{id}");
            if (attendanceRule.getDeptId() != null && attendanceRule.getDeptId() > 0)
                sql.VALUES("dept_id", "#{deptId}");
            if (attendanceRule.getProjectId() != null && attendanceRule.getProjectId() > 0)
                sql.VALUES("project_id", "#{projectId}");
            sql.VALUES("name", "#{name}");
            sql.VALUES("attendance_type", "#{attendanceType}");
            sql.VALUES("clock_in_time", "#{clockInTime}");
            sql.VALUES("clock_off_time", "#{clockOffTime}");
            sql.VALUES("weekday", "#{weekday}");
            sql.VALUES("cut_off_time", "#{cutOffTime}");
            sql.VALUES("legal_holiday_state", "#{legalHolidayState}");
            sql.VALUES("offsite_clock_state", "#{offsiteClockState}");
            if (attendanceRule.getOrderNumber() > 0)
                sql.VALUES("order_number", "#{orderNumber}");
            sql.VALUES("created_time", "#{createdTime}");
            sql.VALUES("created_by", "#{createdBy}");
            if (attendanceRule.getFlag() > 0)
                sql.VALUES("flag", "#{flag}");
            StringBuilder builder = new StringBuilder(sql.toString());
            builder.append(" ON DUPLICATE KEY UPDATE");
            if (attendanceRule.getDeptId() != null && attendanceRule.getDeptId() > 0)
                builder.append(" dept_id = #{deptId},");
            if (attendanceRule.getProjectId() != null && attendanceRule.getProjectId() > 0)
                builder.append(" project_id = #{projectId},");
            builder.append(" name = #{name},");
            builder.append(" attendance_type = #{attendanceType},");
            builder.append(" clock_in_time = #{clockInTime},");
            builder.append(" clock_off_time = #{clockOffTime},");
            builder.append(" weekday = #{weekday},");
            builder.append(" cut_off_time = #{cutOffTime},");
            builder.append(" legal_holiday_state = #{legalHolidayState},");
            builder.append(" offsite_clock_state = #{offsiteClockState},");
            if (attendanceRule.getOrderNumber() > 0)
                builder.append(" order_number = #{orderNumber},");
            builder.append(" modified_time = #{createdTime},");
            builder.append(" modified_by = #{createdBy},");
            builder.append(" noon_rest_start_time = #{noonRestStartTime},");
            builder.append(" noon_rest_end_time = #{noonRestEndTime},");
            builder.append(" enable_custom_holiday = #{enableCustomHoliday}");
//                    .append(Objects.equals(true, attendanceRule.getEnableCustomHoliday()) ? 1 : 0);
            if (attendanceRule.getFlag() > 0)
                builder.append(", flag = #{flag}");
            return builder.toString();
        }

        public String getAttendanceRuleList(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType, @Param(DEFAULT_PAGEABLE_PARAM_NAME) Pageable pageable) {
            SQL sql = this.getSql();
            sql.SELECT("p.name `projectName`");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("sd.name `deptName`");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            if (name != null)
                sql.WHERE("ar.name like #{param1}");
            if (deptName != null)
                sql.WHERE("sd.name like #{param2}");
            if (projectName != null)
                sql.WHERE("p.name like #{param3}");
            if (attendanceType != null)
                sql.WHERE("ar.attendance_type = #{param4}");
            sql.ORDER_BY("ar.order_number DESC, ar.id DESC");
            if (pageable != null)
                return withPageable(sql, pageable);
            else
                return sql.toString();
        }

        public String getAttendanceRuleListByOthers(Integer deptId, Integer projectId, AttendanceTypeEnum attendanceType) {
            SQL sql = this.getSql();
            sql.SELECT("p.name `projectName`");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("sd.name `deptName`");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            if (deptId != null)
                sql.WHERE("sd.id = #{param1}");
            if (projectId != null)
                sql.WHERE("p.id = #{param2}");
            if (attendanceType != null)
                sql.WHERE("ar.attendance_type = #{param3}");
            sql.ORDER_BY("ar.order_number, ar.id");
            return sql.toString();
        }

        public String getAttendanceRuleListByName(String name) {
            SQL sql = this.getSql();
            sql.SELECT("p.name `projectName`");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("sd.name `deptName`");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            if (name != null)
                sql.WHERE("ar.name = #{param1}");
            sql.ORDER_BY("ar.order_number, ar.id");
            return sql.toString();
        }

        public String getAttendanceRuleCount(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType) {
            SQL sql = new SQL();
            sql.SELECT("count(ar.id)");
            sql.FROM(SYS_ATTENDANCE_RULE_TABLE + " ar");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            if (name != null)
                sql.WHERE("ar.name like #{param1}");
            if (deptName != null)
                sql.WHERE("sd.name like #{param2}");
            if (projectName != null)
                sql.WHERE("p.name like #{param3}");
            if (attendanceType != null)
                sql.WHERE("ar.attendance_type = #{param4}");
            sql.WHERE("ar.flag & " + DELETED.value() + " = 0");
            return sql.toString();
        }

        public String getAttendanceRuleById(int id) {
            SQL sql = this.getSql();
            sql.SELECT("p.name `projectName`");
            sql.LEFT_OUTER_JOIN(PRO_PROJECT_TABLE + " p on p.id = ar.project_id");
            sql.SELECT("sd.name `deptName`");
            sql.LEFT_OUTER_JOIN(SYSTEM_DEPT_TABLE + " sd on sd.id = ar.dept_id");
            sql.WHERE("ar.id = #{id}");
            return sql.toString();
        }

        public String deleteAttendanceRule(int id, int unixTime, int modifier) {
            SQL sql = new SQL();
            sql.UPDATE(SYS_ATTENDANCE_RULE_TABLE);
            sql.SET("modified_time = #{param2}");
            sql.SET("modified_by = #{param3}");
            sql.SET("flag = flag | " + DELETED.value());
            sql.WHERE("id = #{param1}");
            return sql.toString();
        }

        public String getAttendanceRuleByDeptId(int deptId) {
            SQL sql = this.getSql();
            sql.WHERE("dept_id = #{deptId}");
            return sql.toString();
        }

        public String getAttendanceRuleByProjectId(int projectId) {
            SQL sql = this.getSql();
            sql.WHERE("project_id = #{projectId}");
            return sql.toString();
        }

        public String getAttendanceRuleByProjectIds(String projectIds) {
            SQL sql = this.getSql();
            sql.WHERE("project_id in " + projectIds);
            return sql.toString();
        }

        public String selectAll() {
            return "SELECT * FROM sys_attendance_rule WHERE flag & " + DELETED.value() + " = 0";
        }

        public String deleteByIds(Set<Integer> ids) {
            return "UPDATE sys_attendance_rule SET flag = flag |" + DELETED.value() +
                    " WHERE id IN (" + Joiner.on(",").join(ids) + ")";
        }

        public String selectListByType(AttendanceTypeEnum attendanceType) {
            return "SELECT * FROM sys_attendance_rule WHERE flag & " + DELETED.value() + " = 0 " +
                    "AND attendance_type = '" + attendanceType.name() + "'";
        }

    }
}
