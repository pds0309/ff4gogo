package com.pds.common.config;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenModelMapper<D> {

    private final ModelMapper modelMapper;

    public D entityToDto(Object obj, Class<D> d){
        return modelMapper.map(obj,d);
    }
    public Object dtoToEntity(D d ,Class<Object> objectClass){
        return  modelMapper.map(d,objectClass);
    }

}
