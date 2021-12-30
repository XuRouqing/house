package com.example.house.service;

import com.example.house.dao.WorkerMapper;
import com.example.house.pojo.Index;
import com.example.house.pojo.Worker;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WorkerServiceImp implements WorkerService  {
    @Autowired
    private WorkerMapper workerMapper;

    @Override
    public void addWorker(Worker worker){
        this.workerMapper.insertWorker(worker);
    }

    @Override
    public Worker findWorkerById(Integer id){
        return workerMapper.selectWorkerById(id);
    }

    @Override
    public List<Worker> getWorkerList(){
        return workerMapper.selectWorkerAll();
    }

    @Override
    public List<Worker> getWorkerByType(String type){
        List<Worker> workers = workerMapper.selectWorkerByType(type);
        return workers;
    }

    @Override
    public Worker findWorkerByName(String name){
        return workerMapper.findWorkerByName(name);
    }

    @Override
    public void modifyWorker(Worker worker){
        workerMapper.modifyWorker(worker);
    }

    @Override
    public void deleteWorker(Integer id){
        workerMapper.deleteWorker(id);
    }

    @Override
    public List<Index> getWorkerType(){
        return workerMapper.selectWorkerType();
    }
}
