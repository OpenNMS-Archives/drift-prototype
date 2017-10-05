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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opennms.drift.model.Flow;
import org.opennms.drift.model.FlowBody;

public class NetflowConverter {

    public List<NetflowDocument> convert(Flow flowPackage) throws Exception {
        List<NetflowDocument> documents = new ArrayList<>();

        int i=1;
        for (FlowBody body : flowPackage.getBodies()) {
            final NetflowDocument document = new NetflowDocument();
            // Header
            document.setProtocol("netflow");
            document.setFlowId(i);
            document.setVersion(flowPackage.getHeader().getValue("version"));
            document.setCount(flowPackage.getHeader().getValue("count"));
            document.setSysUptime(flowPackage.getHeader().getValue("SysUptime"));
            document.setTimestamp(new Date(((Integer) flowPackage.getHeader().getValue("unix_secs")) * 1000L));
            document.setFlowSequence(flowPackage.getHeader().getValue("flow_sequence"));
            document.setEngineType(flowPackage.getHeader().getValue("engine_type"));
            document.setEngineId(flowPackage.getHeader().getValue("engine_id"));

            // Body
            document.setSourceAddress(((InetAddress)body.getValue("srcaddr")).toString());
            document.setSourcePort(body.getValue("srcport"));
            document.setDestAddress(((InetAddress)body.getValue("dstaddr")).toString());
            document.setDestPort(body.getValue("dstport"));
            document.setNextHopAddress(((InetAddress) body.getValue("nexthop")).toString());
            document.setInput(body.getValue("input"));
            document.setOutput(body.getValue("output"));
            document.setOctets(body.getValue("dOctets"));
            document.setPackages(body.getValue("dPkts"));
            document.setFirst(body.getValue("First"));
            document.setLast(body.getValue("Last"));
            document.setTcpFlags(body.getValue("tcp_flags"));
            document.setProtocolType(body.getValue("prot"));
            document.setTypeOfService(body.getValue("tos"));
            document.setSourceAs(body.getValue("src_as"));
            document.setDestAs(body.getValue("dst_as"));
            document.setSourceMask(body.getValue("src_mask"));
            document.setDestMask(body.getValue("src_mask"));
            documents.add(document);
            i++;
        }
        return documents;
    }

}
