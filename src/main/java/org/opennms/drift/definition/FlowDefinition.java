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

package org.opennms.drift.definition;

import java.util.List;
import java.util.NoSuchElementException;

import org.opennms.drift.model.Flow;

public class FlowDefinition {
    private List<Field> header;
    private List<Field> body;

    public FlowDefinition(List<Field> header, List<Field> body) {
        this.header = header;
        this.body = body;

        int start = 0;
        for (Field field : header) {
            field.setStartByte(start);
            field.setEndByte(start + field.getLength() - 1);
            start += field.getLength();
        }
        start = 0;
        for (Field field : body) {
            field.setStartByte(start);
            field.setEndByte(start + field.getLength() - 1);
            start += field.getLength();
        }
    }

    public List<Field> getHeader() {
        return header;
    }

    public List<Field> getBody() {
        return body;
    }

    public Flow parse(byte[] flowPacket) {
        return new Flow(this, flowPacket);
    }

    public Field findBodyField(String field) {
        return body.stream().filter(f -> f.getName().equals(field)).findFirst().orElseThrow(() -> new NoSuchElementException("Field " + field + " not found"));
    }

    public Field findHeaderField(String field) {
        return header.stream().filter(f -> f.getName().equals(field)).findFirst().orElseThrow(() -> new NoSuchElementException("Field " + field + " not found"));
    }

    public int getHeaderSize() {
        return this.header.stream().mapToInt(f -> f.getEndByte() - f.getStartByte() + 1).sum();
    }

    public int getBodySize() {
        return this.body.stream().mapToInt(f -> f.getEndByte() - f.getStartByte() + 1).sum();
    }
}
