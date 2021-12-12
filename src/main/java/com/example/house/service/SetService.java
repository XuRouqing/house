package com.example.house.service;

import com.example.house.pojo.Set;

import java.util.List;

public interface SetService {
    public void addSet(Set set);
    public Set findSetById(Integer setId);
    public List<Set> getSetList();
    public void modifySet(Set set);
    public void deleteSet(Integer setId);
}
