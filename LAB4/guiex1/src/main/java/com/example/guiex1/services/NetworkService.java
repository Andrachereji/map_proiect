package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.Repository;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkService {
    private Repository<Long, Prietenie> friendshipRepository;
    private Repository<Long, Utilizator> userRepository;

    Network network;

    public NetworkService(Repository<Long, Prietenie> repo, Repository<Long, Utilizator> repo2) {
        this.friendshipRepository = repo;
        this.userRepository = repo2;
        network = buildNetwork();
    }

    public int fridshipE(Long id1, Long id2) {

        Map<Utilizator, LocalDate> friends = friendshipsOfAnUser(id1);
        for (Map.Entry<Utilizator, LocalDate> friend : friends.entrySet()) {
            if (friend.getKey().getId().equals(id2))
                return 1;
        }
        return 0;
    }

    public Map<Utilizator, LocalDate> friendshipsOfAnUser(Long id) {

        HashSet<Prietenie> all = (HashSet<Prietenie>) this.friendshipRepository.findAll();

        Map<Utilizator, LocalDate> friends = new HashMap<Utilizator, LocalDate>();

        for(Prietenie friendship : all){
            if(Objects.equals(friendship.getIdu1(), id)){
                friends.put(userRepository.getOne(friendship.getIdu2()), friendship.getDate());
            }
            if(Objects.equals(friendship.getIdu2(), id)){
                friends.put(userRepository.getOne(friendship.getIdu1()), friendship.getDate());
            }
        }

        return friends;
    }

    public Map<Utilizator, LocalDate> friendshipsOfAnUser(Long id, int month) {
        Map<Utilizator, LocalDate> allFriends = this.friendshipsOfAnUser(id);

        return allFriends.entrySet().stream().filter(p -> p.getValue().getMonthValue() == month).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * class which can include the relationships between all the users - it is a graph
     */
    private static class Network {
        int numberOfCommunities = 0;
        private List<List<Long>> relationships;
        private boolean[] visited;
        private int numberOfUsers;
        private final List<List<Long>> communities = new ArrayList<>();
        private final List<Long> biggestCommunity = new ArrayList<>();

        /**
         * @param nodes - the number of users that exist
         *              relationships - the friends lists of all the users
         */
        public Network(int nodes) {
            relationships = new ArrayList<>();
            visited = new boolean[nodes];
            this.numberOfUsers = nodes;

            relationships.add(0, null);

            for (int i = 1; i < nodes; i++) {
                relationships.add(i, new ArrayList<>());
            }
        }
        void publicNetwork(int nodes) {
            relationships = new ArrayList<>();
            visited = new boolean[nodes];
            this.numberOfUsers = nodes;

            relationships.add(0, null);

            for (int i = 1; i < nodes; i++) {
                relationships.add(i, new ArrayList<>());
            }
        }

        public void addFriendshipInNetwork(Long a, Long b) {
            relationships.get(a.intValue()).add(b);
            relationships.get(b.intValue()).add(a);
        }

    }
    private Network buildNetwork() {
        Long max = 0L;
        assert userRepository != null;
        for (Utilizator u : userRepository.findAll()) {
            if (u.getId() > max) {
                max = u.getId();
            }
        }
        Network network = new Network(Math.toIntExact(max) + 1);
        assert this.friendshipRepository != null;
        for (Prietenie f : this.friendshipRepository.findAll()) {
            network.addFriendshipInNetwork(f.getIdu1(), f.getIdu2());
        }
        return network;
    }
}