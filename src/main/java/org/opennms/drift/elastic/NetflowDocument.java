/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.drift.elastic;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "opennms", type="netflow")
public class NetflowDocument {

    @Id
    private String id;
    private int version;
    private int flowId;
    private int count;
    @Field(type = FieldType.text, fielddata = true)
    private String protocol;
    private int octets;
    @Field(type = FieldType.text, fielddata = true)
    private String exportAddress;
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    private Date timestamp;
    @Field(type = FieldType.text, fielddata = true)
    private String destAddress;
    private int sourcePort;
    @Field(type = FieldType.text, fielddata = true)
    private String sourceAddress;
    private int destPort;
    private int sysUptime;
    private int flowSequence;
    private int engineType;
    private int engineId;
    @Field(type = FieldType.text, fielddata = true)
    private String nextHopAddress;
    private int input;
    private int output;
    private int packages;
    private int first;
    private int last;
    private int tcpFlags;
    private int protocolType;
    private int typeOfService;
    private int sourceAs;
    private int destAs;
    private int sourceMask;
    private int destMask;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setOctets(int octets) {
        this.octets = octets;
    }

    public int getOctets() {
        return octets;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public int getDestPort() {
        return destPort;
    }

    public String getExportAddress() {
        return exportAddress;
    }

    public void setExportAddress(String exportAddress) {
        this.exportAddress = exportAddress;
    }

    public void setSysUptime(int sysUptime) {
        this.sysUptime = sysUptime;
    }

    public int getSysUptime() {
        return sysUptime;
    }

    public int getFlowSequence() {
        return flowSequence;
    }

    public void setFlowSequence(int flowSequence) {
        this.flowSequence = flowSequence;
    }

    public int getEngineType() {
        return engineType;
    }

    public void setEngineType(int engineType) {
        this.engineType = engineType;
    }

    public int getEngineId() {
        return engineId;
    }

    public void setEngineId(int engineId) {
        this.engineId = engineId;
    }

    public void setNextHopAddress(String nextHopAddress) {
        this.nextHopAddress = nextHopAddress;
    }

    public String getNextHopAddress() {
        return nextHopAddress;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getTcpFlags() {
        return tcpFlags;
    }

    public void setTcpFlags(int tcpFlags) {
        this.tcpFlags = tcpFlags;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public int getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(int typeOfService) {
        this.typeOfService = typeOfService;
    }

    public int getSourceAs() {
        return sourceAs;
    }

    public void setSourceAs(int sourceAs) {
        this.sourceAs = sourceAs;
    }

    public int getDestAs() {
        return destAs;
    }

    public void setDestAs(int destAs) {
        this.destAs = destAs;
    }

    public int getSourceMask() {
        return sourceMask;
    }

    public void setSourceMask(int sourceMask) {
        this.sourceMask = sourceMask;
    }

    public int getDestMask() {
        return destMask;
    }

    public void setDestMask(int destMask) {
        this.destMask = destMask;
    }
}
