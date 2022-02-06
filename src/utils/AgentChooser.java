package utils;

public class AgentChooser {

    private final int agentNumber;
    private int currentAgent = 0;

    public AgentChooser(int agentNumber) {
        this.agentNumber = agentNumber;
    }

    public int giveAgent() {
        return currentAgent;
    }

    public void nextAgent() {
        currentAgent = (currentAgent + 1) % agentNumber;
    }
}
