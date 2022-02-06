import agents.BufferAgent;
import agents.Producer;
import agents.Consumer;
import agents.Splitter;
import org.jcsp.lang.*;
import utils.AgentsObserver;
import utils.Buffer;
import utils.BufferObserver;


public class Main {

    private final static int PRODUCERS = 10;
    private final static int CONSUMERS = 10;
    private final static int BUFFERS = 5;
    private final static int MAX_BUFFER_SIZE = 100;
    private final static int STATS_TIME_INTERVAL = 1000;
    private final static int BUFFER_STATS_TIME_INTERVAL = 60000;

    public static void main (String[] args) {
        AgentsObserver observer = new AgentsObserver(STATS_TIME_INTERVAL);
        BufferObserver bufferObserver = new BufferObserver(BUFFER_STATS_TIME_INTERVAL);

        Any2OneChannelInt splitterIn = Channel.any2oneInt(); // Agents asks splitter
        One2OneChannelInt[] splitterOut = new One2OneChannelInt[PRODUCERS+CONSUMERS]; // Splitter answers to agents
        Any2OneChannelInt[] buffersIn = new Any2OneChannelInt[BUFFERS]; // Agents asks buffers
        One2OneChannelInt[][] buffersOut = new One2OneChannelInt[BUFFERS][PRODUCERS+CONSUMERS]; // Buffers answers agents

        for (int i=0; i<PRODUCERS+CONSUMERS; i++) {
            splitterOut[i] = Channel.one2oneInt();
        }
        for (int i = 0; i<BUFFERS; i++) {
            buffersIn[i] = Channel.any2oneInt();
        }
        for (int i=0; i<BUFFERS; i++) {
            for (int j=0; j<PRODUCERS+CONSUMERS; j++) {
                buffersOut[i][j] = Channel.one2oneInt();
            }
        }


        CSProcess[] procList = new CSProcess[PRODUCERS+CONSUMERS+BUFFERS+1];
        for (int i=0; i<PRODUCERS; i++) {
            procList[i] = new Producer(splitterIn, splitterOut[i] , buffersIn, buffersOut, observer);
        }
        for (int i=0; i<CONSUMERS; i++) {
            procList[PRODUCERS+i] = new Consumer(splitterIn, splitterOut[PRODUCERS+i] , buffersIn, buffersOut, observer);
        }
        for (int i=0; i<BUFFERS; i++) {
            procList[PRODUCERS+CONSUMERS+i] = new BufferAgent(buffersIn[i], buffersOut[i], new Buffer(MAX_BUFFER_SIZE),
                    bufferObserver);
        }
        procList[PRODUCERS+CONSUMERS+BUFFERS] = new Splitter(splitterIn, splitterOut, BUFFERS);

        Parallel par = new Parallel(procList);
        new Parallel();
        par.run();
    }
}