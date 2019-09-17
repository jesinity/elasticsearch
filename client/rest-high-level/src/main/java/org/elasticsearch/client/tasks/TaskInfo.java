/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.client.tasks;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ObjectParser;
import org.elasticsearch.common.xcontent.XContentParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * client side counterpart of server side
 * <p>
 * {@link org.elasticsearch.tasks.TaskInfo}
 */
public class TaskInfo {

    private TaskId taskId;
    private String type;
    private String action;
    private String description;
    private long startTime;
    private long runningTimeNanos;
    private boolean cancellable;
    private TaskId parentTaskId;
    private final Map<String, String> headers = new HashMap<>();

    /**
     * as it is a named object
     * in the JSON structure, it requires
     * a name in the constructor to cope with signature of:
     * {@link ObjectParser#declareNamedObjects(BiConsumer, ObjectParser.NamedObjectParser, ParseField)}
     *
     * @param taskId the aggregate identifier of a task (node id + task id )
     */
    public TaskInfo(TaskId taskId) {
        this.taskId = taskId;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public String getNodeId() {
        return taskId.nodeId;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    private void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    private void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRunningTimeNanos() {
        return runningTimeNanos;
    }

    private void setRunningTimeNanos(long runningTimeNanos) {
        this.runningTimeNanos = runningTimeNanos;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    private void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public TaskId getParentTaskId() {
        return parentTaskId;
    }

    private void setParentTaskId(String parentTaskId) {
        this.parentTaskId = new TaskId(parentTaskId);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    private void noOpParse(Object s) {}

    @Override
    public String toString() {
        return "TaskInfo{" +
            "taskId=" + taskId +
            ", type='" + type + '\'' +
            ", action='" + action + '\'' +
            ", description='" + description + '\'' +
            ", startTime=" + startTime +
            ", runningTimeNanos=" + runningTimeNanos +
            ", cancellable=" + cancellable +
            ", parentTaskId=" + parentTaskId +
            ", headers=" + headers +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskInfo)) return false;
        TaskInfo taskInfo = (TaskInfo) o;
        return getStartTime() == taskInfo.getStartTime() &&
            getRunningTimeNanos() == taskInfo.getRunningTimeNanos() &&
            isCancellable() == taskInfo.isCancellable() &&
            getTaskId().equals(taskInfo.getTaskId()) &&
            Objects.equals(getType(), taskInfo.getType()) &&
            Objects.equals(getAction(), taskInfo.getAction()) &&
            Objects.equals(getDescription(), taskInfo.getDescription()) &&
            Objects.equals(getParentTaskId(), taskInfo.getParentTaskId()) &&
            Objects.equals(getHeaders(), taskInfo.getHeaders());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getTaskId(),
            getType(),
            getAction(),
            getDescription(),
            getStartTime(),
            getRunningTimeNanos(),
            isCancellable(),
            getParentTaskId(),
            getHeaders()
        );
    }


    public static final ObjectParser.NamedObjectParser<TaskInfo, Void> PARSER;

    static {
        ObjectParser<TaskInfo, Void> parser = new ObjectParser<>("tasks", true, null);
        // already provided in constructor: triggering a no-op
        parser.declareString(TaskInfo::noOpParse, new ParseField("node"));
        // already provided in constructor: triggering a no-op
        parser.declareLong(TaskInfo::noOpParse, new ParseField("id"));
        parser.declareString(TaskInfo::setType, new ParseField("type"));
        parser.declareString(TaskInfo::setAction, new ParseField("action"));
        parser.declareString(TaskInfo::setDescription, new ParseField("description"));
        parser.declareLong(TaskInfo::setStartTime, new ParseField("start_time_in_millis"));
        parser.declareLong(TaskInfo::setRunningTimeNanos, new ParseField("running_time_in_nanos"));
        parser.declareBoolean(TaskInfo::setCancellable, new ParseField("cancellable"));
        parser.declareString(TaskInfo::setParentTaskId, new ParseField("parent_task_id"));
        parser.declareObject(TaskInfo::setHeaders, (p, c) -> p.mapStrings(), new ParseField("headers"));
        PARSER = (XContentParser p, Void v, String name) -> parser.parse(p, new TaskInfo(new TaskId(name)), null);
    }
}
