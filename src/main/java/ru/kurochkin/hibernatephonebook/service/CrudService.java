package ru.kurochkin.hibernatephonebook.service;

import ru.kurochkin.hibernatephonebook.dto.BaseResponse;

import java.util.List;

public interface CrudService<T> {
    List<T> findAll();

    List<T> findAll(String term);

    BaseResponse delete(T data);

    BaseResponse delete(List<Integer> ids);

    BaseResponse save(T data);
}
