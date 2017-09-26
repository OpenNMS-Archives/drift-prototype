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

package org.opennms.drift.model;

import java.util.ArrayList;
import java.util.List;

import org.opennms.drift.definition.Field;
import org.opennms.drift.definition.FlowDefinition;

public class Flow {
    private final FlowDefinition definition;
    private final byte[] packet;

    public Flow(FlowDefinition flowDefinition, byte[] packet) {
        this.definition = flowDefinition;
        this.packet = packet;
    }

    public FlowHeader getHeader() {
        return new FlowHeader(this);
    }

    public List<FlowBody> getBodies() throws Exception {
        List<FlowBody> bodies = new ArrayList<>();
        for (int i=0; i<getBodyCount(); i++) {
            bodies.add(getBody(i));
        }
        return bodies;
    }

    public List<FieldValue> getHeaders() {
        List<FieldValue> fields = new ArrayList<>();
        for (Field f : getDefinition().getHeader()) {
            fields.add(new FieldValue(getHeader().getField(f.getName()), 0, packet));
        }
        return fields;
    }

    public FlowDefinition getDefinition() {
         return definition;
    }

    public byte[] getPacket() {
        return packet;
    }

    public FlowBody getBody(int number) {
        int headerSize = getDefinition().getHeaderSize();
        int bodySize = getDefinition().getBodySize();
        int offset = number * bodySize + headerSize;
        return new FlowBody(this, offset);
    }

    public int getBodyCount() throws Exception {
        Field f = getHeader().getField("count");
        return new FieldValue(f, 0, packet).getValue();
    }

    public int getVersion() throws Exception {
        Field f = getHeader().getField("version");
        Integer value = new FieldValue(f, 0, packet).getValue();
        return value;
    }
}
