package com.hyf.skywalking.alarm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 告警邮件实体
 *
 * @author baB_hyf
 * @date 2021/06/15
 */
public class AlarmMessage {

    /** 告警的范围，和枚举名称一致 */
    private String    scope;
    /** 告警范围的ID编号 */
    private int       scopeId;
    /** 告警范围的名称 */
    private String    name;
    /** 匹配name的范围实体(Entity)的ID，关系范围时，为源实体ID */
    private String    id0;
    /** 关系范围时，为目标实体ID，否则为空 */
    private String    id1;
    /** 告警的规则名称 */
    private String    ruleName;
    /** 告警消息 */
    private String    alarmMessage;
    /** 告警时间 */
    private Date      startTime;
    /** 告警标签 */
    private List<Tag> tags;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getScopeId() {
        return scopeId;
    }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId0() {
        return id0;
    }

    public void setId0(String id0) {
        this.id0 = id0;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=============告警通知=============").append("\n");
        sb.append("告警范围Id: ").append(scopeId).append("\n");
        sb.append("告警类型: ").append(scope).append("\n");
        sb.append("告警位置: ").append(Scope.getScope(scopeId).getFormatName(name)).append("\n");
        sb.append("告警源范围Id: ").append(id0).append("\n");
        if (id1 != null && !"".equals(id1.trim())) {
            sb.append("告警目标范围Id: ").append(id1).append("\n");
        }
        sb.append("告警规则名称: ").append(ruleName).append("\n");
        sb.append("告警时间: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)).append("\n");
        sb.append("告警消息: ").append(alarmMessage).append("\n");
        Optional.ofNullable(tags).ifPresent(list -> {
            sb.append("告警标签: ").append("\n");
            list.forEach(sb::append);
        });
        sb.append("=================================").append("\n");
        return sb.toString();
    }

    /**
     * 范围
     *
     * see DefaultScopeDefine
     */
    public enum Scope {

        // Service name
        SERVICE(1, "Service") {
            @Override
            String getFormatName(String name) {
                return String.format("【%s】服务", name);
            }
        },
        // {Instance name} of {Service name}
        INSTANCE(2, "Instance") {
            @Override
            String getFormatName(String name) {
                String[] names = name.split(" of ");
                String instanceName = names[0];
                String serviceName = names[1];
                return String.format("【%s】服务中的【%s】实例", serviceName, instanceName);
            }
        },
        // {Endpoint name} in {Service name}
        ENDPOINT(3, "Endpoint") {
            @Override
            String getFormatName(String name) {
                String[] names = name.split(" in ");
                String endpointName = names[0];
                String serviceName = names[1];
                return String.format("【%s】服务中的【%s】端点", serviceName, endpointName);
            }
        },
        // {Source service name} to {Dest service name}
        SERVICE_RELATION(4, "Service Relation") {
            @Override
            String getFormatName(String name) {
                String[] names = name.split(" to ");
                String sourceServiceName = names[0];
                String destServiceName = names[1];
                return String.format("【%s】服务调用【%s】服务之间", sourceServiceName, destServiceName);
            }
        },
        // {Source instance name} of {Source service name} to {Dest instance name} of {Dest service name}
        INSTANCE_RELATION(5, "Instance Relation") {
            @Override
            String getFormatName(String name) {
                String[] names = name.split(" of | to ");
                String sourceInstanceName = names[0];
                String sourceServiceName = names[1];
                String destInstanceName = names[2];
                String destServiceName = names[3];
                return String.format("【%s】服务的【%s】实例调用【%s】服务的【%s】实例之间",
                        sourceServiceName, sourceInstanceName, destServiceName, destInstanceName);
            }
        },
        // {Source endpoint name} in {Source Service name} to {Dest endpoint name} in {Dest service name}
        ENDPOINT_RELATION(6, "Endpoint Relation") {
            @Override
            String getFormatName(String name) {
                String[] names = name.split(" in | to ");
                String sourceEndpointName = names[0];
                String sourceServiceName = names[1];
                String destEndpointName = names[2];
                String destServiceName = names[3];
                return String.format("【%s】服务的【%s】端点调用【%s】服务的【%s】端点之间",
                        sourceServiceName, sourceEndpointName, destServiceName, destEndpointName);
            }
        },
        // Database service name
        DATABASE(17, "Database") {
            @Override
            String getFormatName(String name) {
                return String.format("【%s】数据库服务", name);
            }
        },
        UNKNOWN(999, "Unknown") {
            @Override
            String getFormatName(String name) {
                return String.format("未知的服务【%s】", name);
            }
        },
        ;

        private int    id;
        private String name;

        Scope(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Scope getScope(int id) {
            for (Scope scope : values()) {

                if (scope.getId() == id) {
                    return scope;
                }
            }
            return UNKNOWN;
        }

        abstract String getFormatName(String name);

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 标签，alarm-settings.yml中配置的
     */
    public static class Tag {

        /** 标签名称 */
        private String key;
        /** 标签值 */
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("----名称: %s, 值: %s\n", key, value);
        }
    }
}
