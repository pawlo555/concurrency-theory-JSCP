package agents;

import org.jcsp.lang.*;
import utils.AgentsObserver;


public abstract class Agent implements CSProcess {
    private static int CURRENT_ID = 0;
    public final int id;

    protected ChannelInputInt readingInputSplitter;
    protected ChannelInputInt[] readingInputBuffer;

    protected final SharedChannelOutputInt writingOutputSplitter;
    protected SharedChannelOutputInt[] writingOutputBuffer;

    private final AgentsObserver observer;

    Agent(Any2OneChannelInt agent2Splitter, One2OneChannelInt splitter2Agent, Any2OneChannelInt[] agent2Buffer,
          One2OneChannelInt[][] buffers2Agent, AgentsObserver observer) {
        this.id = getNextId();
        this.observer = observer;
        observer.addUser(id);

        this.writingOutputSplitter = agent2Splitter.out();
        this.readingInputSplitter = splitter2Agent.in();

        int buffersNumber = buffers2Agent.length;
        this.writingOutputBuffer = new SharedChannelOutputInt[buffersNumber];
        this.readingInputBuffer = new ChannelInputInt[buffersNumber];
        for (int i=0; i<buffersNumber; i++) {
            this.writingOutputBuffer[i] = agent2Buffer[i].out();
            this.readingInputBuffer[i] = buffers2Agent[i][id].in();
        }

    }

    private static int getNextId() {
        CURRENT_ID = CURRENT_ID + 1;
        return CURRENT_ID - 1;
    }

    @Override
    public void run() {
        while (true) {
            boolean finish = false;
            while (!finish) {
                int buffer = askSplitter();
                for (int i=0; i<3; i++) {
                    int bufferResult = askBuffer(buffer);
                    if (bufferResult == 1) {
                        finish = true;
                        break;
                    }
                }
            }
            notifyObserver();
        }
    }

    protected int askSplitter() {
        writingOutputSplitter.write(getAgentNumber());
        splitterAsked();
        return readingInputSplitter.read();
    }

    protected int askBuffer(int buffer) {
        writingOutputBuffer[buffer].write(getAgentNumber());
        bufferAsked();
        return readingInputBuffer[buffer].read();
    }

    protected void bufferAsked() {
        observer.askToBuffer(id);
    }

    protected void splitterAsked() {
        observer.askToSplitter(id);
    }

    private void notifyObserver() {
        observer.normalWorkDone(id);
    }

    protected abstract int getAgentNumber();
}
