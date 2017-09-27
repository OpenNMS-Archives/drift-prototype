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
            document.setCount(flowPackage.getHeader().getValue("count"));
            document.setVersion(flowPackage.getHeader().getValue("version"));
            document.setProtocol("netflow");
            document.setFlowId(i);
            document.setOctets(body.getValue("dOctets"));
            document.setTimestamp(new Date(((Integer) flowPackage.getHeader().getValue("unix_secs")) * 1000L));
            document.setSourceAddress(((InetAddress)body.getValue("srcaddr")).toString());
            document.setSourcePort(body.getValue("srcport"));
            document.setDestAddress(((InetAddress)body.getValue("dstaddr")).toString());
            document.setDestPort(body.getValue("dstport"));
            documents.add(document);
            i++;
        }
        return documents;
    }

}
