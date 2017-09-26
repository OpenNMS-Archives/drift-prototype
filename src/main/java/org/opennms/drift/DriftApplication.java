package org.opennms.drift;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.opennms.drift.definition.Field;
import org.opennms.drift.definition.FlowDefinition;
import org.opennms.drift.model.Flow;
import org.opennms.drift.renderer.FlowDefinitionRenderer;
import org.opennms.drift.renderer.FlowRenderer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DriftApplication {

	private static FlowDefinition flowDefinition;

	static {
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

		flowDefinition = new FlowDefinition(header, body);
	}

	public static void main(String[] args) throws SocketException, UnknownHostException {

		System.out.println(new FlowDefinitionRenderer().render(flowDefinition));

//		SpringApplication.run(DriftApplication.class, args);

		DatagramSocket serverSocket = new DatagramSocket(8877, InetAddress.getLocalHost());
		byte[] receiveData = new byte[100];
		while(true)
		{
			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				Flow flow = flowDefinition.parse(receiveData);
				System.out.println("Received flow from " + receivePacket.getAddress() +":" + receivePacket.getPort() + " version: " + flow.getVersion());
				if (flow.getVersion() != 5) {
					System.err.println("Unsupported version. Dropping");
					continue;
				}
//				if (!flow.isValid()) {
//					System.err.println("Flow is not valid. Dropping");
//					continue;
//				}
				String flowString = new FlowRenderer().render(flow);
				System.out.println(flowString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
}
