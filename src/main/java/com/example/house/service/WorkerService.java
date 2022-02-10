package com.example.house.service;

import com.example.house.pojo.Index;
import com.example.house.pojo.Worker;

import java.util.List;

public interface WorkerService {
    public void addWorker(Worker worker);
    public Worker findWorkerById(Integer id);
    public List<Worker> getWorkerList();
    public List<Worker> getWorkerByType(String type);
    public List<Worker> findWorkerByName(String name);
    public List<Worker> findWorkerByHouse(int houseId);
    public void modifyWorker(Worker worker);
    public void deleteWorker(Integer id);
    public List<Index> getWorkerType();
    public int getWorkerNum();
}
