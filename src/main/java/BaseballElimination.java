import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private int numTeams;
    private int matchUpSize;
    private Map<String, Integer> teams = new HashMap<>();
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private boolean[] eliminated;
    private Map<String, List<String>> certificates;
    private int[][] pairs;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numTeams = in.readInt();
        matchUpSize = (numTeams - 2) * (numTeams - 1) / 2;
        wins = new int[numTeams];
        losses = new int[numTeams];
        remaining = new int[numTeams];
        pairs = new int[numTeams][numTeams];
        eliminated = new boolean[numTeams];
        certificates = new HashMap<>();
        for (int thisTeam = 0; thisTeam < numTeams; thisTeam++) {
            teams.put(in.readString(), thisTeam);
            wins[thisTeam] = in.readInt();
            losses[thisTeam] = in.readInt();
            remaining[thisTeam] = in.readInt();
            for (int thatTeam = 0; thatTeam < numTeams; thatTeam++) {
                pairs[thisTeam][thatTeam] = in.readInt();
            }
        }
        analyze();
    }
    
    public int numberOfTeams() {
        return numTeams;
    }
    
    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String team) {
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        return losses[teams.get(team)];
    }

    public int remaining(String team) {
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        return pairs[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team) {
        return eliminated[teams.get(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        return certificates.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void analyze() {
        for (String team : teams.keySet()) {
            int teamX = teams.get(team);
            int networkSize = 2 + matchUpSize + (numTeams - 1);
            FlowNetwork network = new FlowNetwork(networkSize);
            int totalGamesLeft = 0;
            List<String> teamCertificates = new ArrayList<>();
            for (int teamI = 0; teamI < numTeams; teamI++) {
                if (wins[teamI] > wins[teamX] + remaining[teamX]) {
                    eliminated[teamX] = true;
                }
            }
            if (eliminated[teamX]) {
                for (String teamI : teams.keySet()) {
                    if (!teamI.equals(team)) {
                        teamCertificates.add(teamI);
                    }
                }
                certificates.put(team, teamCertificates);
                continue;
            }
            for (int teamI = 0; teamI < numTeams - 1; teamI++) {
                if (teamI != teamX) {
                    for (int teamJ = teamI + 1; teamJ < numTeams; teamJ++) {
                        if (teamJ != teamX) {
                            int aliasTeamI = teamI;
                            if (teamI > teamX) {
                                aliasTeamI--;
                            }
                            int aliasTeamJ = teamJ;
                            if (teamJ > teamX) {
                                aliasTeamJ--;
                            }
                            int position = ((numTeams - 2) + (numTeams - 1 - aliasTeamI)) * aliasTeamI / 2 + (aliasTeamJ - aliasTeamI);
                            int gamesLeft = pairs[teamI][teamJ];
                            totalGamesLeft += gamesLeft;
                            FlowEdge edgeGameLeft = new FlowEdge(0, position, gamesLeft);
                            FlowEdge edgeGameTeamI = new FlowEdge(position, 1 + matchUpSize + aliasTeamI, Integer.MAX_VALUE);
                            FlowEdge edgeGameTeamJ = new FlowEdge(position, 1 + matchUpSize + aliasTeamJ, Integer.MAX_VALUE);
                            network.addEdge(edgeGameLeft);
                            network.addEdge(edgeGameTeamI);
                            network.addEdge(edgeGameTeamJ);
                        }
                    }
                }
            }
            for (int teamI = 0; teamI < numTeams; teamI++) {
                if (teamI != teamX) {
                    int canWin = wins[teamX] + remaining[teamX] - wins[teamI];
                    int aliasTeamI = teamI;
                    if (teamI > teamX) {
                        aliasTeamI--;
                    }
                    FlowEdge edgeTeamI = new FlowEdge(1 + matchUpSize + aliasTeamI, networkSize - 1, canWin);
                    network.addEdge(edgeTeamI);
                }
            }
            FordFulkerson maxFlow = new FordFulkerson(network, 0, networkSize - 1);
            eliminated[teamX] = !(maxFlow.value() == totalGamesLeft);
            for (String teamI : teams.keySet()) {
                if ((!teamI.equals(team)) && maxFlow.inCut(teams.get(teamI))) {
                    teamCertificates.add(teamI);
                }
            }
            certificates.put(team, teamCertificates);
        }
        for (String teamI : teams.keySet()) {
            Iterator<String> temp = certificates.get(teamI).iterator();
            while (temp.hasNext()) {
                String teamJ = temp.next();
                if (eliminated[teams.get(teamJ)]) {
                    temp.remove();
                }
            }
        }
    }
}
