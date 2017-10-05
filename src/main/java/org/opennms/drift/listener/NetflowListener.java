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

package org.opennms.drift.listener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.opennms.drift.definition.Field;
import org.opennms.drift.definition.FlowDefinition;
import org.opennms.drift.elastic.NetflowConverter;
import org.opennms.drift.elastic.NetflowDocument;
import org.opennms.drift.model.Flow;
import org.opennms.drift.renderer.FlowDefinitionRenderer;
import org.opennms.drift.renderer.FlowRenderer;
import org.opennms.drift.repository.NetflowRepository;

public class NetflowListener {

    private final NetflowRepository repository;

    public NetflowListener(NetflowRepository repository) {
        this.repository = repository;
    }

    public void listen() throws UnknownHostException, SocketException {
        final FlowDefinition definition = getDefinition();
        String definitionString = new FlowDefinitionRenderer().render(definition);

        System.out.println("New Netflowlistener for Netflow v5 packages:");
        System.out.println(definitionString);
        System.out.println("Listening to UDP Netflow v5 packages on port 8877");


        DatagramSocket serverSocket = new DatagramSocket(8877, null);
        byte[] receiveData = new byte[4096]; // TODO MVR ensure that all protocols can be read with this setting
        while(true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                Flow flow = definition.parse(receiveData);
                System.out.println("Received flow package from " + receivePacket.getAddress() +":" + receivePacket.getPort() + ", Protocol: Netflow, Version: " + flow.getVersion());
                if (flow.getVersion() != 5) {
                    System.err.println("Unsupported version. Dropping");
                    continue;
                }
                String flowString = new FlowRenderer().render(flow);
                System.out.println(flowString);
                List<NetflowDocument> documents = new NetflowConverter().convert(flow);
                for (NetflowDocument document : documents) {
                    document.setExportAddress(receivePacket.getAddress().toString());
                    repository.save(document);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private FlowDefinition getDefinition() {
        // TODO Verify if types are correct. Per definition all number fields are unsigned, where in java they are signed.
        // 4 bytes => 32 bit unsigned. Integer is signed. Therefore all Integers should probably be Long. In addition the types in general are not correct
        final List<Field> header = new ArrayList<>();
        header.add(new Field("version", 2,  Integer.class,"NetFlow export format version number"));
        header.add(new Field("count", 2, Integer.class, "Number of flows exported in this packet (1-30)"));
        header.add(new Field("SysUptime", 4, Integer.class, "Current time in milliseconds since the export device booted"));
        header.add(new Field("unix_secs", 4, Integer.class,"Current count of seconds since 0000 UTC 1970"));
        header.add(new Field("unix_nsecs", 4, Integer.class,"Residual nanoseconds since 0000 UTC 1970"));
        header.add(new Field("flow_sequence", 4, Integer.class,"Sequence counter of total flows seen"));
        header.add(new Field("engine_type", 1, Integer.class,"Type of flow-switching engine"));
        header.add(new Field("engine_id", 1, Integer.class,"Slot number of the flow-switching engine"));
        header.add(new Field("sampling_interval", 2, Integer.class,"First two bits hold the sampling mode; remaining 14 bits hold value of sampling interval"));

        final List<Field> body = new ArrayList<>();
        body.add(new Field("srcaddr", 4, InetAddress.class, "Source IP address"));
        body.add(new Field("dstaddr", 4, InetAddress.class,  "Destination IP address"));
        body.add(new Field("nexthop", 4, InetAddress.class, "IP address of next hop router"));
        body.add(new Field("input", 2, Integer.class, "SNMP index of input interface"));
        body.add(new Field("output", 2, Integer.class, "SNMP index of output interface"));
        body.add(new Field("dPkts", 4, Integer.class, "Packets in the flow"));
        body.add(new Field("dOctets", 4, Integer.class, "Total number of Layer 3 bytes in the packets of the flow"));
        body.add(new Field("First", 4, Integer.class, "SysUptime at start of flow"));
        body.add(new Field("Last", 4, Integer.class, "SysUptime at the time the last packet of the flow was received"));
        body.add(new Field("srcport", 2, Integer.class, "TCP/UDP source port number or equivalent"));
        body.add(new Field("dstport", 2, Integer.class, "TCP/UDP destination port number or equivalent"));
        body.add(new Field("pad1", 1, Void.class, "Unused (zero) bytes"));
        body.add(new Field("tcp_flags", 1, Integer.class,  "Cumulative OR of TCP flags"));
        body.add(new Field("prot", 1, Integer.class, "IP protocol type (for example, TCP = 6, UDP = 17)"));
        body.add(new Field("tos", 1, Integer.class, "IP type of service (ToS)"));
        body.add(new Field("src_as", 2, Integer.class,"Autonomous system number of the source, either origin or peer"));
        body.add(new Field("dst_as", 2, Integer.class,"Autonomous system number of the destination, either origin or peer"));
        body.add(new Field("src_mask", 1, Integer.class,"Source address prefix mask bits"));
        body.add(new Field("dst_mask", 1, Integer.class,"Destination address prefix mask bits"));
        body.add(new Field("pad2", 2, Void.class,"Unused (zero) bytes"));

        return new FlowDefinition(header, body);
    }
}
