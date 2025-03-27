package com.talento_futuro.ProyectoFinal.service.impl;

import java.util.List;

import com.talento_futuro.ProyectoFinal.exception.ModelNotFoundException;
import com.talento_futuro.ProyectoFinal.repository.IGenericRepository;
import com.talento_futuro.ProyectoFinal.service.ICRUDService;

public abstract class CRUDServiceImpl<T, ID> implements ICRUDService<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepository();

   @Override
    public void delete(ID id) {
        getRepository().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND" + id));
        getRepository().deleteById(id);
        
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepository().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND" + id));
    }

    @Override
    public T save(T t) {
        return getRepository().save(t);
    }

    @Override
    public T update(T t, ID id) {
        getRepository().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND" + id));
        return getRepository().save(t);
    }


}
