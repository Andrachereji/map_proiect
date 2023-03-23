package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PrietenieService implements Observable<PrietenieEntityChangeEvent> {
    private Repository<Long, Prietenie> repo;

    private Repository<Long, Utilizator> userRepo;
    private List<Observer<PrietenieEntityChangeEvent>> observers=new ArrayList<>();

    public PrietenieService(Repository<Long, Prietenie> repo, Repository<Long, Utilizator> userRepository) {
        this.repo = repo;
    }


    public Prietenie addPrietenie(Prietenie prietenie) {
        if(repo.save(prietenie).isEmpty()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.ADD, prietenie);
            notifyObservers(event);
            return null;
        }
        return prietenie;
    }

    public Prietenie deletePrietenie(Long id, Long utilizatorId){
        for(Prietenie prietenie : repo.findAll()){
            if((Objects.equals(prietenie.getIdu1(), id) && Objects.equals(prietenie.getIdu2(), utilizatorId) )||
                    (Objects.equals(prietenie.getIdu2(), id) && Objects.equals(prietenie.getIdu1(), utilizatorId))){
                repo.delete(prietenie.getId());
                return prietenie;
            }
        }
        return null;
    }

    public Iterable<Prietenie> getAll(){
        return repo.findAll();
    }



    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }
    public void deleteFriendshipBetweenTwoUsers(Long id1, Long id2) {
        AtomicReference<ArrayList<Prietenie>> friendships = new AtomicReference<>
                (new ArrayList<>((Collection<Prietenie>) this.repo.findAll()));
        int size = friendships.get().size();
        for (int i = 0; i < size; i++) {
            friendships.set(new ArrayList<>((Collection<Prietenie>) this.repo.findAll()));
            for (Prietenie friendship : friendships.get()) {
                if ((Objects.equals(friendship.getIdu1(), id1) && Objects.equals(friendship.getIdu2(), id2))
                        || (Objects.equals(friendship.getIdu2(), id1) && Objects.equals(friendship.getIdu1(), id2))) {
                    deletePrietenie(friendship.getIdu1(), friendship.getIdu2());
                    break;
                }
            }
        }
    }


}

