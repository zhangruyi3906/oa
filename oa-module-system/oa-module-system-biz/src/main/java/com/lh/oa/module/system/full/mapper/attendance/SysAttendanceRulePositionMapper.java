package com.lh.oa.module.system.full.mapper.attendance;

import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRulePositionEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.lh.oa.module.system.full.enums.FlagStateEnum.DELETED;

@Mapper
public interface SysAttendanceRulePositionMapper {

    @InsertProvider(type = SqlProvider.class, method = "saveAttendanceRulePosition")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveAttendanceRulePosition(SysAttendanceRulePositionEntity attendanceRulePosition);

    @SelectProvider(type = SqlProvider.class, method = "getAttendanceRulePositionListByAttendanceRuleId")
    List<SysAttendanceRulePositionEntity> getAttendanceRulePositionListByAttendanceRuleId(int attendanceRuleId);

    @UpdateProvider(type = SqlProvider.class, method = "deleteAttendanceRulePositionByAttendanceRuleId")
    void deleteAttendanceRulePositionByAttendanceRuleId(int attendanceRuleId, int unixTime, int modifier);

    @SelectProvider(type = SqlProvider.class, method = "getRulePositionByRuleIds")
    List<SysAttendanceRulePositionEntity> getRulePositionByRuleIds(String ruleIdsStr);

    class SqlProvider {
        private final String SYS_ATTENDANCE_RULE_POSITION_TABLE = "sys_attendance_rule_position";

        private SQL getSql() {
            SQL sql = new SQL();
            sql.SELECT("id");
            sql.SELECT("dept_id `deptId`");
            sql.SELECT("project_id `projectId`");
            sql.SELECT("attendance_rule_id `attendanceRuleId`");
            sql.SELECT("longitude");
            sql.SELECT("latitude");
            sql.SELECT("position");
            sql.SELECT("ranges `range`");
            sql.SELECT("order_number `orderNumber`");
            sql.SELECT("created_time `createdTime`");
            sql.SELECT("created_by `createdBy`");
            sql.SELECT("flag");
            sql.FROM(SYS_ATTENDANCE_RULE_POSITION_TABLE);
            sql.WHERE("flag & " + DELETED.value() + " = 0");
            return sql;
        }

        public String saveAttendanceRulePosition(SysAttendanceRulePositionEntity attendanceRulePosition) {
            SQL sql = new SQL();
            sql.INSERT_INTO(SYS_ATTENDANCE_RULE_POSITION_TABLE);
            if (attendanceRulePosition.getId() > 0)
                sql.VALUES("id", "#{id}");
            sql.VALUES("dept_id", "#{deptId}");
            sql.VALUES("project_id", "#{projectId}");
            sql.VALUES("attendance_rule_id", "#{attendanceRuleId}");
            sql.VALUES("longitude", "#{longitude}");
            sql.VALUES("latitude", "#{latitude}");
            sql.VALUES("position", "#{position}");
            sql.VALUES("ranges", "#{range}");
            if (attendanceRulePosition.getOrderNumber() > 0)
                sql.VALUES("order_number", "#{orderNumber}");
            sql.VALUES("created_time", "#{createdTime}");
            sql.VALUES("created_by", "#{createdBy}");
            if (attendanceRulePosition.getFlag() > 0)
                sql.VALUES("flag", "#{flag}");
            StringBuilder builder = new StringBuilder(sql.toString());
            builder.append(" ON DUPLICATE KEY UPDATE");
            if (attendanceRulePosition.getDeptId() != null && attendanceRulePosition.getDeptId() > 0)
                builder.append(" dept_id = #{deptId},");
            if (attendanceRulePosition.getProjectId() != null &&attendanceRulePosition.getProjectId() > 0)
                builder.append(" project_id = #{projectId},");
            builder.append(" attendance_rule_id = #{attendanceRuleId},");
            builder.append(" longitude = #{longitude},");
            builder.append(" latitude = #{latitude},");
            builder.append(" position = #{position},");
            builder.append(" ranges = #{range},");
            if (attendanceRulePosition.getOrderNumber() > 0)
                builder.append(" order_number = #{orderNumber},");
            builder.append(" modified_time = #{createdTime},");
            builder.append(" modified_by = #{createdBy}");
            if (attendanceRulePosition.getFlag() > 0)
                builder.append(", flag = #{flag}");
            return builder.toString();
        }

        public String getAttendanceRulePositionListByAttendanceRuleId(int attendanceRuleId) {
            SQL sql = this.getSql();
            if (attendanceRuleId > 0)
                sql.WHERE("attendance_rule_id = #{attendanceRuleId}");
            sql.ORDER_BY("order_number DESC, id DESC");
            return sql.toString();
        }

        public String deleteAttendanceRulePositionByAttendanceRuleId(int attendanceRuleId, int unixTime, int modifier) {
            SQL sql = new SQL();
            sql.UPDATE(SYS_ATTENDANCE_RULE_POSITION_TABLE);
            sql.SET("modified_time = #{param2}");
            sql.SET("modified_by = #{param3}");
            sql.SET("flag = flag | " + DELETED.value());
            sql.WHERE("attendance_rule_id = #{param1}");
            return sql.toString();
        }

        public String getRulePositionByRuleIds(String ruleIdsStr) {
            SQL sql = this.getSql();
            sql.WHERE("attendance_rule_id IN " + ruleIdsStr);
            sql.ORDER_BY("order_number, id");
            return sql.toString();
        }

    }
}
