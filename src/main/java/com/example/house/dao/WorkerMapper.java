package com.example.house.dao;

import com.example.house.pojo.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface WorkerMapper {
    void insertWorker(Worker worker);
    Worker selectWorkerById(int id);
    List<Worker> selectWorkerAll();
    public Worker findWorkerByName(@Param("name") String name);
    public List<Worker> selectWorkerByType(String type);
    public void modifyWorker(Worker worker);
    public void deleteWorker(Integer id);
}