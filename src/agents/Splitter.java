package agents;

import org.jcsp.lang.*;
import utils.AgentChooser;


public class Splitter implements CSProcess {
    private final AltingChannelInputInt requestsFromAgents;
    private final ChannelOutputInt[] message2Agents;

    private final AgentChooser producerChooser;
    private final AgentChooser consumerChooser;

    public Splitter(Any2OneChannelInt agents2SplitterChannel, One2OneChannelInt[] splitter2Agents, int buffersNumber) {
        producerChooser = new AgentChooser(buffersNumber);
        consumerChooser = new AgentChooser(buffersNumber);

        requestsFromAgents = agents2SplitterChannel.in();
        message2Agents = new ChannelOutputInt[splitter2Agents.length];
        for (int i=0; i<message2Agents.length; i++) {
            message2Agents[i] = splitter2Agents[i].out();
        }
    }

    @Override
    public void run() {
        while(true) {
            int readerValue = requestsFromAgents.read();
            if (readerValue > 0) {
                message2Agents[readerValue].write(producerChooser.giveAgent());
                producerChooser.nextAgent();
            }
            else {
                message2Agents[Math.abs(readerValue)].write(consumerChooser.giveAgent());
                consumerChooser.nextAgent();
            }
        }
    }
}
