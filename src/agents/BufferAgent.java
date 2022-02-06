package agents;

import org.jcsp.lang.*;
import utils.AgentChooser;
import utils.Buffer;
import utils.BufferObserver;

public class BufferAgent implements CSProcess {
    private static int nextId;

    private final AltingChannelInputInt requestsFromAgents;
    private final ChannelOutputInt[] message2Agents;

    private final Buffer buffer;
    private final BufferObserver observer;

    private final int id;

    public BufferAgent(Any2OneChannelInt agents2SplitterChannel, One2OneChannelInt[] splitter2Agents, Buffer buffer,
                       BufferObserver observer) {
        requestsFromAgents = agents2SplitterChannel.in();
        message2Agents = new ChannelOutputInt[splitter2Agents.length];
        for (int i=0; i< message2Agents.length; i++) {
            message2Agents[i] = splitter2Agents[i].out();
        }

        this.buffer = buffer;
        this.id = getNextId();
        this.observer = observer;
        observer.addBuffer(id);
    }

    @Override
    public void run() {
        while (true) {
            int agentId = requestsFromAgents.read();
            if (shouldCheckProducers(agentId)) {
                checkProducers(agentId);
            }
            else {
                checkConsumers(agentId);
            }
        }
    }

    private void checkProducers(int agentId) {
        int intMessage = 0;
        if (!buffer.toLittleSpace()) {
            buffer.increment();
            observer.actionSuccess(id);
            intMessage = 1;
        }
        else {
            observer.actionFail(id);
        }
        message2Agents[agentId].write(intMessage);
    }

    private void checkConsumers(int agentId) {
        int intMessage = 0;
        if (!buffer.toLittleResources()) {
            buffer.decrement();
            intMessage = 1;
            observer.actionSuccess(id);
        }
        else {
            observer.actionFail(id);
        }
        message2Agents[-agentId].write(intMessage);
    }

    public boolean shouldCheckProducers(int value) {
        return value >= 0;
    }

    private static int getNextId() {
        nextId = nextId+1;
        return nextId-1;
    }
}
