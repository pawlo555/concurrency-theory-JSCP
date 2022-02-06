package agents;

import org.jcsp.lang.*;
import utils.AgentsObserver;

public class Producer extends Agent {

    public Producer(Any2OneChannelInt agent2Splitter, One2OneChannelInt splitter2Agent, Any2OneChannelInt[] agent2Buffer,
                    One2OneChannelInt[][] buffers2Agent, AgentsObserver observer) {
        super(agent2Splitter, splitter2Agent, agent2Buffer, buffers2Agent, observer);
    }

    @Override
    protected int getAgentNumber() {
        return id;
    }
}